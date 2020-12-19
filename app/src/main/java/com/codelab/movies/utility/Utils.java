package com.codelab.movies.utility;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.codelab.movies.R;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Utils {


    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager != null && connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    public static void noInternetWarning(TextView view, final Context context) {
        view.setText(context.getResources().getString(R.string.please_connect));
        Snackbar snackbar = Snackbar.make(view, context.getString(R.string.no_internet), Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(context.getString(R.string.connect), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.provider.Settings.ACTION_SETTINGS);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
        snackbar.show();
    }

    public static String formatDate(String strDate) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("D MMM YYYY", Locale.US);
            return sdf.format(strDate);
        } catch (Exception e) {
            e.printStackTrace();
            return strDate;
        }
    }

    /**
     * TODO:
     * 1) Check date format again
     * 2) Implement pagination
     */


}
