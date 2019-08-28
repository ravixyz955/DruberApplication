package com.example.user.druberapplication.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.user.druberapplication.network.model.User;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * Created by KT on 28/12/15.
 */
public class DataUtils {

    public static final String KEY_EMAIL = "key_email";
    public static final String KEY_MOBILE = "key_mobile";
    public static final String KEY_COUNTRY_CODE = "key_country_code";
    public static final String KEY_NAME = "key_name";
    public static final String KEY_PASSWORD = "key_password";
    public static final String KEY_TOKEN = "key_token";
    public static final String KEY_STATUS = "key_status";
    public static final String KEY_AUTO_STATUS = "key_auto_status";
    public static final String KEY_ACTIVE = "key_active";
    public static final String KEY_USER = "key_user";
    public static final String KEY_ID = "key_id";
    public static final String USER_PREF = "user_pref";

    public static void saveName(Context mContext, String name) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(USER_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_NAME, name);
        editor.apply();
    }

    public static void saveEmail(Context mContext, String email) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(USER_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_EMAIL, email);
        editor.apply();
    }

    public static void saveMobile(Context mContext, String mobile) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(USER_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_MOBILE, mobile);
        editor.apply();
    }

    public static void savePassword(Context mContext, String password) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(USER_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_PASSWORD, password);
        editor.apply();
    }

    public static void saveToken(Context mContext, String token) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(USER_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_TOKEN, token);
        editor.apply();
    }

    public static void saveCountryCode(Context mContext, String countryCode) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(USER_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_COUNTRY_CODE, countryCode);
        editor.apply();
    }

    public static void saveUser(Context mContext, String user) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(USER_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USER, user);
        editor.apply();
    }

    public static void saveId(Context mContext, String user) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(USER_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_ID, user);
        editor.apply();
    }

    public static String getId(Context mContext) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(USER_PREF, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_ID, null);
    }

    public static String getEmail(Context mContext) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(USER_PREF, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_EMAIL, null);
    }

    public static String getName(Context mContext) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(USER_PREF, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_NAME, null);
    }

    public static String getMobileNumber(Context mContext) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(USER_PREF, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_MOBILE, null);
    }

    public static String getPassword(Context mContext) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(USER_PREF, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_PASSWORD, null);
    }

    public static String getToken(Context mContext) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(USER_PREF, Context.MODE_PRIVATE);
        Log.d("TOKEN", "getToken: " + sharedPreferences.getString(KEY_TOKEN, null));
        return sharedPreferences.getString(KEY_TOKEN, null);
    }

    public static User getUser(Context mContext) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(USER_PREF, Context.MODE_PRIVATE);
        String userStr = sharedPreferences.getString(KEY_USER, null);
        User user = null;
        try {
            user = new Gson().fromJson(userStr, User.class);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return user;
    }

    public static String getCountryCode(Context mContext) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(USER_PREF, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_COUNTRY_CODE, null);
    }

    public static void saveStatus(Context mContext, String status) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(USER_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_AUTO_STATUS, null);
        editor.putString(KEY_STATUS, status);
        editor.apply();
    }

    public static void saveAutoStatus(Context mContext, String status) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(USER_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_AUTO_STATUS, status);
        editor.apply();
    }

    public static String getStatus(Context mContext) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(USER_PREF, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_STATUS, null);
    }

    public static String getAutoStatus(Context mContext) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(USER_PREF, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_AUTO_STATUS, null);
    }

    public static void setActive(Context mContext, boolean active) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(USER_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_ACTIVE, active);
        editor.apply();
    }

    public static boolean isActivated(Context mContext) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(USER_PREF, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(KEY_ACTIVE, false);
    }

    public static void logout(Context mContext) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(USER_PREF, Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().commit();
    }
}