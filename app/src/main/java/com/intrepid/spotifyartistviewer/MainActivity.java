package com.intrepid.spotifyartistviewer;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.gson.Gson;
import com.intrepid.spotifyartistviewer.ArtistInfoPojo.ArtistInfo;
import com.intrepid.spotifyartistviewer.ArtistInfoPojo.Item;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    ArtistAdapter artistAdapter;
    private RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText etArtistName = (EditText) findViewById(R.id.etArtistName);
        etArtistName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchArtist(etArtistName);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        ImageButton btnArtist = (ImageButton) findViewById(R.id.btnArtist);
        assert btnArtist != null;
        btnArtist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etArtistName.setText("");
            }
        });
        artistAdapter = new ArtistAdapter(this, true);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv = (RecyclerView) findViewById(R.id.recyclerView);
        rv.setAdapter(artistAdapter);
        rv.setLayoutManager(llm);
    }

    public void searchArtist(EditText et) {
        String search = et.getText().toString();
        if (!search.isEmpty()) {
            try {
                List<Item> items = new FetchArtistTask().execute(search).get();
                artistAdapter.newSearch();
                for (Item item : items) {
                    artistAdapter.addArtistItem(item);
                }
                artistAdapter.notifyDataSetChanged();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        } else {
            artistAdapter.artists.clear();
            artistAdapter.notifyDataSetChanged();
        }
    }

    public String sanitizeQuery(String query) {
        return query.replace(" ", "+");
    }

    public class FetchArtistTask extends AsyncTask<String, Void, List<Item>> {

        @Override
        protected List<Item> doInBackground(String... params) {
            URL url = null;
            try {
                url = new URL(String.format("https://api.spotify.com/v1/search?type=artist&q=%s",
                        sanitizeQuery(params[0])));
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
            ArtistInfo ai = gson.fromJson(json.toString(), ArtistInfo.class);
            return ai.getArtists().getItems();
            /*
            SpotifyEndpoint spotifyEndpoint = SpotifyEndpoint.retrofit.create(SpotifyEndpoint.class);
            Call<ArtistInfo> call = spotifyEndpoint.artist(sanitizeQuery(params[0]));
            try {
                Response<ArtistInfo> exexution = call.execute();
                ArtistInfo body = exexution.body();
                body.getArtists();
//                body.toArray();
//                LinkedTreeMap results = (LinkedTreeMap) body;
//                Object artist = results.entrySet().toArray()[0];
//                Toast.makeText(MainActivity.this, "f", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
            */
        }
    }
}
