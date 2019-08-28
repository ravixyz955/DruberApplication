package com.example.user.druberapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.WindowManager;

import com.example.user.druberapplication.fragment.SigninFragment;
import com.example.user.druberapplication.utils.DataUtils;

public class RegistrationActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        fragmentManager = getSupportFragmentManager();
        if (TextUtils.isEmpty(DataUtils.getToken(this))) {
            if (getIntent().hasExtra("active")) {
            } else if (getIntent().hasExtra("complete")) {
            } else if (getIntent().hasExtra("complete")) {
            } else {
                replaceFragment(SigninFragment.newInstance(null, null));
            }
        } else {
            startActivity(new Intent(this, JobsActivity.class));
            finish();
        }
    }

    public void replaceFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.register_container, fragment);
        fragmentTransaction.commit();
    }
}