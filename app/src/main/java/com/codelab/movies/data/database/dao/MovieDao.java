package com.codelab.movies.data.database.dao;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.codelab.movies.model.MovieModel;

import java.util.List;

@Dao
public interface MovieDao {

    @Query("SELECT * FROM MovieModel WHERE type = :type ORDER BY id DESC")
    List<MovieModel> getAll(int type);

    @Query("SELECT * FROM MovieModel WHERE id = :id")
    MovieModel getMovie(int id);

    @Query("UPDATE MovieModel SET favStatus = :isFav WHERE id = :id")
    void updateWishlist(int id, boolean isFav);

    @Insert
    void insert(MovieModel movieModel);

}
