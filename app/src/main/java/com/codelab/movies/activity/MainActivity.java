package com.codelab.movies.activity;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codelab.movies.R;
import com.codelab.movies.adapter.MovieAdapter;
import com.codelab.movies.api.helper.RequestMovieList;
import com.codelab.movies.data.constant.Constants;
import com.codelab.movies.data.database.helpers.DaoHelper;
import com.codelab.movies.data.database.helpers.DbLoaderInterface;
import com.codelab.movies.data.database.loader.MovieDataLoader;
import com.codelab.movies.http.ResponseListener;
import com.codelab.movies.model.MovieModel;
import com.codelab.movies.utility.ActivityUtils;
import com.codelab.movies.utility.Utils;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Context mContext;

    private TextView tvErrorMessage;
    private ProgressBar pbLoadMore;
    private RelativeLayout rlLoader;

    private RecyclerView recyclerView, recyclerViewSeries, recyclerViewTrending;
    private LinearLayoutManager mLayoutManager, seriesManager, trendingManager;
    private ArrayList<MovieModel> arrayList, seriesList, trendingList;
    private MovieAdapter mAdapter, seriesAdapter, trendingAdapter;

    private int pageNumber = Constants.DEFAULT_PAGE;
    private boolean loading = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initVars();
        initViews();
        initFunctionality(savedInstanceState);
        initListeners();

    }

    private void initVars() {
        mContext = MainActivity.this.getApplicationContext();
        arrayList = new ArrayList<>();
        seriesList = new ArrayList<>();
        trendingList = new ArrayList<>();
    }

    private void initViews() {
        tvErrorMessage = findViewById(R.id.tv_error_message);
        recyclerView = findViewById(R.id.recycler_view_popular);
        recyclerViewSeries = findViewById(R.id.recycler_view_series);
        recyclerViewTrending = findViewById(R.id.recycler_view_trending);
        pbLoadMore = findViewById(R.id.pb_load_more);
        rlLoader = findViewById(R.id.rlLoader);
    }

    private void initFunctionality(Bundle savedInstanceState) {
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MovieAdapter(mContext, arrayList, Constants.VIEW_HORIZONTAL);
        recyclerView.setAdapter(mAdapter);

        seriesManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewSeries.setLayoutManager(seriesManager);
        seriesAdapter = new MovieAdapter(mContext, seriesList, Constants.VIEW_HORIZONTAL);
        recyclerViewSeries.setAdapter(seriesAdapter);

        trendingManager = new LinearLayoutManager(this);
        recyclerViewTrending.setLayoutManager(trendingManager);
        trendingAdapter = new MovieAdapter(mContext, trendingList, Constants.VIEW_VERTICAL);
        recyclerViewTrending.setAdapter(trendingAdapter);

        loadMovies(pageNumber);
        loadTvShows(pageNumber);
        loadTrendingMovies(pageNumber);

    }

    private void initListeners() {
        mAdapter.setItemClickListener(new MovieAdapter.ItemClickListener() {
            @Override
            public void onItemClick(int position) {
                ActivityUtils.getInstance().invokeDetailsActivity(MainActivity.this, arrayList.get(position).getId(), Constants.TYPE_POPULAR);
            }
        });

        seriesAdapter.setItemClickListener(new MovieAdapter.ItemClickListener() {
            @Override
            public void onItemClick(int position) {
                ActivityUtils.getInstance().invokeDetailsActivity(MainActivity.this, seriesList.get(position).getId(), Constants.TYPE_TV_SERIES);
            }
        });

        /*recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    onScrolledMore();
                }
            }
        });*/

    }

    private void onScrolledMore() {
        int visibleItemCount, totalItemCount, pastVisiblesItems;
        visibleItemCount = mLayoutManager.getChildCount();
        totalItemCount = mLayoutManager.getItemCount();
        pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();

        if (loading) {
            if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                loading = false;
                pbLoadMore.setVisibility(View.VISIBLE);
                pageNumber = pageNumber + 1;
                loadMovies(pageNumber);
            }
        }
    }

    @SuppressWarnings({"unchecked"})
    private void loadMovies(int page) {

        if (Utils.isNetworkAvailable(mContext)) {
            tvErrorMessage.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            // show center loader only for first time loading
            if (page == 1) {
                rlLoader.setVisibility(View.VISIBLE);
            }

            // load popular movies
            RequestMovieList requestMovieList = new RequestMovieList(Constants.TYPE_POPULAR);
            requestMovieList.buildPopularOrTvSeriesParams(page);
            requestMovieList.setResponseListener(new ResponseListener() {
                @Override
                public void onResponse(Object data) {

                    if (data != null) {
                        ArrayList<MovieModel> movieList = (ArrayList<MovieModel>) data;

                        if (movieList.isEmpty()) {
                            tvErrorMessage.setVisibility(View.VISIBLE);
                            tvErrorMessage.setText(getString(R.string.no_data));
                        } else {
                            loadListData(movieList);

                            cacheAllData(Constants.TYPE_POPULAR, movieList);
                        }
                    } else {
                        tvErrorMessage.setVisibility(View.VISIBLE);
                        tvErrorMessage.setText(getString(R.string.no_data));
                    }
                    rlLoader.setVisibility(View.GONE);
                    pbLoadMore.setVisibility(View.GONE);
                    loading = true;

                }
            });
            requestMovieList.execute();
        } else {
            Utils.noInternetWarning(tvErrorMessage, mContext);
        }
    }

    private void loadTvShows(int page) {
        if (Utils.isNetworkAvailable(mContext)) {

            // load tv series
            RequestMovieList requestMovieList = new RequestMovieList(Constants.TYPE_TV_SERIES);
            requestMovieList.buildPopularOrTvSeriesParams(page);
            requestMovieList.setResponseListener(new ResponseListener() {
                @Override
                public void onResponse(Object data) {

                    if (data != null) {
                        ArrayList<MovieModel> movieList = (ArrayList<MovieModel>) data;

                        if (!movieList.isEmpty()) {
                            seriesList.addAll(movieList);
                            seriesAdapter.notifyDataSetChanged();

                            cacheAllData(Constants.TYPE_TV_SERIES, movieList);
                        }
                    }

                }
            });
            requestMovieList.execute();

        } else {
            Utils.noInternetWarning(tvErrorMessage, mContext);
        }
    }

    private void loadTrendingMovies(int page) {
        if (Utils.isNetworkAvailable(mContext)) {

            // load tv series
            RequestMovieList requestMovieList = new RequestMovieList(Constants.TYPE_TRENDING);
            requestMovieList.buildTrendingParams(page);
            requestMovieList.setResponseListener(new ResponseListener() {
                @Override
                public void onResponse(Object data) {

                    if (data != null) {
                        ArrayList<MovieModel> movieList = (ArrayList<MovieModel>) data;

                        if (!movieList.isEmpty()) {
                            trendingList.addAll(movieList);
                            trendingAdapter.notifyDataSetChanged();

                            cacheAllData(Constants.TYPE_TRENDING, movieList);
                        }
                    }

                }
            });
            requestMovieList.execute();

        } else {
            Utils.noInternetWarning(tvErrorMessage, mContext);
        }
    }

    private void loadListData(ArrayList<MovieModel> dataList) {
        arrayList.addAll(dataList);
        mAdapter.notifyDataSetChanged();
        recyclerView.setVisibility(View.VISIBLE);
        rlLoader.setVisibility(View.GONE);
        pbLoadMore.setVisibility(View.GONE);

        if(dataList != null && dataList.isEmpty()) {
            tvErrorMessage.setVisibility(View.VISIBLE);
        } else {
            tvErrorMessage.setVisibility(View.GONE);
        }
    }

    private void cacheAllData(int type, ArrayList<MovieModel> arrayList) {
        MovieDataLoader movieDataLoader = new MovieDataLoader(mContext);
        movieDataLoader.setDbLoaderInterface(new DbLoaderInterface() {
            @Override
            public void onFinished(Object object) {
            }
        });
        movieDataLoader.execute(DaoHelper.INSERT, arrayList);
    }

}
