package com.rifqi.courseonline.model.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.rifqi.courseonline.model.entities.PreProcessingData;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface PreProcessingDao {

    @Insert(onConflict = REPLACE)
    void insert(PreProcessingData preProcessingData);

    @Update
    void update(PreProcessingData preProcessingData);

    @Query("SELECT * FROM preprocessing")
    List<PreProcessingData> getAll();


    @Query("DELETE FROM preprocessing")
    void deleteResult();

    @Query("SELECT * FROM preprocessing WHERE id_course = :id")
    PreProcessingData searchCourse(int id);

    @Query("SELECT * FROM preprocessing WHERE ID = :id")
    PreProcessingData search(int id);

    @Delete
    void delete(PreProcessingData preProcessingData);
}
