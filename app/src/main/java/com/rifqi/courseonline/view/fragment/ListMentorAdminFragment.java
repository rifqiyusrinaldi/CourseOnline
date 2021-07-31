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
import com.rifqi.courseonline.model.entities.MentorData;
import com.rifqi.courseonline.view.activity.MainActivity;
import com.rifqi.courseonline.view.adapter.ListMentorAdapter;

import java.util.ArrayList;
import java.util.List;

public class ListMentorAdminFragment extends Fragment {
    private RecyclerView rvMentor;
    private List<MentorData> mentorData = new ArrayList<MentorData>();

    RoomDB database;
    private ListMentorAdapter listMentorAdapter;
    private MainActivity mainActivity;
    //Constructor
    public ListMentorAdminFragment(){};
    private FloatingActionButton btn_add_mentor;

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
        View view = inflater.inflate(R.layout.fragment_list_mentor_admin, container, false);
        rvMentor = view.findViewById(R.id.rv_list_mentor_admin);
        rvMentor.setHasFixedSize(true);

        database = RoomDB.getInstance(getContext());
        mentorData = database.mentorDao().getAll();



        database.mentorDao().getAllLiveData().observe(getActivity(),mentorData -> {
            listMentorAdapter.updateData(mentorData);
        });

        btn_add_mentor = view.findViewById(R.id.add_mentor_float_btn);
        btn_add_mentor.setOnClickListener(view1 -> {
            Bundle bundle=new Bundle();
            bundle.putInt("message", 0);
            bundle.putString("type", "mentor");
            InputMentorFragment inputMentorFragment = new InputMentorFragment();
            inputMentorFragment.setArguments(bundle);
            mainActivity.replaceFragment(R.id.frame_container_list_user, inputMentorFragment,true);
        });
        showRecycleRow();
        return view;
    }

    private void showRecycleRow() {
        rvMentor.setLayoutManager(new LinearLayoutManager(getContext()));
        listMentorAdapter = new ListMentorAdapter(getContext(),mentorData);
        rvMentor.setAdapter(listMentorAdapter);
    }
}
