package com.rifqi.courseonline.model.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.rifqi.courseonline.model.entities.AdminData;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface AdminDao {
    @Insert(onConflict = REPLACE)
    void insert(AdminData adminData);

    @Update
    void update(AdminData adminData);

    @Query("SELECT * FROM admin WHERE username = :usr AND password = :pass")
    AdminData login(String usr, String pass);

    @Query("SELECT * FROM admin WHERE ID = :id")
    AdminData search(int id);

    @Query("SELECT * FROM admin WHERE status_log = 1")
    AdminData check_logIn();

    @Delete
    void delete(AdminData adminData);
}
