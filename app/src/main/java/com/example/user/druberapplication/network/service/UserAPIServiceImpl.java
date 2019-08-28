package com.example.user.druberapplication.network.service;

import com.example.user.druberapplication.network.RemoteServerAPI;
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
public class UserAPIServiceImpl implements UserAPIService {

    private final RemoteServerAPI remoteServerAPI;

    public UserAPIServiceImpl(RemoteServerAPI remoteServerAPI) {
        this.remoteServerAPI = remoteServerAPI;
    }

    @Override
    public Call<User> registerUser(RegisterUserRequest request) {
        return remoteServerAPI.registerUser(request);
    }

    @Override
    public Call<User> authenticate(AuthenticateUserRequest request) {
        return remoteServerAPI.authenticate(request);
    }

    @Override
    public Call<Void> activateUser(final ActivateUserRequest request) {
        return remoteServerAPI.activateUser(request);
    }

    @Override
    public Call<PathMarker> getPathMarkerList() {
        return remoteServerAPI.getPathMarkerList();
    }

    @Override
    public Call<ArrayList<Mission>> getMissionsList(String user_id) {
        return remoteServerAPI.getDroneMissionList(user_id);
    }

    @Override
    public Call<Object> getElevation(String longitude, String latitude) {
        return remoteServerAPI.getElevation(longitude, latitude);
    }

    @Override
    public Call<Object> startJob(String jobId, RequestBody requestBody) {
        return remoteServerAPI.startJob(jobId, requestBody);
    }

    @Override
    public Call<FinalPath> getMissionPath(String path) {
        return remoteServerAPI.getMissionPath(path);
    }

    @Override
    public Call<Object> getMissionPathAction(String jobId, String pathId, String action, String latlng) {
        return remoteServerAPI.getMissionPathAction(jobId, pathId, action, latlng);
    }
}
