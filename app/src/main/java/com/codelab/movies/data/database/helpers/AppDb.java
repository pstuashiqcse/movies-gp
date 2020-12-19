package com.codelab.movies.data.database.helpers;

import android.content.Context;

import androidx.room.Room;

public class AppDb {

    private static DaoHelper daoHelper;

    public static DaoHelper getAppDb(Context context) {
        if(daoHelper == null) {
            daoHelper = Room.databaseBuilder(context, DaoHelper.class, DaoHelper.DATABASE_NAME)
                                        .build();
        }
        return daoHelper;
    }
}
