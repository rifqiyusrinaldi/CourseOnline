package com.rifqi.courseonline.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rifqi.courseonline.R;
import com.rifqi.courseonline.model.RoomDB;
import com.rifqi.courseonline.model.entities.view.VCourse;
import com.rifqi.courseonline.view.activity.CourseActivityDetail;
import com.rifqi.courseonline.view.adapter.GridCourseAdapter;

import java.util.ArrayList;
import java.util.List;

public class WorkshopFragment extends Fragment {
    private RecyclerView rvCourseRecomended;
    //private ArrayList<Course> list = new ArrayList<>();
    private List<VCourse> listData = new ArrayList<>();
    RoomDB database;
    //Constructor
    public WorkshopFragment(){};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View PageTwo = inflater.inflate(R.layout.fragment_workshop, container, false);

        rvCourseRecomended = PageTwo.findViewById(R.id.rv_course);
        rvCourseRecomended.setHasFixedSize(true);

        database = RoomDB.getInstance(getContext());
        listData = database.courseDao().getVWorkshop();
        showRecycleGrid();
        return PageTwo;
    }
    private void showRecycleGrid() {
        rvCourseRecomended.setLayoutManager(new GridLayoutManager(getContext(), 1));
        GridCourseAdapter gridCourseAdapter = new GridCourseAdapter(getContext(), listData);
        rvCourseRecomended.setAdapter(gridCourseAdapter);


        gridCourseAdapter.setOnItemClickCallback(data -> showSelectedCourse(data));
    }

    private void showSelectedCourse(VCourse course) {
        Intent intent = new Intent(getContext(), CourseActivityDetail.class);
        intent.putExtra(CourseActivityDetail.EXTRA_ID, course.getID());
        startActivity(intent);
    }
}
