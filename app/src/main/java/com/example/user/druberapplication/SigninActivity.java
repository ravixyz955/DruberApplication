package com.example.user.druberapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.user.druberapplication.network.service.UserAPIService;
import com.example.user.druberapplication.utils.NetworkUtils;
import com.example.user.druberapplication.utils.Utils;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SigninActivity extends AppCompatActivity {
    @BindView(R.id.email_txt)
    EditText email_txt;
    @BindView(R.id.pwd_txt)
    EditText pwd_txt;
    @BindView(R.id.login_txt)
    ShimmerTextView login_txt;

    @BindView(R.id.signup_txt)
    TextView signup_txt;
    @BindView(R.id.forgot_pwd_txt)
    TextView forgot_pwd_txt;

    private String email, password;
    private Shimmer shimmer;
    private boolean isStart = false;
    private Unbinder unbinder;
    private UserAPIService userAPIService;
    private FrameLayout progress_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        Window window = this.getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        unbinder = ButterKnife.bind(this);
        userAPIService = NetworkUtils.provideUserAPIService(this, "https://missions.");
        progress_bar = findViewById(R.id.progress_bar);
        shimmer = new Shimmer()
                .setDirection(Shimmer.ANIMATION_DIRECTION_LTR)
                .setDuration(3000)
                .setStartDelay(2000);

        if (!isStart)
            shimmer.start(login_txt);
    }

    @OnClick(R.id.login_txt)
    public void login() {
        if (TextUtils.isEmpty(email_txt.getText().toString().trim()))
            Utils.getSneaker(this, "Email can't be empty!");
        else if (TextUtils.isEmpty(pwd_txt.getText().toString().trim()))
            Utils.getSneaker(this, "Password can't be empty!");
        else if (!email_txt.getText().toString().trim().contains("@"))
            Utils.getSneaker(this, "Not a valid email");
        else {
            doLogin();
        }
    }

    @OnClick(R.id.signup_txt)
    public void signUp() {
        startActivity(new Intent(this, SignUpActivity.class));
    }

    private void doLogin() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                email = email_txt.getText().toString().trim();
                password = pwd_txt.getText().toString().trim();
                startActivity(new Intent(SigninActivity.this, JobsActivity.class));
                finish();
            }
        }, 800);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (isStart) {
            isStart = false;
            shimmer.start(login_txt);
        }
    }

    @Override
    protected void onPause() {
        if (!isStart) {
            isStart = true;
            shimmer.cancel();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}