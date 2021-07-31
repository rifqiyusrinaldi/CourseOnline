package com.rifqi.courseonline.model;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.rifqi.courseonline.model.dao.AdminDao;
import com.rifqi.courseonline.model.dao.CategoryDao;
import com.rifqi.courseonline.model.dao.CourseDao;
import com.rifqi.courseonline.model.dao.LogedInUserDao;
import com.rifqi.courseonline.model.dao.MentorDao;
import com.rifqi.courseonline.model.dao.PreProcessingDao;
import com.rifqi.courseonline.model.dao.RatingDao;
import com.rifqi.courseonline.model.dao.UserDao;
import com.rifqi.courseonline.model.entities.AdminData;
import com.rifqi.courseonline.model.entities.CategoryData;
import com.rifqi.courseonline.model.entities.CourseData;
import com.rifqi.courseonline.model.entities.LogedInUserData;
import com.rifqi.courseonline.model.entities.MentorData;
import com.rifqi.courseonline.model.entities.PreProcessingData;
import com.rifqi.courseonline.model.entities.RatingData;
import com.rifqi.courseonline.model.entities.UserData;

@Database(entities = {CourseData.class, CategoryData.class, UserData.class,
        AdminData.class, RatingData.class,
        LogedInUserData.class, PreProcessingData.class,
        MentorData.class},version = 12,exportSchema = false)

public abstract class RoomDB extends RoomDatabase {
    private static RoomDB database;

    private static String DATEBASE_NAME = "database";

    public synchronized static RoomDB getInstance(Context context){
        if (database==null){
            database = Room.databaseBuilder(context.getApplicationContext()
                    ,RoomDB.class,DATEBASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return database;
    }
    public abstract CourseDao courseDao();
    public abstract CategoryDao categoryDao();
    public abstract UserDao userDao();
    public abstract AdminDao adminDao();
    public abstract RatingDao ratingDao();
    public abstract LogedInUserDao logedInUserDao();
    public abstract PreProcessingDao preProcessingDao();
    public abstract MentorDao mentorDao();
}
