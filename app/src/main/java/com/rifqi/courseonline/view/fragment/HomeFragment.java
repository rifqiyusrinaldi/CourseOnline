package com.rifqi.courseonline.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.rifqi.courseonline.R;
import com.rifqi.courseonline.view.adapter.MyViewPageAdapter;


public class HomeFragment extends Fragment {
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private MyViewPageAdapter myViewPageAdapter = null;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tabLayout = view.findViewById(R.id.tl_recomended);
        viewPager = view.findViewById(R.id.MyPage);

        tabLayout.setupWithViewPager(viewPager);
        SetUpViewPager(viewPager);
    }

    public void SetUpViewPager(ViewPager viewPager) {
        if (myViewPageAdapter == null){
            myViewPageAdapter = new MyViewPageAdapter(getChildFragmentManager());

            myViewPageAdapter.AddFragmentPage(new CourseFragment(), "Kursus");
            myViewPageAdapter.AddFragmentPage(new WorkshopFragment(), "Workshop");

        }
        viewPager.setAdapter(myViewPageAdapter);
    }
}