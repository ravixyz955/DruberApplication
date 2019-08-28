package com.example.user.druberapplication;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;

import com.example.user.druberapplication.adapter.EmployeeAdapter;
import com.example.user.druberapplication.network.model.Employee;

import java.util.ArrayList;

public class RecyclerViewWithCollapsingToobarLayout extends AppCompatActivity {
    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ImageView collapsingToolbarImageView;
    private RecyclerView collapsing_toolbar_recycler_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view_with_collapsing_toobar_layout);

        toolbar = findViewById(R.id.collapsing_toolbar);
        collapsing_toolbar_recycler_view = findViewById(R.id.collapsing_toolbar_recycler_view);
        collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar_layout);
        collapsingToolbarImageView = findViewById(R.id.collapsing_toolbar_image_view);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);
        collapsingToolbarLayout.setTitle("This is collapsingtollbarlayout");
        collapsingToolbarImageView.setImageResource(R.drawable.collapsingtoolbarimage);

        collapsing_toolbar_recycler_view.setLayoutManager(new LinearLayoutManager(this));
        collapsing_toolbar_recycler_view.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        collapsing_toolbar_recycler_view.setAdapter(new EmployeeAdapter(this, getEmployees()));
    }

    private ArrayList<Employee> getEmployees() {
        ArrayList<Employee> employees = new ArrayList<>();

        Employee employee = new Employee();
        employee.setName("Robert");
        employee.setAddress("New York");
        employee.setPhone("+61234456");
        employees.add(employee);

        employee = new Employee();
        employee.setName("Tom");
        employee.setAddress("California");
        employee.setEmail("tom_frank@gmail.com");
        employees.add(employee);

        employee = new Employee();
        employee.setName("Smith");
        employee.setAddress("Philadelphia");
        employee.setEmail("smith_carrol@gmail.com");
        employees.add(employee);

        employee = new Employee();
        employee.setName("Ryan");
        employee.setAddress("Canada");
        employee.setPhone("+612001456");
        employees.add(employee);

        employee = new Employee();
        employee.setName("Mark");
        employee.setAddress("Boston");
        employee.setEmail("mark_walmerd@gmail.com");
        employees.add(employee);

        employee = new Employee();
        employee.setName("Adam");
        employee.setAddress("Brooklyn");
        employee.setPhone("+61211780");
        employees.add(employee);

        employee = new Employee();
        employee.setName("Kevin");
        employee.setAddress("New Jersey");
        employee.setPhone("+94221035");
        employees.add(employee);

        employee = new Employee();
        employee.setName("Kevin");
        employee.setAddress("New Jersey");
        employee.setPhone("+94221035");
        employees.add(employee);

        employee = new Employee();
        employee.setName("Kevin");
        employee.setAddress("New Jersey");
        employee.setPhone("+94221035");
        employees.add(employee);

        employee = new Employee();
        employee.setName("Kevin");
        employee.setAddress("New Jersey");
        employee.setPhone("+94221035");
        employees.add(employee);

        employee = new Employee();
        employee.setName("Kevin");
        employee.setAddress("New Jersey");
        employee.setPhone("+94221035");
        employees.add(employee);

        employee = new Employee();
        employee.setName("Kevin");
        employee.setAddress("New Jersey");
        employee.setPhone("+94221035");
        employees.add(employee);

        employee = new Employee();
        employee.setName("Kevin");
        employee.setAddress("New Jersey");
        employee.setPhone("+94221035");
        employees.add(employee);

        return employees;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        // When user click home menu item then quit this activity.
        if (itemId == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
