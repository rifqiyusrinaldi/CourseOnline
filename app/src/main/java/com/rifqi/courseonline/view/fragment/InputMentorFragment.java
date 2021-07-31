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
import com.rifqi.courseonline.model.dao.MentorDao;
import com.rifqi.courseonline.model.entities.MentorData;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class InputMentorFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    //private ArrayList<Course> list = new ArrayList<>();
    RoomDB database;
    private EditText namaMentor, emailMentor, ketMentor, pekerjaanMentor;
    private CircleImageView mentorImg;
    private static final String TAG = InputMentorFragment.class.getName();
    private String jk_choose, type;
    private Spinner jkMentor;
    private Button btnInput, addImg;
    private int id_mentor = 0;
    private String imageFileName;
    RequestOptions roPlaceholder;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mentorPage = inflater.inflate(R.layout.fragment_input_mentor, container, false);
        id_mentor = getArguments().getInt("message");
        type = getArguments().getString("type");
        namaMentor = mentorPage.findViewById(R.id.et_nama_mentor);
        emailMentor = mentorPage.findViewById(R.id.et_email_mentor);
        ketMentor = mentorPage.findViewById(R.id.et_deskripsi_mentor);
        pekerjaanMentor = mentorPage.findViewById(R.id.et_pekerjaan_mentor);
        jkMentor = mentorPage.findViewById(R.id.spinner_jk_mentor);
        btnInput = mentorPage.findViewById(R.id.btn_input_mantor);
        addImg = mentorPage.findViewById(R.id.btn_save_image);
        mentorImg = mentorPage.findViewById(R.id.iv_mentor);
        roPlaceholder = new RequestOptions().placeholder(R.drawable.ic_union);

        if (id_mentor != 0 && type.equals("mentor")){
            fillUser(id_mentor);
        }
        database = RoomDB.getInstance(getContext());

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.jenis_kelamin, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        jkMentor.setAdapter(adapter);
        jkMentor.setOnItemSelectedListener(this);

        btnInput.setOnClickListener(view -> {
            String input_name = namaMentor.getText().toString().trim();
            String input_email = emailMentor.getText().toString().trim();
            String input_ket = ketMentor.getText().toString().trim();
            String input_job = pekerjaanMentor.getText().toString().trim();
            if (!input_name.equals("")&&!input_email.equals("")&&!input_ket.equals("")&&!input_job.equals("")){
                MentorData mentorData = new MentorData();
                mentorData.setNama(input_name);
                mentorData.setEmail(input_email);
                mentorData.setKeterangan(input_ket);
                mentorData.setPekerjaan(input_job);
                mentorData.setJk(jk_choose);
                mentorData.setGambar(imageFileName);

                if (database.mentorDao().getMentor(id_mentor) != null){
                    database.mentorDao().update(mentorData);

                    namaMentor.setText("");
                    emailMentor.setText("");
                    ketMentor.setText("");
                    pekerjaanMentor.setText("");

                    Toast.makeText(getContext(), "Mentor Berhasil diubah", Toast.LENGTH_SHORT).show();
                    requireActivity().onBackPressed();
                }else{
                    database.mentorDao().insert(mentorData);

                    namaMentor.setText("");
                    emailMentor.setText("");
                    ketMentor.setText("");
                    pekerjaanMentor.setText("");

                    Toast.makeText(getContext(), "Mentor Berhasil dibuat", Toast.LENGTH_SHORT).show();
                    requireActivity().onBackPressed();
                }
            }
        });
        addImg.setOnClickListener(view -> {
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
        return mentorPage;
    }

    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        jk_choose = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
    private void fillUser(int idUser) {
        database = RoomDB.getInstance(getContext());
        MentorDao mentorDao = database.mentorDao();
        MentorData mentorData = mentorDao.getMentor(idUser);
        namaMentor.setText(mentorData.getNama());
        pekerjaanMentor.setText(mentorData.getPekerjaan());
        ketMentor.setText(mentorData.getKeterangan());
        emailMentor.setText(mentorData.getEmail());
        imageFileName = mentorData.getGambar();
        String target = FileHelper.FILE_SCHEMA + FileHelper.getProfileThumbPath(getContext());
        target += imageFileName;
        Uri targetUri = Uri.parse(target);
        loadAvatar(targetUri);
    }
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
                    .into(mentorImg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
