package com.codelab.movies.api.parser;

import android.util.Log;

import com.codelab.movies.api.params.HttpParams;
import com.codelab.movies.model.MovieDetailsModel;
import com.codelab.movies.utility.Utils;

import org.json.JSONObject;


public class MovieDetailsParser {

    public MovieDetailsModel getData(String response) {

        try {

            JSONObject jsonObject = new JSONObject(response);

            String title = null, release_date = null;

            int id = jsonObject.getInt("id");
            if (jsonObject.has("release_date")) {
                release_date = Utils.formatDate(jsonObject.getString("release_date"));
            } else if (jsonObject.has("first_air_date")) {
                release_date = Utils.formatDate(jsonObject.getString("first_air_date"));
            }
            if (jsonObject.has("title")) {
                title = jsonObject.getString("title");
            } else if (jsonObject.has("name")) {
                title = jsonObject.getString("name");
            }
            String poster = HttpParams.IMAGE_BASE_URL + jsonObject.getString("poster_path");
            String overview = jsonObject.getString("overview");
            String backdrop = HttpParams.BACKDROP_BASE_URL + jsonObject.getString("backdrop_path");


            //backdrop_path, overview, poster_path, release_date, title
            //backdrop_path, overview, poster_path, first_air_date, name

            return new MovieDetailsModel(id, title, poster, release_date, overview, backdrop);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}