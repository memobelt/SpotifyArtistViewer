package com.intrepid.spotifyartistviewer;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText etArtistName = (EditText) findViewById(R.id.etArtistName);
        Button btnArtist = (Button) findViewById(R.id.btnArtist);
        assert btnArtist != null;
        btnArtist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchArtist(etArtistName);
            }
        });
    }

    public void searchArtist(EditText et){
        new FetchArtistTask().execute(et.getText().toString());
    }

    public String sanitizeQuery(String query){
        return query.replace(" ", "+");
    }

    public class FetchArtistTask extends AsyncTask<String, Void, Void>{

        @Override
        protected Void doInBackground(String... params) {
            SpotifyEndpoint spotifyEndpoint = SpotifyEndpoint.retrofit.create(SpotifyEndpoint.class);
            Call<List<ArtistsInfo>> call = spotifyEndpoint.artist(sanitizeQuery(params[0]));
            try {
                Response<List<ArtistsInfo>> exexution = call.execute();
                List<ArtistsInfo> body = exexution.body();
                body.toArray();
//                LinkedTreeMap results = (LinkedTreeMap) body;
//                Object artist = results.entrySet().toArray()[0];
//                Toast.makeText(MainActivity.this, "f", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
