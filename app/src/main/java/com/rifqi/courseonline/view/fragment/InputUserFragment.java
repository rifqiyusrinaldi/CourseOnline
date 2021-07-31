package com.rifqi.courseonline.view.fragment;

import android.Manifest;
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
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.rifqi.courseonline.R;
import com.rifqi.courseonline.helper.FileHelper;
import com.rifqi.courseonline.model.RoomDB;
import com.rifqi.courseonline.model.dao.UserDao;
import com.rifqi.courseonline.model.entities.CourseData;
import com.rifqi.courseonline.model.entities.LogedInUserData;
import com.rifqi.courseonline.model.entities.RatingData;
import com.rifqi.courseonline.model.entities.UserData;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class InputUserFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    //Constructor
    public InputUserFragment(){};
    RoomDB database;
    private Spinner jk_spinner;
    String jk_choose;
    private List<CourseData> courseData  = new ArrayList<CourseData>();
    private static final String TAG = InputUserFragment.class.getName();
    private EditText namaUser, usernameUser, passUser;
    Button btnInput, btnUploadImg;
    private CircleImageView userImg;
    private String imageFileName;
    RequestOptions roPlaceholder;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_input_user, container, false);
        namaUser = view.findViewById(R.id.et_nama_user);
        usernameUser = view.findViewById(R.id.et_username);
        passUser = view.findViewById(R.id.et_password);
        jk_spinner = view.findViewById(R.id.spinner_jk_user);
        btnInput = view.findViewById(R.id.btn_input_user);
        btnUploadImg = view.findViewById(R.id.btn_save_image);
        userImg = view.findViewById(R.id.iv_user);
        roPlaceholder = new RequestOptions().placeholder(R.drawable.ic_union);



        database = RoomDB.getInstance(getContext());
        database.userDao().getAll();
        LogedInUserData loged = database.logedInUserDao().checkUser();
        if (loged != null){
            fillUser(loged.getId_user());
        }

        database = RoomDB.getInstance(getContext());
        UserDao userDao = database.userDao();
        //UserData userData2 = userDao.user(loged.getId_user());



        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.jenis_kelamin, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        jk_spinner.setAdapter(adapter);
        jk_spinner.setOnItemSelectedListener(this);


        btnInput.setOnClickListener(view1 -> {
            String input_name = namaUser.getText().toString().trim();
            String input_username = usernameUser.getText().toString().trim();
            String input_pass = passUser.getText().toString().trim();
            if (!input_name.equals("")&&!input_username.equals("")&&!input_pass.equals("")){
                UserData userData = new UserData();

                userData.setNama(input_name);
                userData.setUsername(input_username);
                userData.setPassword(input_pass);
                userData.setJk(jk_choose);
                userData.setGambar(imageFileName);

                if (loged != null){
                    database.userDao().updateUser(loged.getId_user(), input_name, jk_choose, input_username, input_pass, imageFileName);
                    database.logedInUserDao().updateUserLoged(loged.getID(), input_username, input_pass);
                    //database.userDao().update(userData);

                    namaUser.setText("");
                    passUser.setText("");
                    usernameUser.setText("");

                    Toast.makeText(getContext(), "Akun Berhasil diubah", Toast.LENGTH_SHORT).show();
                    requireActivity().onBackPressed();
                }else {
                    database.userDao().insert(userData);
                    UserData userData1 = database.userDao().userRate(input_username);
                    courseData = database.courseDao().getAll();
                    for(int i = 0; i < courseData.size(); i++ ){
                        //Log.d(TAG, ""+courseData.get(i).getID());
                        RatingData ratingData = new RatingData();
                        ratingData.setId_user(userData1.getID());
                        ratingData.setId_course(courseData.get(i).getID());
                        ratingData.setRating(0);
                        database.ratingDao().insert(ratingData);
                    }
                    namaUser.setText("");
                    passUser.setText("");
                    usernameUser.setText("");
                    Toast.makeText(getContext(), "Akun Berhasil dibuat", Toast.LENGTH_SHORT).show();
                    requireActivity().onBackPressed();
                }
            }
        });
        btnUploadImg.setOnClickListener(view1 -> {
            if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 500);

            }


            if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1)
                        .start(requireActivity());
            }
        });
        return view;
    }

    private void fillUser(int idUser) {
        database = RoomDB.getInstance(getContext());
        UserDao userDao = database.userDao();
        UserData userData = userDao.user(idUser);
        namaUser.setText(userData.getNama());
        usernameUser.setText(userData.getUsername());
        passUser.setText(userData.getPassword());
        imageFileName = userData.getGambar();
        String target = FileHelper.FILE_SCHEMA + FileHelper.getProfileThumbPath(getContext());
        target += imageFileName;
        Uri targetUri = Uri.parse(target);
        loadAvatar(targetUri);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        jk_choose = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                Log.d(TAG, "onActvityResult: Image Uri = " + resultUri.toString());

                imageFileName = "avatar_" + System.currentTimeMillis() + "_" + System.currentTimeMillis() + ".jpg";
                String target = FileHelper.FILE_SCHEMA + FileHelper.getProfileThumbPath(getContext());
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
                    .into(userImg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
