package com.rifqi.courseonline.model.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "category")
public class CategoryData implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int ID;

    private String kategori;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    @NonNull
    @Override
    public String toString() {
        return getKategori();
    }
}
