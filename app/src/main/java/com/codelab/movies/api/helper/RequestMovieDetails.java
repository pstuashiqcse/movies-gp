package com.codelab.movies.api.helper;

import com.codelab.movies.api.params.HttpParams;
import com.codelab.movies.api.parser.MovieDetailsParser;
import com.codelab.movies.http.BaseHttp;
import com.codelab.movies.http.ResponseListener;

import java.util.HashMap;


public class RequestMovieDetails extends BaseHttp {

    private Object object;
    private ResponseListener responseListener;

    public RequestMovieDetails(int type, int movieId) {
        super(HttpParams.getDetailsApi(type, movieId));
    }

    public void setResponseListener(ResponseListener responseListener) {
        this.responseListener = responseListener;
    }

    public void buildParams() {

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(HttpParams.KEY_API, HttpParams.API_KEY);

        setParams(hashMap, GET);
    }


    @Override
    public void onBackgroundTask(String response) {
        object = new MovieDetailsParser().getData(response);
    }

    @Override
    public void onTaskComplete() {
        if (responseListener != null) {
            responseListener.onResponse(object);
        }
    }
}
