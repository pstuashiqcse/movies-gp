package com.codelab.movies.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


public class MovieDetailsModel{

    private int id;
    private String title;
    private String poster;
    private String releaseDate;
    private String overview;
    private String backdrop;

    public MovieDetailsModel(int id, String title, String poster, String releaseDate, String overview, String backdrop) {
        this.id = id;
        this.title = title;
        this.poster = poster;
        this.releaseDate = releaseDate;
        this.overview = overview;
        this.backdrop = backdrop;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getPoster() {
        return poster;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getOverview() {
        return overview;
    }

    public String getBackdrop() {
        return backdrop;
    }
}
