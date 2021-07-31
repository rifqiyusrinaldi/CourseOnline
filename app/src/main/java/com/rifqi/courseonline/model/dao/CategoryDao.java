package com.rifqi.courseonline.model.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.rifqi.courseonline.model.entities.CategoryData;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface CategoryDao {
    @Insert(onConflict = REPLACE)
    void insert(CategoryData categoryData);

    @Delete
    void delete(CategoryData categoryData);

    @Query("SELECT * FROM category")
    List<CategoryData> getAll();
}
