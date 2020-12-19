package com.codelab.movies.activity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codelab.movies.R;
import com.codelab.movies.api.helper.RequestMovieDetails;
import com.codelab.movies.data.constant.Constants;
import com.codelab.movies.data.database.helpers.DaoHelper;
import com.codelab.movies.data.database.helpers.DbLoaderInterface;
import com.codelab.movies.data.database.loader.MovieDataLoader;
import com.codelab.movies.http.ResponseListener;
import com.codelab.movies.model.MovieDetailsModel;
import com.codelab.movies.utility.ActivityUtils;
import com.codelab.movies.utility.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieDetailsActivity extends AppCompatActivity {

    private Context mContext;

    private LinearLayout llParentView;
    private TextView tvErrorMessage, movieTitle, tvRelease, tvOverview;
    private ImageView ivPoster, ivBackdrop;
    private Button btnAddToFav;
    private ProgressBar pbLoadingIndicator, pbTrailerProgress, pbReviewProgress;

    private String moviePoster, movieName;
    private int movieId, type;

    private MovieDetailsModel movieDetailsModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        initVars();
        initViews();
        initFunctionality(savedInstanceState);
        initListeners();
    }

    private void initVars() {
        mContext = MovieDetailsActivity.this.getApplicationContext();

        Intent intent = getIntent();
        if (intent.hasExtra(Constants.KEY_MOVIE_ID)) {
            movieId = intent.getIntExtra(Constants.KEY_MOVIE_ID, -1);
        }
        if (intent.hasExtra(Constants.KEY_TYPE)) {
            type = intent.getIntExtra(Constants.KEY_TYPE, -1);
        }
    }

    private void initViews() {
        llParentView = findViewById(R.id.ll_parent_view);
        movieTitle = findViewById(R.id.movie_title);
        ivPoster = findViewById(R.id.iv_poster);
        tvRelease = findViewById(R.id.tv_release);
        ivBackdrop = findViewById(R.id.iv_backdrop);
        btnAddToFav = findViewById(R.id.btn_add_to_fav);
        tvOverview =  findViewById(R.id.tv_overview);

        tvErrorMessage = (TextView) findViewById(R.id.tv_error_message);
        pbLoadingIndicator = (ProgressBar) findViewById(R.id.pd_loading_indicator);
    }

    private void initFunctionality(Bundle savedInstanceState) {

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        loadMovieDetails();

    }

    private void initListeners() {

        btnAddToFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MovieDataLoader movieDataLoader = new MovieDataLoader(mContext);
                movieDataLoader.setDbLoaderInterface(new DbLoaderInterface() {
                    @Override
                    public void onFinished(Object object) {
                        boolean isFav = (boolean) object;

                        if (isFav) {
                            btnAddToFav.setTextColor(ContextCompat.getColor(mContext, R.color.remove_fav));
                            btnAddToFav.setText(getString(R.string.remove_from_wishlist));
                        } else {
                            btnAddToFav.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                            btnAddToFav.setText(getString(R.string.add_to_wishlist));
                        }
                    }
                });
                movieDataLoader.execute(DaoHelper.MODIFY_WISHLIST, movieDetailsModel);
            }
        });
    }

    private void loadMovieDetails() {

        if (Utils.isNetworkAvailable(mContext)) {
            tvErrorMessage.setVisibility(View.GONE);
            llParentView.setVisibility(View.GONE);
            pbLoadingIndicator.setVisibility(View.VISIBLE);

            RequestMovieDetails requestMovieDetails = new RequestMovieDetails(type, movieId);
            requestMovieDetails.buildParams();
            requestMovieDetails.setResponseListener(new ResponseListener() {
                @Override
                public void onResponse(Object data) {

                    if (data != null) {
                        movieDetailsModel = (MovieDetailsModel) data;
                        showData();
                    } else {
                        tvErrorMessage.setVisibility(View.VISIBLE);
                        tvErrorMessage.setText(getString(R.string.no_data));
                    }
                    pbLoadingIndicator.setVisibility(View.GONE);


                }
            });
            requestMovieDetails.execute();
        } else {
            Utils.noInternetWarning(tvErrorMessage, mContext);
        }
    }

 /*   private void setFavStatus() {
        if (hasData(movieId)) {
            btnAddToFav.setTextColor(ContextCompat.getColor(mContext, R.color.remove_fav));
            btnAddToFav.setText(getString(R.string.remove_fav));
        } else {
            btnAddToFav.setTextColor(ContextCompat.getColor(mContext, R.color.white));
            btnAddToFav.setText(getString(R.string.fav_button));
        }
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean hasData(String movieId) {


        return false;
    }

    private void deleteAData(String itemId) {
    }

    private void showData() {
        if (movieDetailsModel != null) {
            movieName = movieDetailsModel.getTitle();
            movieTitle.setText(movieName);
            tvRelease.setText(getString(R.string.release_date)+movieDetailsModel.getReleaseDate());

            tvOverview.setText(movieDetailsModel.getOverview());
            moviePoster = movieDetailsModel.getPoster();
            Picasso.get().
                    load(moviePoster).
                    placeholder(R.color.gray).
                    into(ivPoster);

            Picasso.get().
                    load(movieDetailsModel.getBackdrop()).
                    placeholder(R.color.gray).
                    into(ivBackdrop);
            llParentView.setVisibility(View.VISIBLE);
        } else {
            tvErrorMessage.setVisibility(View.VISIBLE);
            tvErrorMessage.setText(getString(R.string.no_data));
        }
    }

}
