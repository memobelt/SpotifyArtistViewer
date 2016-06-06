package com.intrepid.spotifyartistviewer;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Memo on 6/6/16.
 */
public interface SpotifyEndpoint {
    @GET("search?type=artist")
    Call<List<ArtistsInfo>> artist(@Query("q") String artist);
    public static final String BASE_URL = "https://api.spotify.com/v1/";
    public static final Retrofit retrofit = new Retrofit.Builder()
                                            .addConverterFactory(GsonConverterFactory.create())
                                            .baseUrl(BASE_URL)
                                            .build();
}
