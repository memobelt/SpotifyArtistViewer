package com.intrepid.spotifyartistviewer.ArtistDetails;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.intrepid.spotifyartistviewer.ArtistAdapter;
import com.intrepid.spotifyartistviewer.ArtistDetails.ArtistAlbumPojo.ArtistAlbums;
import com.intrepid.spotifyartistviewer.ArtistDetails.ArtistAlbumPojo.Item;
import com.intrepid.spotifyartistviewer.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ArtistDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_details);
        String ID = getIntent().getStringExtra(ArtistAdapter.ID);
        int popularity = getIntent().getIntExtra(ArtistAdapter.POPULARITY, 0);
        String name = getIntent().getStringExtra(ArtistAdapter.NAME);
        TextView tvArtistDeets = (TextView) findViewById(R.id.tvArtistDeets);
        tvArtistDeets.setText(String.format(getString(R.string.artist_desc), name,
                String.valueOf(popularity)));


        ArtistAdapter artistAdapter = new ArtistAdapter(this, false);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        RecyclerView rv = (RecyclerView) findViewById(R.id.rvAlbums);
        rv.setAdapter(artistAdapter);
        rv.setLayoutManager(llm);
        try {
            List<Item> items = new LoadAlbums().execute(ID).get();
            HashSet<String> repeatAlbums = new HashSet<>();
            for (Item item : items) {
                if(!repeatAlbums.contains(item.getName())) {
                    artistAdapter.addAlbumItem(item);
                    repeatAlbums.add(item.getName());
                }
            }
            artistAdapter.notifyDataSetChanged();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public class LoadAlbums extends AsyncTask<String, Void, List<Item>> {
        @Override
        protected List<Item> doInBackground(String... params) {
            URL url = null;
            try {
                url = new URL(String.format("https://api.spotify.com/v1/artists/%s/albums?limit=50&market=US",
                        params[0]));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            HttpURLConnection connection =
                    null;
            try {
                connection = (HttpURLConnection) url.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }

            BufferedReader reader = null;
            try {
                reader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }

            StringBuffer json = new StringBuffer(1024);
            String tmp = "";
            try {
                while ((tmp = reader.readLine()) != null)
                    json.append(tmp).append("\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Gson gson = new Gson();
            ArtistAlbums aa = gson.fromJson(json.toString(), ArtistAlbums.class);
            aa.getItems().get(0);
            return aa.getItems();
        }
    }
}
