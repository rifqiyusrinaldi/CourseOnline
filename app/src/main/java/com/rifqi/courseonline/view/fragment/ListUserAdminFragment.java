package com.rifqi.courseonline.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.rifqi.courseonline.R;
import com.rifqi.courseonline.model.RoomDB;
import com.rifqi.courseonline.model.entities.UserData;
import com.rifqi.courseonline.view.activity.MainActivity;
import com.rifqi.courseonline.view.adapter.ListUserAdapter;

import java.util.ArrayList;
import java.util.List;

public class ListUserAdminFragment extends Fragment {
    private RecyclerView rvUser;
    private List<UserData> userData = new ArrayList<UserData>();

    RoomDB database;
    private ListUserAdapter listUserAdapter;
    private MainActivity mainActivity;
    //Constructor
    public ListUserAdminFragment(){};
    private FloatingActionButton btn_add_user;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getActivity()instanceof MainActivity) {
            mainActivity = (MainActivity) getActivity();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_user_admin, container, false);
        rvUser = view.findViewById(R.id.rv_list_user_admin);
        rvUser.setHasFixedSize(true);

        database = RoomDB.getInstance(getContext());
        userData = database.userDao().getAll();



        database.userDao().getAllLiveData().observe(getActivity(),userData1 -> {
            listUserAdapter.updateData(userData1);
        });

        btn_add_user = view.findViewById(R.id.btn_float_add_user);
        btn_add_user.setOnClickListener(view1 -> {
//            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_container_list_user,
//                    new InputUserFragment()).commit();
            mainActivity.replaceFragment(R.id.frame_container_list_user, new InputUserFragment(),true);
        });
        showRecycleRow();
        return view;
    }

    private void showRecycleRow() {
        rvUser.setLayoutManager(new LinearLayoutManager(getContext()));
        listUserAdapter = new ListUserAdapter(getContext(),userData);
        rvUser.setAdapter(listUserAdapter);
    }
}
