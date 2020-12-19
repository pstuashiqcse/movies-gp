package com.codelab.movies.api.params;

import com.codelab.movies.BuildConfig;
import com.codelab.movies.data.constant.Constants;

public class HttpParams {

    private static final String BASE_URL = "https://api.themoviedb.org/3/";
    public static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w342";
    public static final String BACKDROP_BASE_URL = "https://image.tmdb.org/t/p/w780";

    public static final String KEY_API = "api_key";
    public static final String API_KEY = BuildConfig.API_KEY;;

    private static final String API_MOVIE_POPULAR = "discover/movie";
    private static final String API_TV_SERIES = "discover/tv";
    private static final String API_MOVIE_TRENDING = "trending/all/week";

    public static final String KEY_PRIMARY_RELEASE_YEAR = "primary_release_year";
    public static final String KEY_SORT_BY = "sort_by";

    public static String getMovieApi(int type) {
        if(type == Constants.TYPE_POPULAR) {
            return BASE_URL + API_MOVIE_POPULAR;
        } else if (type == Constants.TYPE_TV_SERIES) {
            return BASE_URL + API_TV_SERIES;
        } else if (type == Constants.TYPE_TRENDING) {
            return BASE_URL + API_MOVIE_TRENDING;
        }
        return null;
    }

    public static final String KEY_PAGE = "page";

    private static final String API_DETAILS_POPULAR = "movie/";
    private static final String API_DETAILS_TV_SERIES = "tv/";
    public static String getDetailsApi(int type, int movieId) {
        if (type == Constants.TYPE_POPULAR) {
            return BASE_URL + API_DETAILS_POPULAR + movieId;
        } else if (type == Constants.TYPE_TV_SERIES) {
            return BASE_URL + API_DETAILS_TV_SERIES + movieId;
        }
        return null;
    }

}
