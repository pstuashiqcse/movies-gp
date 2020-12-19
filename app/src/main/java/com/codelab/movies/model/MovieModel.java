package com.codelab.movies.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class MovieModel {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;

    @ColumnInfo(name = "poster")
    private String poster;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "releaseDate")
    private String releaseDate;

    @ColumnInfo(name = "voteCount")
    private int voteCount;

    @ColumnInfo(name = "favStatus")
    private boolean favStatus;

    @ColumnInfo(name = "type")
    private int type;

    public MovieModel(int id, String poster, String title, String releaseDate, int voteCount) {
        this.id = id;
        this.poster = poster;
        this.title = title;
        this.releaseDate = releaseDate;
        this.voteCount = voteCount;
    }

    public int getId() {
        return id;
    }

    public String getPoster() {
        return poster;
    }

    public String getTitle() {
        return title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public boolean isFavStatus() {
        return favStatus;
    }

    public void setFavStatus(boolean favStatus) {
        this.favStatus = favStatus;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

}
