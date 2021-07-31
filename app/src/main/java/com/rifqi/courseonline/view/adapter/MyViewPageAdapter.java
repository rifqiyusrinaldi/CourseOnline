package com.rifqi.courseonline.view.adapter;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class MyViewPageAdapter extends FragmentPagerAdapter {
    private List<Fragment> MyFragment = new ArrayList<>();
    private List<String> MyPagetitle = new ArrayList<>();

    public MyViewPageAdapter(FragmentManager manager){
        super(manager);
    }
    public void AddFragmentPage(Fragment Frag, String Title){
        MyFragment.add(Frag);
        MyPagetitle.add(Title);
    }

    @Override
    public Fragment getItem(int position) {
        return MyFragment.get(position);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return MyPagetitle.get(position);
    }

    @Override
    public int getCount() {
        return 2;
    }

    public boolean isFull() {
        return MyFragment.size() == 2;
    }

}
