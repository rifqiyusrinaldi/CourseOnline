package com.rifqi.courseonline.model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.rifqi.courseonline.model.entities.MentorData;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface MentorDao {
    @Insert(onConflict = REPLACE)
    void insert(MentorData mentorData);

    @Delete
    void delete(MentorData mentorData);

    @Update
    void update(MentorData mentorData);

    @Query("SELECT * FROM mentor")
    List<MentorData> getAll();

    @Query("SELECT * FROM mentor WHERE ID = :id")
    MentorData getMentor(int id);

    @Query("SELECT * FROM mentor")
    LiveData<List<MentorData>> getAllLiveData();
}
