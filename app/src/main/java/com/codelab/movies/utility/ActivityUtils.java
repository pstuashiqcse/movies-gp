package com.codelab.movies.utility;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import com.codelab.movies.activity.MovieDetailsActivity;
import com.codelab.movies.data.constant.Constants;


public class ActivityUtils {

    private static ActivityUtils sActivityUtils = null;
    public static ActivityUtils getInstance() {
        if(sActivityUtils == null) {
            sActivityUtils = new ActivityUtils();
        }
        return sActivityUtils;
    }



    public void invokeDetailsActivity(Activity activity, int movieId, int type) {
        Intent intent = new Intent(activity, MovieDetailsActivity.class);
        intent.putExtra(Constants.KEY_MOVIE_ID, movieId);
        intent.putExtra(Constants.KEY_TYPE, type);
        activity.startActivity(intent);
    }



}
