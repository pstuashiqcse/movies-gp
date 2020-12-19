package com.codelab.movies.data.database.loader;

import android.content.Context;
import android.os.AsyncTask;

import com.codelab.movies.data.database.helpers.AppDb;
import com.codelab.movies.data.database.helpers.DaoHelper;
import com.codelab.movies.data.database.helpers.DbLoaderInterface;
import com.codelab.movies.model.MovieModel;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class MovieDataLoader extends AsyncTask<Object, Void, Object>{

    private DbLoaderInterface dbLoaderInterface;
    private WeakReference<Context> weakContext;

    public MovieDataLoader(Context context) {
        weakContext = new WeakReference<Context>(context);
    }

    public void setDbLoaderInterface(DbLoaderInterface dbLoaderInterface) {
        this.dbLoaderInterface = dbLoaderInterface;
    }

    @Override
    protected Object doInBackground(Object... object) {
        Context context = weakContext.get();
        int command = (int) object[0];

        if (command == DaoHelper.INSERT) {

            ArrayList<MovieModel> movieModels = (ArrayList<MovieModel>) object[1];

            for (MovieModel movieModel: movieModels) {

                MovieModel checkExist = AppDb.getAppDb(context).getMovieDao().getMovie(movieModel.getId());
                if (checkExist == null) {
                    AppDb.getAppDb(context).getMovieDao().insert(checkExist);
                }
            }
        } else if (command == DaoHelper.FETCH_ALL) {
            int type = (int) object [1];
            return AppDb.getAppDb(context).getMovieDao().getAll(type);
        } else if (command == DaoHelper.FETCH_SINGLE) {
            MovieModel movieModel = (MovieModel) object [1];
            return AppDb.getAppDb(context).getMovieDao().getMovie(movieModel.getId());
        } else if (command == DaoHelper.MODIFY_WISHLIST) {
            MovieModel movieModel = (MovieModel) object [1];

            MovieModel checkExist = AppDb.getAppDb(context).getMovieDao().getMovie(movieModel.getId());
            if (checkExist != null) {
                AppDb.getAppDb(context).getMovieDao().updateWishlist(movieModel.getId(), !checkExist.isFavStatus());
                return !checkExist.isFavStatus();
            }

        }

        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);

        if(dbLoaderInterface != null) {
            dbLoaderInterface.onFinished(o);
        }
    }
}
