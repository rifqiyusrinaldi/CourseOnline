package com.rifqi.courseonline.view.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.navigation.NavigationView;
import com.rifqi.courseonline.R;
import com.rifqi.courseonline.helper.FileHelper;
import com.rifqi.courseonline.model.RoomDB;
import com.rifqi.courseonline.model.dao.LogedInUserDao;
import com.rifqi.courseonline.model.entities.AdminData;
import com.rifqi.courseonline.model.entities.LogedInUserData;
import com.rifqi.courseonline.model.entities.PreProcessingData;
import com.rifqi.courseonline.model.entities.RatingData;
import com.rifqi.courseonline.model.entities.UserData;
import com.rifqi.courseonline.view.fragment.HomeFragment;
import com.rifqi.courseonline.view.fragment.InputCourseFragment;
import com.rifqi.courseonline.view.fragment.InputWorkshopFragment;
import com.rifqi.courseonline.view.fragment.ListMentorAdminFragment;
import com.rifqi.courseonline.view.fragment.ListUserAdminFragment;
import com.rifqi.courseonline.view.fragment.ProfileFragment;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private RoomDB database;
    private static final String TAG = MainActivity.class.getName();
    public static final String EXTRA_ID = "EXTRA_ID" ;
    public static final String EXTRA_USER = "EXTRA_USER" ;
    private List<RatingData> ratingData = new ArrayList<RatingData>();
    private List<PreProcessingData> preProcessingData = new ArrayList<PreProcessingData>();
    public Button logoutBtn;
    public String userType;
    private CircleImageView usrImage;
    private String imageFileName;
    public int idUser;
    RequestOptions roPlaceholder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        database = RoomDB.getInstance(this);
        drawerLayout = findViewById(R.id.drawer_layout);
        roPlaceholder = new RequestOptions().placeholder(R.drawable.ic_union);
        toggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close);

        userType = getIntent().getStringExtra(EXTRA_USER);
        idUser = getIntent().getIntExtra(EXTRA_ID, 0);

        Log.d(TAG, userType+" "+idUser);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setNavigationViewListener();
        replaceFragment(R.id.frame_container, new HomeFragment(), true);
        //similarityCount();

    }




    private void setNavigationViewListener() {
        navigationView = findViewById(R.id.nav_menu);
        View headerNavigation = navigationView.getHeaderView(0);
        TextView tvUser = headerNavigation.findViewById(R.id.tv_nama_user_nav);
        usrImage = headerNavigation.findViewById(R.id.img_item_photo);
        Menu menuItem = navigationView.getMenu();
        LogedInUserData loged = database.logedInUserDao().checkUser();
        if(loged != null){
            UserData userData = database.userDao().user(loged.getId_user());
            //Log.d(TAG, userData.getNama());
            tvUser.setText(userData.getNama());
            imageFileName = userData.getGambar();
            String target = FileHelper.FILE_SCHEMA + FileHelper.getProfileThumbPath(this);
            target += imageFileName;
            Uri targetUri = Uri.parse(target);
            loadAvatar(targetUri);
            menuItem.findItem(R.id.user_navigation).setVisible(true);
            menuItem.findItem(R.id.admin_navigation).setVisible(false);
            navigationView.setNavigationItemSelectedListener(this);
        }else{
            AdminData adminData = database.adminDao().search(idUser);
            tvUser.setText(adminData.getUsername());
            menuItem.findItem(R.id.user_navigation).setVisible(false);
            menuItem.findItem(R.id.admin_navigation).setVisible(true);
            navigationView.setNavigationItemSelectedListener(this);
        }
        logoutBtn = findViewById(R.id.btn_logout);
        logoutBtn.setOnClickListener(view -> {
            if (loged != null){
                Intent intent = new Intent(this, LoginActivity.class);
                final LogedInUserDao logedInUserDao = database.logedInUserDao();
                LogedInUserData l = logedInUserDao.checkUser();
                database.logedInUserDao().delete(l);
                startActivity(intent);
                finish();
            }else {
                Intent intent = new Intent(this, LoginActivity.class);
                AdminData adminData = new AdminData();
                adminData.setID(1);
                adminData.setUsername("admin");
                adminData.setPassword("123");
                adminData.setStatus_log(0);
                database.adminDao().insert(adminData);
                startActivity(intent);
                finish();
            }
        });
    }



    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case (R.id.item1):{
                Bundle bundle=new Bundle();
                bundle.putInt("message", database.logedInUserDao().checkUser().getId_user());
                bundle.putString("type", "user");
                ProfileFragment profileFragment = new ProfileFragment();
                profileFragment.setArguments(bundle);
                replaceFragment(R.id.frame_container,profileFragment,true);
                //replaceFragment(R.id.frame_container,new InputUserFragment(),true);
                break;
            }case (R.id.item2):{
                replaceFragment(R.id.frame_container,new InputWorkshopFragment(),true);
//                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,
//                        new InputCourseFragment()).commit();
                break;
            }case (R.id.item3):{
                replaceFragment(R.id.frame_container,new InputCourseFragment(),true);
//                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,
//                        new InputCourseFragment()).commit();
                break;
            } case(R.id.item4):{
                replaceFragment(R.id.frame_container,new ListUserAdminFragment(), true);
//                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,
//                        new ListUserAdminFragment()).commit();
                break;
            }case(R.id.item5):{
                replaceFragment(R.id.frame_container,new ListMentorAdminFragment(), true);
//                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,
//                        new ListUserAdminFragment()).commit();
                break;
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
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
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack();
        }else{
            finish();
        }
    }
    void bindAllOnActivityResult(@Nullable Fragment fragment, int requestCode, int resultCode, @Nullable Intent data) {
        List<Fragment> fragments;
        if (fragment == null) {
            fragments = getSupportFragmentManager().getFragments();
        }
        else {
            fragments = fragment.getChildFragmentManager().getFragments();
        }

        for (Fragment activeFragment : fragments) {
            activeFragment.onActivityResult(requestCode, resultCode, data);

            List<Fragment> childFragments = activeFragment.getChildFragmentManager().getFragments();
            if (childFragments.size() > 0) {
                bindAllOnActivityResult(activeFragment, requestCode, resultCode, data);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        bindAllOnActivityResult(null, requestCode, resultCode, data);
    }

    public void loadAvatar(Uri targetUri) {
        try {
            Glide.with(this)
                    .applyDefaultRequestOptions(roPlaceholder)
                    .load(targetUri)
                    .centerCrop()
                    .into(usrImage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}