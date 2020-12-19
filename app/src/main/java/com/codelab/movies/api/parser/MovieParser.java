package com.codelab.movies.api.parser;



import android.util.Log;

import com.codelab.movies.api.params.HttpParams;
import com.codelab.movies.model.MovieModel;
import com.codelab.movies.utility.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MovieParser {

    public ArrayList<MovieModel> getMovieList(String response) {

        ArrayList<MovieModel> arrayList = new ArrayList<>();
        try {

            JSONObject jsonObject = new JSONObject(response);

            JSONArray resultList = jsonObject.getJSONArray("results");

            for (int i = 0; i < resultList.length(); i++) {
                JSONObject listData = resultList.getJSONObject(i);
                String releaseDate = null, title = null;

                int id = listData.getInt("id");
                if(listData.has("release_date")) {
                    releaseDate = Utils.formatDate(listData.getString("release_date"));
                } else if (listData.has("first_air_date")) {
                    releaseDate = Utils.formatDate(listData.getString("first_air_date"));
                }
                if(listData.has("title")) {
                    title = listData.getString("title");
                } else if (listData.has("name")) {
                    title = listData.getString("name");
                }
                int voteCount = listData.getInt("vote_count");
                String poster = HttpParams.IMAGE_BASE_URL + listData.getString("poster_path");

                arrayList.add(new MovieModel(id, poster, title, releaseDate, voteCount));
            }

            return arrayList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}