package com.rifqi.courseonline.view.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.rifqi.courseonline.R;
import com.rifqi.courseonline.helper.FileHelper;
import com.rifqi.courseonline.model.RoomDB;
import com.rifqi.courseonline.model.dao.LogedInUserDao;
import com.rifqi.courseonline.model.dao.PreProcessingDao;
import com.rifqi.courseonline.model.entities.LogedInUserData;
import com.rifqi.courseonline.model.entities.MentorData;
import com.rifqi.courseonline.model.entities.PreProcessingData;
import com.rifqi.courseonline.model.entities.RatingData;
import com.rifqi.courseonline.model.entities.view.VCourse;
import com.rifqi.courseonline.view.fragment.ProfileFragment;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CourseActivityDetail extends AppCompatActivity {
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    public static final String EXTRA_ID = "EXTRA_ID" ;
    private static final String TAG = "MoveActivity";
    public static final String EXTRA_IMG = "CATEGORY";
    private List<RatingData> ratingData = new ArrayList<RatingData>();
    private RoomDB database;
    private ViewFlipper viewFlipper;
    private int id_course;
    private CircleImageView ivMentor;
    private ImageView gambar;
    private String imageFileNameMentor;
    private String imageFileNameCourse;
    RequestOptions roPlaceholder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);
        id_course = getIntent().getIntExtra(EXTRA_ID, 0);
        //listCourse.addAll(CourseData.getListData());
        TextView nama = findViewById(R.id.tv_nama_kursus);
        TextView deskripsi = findViewById(R.id.tv_deskripsi_khursus);
        TextView kategori = findViewById(R.id.tv_kategori_khursus);
        TextView nilai = findViewById(R.id.tv_rating_khursus);
        TextView harga = findViewById(R.id.tv_harga_khursus);
        TextView tipe = findViewById(R.id.tv_type_kursus);
        TextView tanggal = findViewById(R.id.tv_tanggal_workshop);
        TextView namaMentor = findViewById(R.id.tv_pemateri_khursus);
        TextView bidangMentor = findViewById(R.id.tv_pemateri_master_khursus);
        TextView tvMae = findViewById(R.id.tv_mae_khursus_score);
        LinearLayout maeLayout = findViewById(R.id.ll_mae_sign);
        gambar = findViewById(R.id.iv_gambar_khursus);
        Button btn_add = findViewById(R.id.btn_course);
        Button btn_edit = findViewById(R.id.btn_edit_photo);
        ivMentor = findViewById(R.id.iv_pemateri);
        roPlaceholder = new RequestOptions().placeholder(R.drawable.ic_union);



        database = RoomDB.getInstance(this);
        LogedInUserData loged = database.logedInUserDao().checkUser();
        if (database.adminDao().check_logIn() != null){
            maeLayout.setVisibility(View.VISIBLE);
            btn_add.setVisibility(View.GONE);
            countMae(tvMae);
        }

        VCourse vCourse = database.courseDao().getVCourse(id_course);
        MentorData mentorData = database.mentorDao().getMentor(vCourse.getIdMentor());

        String name = vCourse.getNama();
        String detail = vCourse.getKeterangan();
        String category = vCourse.getKategori();
        String rating = ratingAvg(id_course);
        String price = vCourse.getHarga();
        int img = getIntent().getIntExtra(EXTRA_IMG, 0);



        nama.setText(name);
        deskripsi.setText(detail);
        kategori.setText(category);
        tipe.setText(vCourse.getType());
        namaMentor.setText(mentorData.getNama());
        bidangMentor.setText(mentorData.getPekerjaan());
        if (vCourse.getType().equals("workshop")){
            tanggal.setVisibility(View.VISIBLE);
            tanggal.setText(vCourse.getTanggal());
        }
        nilai.setText(rating);
        harga.setText(price);
        imageFileNameMentor = mentorData.getGambar();
        imageFileNameCourse = vCourse.getGambar();

        String targetMentor = FileHelper.FILE_SCHEMA + FileHelper.getProfileThumbPath(this);
        String targetCourse = FileHelper.FILE_SCHEMA + FileHelper.getCourseThumbPath(this);

        targetMentor += imageFileNameMentor;
        targetCourse += imageFileNameCourse;

        Uri targetUriMentor = Uri.parse(targetMentor);
        Uri targetUriCourse = Uri.parse(targetCourse);

        loadAvatar(targetUriMentor);
        loadAvatarCourse(targetUriCourse);

        ivMentor.setOnClickListener(view -> {
            Bundle bundle=new Bundle();
            bundle.putInt("message", mentorData.getID());
            bundle.putString("type", "mentor");
            ProfileFragment profileFragment = new ProfileFragment();
            profileFragment.setArguments(bundle);
            replaceFragment(R.id.frame_container_detail, profileFragment, true);
        });
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final LogedInUserDao loged = database.logedInUserDao();
                LogedInUserData loged1 = loged.checkUser();
                RatingData ratingData = database.ratingDao().checkRating(loged1.getId_user(),id_course);
                if (ratingData.getLearned()==1){
                    Toast.makeText(getApplicationContext(), "Kursus Telah Diambil", Toast.LENGTH_SHORT).show();
                }else{
                    createNewRatingDialog();
                }
            }
        });
        Log.d(TAG, "onCreate: started.");

        btn_edit.setOnClickListener(view -> {

        });

    }

    private void countMae(TextView tv) {
        List<RatingData> ratings = database.ratingDao().courseUser(id_course);
        double mae = 0;
        for (RatingData rating :
                ratings) {
            VCourse course = database.courseDao().getCoursePredictionValue(rating.getId_user(), id_course);
            if (course != null){
                mae += Math.abs(course.getPrediksi() - rating.getRating());
            }
        }

        mae /= ratings.size();
        DecimalFormat decimalFormat = new DecimalFormat("#.#####");
        tv.setText(decimalFormat.format(mae));
    }

    private void createNewRatingDialog() {
        dialogBuilder = new AlertDialog.Builder(this);
        final View ratingPopUpView = getLayoutInflater().inflate(R.layout.add_rating_pop_up,null);
        database = RoomDB.getInstance(this);
        Button btnSaveRatingCourse = ratingPopUpView.findViewById(R.id.btn_rate_course);
        Button btnCancleRatingCourse = ratingPopUpView.findViewById(R.id.btn_cancle);
        RadioGroup radioGroupRating = ratingPopUpView.findViewById(R.id.rg_rating);

        RatingData data = new RatingData();
        final LogedInUserDao loged = database.logedInUserDao();
        LogedInUserData loged1 = loged.checkUser();

        RatingData ratingData = database.ratingDao().checkRating(loged1.getId_user(),id_course);

        dialogBuilder.setView(ratingPopUpView);
        dialog = dialogBuilder.create();
        dialog.show();

        btnSaveRatingCourse.setOnClickListener(view -> {
            int checkId = radioGroupRating.getCheckedRadioButtonId();
            data.setID(ratingData.getID());
            data.setId_user(loged1.getId_user());
            data.setId_course(id_course);
            data.setRating(findRadioButton(checkId));
            data.setLearned(1);
            database.ratingDao().insert(data);
            preProcessingData();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
        btnCancleRatingCourse.setOnClickListener(view -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
    }

    private void preProcessingData() {
        float average;
        double square, root;
        database = RoomDB.getInstance(this);
        average = setAverage();
        square = setSquare(average);

        root = Math.sqrt(square);

        PreProcessingData processingData = new PreProcessingData();

        final PreProcessingDao processingDao = database.preProcessingDao();
        PreProcessingData check = processingDao.searchCourse(id_course);

        if (database.preProcessingDao().searchCourse(id_course) == null){
            processingData.setId_course(id_course);
            processingData.setAverage(average);
            processingData.setSquare(square);
            processingData.setRoot(root);
            database.preProcessingDao().insert(processingData);
        }else{
            processingData.setID(check.getID());
            processingData.setId_course(id_course);
            processingData.setAverage(average);
            processingData.setSquare(square);
            processingData.setRoot(root);
            database.preProcessingDao().insert(processingData);

        }
    }



    private double setSquare(float average) {
        float total = 0;
        ratingData = database.ratingDao().courseUser(id_course);
        for(int i = 0; i < ratingData.size(); i++ ){
            total += Math.pow((ratingData.get(i).getRating() - average), 2);
        }
        return total;
    }

    private double setRoot(float average) {
        float total = 0;
        ratingData = database.ratingDao().courseUser(id_course);
        for(int i = 0; i < ratingData.size(); i++ ){
            total += Math.pow((ratingData.get(i).getRating() - average), 2);
        }
        return Math.sqrt(total);
    }

    private float setAverage() {
        float rateAvg = 0, total = 0;
        database = RoomDB.getInstance(this);
        ratingData = database.ratingDao().courseUser(id_course);
        for(int i = 0; i < ratingData.size(); i++ ){
            total += ratingData.get(i).getRating();
        }
        rateAvg = total/ratingData.size();;
        return rateAvg ;
    }

    private String ratingAvg(int id_course) {
        float rateAvg = 0, total = 0;
        database = RoomDB.getInstance(this);
        ratingData = database.ratingDao().courseUserRating(id_course);
        for(int i = 0; i < ratingData.size(); i++ ){
            total += ratingData.get(i).getRating();
        }
        rateAvg = total/ratingData.size();
        DecimalFormat decimalFormat = new DecimalFormat("#.#");
        return decimalFormat.format(rateAvg) ;
    }

    private int findRadioButton(int checkId) {
        int rating = 0;
        switch (checkId){
            case R.id.rb_rating1:
                rating = 1;
                break;
            case R.id.rb_rating2:
                rating = 2;
                break;
            case R.id.rb_rating3:
                rating = 3;
                break;
            case R.id.rb_rating4:
                rating = 4;
                break;
            case R.id.rb_rating5:
                rating = 5;
                break;
        }
        return rating;
    }


    public void replaceFragment(int frameId, Fragment fragment, boolean backStack) {
        if (backStack) {
            getSupportFragmentManager().beginTransaction().replace(frameId,
                    fragment).addToBackStack(null).commit();
        }
        else {
            getSupportFragmentManager().beginTransaction().replace(frameId,
                    fragment).commit();
        }
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() >= 1) {
            getSupportFragmentManager().popBackStack();
        }else{
            finish();
        }
    }
    public void loadAvatar(Uri targetUri) {
        try {
            Glide.with(this)
                    .applyDefaultRequestOptions(roPlaceholder)
                    .load(targetUri)
                    .centerCrop()
                    .into(ivMentor);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void loadAvatarCourse(Uri targetUri) {
        try {
            Glide.with(this)
                    .applyDefaultRequestOptions(roPlaceholder)
                    .load(targetUri)
                    .centerCrop()
                    .into(gambar);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}