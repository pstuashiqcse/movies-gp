package com.codelab.movies.api.helper;

import com.codelab.movies.api.params.HttpParams;
import com.codelab.movies.api.parser.MovieParser;
import com.codelab.movies.data.constant.Constants;
import com.codelab.movies.http.BaseHttp;
import com.codelab.movies.http.ResponseListener;

import java.util.HashMap;

public class RequestMovieList extends BaseHttp {

    private Object object;
    private ResponseListener responseListener;

    public RequestMovieList(int type) {
        super(HttpParams.getMovieApi(type));
    }

    public void setResponseListener(ResponseListener responseListener) {
        this.responseListener = responseListener;
    }

    public void buildPopularOrTvSeriesParams(int pageNo) {

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(HttpParams.KEY_API, HttpParams.API_KEY);
        hashMap.put(HttpParams.KEY_PAGE, String.valueOf(pageNo));
        hashMap.put(HttpParams.KEY_PRIMARY_RELEASE_YEAR, Constants.PRIMARY_RELEASE_YEAR);
        hashMap.put(HttpParams.KEY_SORT_BY, Constants.SORT_BY);

        setParams(hashMap, GET);
    }

    public void buildTrendingParams(int pageNo) {

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(HttpParams.KEY_API, HttpParams.API_KEY);
        hashMap.put(HttpParams.KEY_PAGE, String.valueOf(pageNo));

        setParams(hashMap, GET);
    }


    @Override
    public void onBackgroundTask(String response) {
        object = new MovieParser().getMovieList(response);
    }

    @Override
    public void onTaskComplete() {
        if (responseListener != null) {
            responseListener.onResponse(object);
        }
    }
}
