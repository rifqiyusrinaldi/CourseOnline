package com.rifqi.courseonline.model.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "rating")
public class RatingData implements Serializable{
    @PrimaryKey(autoGenerate = true)
    private int ID;

    @ColumnInfo(name = "id_user")
    private int id_user;
    @ColumnInfo(name = "id_course")
    private int id_course;
    @ColumnInfo(name = "rating")
    private int rating;

    @ColumnInfo(name = "predictions")
    private float predictions;

    @ColumnInfo(name = "learned")
    private int learned;

    public float getPredictions() {
        return predictions;
    }

    public void setPredictions(float predictions) {
        this.predictions = predictions;
    }

    public int getLearned() {
        return learned;
    }

    public void setLearned(int learned) {
        this.learned = learned;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public int getId_course() {
        return id_course;
    }

    public void setId_course(int id_course) {
        this.id_course = id_course;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
}

