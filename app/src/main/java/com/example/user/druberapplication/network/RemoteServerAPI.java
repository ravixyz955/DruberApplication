package com.example.user.druberapplication.network;

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
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Multipart;
import retrofit.http.PATCH;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by KT on 23/12/15.
 */
public interface RemoteServerAPI {

    String BASE_CONTEXT = "/api";

    @Multipart
    @POST(BASE_CONTEXT + "/users/image")
    Call<Void> uploadImage(@Header("x-auth-token") String fcmId, @Part("file") RequestBody request);

    @POST(BASE_CONTEXT + "/register")
    Call<User> registerUser(@Body RegisterUserRequest request);

    @POST(BASE_CONTEXT + "/v1/auth/authenticate")
//    @POST("https://auth.api.staging.xyzinnotech.com/")
    Call<User> authenticate(@Body AuthenticateUserRequest request);

    @POST("/activate")
    Call<Void> activateUser(@Body ActivateUserRequest request);

    @GET(BASE_CONTEXT + "/mission-management/job")
    Call<ArrayList<Mission>> getDroneMissionList(@Query("assignee") String user_id);

    @GET("http://www.mocky.io/v2/5c63bc0f3200004f1693f54d")
    Call<PathMarker> getPathMarkerList();

    @GET("https://api.mapbox.com/v4/mapbox.mapbox-terrain-v2/tilequery/" + "{longitude}" + "," + "{latitudue}" + ".json?&access_token=pk.eyJ1IjoiZGV2ZWxvcGVyeHl6IiwiYSI6ImNqa2poMXV1cjFibzQza2p5c3YyZGl6cGMifQ.uMu2kiKV4918S7ITBjdXZQ")
    Call<Object> getElevation(@Path("longitude") String longitude, @Path("latitudue") String latitude);

    @PATCH(BASE_CONTEXT + "/mission-management/job/{jobId}")
    Call<Object> startJob(@Path("jobId") String jobId, @Body RequestBody requestBody);

    @GET(BASE_CONTEXT + "/mission-management/mission/path/{path}")
    Call<FinalPath> getMissionPath(@Path("path") String path);

    @PATCH(BASE_CONTEXT + "/mission-management/job/{jobId}/path/{pathId}")
    Call<Object> getMissionPathAction(@Path("jobId") String jobId, @Path("pathId") String pathId, @Query("action") String action, @Query("latlng") String latlng);
}