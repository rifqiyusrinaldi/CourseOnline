package com.rifqi.courseonline.model.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "admin")
public class AdminData implements Serializable{
    @PrimaryKey(autoGenerate = true)
    private int ID;

    @ColumnInfo(name = "username")
    private String username;
    @ColumnInfo(name = "password")
    private String password;
    @ColumnInfo(name = "status_log")
    private int status_log;

    public int getStatus_log() {
        return status_log;
    }

    public void setStatus_log(int status_log) {
        this.status_log = status_log;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
}

