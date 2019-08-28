package com.example.user.druberapplication;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.example.user.druberapplication.adapter.Pager;
import com.example.user.druberapplication.network.model.Mission;
import com.example.user.druberapplication.network.service.UserAPIService;
import com.example.user.druberapplication.utils.DataUtils;
import com.example.user.druberapplication.utils.NetworkUtils;
import com.example.user.druberapplication.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class JobsActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener {
    @BindView(R.id.progress_bar)
    FrameLayout progress_bar;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.pager)
    ViewPager viewPager;
    @BindView(R.id.coordinator_layout)
    CoordinatorLayout coordinatorLayout;

    private UserAPIService userAPIService;
    private Unbinder unbinder;
    private Pager adapter;
    private TabLayout.TabLayoutOnPageChangeListener tabLayoutOnPageChangeListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_missions);
        unbinder = ButterKnife.bind(this);
        userAPIService = NetworkUtils.provideUserAPIService(this, "https://missions.");
        setSupportActionBar(toolbar);
        Window window = this.getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        tabLayoutOnPageChangeListener = new TabLayout.TabLayoutOnPageChangeListener(tabLayout);
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorMaroon));
        viewPager.addOnPageChangeListener(tabLayoutOnPageChangeListener);
        tabLayout.addOnTabSelectedListener(JobsActivity.this);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewPager.addOnPageChangeListener(tabLayoutOnPageChangeListener);
        tabLayout.addOnTabSelectedListener(JobsActivity.this);
        if (!NetworkUtils.isConnectingToInternet(this)) {
            progress_bar.setVisibility(View.GONE);
            Utils.getSnackbar(coordinatorLayout, "Please check internet connection!");
        } else {
            userAPIService.getMissionsList(DataUtils.getId(this)).enqueue(new Callback<ArrayList<Mission>>() {
                @Override
                public void onResponse(Response<ArrayList<Mission>> response, Retrofit retrofit) {
                    progress_bar.setVisibility(View.GONE);
                    if (response.isSuccess()) {
                        ArrayList<Mission> missions = response.body();
                        Bundle missions_bundle = new Bundle();
                        missions_bundle.putParcelableArrayList("missions", missions);
                        adapter = new Pager(getSupportFragmentManager(), tabLayout.getTabCount(), missions);
                        viewPager.setAdapter(adapter);
                        tabLayout.setupWithViewPager(viewPager);
                    } else {
                        try {
                            Log.i("Error", response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    progress_bar.setVisibility(View.GONE);
                    Log.d("Failure:", "onFailure: " + t.getMessage());
                }
            });
        }
    }

    public TabLayout getTabLayout() {
        return tabLayout;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
        int position = tab.getPosition();
        if (position == 0)
            tabLayout.setBackgroundColor(getResources().getColor(R.color.colorOrange));
        else if (position == 1) {
            viewPager.setCurrentItem(tab.getPosition());
            tabLayout.setBackgroundColor(getResources().getColor(R.color.colorBlue));
        } else
            tabLayout.setBackgroundColor(getResources().getColor(R.color.colorGreen));
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    public ViewPager getViewPager() {
        return viewPager;
    }
}