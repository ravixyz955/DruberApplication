package com.example.user.druberapplication.network;

import android.content.Context;

import com.example.user.druberapplication.network.service.UserAPIService;
import com.example.user.druberapplication.utils.NetworkUtils;
import com.squareup.okhttp.Authenticator;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.net.Proxy;

/**
 * Created by apple on 23/01/18.
 */

public class TokenAuthenticator implements Authenticator {

    private static final String AUTHORIZATION = "x-auth-token";
    private Context mContext;
    private String newAccessToken;

    private UserAPIService userAPIService;

    public TokenAuthenticator(Context mContext) {
        this.mContext = mContext;
        userAPIService = NetworkUtils.provideUserAPIService(mContext, "https://auth.");
    }

    @Override
    public Request authenticate(Proxy proxy, Response response) throws IOException {
        // Refresh your access_token using a synchronous api request

     /*   if (!TextUtils.isEmpty(DataUtils.getEmail(mContext))) {
            if (!response.request().url().getPath().endsWith("/authenticate")) {
                AuthenticateUserRequest authenticateUserRequest = new AuthenticateUserRequest();
                authenticateUserRequest.setEmail(DataUtils.getEmail(mContext));
                authenticateUserRequest.setPassword(DataUtils.getPassword(mContext));
                retrofit.Response<User> authenticateResponse = userAPIService.authenticate(authenticateUserRequest).execute();
                if (authenticateResponse.isSuccess()) {
                    User user = authenticateResponse.body();
                    DataUtils.saveToken(mContext, user.getToken());
                    this.newAccessToken = user.getToken();
                } else
                    return null;
            } else
                return null;


            // Add new header to rejected request and retry it
            return response.request().newBuilder()
                    .header(AUTHORIZATION, newAccessToken)
                    .build();
        } else
            return null;*/
        return null;
    }

    @Override
    public Request authenticateProxy(Proxy proxy, Response response) throws IOException {
        // Null indicates no attempt to authenticate.
        return null;
    }
}