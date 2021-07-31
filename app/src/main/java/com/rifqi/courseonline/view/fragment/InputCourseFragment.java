package com.rifqi.courseonline.view.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.rifqi.courseonline.R;
import com.rifqi.courseonline.helper.FileHelper;
import com.rifqi.courseonline.model.RoomDB;
import com.rifqi.courseonline.model.entities.CategoryData;
import com.rifqi.courseonline.model.entities.CourseData;
import com.rifqi.courseonline.model.entities.MentorData;
import com.rifqi.courseonline.model.entities.RatingData;
import com.rifqi.courseonline.model.entities.UserData;
import com.rifqi.courseonline.view.activity.MainActivity;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class InputCourseFragment extends Fragment{
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private Button btn_save, btn_image, btn_category;
    private ImageView img_course;
    OutputStream outputStream;
    RoomDB database;

    private static final String TAG = InputCourseFragment.class.getName();
    private CategoryData category_selected;
    private MentorData mentor_selected;
    private ArrayList<CourseData> listData = new ArrayList<>();
    private ArrayAdapter<CategoryData> arrayAdapter;
    private MainActivity mainActivity;
    private ArrayAdapter<MentorData> arrayAdapterMentor;
    private List<CategoryData> listCategoryData = new ArrayList<>();
    private List<MentorData> listMentorData = new ArrayList<>();
    private List<UserData> userData  = new ArrayList<UserData>();
    private EditText nama_course, harga_course, deskripsi_course, category_course;
    private Spinner kategori_course, mentor_course;
    private FloatingActionButton btn_add_category, btn_add_mentor;
    private String imageFileName;
    RequestOptions roPlaceholder;

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getActivity()instanceof MainActivity) {
            mainActivity = (MainActivity) getActivity();
        }
    }
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        final View InputCourseFragment = inflater.inflate(R.layout.fragment_input_course, container, false);

        img_course = InputCourseFragment.findViewById(R.id.iv_course);
        nama_course = InputCourseFragment.findViewById(R.id.et_nama_kursus);
        harga_course = InputCourseFragment.findViewById(R.id.et_harga_kursus);
        deskripsi_course = InputCourseFragment.findViewById(R.id.et_deskripsi_kursus);
        kategori_course = InputCourseFragment.findViewById(R.id.spinner_kategori_kursus);
        mentor_course = InputCourseFragment.findViewById(R.id.spinner_mentor_kursus);
        btn_save= InputCourseFragment.findViewById(R.id.btn_input_course);
        btn_add_category =InputCourseFragment.findViewById(R.id.add_category_float_btn);
        btn_add_mentor = InputCourseFragment.findViewById(R.id.add_mentor_float_btn);
        roPlaceholder = new RequestOptions().placeholder(R.drawable.ic_union);

        btn_image = InputCourseFragment.findViewById(R.id.btn_save_image);


        database = RoomDB.getInstance(getContext());
        database.courseDao().getAll();
        listCategoryData = database.categoryDao().getAll();
        listMentorData = database.mentorDao().getAll();


        arrayAdapter = new ArrayAdapter<CategoryData>(getContext(),android.R.layout.simple_spinner_item, listCategoryData);
        arrayAdapterMentor = new ArrayAdapter<MentorData>(getContext(),android.R.layout.simple_spinner_item, listMentorData);

        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        arrayAdapterMentor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        kategori_course.setAdapter(arrayAdapter);
        mentor_course.setAdapter(arrayAdapterMentor);

        kategori_course.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                category_selected = (CategoryData) adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mentor_course.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mentor_selected = (MentorData) adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input_name = nama_course.getText().toString().trim();
                String input_harga = harga_course.getText().toString().trim();
                String input_deskripsi = deskripsi_course.getText().toString().trim();
                if (!input_name.equals("")&&!input_harga.equals("")&&!input_deskripsi.equals("")){
                    CourseData data = new CourseData();
                    data.setNama(input_name);
                    data.setHarga(input_harga);
                    data.setIdKategori(category_selected.getID());
                    data.setIdMentor(mentor_selected.getID());
                    data.setType("course");
                    data.setKeterangan(input_deskripsi);
                    data.setGambar(imageFileName);


                    database.courseDao().insert(data);
                    CourseData courseData = database.courseDao().courseRating(input_name);
                    userData = database.userDao().getAll();
                    for(int i = 0; i < userData.size(); i++ ){
                        //Log.d(TAG, ""+courseData.get(i).getID());
                        RatingData ratingData = new RatingData();
                        ratingData.setId_user(userData.get(i).getID());
                        ratingData.setId_course(courseData.getID());
                        ratingData.setRating(0);
                        database.ratingDao().insert(ratingData);
                    }

                    nama_course.setText("");
                    harga_course.setText("");
                    deskripsi_course.setText("");

                    listData.clear();
                    listData.addAll(database.courseDao().getAll());
                    requireActivity().onBackPressed();
                }
            }
        });

        btn_add_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewCategoryDialog();
            }
        });

        btn_add_mentor.setOnClickListener(view -> {
            Bundle bundle=new Bundle();
            bundle.putInt("message", 0);
            bundle.putString("type", "mentor");
            InputMentorFragment inputMentorFragment = new InputMentorFragment();
            inputMentorFragment.setArguments(bundle);
            mainActivity.replaceFragment(R.id.frame_container,inputMentorFragment,true);
        });

        btn_image.setOnClickListener(view -> {
            if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 500);

            }


            if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(2, 1)
                        .start(requireActivity());
            }
        });
        return InputCourseFragment;
    }

    public void getSecelctedUser(View v){
        CategoryData categoryData = (CategoryData) kategori_course.getSelectedItem();
    }
    public void getSecelctedMentor(View v){
        MentorData mentorData = (MentorData) mentor_course.getSelectedItem();
    }

    public void createNewCategoryDialog(){
        dialogBuilder = new AlertDialog.Builder(getContext());
        final View categoryPopUpView = getLayoutInflater().inflate(R.layout.add_category_pop_up,null);
        category_course = categoryPopUpView.findViewById(R.id.et_kategori_kursus);
        btn_category = categoryPopUpView.findViewById(R.id.btn_save_category);

        dialogBuilder.setView(categoryPopUpView);
        dialog = dialogBuilder.create();
        dialog.show();

        btn_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input_kategori = category_course.getText().toString().trim();
                if (!input_kategori.equals("")){
                    CategoryData categoryData = new CategoryData();
                    categoryData.setKategori(input_kategori);

                    database.categoryDao().insert(categoryData);
                    category_course.setText("");

                    listCategoryData.clear();
                    listCategoryData.addAll(database.categoryDao().getAll());
                    arrayAdapter.notifyDataSetChanged();
                    dialog.dismiss();
                }
            }
        });
    }
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                Log.d(TAG, "onActvityResult: Image Uri = " + resultUri.toString());

                imageFileName = "avatar_" + System.currentTimeMillis() + "_" + System.currentTimeMillis() + ".jpg";
                String target = FileHelper.FILE_SCHEMA + FileHelper.getCourseThumbPath(getContext());
                target += imageFileName;
                Uri targetUri = Uri.parse(target);

                try {
                    FileHelper.copyFile(new File(resultUri.getPath()), new File(targetUri.getPath()));
                    loadAvatar(targetUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                error.printStackTrace();
            }
        }
    }
    private void loadAvatar(Uri targetUri) {
        try {
            Glide.with(requireActivity())
                    .applyDefaultRequestOptions(roPlaceholder)
                    .load(targetUri)
                    .centerCrop()
                    .into(img_course);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}