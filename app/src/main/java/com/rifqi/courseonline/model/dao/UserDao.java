package com.rifqi.courseonline.model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.rifqi.courseonline.model.entities.UserData;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface UserDao {
    @Insert(onConflict = REPLACE)
    void insert(UserData userData);

    @Delete
    void delete(UserData userData);

    @Update
    void update(UserData userData);

    @Query("SELECT * FROM user")
    List<UserData> getAll();

    @Query("SELECT * FROM user")
    LiveData<List<UserData>> getAllLiveData();

    @Query("SELECT * FROM user WHERE username = :usr AND password = :pass")
    UserData login(String usr, String pass);

    @Query("SELECT * FROM user WHERE ID = :id")
    UserData user(int id);

    @Query("SELECT * FROM user WHERE username = :usr")
    UserData userRate(String usr);

    @Query("UPDATE user SET nama = :nama, jk = :jk, username = :usr, password = :pass, gambar = :img WHERE id = :id")
    void updateUser(long id, String nama, String jk, String usr, String pass, String img);
}
