package com.rifqi.courseonline.model.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.rifqi.courseonline.model.entities.RatingData;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface RatingDao {
    @Insert(onConflict = REPLACE)
    void insert(RatingData ratingData);

    @Delete
    void delete(RatingData ratingData);

    @Update
    void update(RatingData ratingData);

    @Query("SELECT * FROM rating")
    List<RatingData> getAll();

    @Query("SELECT * FROM rating WHERE id_course = :idCourse")
    List<RatingData> courseUser(int idCourse);

    @Query("SELECT * FROM rating WHERE id_course = :idCourse AND  rating != 0")
    List<RatingData> courseUserRating(int idCourse);

    @Query("SELECT * FROM rating WHERE id_user = :idu AND id_course = :idc")
    RatingData checkRating(int idu, int idc);

    @Query("SELECT *FROM course c JOIN mentor m ON c.id_mentor = m.ID JOIN rating r ON c.ID = r.id_course WHERE r.learned = 1 AND m.ID = :id")
    List<RatingData> getMentorRating(int id);

}
