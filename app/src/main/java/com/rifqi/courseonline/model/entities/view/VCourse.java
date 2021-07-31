package com.rifqi.courseonline.model.entities.view;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

import com.rifqi.courseonline.model.entities.CourseData;

@Entity
public class VCourse extends CourseData {
    private String kategori;
    private String mentor;
    @ColumnInfo(defaultValue = "0")
    private double prediksi = 0d;

    public double getPrediksi() {
        return prediksi;
    }

    public void setPrediksi(double prediksi) {
        this.prediksi = prediksi;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getMentor() {
        return mentor;
    }

    public void setMentor(String mentor) {
        this.mentor = mentor;
    }

}
