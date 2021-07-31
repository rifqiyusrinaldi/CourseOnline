package com.rifqi.courseonline.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.rifqi.courseonline.R;
import com.rifqi.courseonline.model.RoomDB;
import com.rifqi.courseonline.model.dao.AdminDao;
import com.rifqi.courseonline.model.dao.LogedInUserDao;
import com.rifqi.courseonline.model.dao.UserDao;
import com.rifqi.courseonline.model.entities.AdminData;
import com.rifqi.courseonline.model.entities.LogedInUserData;
import com.rifqi.courseonline.model.entities.UserData;
import com.rifqi.courseonline.view.fragment.InputUserFragment;

import java.util.List;

public class LoginActivity extends AppCompatActivity {
    RoomDB database;
    private EditText username, pass;
    private Button btnLogin, btnRegister;
    private static final String TAG = LoginActivity.class.getName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = findViewById(R.id.et_username);
        pass = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        btnRegister = findViewById(R.id.btn_register);
        database = RoomDB.getInstance(this);
        getSupportActionBar().hide();

        database.userDao().getAll();

        final LogedInUserDao logedInUserDao = database.logedInUserDao();
        final AdminDao adminDao = database.adminDao();
        AdminData adminDataCheck = adminDao.check_logIn();
        LogedInUserData logedInUserData = logedInUserDao.checkUser();
        if (logedInUserData != null){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra(MainActivity.EXTRA_ID, logedInUserData.getId_user());
            intent.putExtra(MainActivity.EXTRA_USER, "user");
            startActivity(intent);
        }else if (adminDataCheck != null){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra(MainActivity.EXTRA_ID, adminDataCheck.getID());
            intent.putExtra(MainActivity.EXTRA_USER, "admin");
            startActivity(intent);
        }else {
            adminLoginInsert();
        }


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "pencet tombol");
                String input_username = username.getText().toString().trim();
                String input_password = pass.getText().toString().trim();
                if (input_username.isEmpty() || input_password.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Username atau Password Masih Kosong", Toast.LENGTH_SHORT).show();
                }else {
                    RoomDB roomDB= RoomDB.getInstance(getApplicationContext());
                    final UserDao userDao = roomDB.userDao();
                    AdminData adminData= adminDao.login(input_username, input_password);
                    if (adminData == null){
                        UserData userData =userDao.login(input_username, input_password);
                        if (userData == null){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "Username Atau Password Salah!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }else{
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.putExtra(MainActivity.EXTRA_ID, userData.getID());
                            intent.putExtra(MainActivity.EXTRA_USER, "user");
                            LogedInUserData loged = new LogedInUserData();
                            loged.setID(1);
                            loged.setId_user(userData.getID());
                            loged.setUsername(userData.getUsername());
                            loged.setPassword(userData.getPassword());
                            database.logedInUserDao().insert(loged);
                            startActivity(intent);
                        }
                    }else {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);;
                        intent.putExtra(MainActivity.EXTRA_ID, adminData.getID());
                        intent.putExtra(MainActivity.EXTRA_USER, "admin");
                        adminData.setStatus_log(1);
                        database.adminDao().update(adminData);
                        startActivity(intent);
                    }
                }
            }
        });
        btnRegister.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putInt("id", 0);
            InputUserFragment inputUserFragment = new InputUserFragment();
            inputUserFragment.setArguments(bundle);
            replaceFragment(R.id.frame_container_register,inputUserFragment,true);
        });
    }

    private void adminLoginInsert() {
        AdminData adminData = new AdminData();
        adminData.setID(1);
        adminData.setUsername("admin");
        adminData.setPassword("123");
        adminData.setStatus_log(0);
        database.adminDao().insert(adminData);
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
}