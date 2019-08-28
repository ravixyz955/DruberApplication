package com.example.user.druberapplication.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.druberapplication.JobsActivity;
import com.example.user.druberapplication.R;
import com.example.user.druberapplication.SignUpActivity;
import com.example.user.druberapplication.network.model.AuthenticateUserRequest;
import com.example.user.druberapplication.network.model.User;
import com.example.user.druberapplication.network.service.UserAPIService;
import com.example.user.druberapplication.utils.DataUtils;
import com.example.user.druberapplication.utils.NetworkUtils;
import com.example.user.druberapplication.utils.Utils;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class SigninFragment extends Fragment {

    private static final String EMAIL_PATTERN = "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$";
    private static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\\\S+$).{4,}$";

    @BindView(R.id.email_txt)
    EditText emailText;
    @BindView(R.id.pwd_txt)
    EditText passwordText;
    @BindView(R.id.login_txt)
    ShimmerTextView login_txt;

    @BindView(R.id.signup_txt)
    TextView signup_txt;
    @BindView(R.id.forgot_pwd_txt)
    TextView forgot_pwd_txt;

    private Shimmer shimmer;
    private Unbinder unbinder;
    private UserAPIService userAPIService;

    public SigninFragment() {
        // Required empty public constructor
    }

    public static SigninFragment newInstance(String param1, String param2) {
        return new SigninFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getActivity().getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        userAPIService = NetworkUtils.provideUserAPIService(getActivity(), "https://auth.");
        Utils.createProgressDialoge(getActivity(), "Please wait...");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_signin, container, false);
        unbinder = ButterKnife.bind(this, root);
        shimmer = new Shimmer()
                .setDirection(Shimmer.ANIMATION_DIRECTION_LTR)
                .setDuration(3000)
                .setStartDelay(2000);
        return root;
    }

    @OnClick(R.id.login_txt)
    public void login() {
        if (TextUtils.isEmpty(emailText.getText().toString().trim()))
            Utils.getSneaker(getActivity(), "Email can't be empty!");
        else if (TextUtils.isEmpty(passwordText.getText().toString().trim()))
            Utils.getSneaker(getActivity(), "Password can't be empty!");
        else if (!emailText.getText().toString().trim().contains("@"))
            Utils.getSneaker(getActivity(), "Not a valid email");
        else {
            doLogin();
        }
    }

    @OnClick(R.id.signup_txt)
    public void signUp() {
        startActivity(new Intent(getActivity(), SignUpActivity.class));
    }

    private void doLogin() {
        AuthenticateUserRequest request = new AuthenticateUserRequest();
        final String email = emailText.getText().toString();
        final String password = passwordText.getText().toString();
        request.setEmail(email);
        request.setPassword(password);

        if (NetworkUtils.isConnectingToInternet(getActivity())) {
            Utils.showProgress();

            userAPIService.authenticate(request).enqueue(new Callback<User>() {
                public void onResponse(Response<User> response, Retrofit retrofit) {
                    if (response.isSuccess()) {
                        User user = response.body();
                        DataUtils.saveId(getActivity(), response.body().getId());
                        DataUtils.saveUser(getActivity(), response.body().toString());
                        DataUtils.saveEmail(getActivity(), user.getEmail());
                        DataUtils.saveName(getActivity(), user.getFullName());
                        DataUtils.saveMobile(getActivity(), user.getMobile());
                        DataUtils.savePassword(getActivity(), password);
                        DataUtils.setActive(getActivity(), false);
                        DataUtils.saveCountryCode(getActivity(), user.getCountryCode());
                        DataUtils.saveToken(getActivity(), user.getToken());
                        startActivity(new Intent(getActivity(), JobsActivity.class));
                        getActivity().finish();
                    } else {
                        Toast.makeText(getActivity(), "Failed to signin!", Toast.LENGTH_LONG).show();
                    }
                    Utils.dissmisProgress();
                }

                @Override
                public void onFailure(Throwable t) {
                    t.printStackTrace();
                    Utils.dissmisProgress();
                }
            });
        } else {
            Toast.makeText(getActivity(), "No Internet!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}