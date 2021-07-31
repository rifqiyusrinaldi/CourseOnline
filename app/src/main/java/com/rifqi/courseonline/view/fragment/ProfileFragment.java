package com.rifqi.courseonline.view.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.rifqi.courseonline.R;
import com.rifqi.courseonline.helper.FileHelper;
import com.rifqi.courseonline.model.RoomDB;
import com.rifqi.courseonline.model.entities.AdminData;
import com.rifqi.courseonline.model.entities.MentorData;
import com.rifqi.courseonline.model.entities.UserData;
import com.rifqi.courseonline.model.entities.view.VCourse;
import com.rifqi.courseonline.view.activity.CourseActivityDetail;
import com.rifqi.courseonline.view.activity.MainActivity;
import com.rifqi.courseonline.view.adapter.GridCourseAdapter;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {
    private MainActivity mainActivity;
    private CourseActivityDetail courseActivityDetail;
    private RecyclerView rvCourse;
    private TextView namaUser, usernameUser, passUser, jkUser, emailMentor, jobMentor, descMentor;
    private CircleImageView ivProfileUser;
    private String imageFileName;
    RequestOptions roPlaceholder;
    private Button btnEdit;
    private RoomDB database;
    private int id_mentor = 0;
    private String type = null;
    private LinearLayout llUser, llAdmin, llUserPlus;
    private List<VCourse> listData = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity()instanceof MainActivity) {
            mainActivity = (MainActivity) getActivity();
        }else if (getActivity()instanceof CourseActivityDetail){
            courseActivityDetail = (CourseActivityDetail) getActivity();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_user, container, false);
        id_mentor = getArguments().getInt("message");
        type = getArguments().getString("type");
        namaUser = view.findViewById(R.id.tv_nama_profile);
        usernameUser = view.findViewById(R.id.tv_user_profile);
        passUser = view.findViewById(R.id.tv_pass_profile);
        jkUser = view.findViewById(R.id.tv_jk_profile);
        ivProfileUser = view.findViewById(R.id.iv_user);
        btnEdit = view.findViewById(R.id.btn_edit_profile);
        rvCourse = view.findViewById(R.id.rv_course);
        emailMentor = view.findViewById(R.id.tv_email_profile);
        jobMentor = view.findViewById(R.id.tv_pekerjaan_profile);
        descMentor = view.findViewById(R.id.tv_desc_profile);
        llUser = view.findViewById(R.id.ll_user);
        llUserPlus = view.findViewById(R.id.ll_user_plus);
        llAdmin = view.findViewById(R.id.ll_admin);
        roPlaceholder = new RequestOptions().placeholder(R.drawable.ic_union);

        database = RoomDB.getInstance(getContext());
        rvCourse.setHasFixedSize(true);
        final AdminData adminData = database.adminDao().check_logIn();
        if (id_mentor !=0 && type.equals("mentor")){
            MentorData mentorData =database.mentorDao().getMentor(id_mentor);
            llUser.setVisibility(View.VISIBLE);
            llUserPlus.setVisibility(View.VISIBLE);
            namaUser.setText(mentorData.getNama());
            jkUser.setText(mentorData.getJk());
            emailMentor.setText(mentorData.getEmail());
            jobMentor.setText(mentorData.getPekerjaan());
            descMentor.setText(mentorData.getKeterangan());
            imageFileName = mentorData.getGambar();
            String target = FileHelper.FILE_SCHEMA + FileHelper.getProfileThumbPath(getContext());
            target += imageFileName;
            Uri targetUri = Uri.parse(target);
            loadAvatar(targetUri);
            listData = database.courseDao().getVCourseMentor(mentorData.getID());
            if (adminData == null){
                llAdmin.setVisibility(View.GONE);
                showRecycleGrid();
            }else {
                llAdmin.setVisibility(View.VISIBLE);
                showRecycleGrid();
                btnEdit.setOnClickListener(view1 -> {
                    Bundle bundle=new Bundle();
                    bundle.putInt("message", mentorData.getID());
                    bundle.putString("type", "mentor");
                    InputMentorFragment inputMentorFragment = new InputMentorFragment();
                    inputMentorFragment.setArguments(bundle);
                    courseActivityDetail.replaceFragment(R.id.frame_container_detail,inputMentorFragment,true);
                });
            }
        }else if (id_mentor !=0 && type.equals("user")){
            UserData userData = database.userDao().user(database.logedInUserDao().checkUser().getId_user());
            llUser.setVisibility(View.GONE);
            llUserPlus.setVisibility(View.GONE);
            llAdmin.setVisibility(View.VISIBLE);
            namaUser.setText(userData.getNama());
            usernameUser.setText(userData.getUsername());
            passUser.setText(userData.getPassword());
            jkUser.setText(userData.getJk());
            rvCourse.setHasFixedSize(true);
            listData = database.courseDao().getVCourseUser(userData.getID());
            imageFileName = userData.getGambar();
            String target = FileHelper.FILE_SCHEMA + FileHelper.getProfileThumbPath(getContext());
            target += imageFileName;
            Uri targetUri = Uri.parse(target);
            loadAvatar(targetUri);
            showRecycleGrid();
            btnEdit.setOnClickListener(view1 -> {
                mainActivity.replaceFragment(R.id.frame_container,new InputUserFragment(),true);
            });
        }

        return view;
    }
    private void showRecycleGrid() {
        rvCourse.setLayoutManager(new GridLayoutManager(getContext(), 1));
        GridCourseAdapter gridCourseAdapter = new GridCourseAdapter(getContext(), listData);
        rvCourse.setAdapter(gridCourseAdapter);


        gridCourseAdapter.setOnItemClickCallback(data -> showSelectedCourse(data));
    }
    private void showSelectedCourse(VCourse course) {
        Intent intent = new Intent(getContext(), CourseActivityDetail.class);
        intent.putExtra(CourseActivityDetail.EXTRA_ID, course.getID());
        startActivity(intent);
    }
    public void loadAvatar(Uri targetUri) {
        try {
            Glide.with(this)
                    .applyDefaultRequestOptions(roPlaceholder)
                    .load(targetUri)
                    .centerCrop()
                    .into(ivProfileUser);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}