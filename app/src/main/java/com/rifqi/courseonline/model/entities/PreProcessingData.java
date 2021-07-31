package com.rifqi.courseonline.model.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "preprocessing")
public class PreProcessingData implements Serializable{
    @PrimaryKey(autoGenerate = true)
    private int ID;

    @ColumnInfo(name = "id_course")
    private int id_course;

    @ColumnInfo(name = "rataRata")
    private float average;
    @ColumnInfo(name = "kuadarat")
    private double square;
    @ColumnInfo(name = "akar")
    private double root;

    public int getId_course() {
        return id_course;
    }

    public void setId_course(int id_course) {
        this.id_course = id_course;
    }

    public float getAverage() {
        return average;
    }

    public void setAverage(float average) {
        this.average = average;
    }

    public double getSquare() {
        return square;
    }

    public void setSquare(double square) {
        this.square = square;
    }

    public double getRoot() {
        return root;
    }

    public void setRoot(double root) {
        this.root = root;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }


  
}

