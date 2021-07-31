package com.rifqi.courseonline.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rifqi.courseonline.R;
import com.rifqi.courseonline.model.RoomDB;
import com.rifqi.courseonline.model.entities.LogedInUserData;
import com.rifqi.courseonline.model.entities.view.VCourse;
import com.rifqi.courseonline.view.activity.CourseActivityDetail;
import com.rifqi.courseonline.view.adapter.GridCourseAdapter;

import java.util.ArrayList;
import java.util.List;

public class CourseFragment extends Fragment {
    private RecyclerView rvCourse, rvCourseRecomended;
    //private ArrayList<Course> list = new ArrayList<>();
    private List<VCourse> listData = new ArrayList<>();
    private List<VCourse> listDataRecomended = new ArrayList<>();
    RoomDB database;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View PageOne = inflater.inflate(R.layout.fragment_course, container, false);

        rvCourse = PageOne.findViewById(R.id.rv_course);
        rvCourseRecomended = PageOne.findViewById(R.id.rv_course_recomended);
        LinearLayout recomendedLayout = PageOne.findViewById(R.id.ll_recomended_layout);
        recomendedLayout.setVisibility(View.GONE);

//        rvCourse.setHasFixedSize(true);
//        rvCourseRecomended.setHasFixedSize(true);

        database = RoomDB.getInstance(getContext());
        listData = database.courseDao().getVCourse();
        Log.d("coursefragment", String.valueOf(listData.size()));
        LogedInUserData loged = database.logedInUserDao().checkUser();

        if (database.adminDao().check_logIn() == null){
            listDataRecomended = database.courseDao().getCourseRecomendation(loged.getId_user());
            if (listDataRecomended != null){
                recomendedLayout.setVisibility(View.VISIBLE);
                showRecycleGridRecomended();
                showRecycleGrid();
            }else{
                recomendedLayout.setVisibility(View.GONE);
            }
        }
        showRecycleGrid();
        return PageOne;
    }
    private void showRecycleGrid() {
        rvCourse.setLayoutManager(new GridLayoutManager(getContext(), 1));
//        rvCourse.setNestedScrollingEnabled(false);
        GridCourseAdapter gridCourseAdapter = new GridCourseAdapter(getContext(), listData);
        rvCourse.setAdapter(gridCourseAdapter);


        gridCourseAdapter.setOnItemClickCallback(data -> showSelectedCourse(data));
    }
    private void showRecycleGridRecomended() {
        rvCourseRecomended.setLayoutManager(new GridLayoutManager(getContext(), 1));
//        rvCourse.setNestedScrollingEnabled(false);
        GridCourseAdapter gridCourseAdapter = new GridCourseAdapter(getContext(), listDataRecomended);
        LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rvCourseRecomended.setLayoutManager(manager);
        rvCourseRecomended.setAdapter(gridCourseAdapter);


        gridCourseAdapter.setOnItemClickCallback(data -> showSelectedCourse(data));
    }
    private void showSelectedCourse(VCourse course) {
        Intent intent = new Intent(getContext(), CourseActivityDetail.class);
        intent.putExtra(CourseActivityDetail.EXTRA_ID, course.getID());
        startActivity(intent);
    }
}
