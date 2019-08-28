package com.example.user.druberapplication.network.service;

import com.example.user.druberapplication.network.model.ActivateUserRequest;
import com.example.user.druberapplication.network.model.AuthenticateUserRequest;
import com.example.user.druberapplication.network.model.FinalPath;
import com.example.user.druberapplication.network.model.Mission;
import com.example.user.druberapplication.network.model.PathMarker;
import com.example.user.druberapplication.network.model.RegisterUserRequest;
import com.example.user.druberapplication.network.model.User;
import com.squareup.okhttp.RequestBody;

import java.util.ArrayList;

import retrofit.Call;

/**
 * Created by KT on 23/12/15.
 */
public interface UserAPIService {

    Call<User> registerUser(RegisterUserRequest request);

    Call<User> authenticate(AuthenticateUserRequest request);

    Call<Void> activateUser(ActivateUserRequest request);

    Call<PathMarker> getPathMarkerList();

    Call<ArrayList<Mission>> getMissionsList(String user_id);

    Call<Object> getElevation(String longitude, String latitude);

    Call<Object> startJob(String jobId, RequestBody requestBody);

    Call<FinalPath> getMissionPath(String path);

    Call<Object> getMissionPathAction(String jobId, String pathId, String action, String latlng);
}
