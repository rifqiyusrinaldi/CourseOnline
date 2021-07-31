package com.rifqi.courseonline.model.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.rifqi.courseonline.model.entities.CourseData;
import com.rifqi.courseonline.model.entities.view.VCourse;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface CourseDao {
    @Insert(onConflict = REPLACE)
    void insert(CourseData courseData);

    @Delete
    void delete(CourseData courseData);

    @Query("SELECT * FROM course")
    List<CourseData> getAll();

    @Query("SELECT c.*, cat.kategori, 0 as prediksi FROM course c JOIN category cat ON c.id_kategori = cat.ID JOIN mentor m ON c.id_mentor = m.ID WHERE c.type = 'course'")
    List <VCourse> getVCourse();

    @Query("SELECT c.*, cat.kategori, 0 as prediksi FROM course c JOIN category cat ON c.id_kategori = cat.ID JOIN mentor m ON c.id_mentor = m.ID WHERE c.type = 'workshop'")
    List <VCourse> getVWorkshop();

    @Query("SELECT c.*, cat.kategori, 0 as prediksi FROM course c JOIN category cat ON c.id_kategori = cat.ID JOIN mentor m ON c.id_mentor = m.ID WHERE c.ID = :id")
    VCourse getVCourse(int id);


    @Query("SELECT c.*, cat.kategori, 0 as prediksi FROM course c JOIN rating rat ON c.ID = rat.id_course JOIN category cat ON c.id_kategori = cat.ID JOIN mentor m ON c.id_mentor = m.ID WHERE rat.id_user = :id AND rat.learned = 1")
    List<VCourse> getVCourseUser(int id);

    @Query("SELECT c.*, cat.kategori, 0 as prediksi FROM course c JOIN rating rat ON c.ID = rat.id_course JOIN category cat ON c.id_kategori = cat.ID JOIN mentor m ON c.id_mentor = m.ID WHERE m.ID = :id")
    List<VCourse> getVCourseMentor(int id);

    @Query("SELECT * FROM course WHERE nama = :courseName")
    CourseData courseRating(String courseName);

    @Query("SELECT c.*, cat.kategori, course_prediction.prediksi FROM \n" +
            "course c\n" +
            "JOIN\n" +
            "(SELECT id1 AS id_course, (SELECT rataRata FROM preprocessing WHERE id_course = id1)+SUM(similarityXr_rr)/SUM(ABS(similarity)) AS prediksi FROM\n" +
            "(SELECT r_rr_p1.id_course as id1, r_rr_p2.id_course as id2, (SUM(r_rr_p1.r_rr*r_rr_p2.r_rr)/((SELECT akar FROM preprocessing WHERE id_course = r_rr_p1.id_course)*(SELECT akar FROM preprocessing WHERE id_course = r_rr_p2.id_course))) similarity, (SELECT (r.rating - p.rataRata) AS r_rr FROM user u JOIN rating r ON r.id_user = u.ID JOIN preprocessing p ON p.id_course = r.id_course WHERE r.id_course = r_rr_p2.id_course AND u.ID = :id_user)*((SUM(r_rr_p1.r_rr*r_rr_p2.r_rr)/((SELECT akar FROM preprocessing WHERE id_course = r_rr_p1.id_course)*(SELECT akar FROM preprocessing WHERE id_course = r_rr_p2.id_course)))) similarityXr_rr  FROM \n" +
            "(SELECT u.ID, r.id_course, (r.rating - p.rataRata) AS r_rr FROM user u JOIN rating r ON r.id_user = u.ID JOIN preprocessing p ON p.id_course = r.id_course WHERE r.id_course IN (SELECT ID from course) ORDER BY u.ID) AS r_rr_p1\n" +
            "JOIN\n" +
            "(SELECT u.ID, r.id_course, (r.rating - p.rataRata) AS r_rr FROM user u JOIN rating r ON r.id_user = u.ID JOIN preprocessing p ON p.id_course = r.id_course WHERE r.id_course IN (SELECT ID from course) ORDER BY u.ID) AS r_rr_p2\n" +
            "ON r_rr_p1.ID = r_rr_p2.ID GROUP BY r_rr_p1.id_course,  r_rr_p2.id_course ) AS similarity_table\n" +
            "GROUP BY id1) course_prediction\n" +
            "ON c.ID = course_prediction.id_course\n" +
            "JOIN category cat ON c.id_kategori = cat.ID\n" +
            "WHERE c.ID IN (SELECT id_course FROM rating WHERE id_user = :id_user AND learned = 0)\n" +
            "ORDER BY course_prediction.prediksi DESC")
    List<VCourse> getCourseRecomendation(int id_user);

    @Query("SELECT c.*, cat.kategori, course_prediction.prediksi FROM \n" +
            "course c\n" +
            "JOIN\n" +
            "(SELECT id1 AS id_course, (SELECT rataRata FROM preprocessing WHERE id_course = id1)+SUM(similarityXr_rr)/SUM(ABS(similarity)) AS prediksi FROM\n" +
            "(SELECT r_rr_p1.id_course as id1, r_rr_p2.id_course as id2, (SUM(r_rr_p1.r_rr*r_rr_p2.r_rr)/((SELECT akar FROM preprocessing WHERE id_course = r_rr_p1.id_course)*(SELECT akar FROM preprocessing WHERE id_course = r_rr_p2.id_course))) similarity, (SELECT (r.rating - p.rataRata) AS r_rr FROM user u JOIN rating r ON r.id_user = u.ID JOIN preprocessing p ON p.id_course = r.id_course WHERE r.id_course = r_rr_p2.id_course AND u.ID = :id_user)*((SUM(r_rr_p1.r_rr*r_rr_p2.r_rr)/((SELECT akar FROM preprocessing WHERE id_course = r_rr_p1.id_course)*(SELECT akar FROM preprocessing WHERE id_course = r_rr_p2.id_course)))) similarityXr_rr  FROM \n" +
            "(SELECT u.ID, r.id_course, (r.rating - p.rataRata) AS r_rr FROM user u JOIN rating r ON r.id_user = u.ID JOIN preprocessing p ON p.id_course = r.id_course WHERE r.id_course IN (SELECT ID from course) ORDER BY u.ID) AS r_rr_p1\n" +
            "JOIN\n" +
            "(SELECT u.ID, r.id_course, (r.rating - p.rataRata) AS r_rr FROM user u JOIN rating r ON r.id_user = u.ID JOIN preprocessing p ON p.id_course = r.id_course WHERE r.id_course IN (SELECT ID from course) ORDER BY u.ID) AS r_rr_p2\n" +
            "ON r_rr_p1.ID = r_rr_p2.ID GROUP BY r_rr_p1.id_course,  r_rr_p2.id_course ) AS similarity_table\n" +
            "GROUP BY id1) course_prediction\n" +
            "ON c.ID = course_prediction.id_course\n" +
            "JOIN category cat ON c.id_kategori = cat.ID\n" +
            "WHERE c.ID = :course_id\n" +
            "ORDER BY course_prediction.prediksi DESC LIMIT 1")
    VCourse getCoursePredictionValue(int id_user, int course_id);
}
