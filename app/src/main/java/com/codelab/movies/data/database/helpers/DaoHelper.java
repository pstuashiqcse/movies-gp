package com.codelab.movies.data.database.helpers;


import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.codelab.movies.data.database.dao.MovieDao;

@Database(entities = {MovieDao.class}, version = 1, exportSchema = false)
public abstract class DaoHelper extends RoomDatabase {

    public static final String DATABASE_NAME = "movies.db";

    // commands
    public static final int INSERT = 1, MODIFY_WISHLIST = 2, FETCH_SINGLE = 3, FETCH_ALL = 4;

    public abstract MovieDao getMovieDao();

}
