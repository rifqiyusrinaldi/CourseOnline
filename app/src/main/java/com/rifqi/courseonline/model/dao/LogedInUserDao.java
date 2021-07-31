package com.rifqi.courseonline.model.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.rifqi.courseonline.model.entities.LogedInUserData;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface LogedInUserDao {
    @Insert(onConflict = REPLACE)
    void insert(LogedInUserData logedInUserData);

    @Query("SELECT * FROM logedinuser")
    LogedInUserData checkUser();

    @Query("UPDATE logedinuser SET username = :usr, password = :pass WHERE id = :id")
    void updateUserLoged(long id, String usr, String pass);

    @Delete
    void delete(LogedInUserData logedInUserData);
}
