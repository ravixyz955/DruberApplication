package com.example.user.druberapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;

import com.example.user.druberapplication.adapter.PracticeRecyclerAdapter;
import com.example.user.druberapplication.network.model.Test;

import java.util.ArrayList;

public class PracticeRecycler extends AppCompatActivity implements View.OnClickListener {
    private ArrayList<Test> tests;
    private RecyclerView recyclerView;
    private Button update_list, add_item, delete_item;
    private PracticeRecyclerAdapter practiceRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice_recycler);
        update_list = findViewById(R.id.update_list);
        add_item = findViewById(R.id.add_item);
        delete_item = findViewById(R.id.delete_item);
        recyclerView = findViewById(R.id.recyler);
        tests = new ArrayList<>();
        update_list.setOnClickListener(this);
        add_item.setOnClickListener(this);
        delete_item.setOnClickListener(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        tests.add(new Test("Abyssinian", 1, R.drawable.xyz_logo));
        tests.add(new Test("Baboon", 2, R.drawable.xyz_logo));
        tests.add(new Test("Caterpillar", 3, R.drawable.xyz_logo));
        tests.add(new Test("Dunker", 4, R.drawable.xyz_logo));
        tests.add(new Test("Eagle", 5, R.drawable.xyz_logo));
        tests.add(new Test("Flounder", 6, R.drawable.xyz_logo));
        practiceRecyclerAdapter = new PracticeRecyclerAdapter(this, tests);
        LinearLayoutManager llm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
//        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(this, R.dimen.nav_header_vertical_spacing);
//        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setLayoutManager(llm);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(practiceRecyclerAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_Search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                practiceRecyclerAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.update_list) {
            practiceRecyclerAdapter.setTests(updateTests());
//            practiceRecyclerAdapter = new PracticeRecyclerAdapter(this, updateTests());
//            recyclerView.setAdapter(practiceRecyclerAdapter);

        } else if (view.getId() == R.id.add_item) {
//            tests.clear();
//            tests.add(new Test("AA", 100, R.drawable.xyz_logo));
//            practiceRecyclerAdapter.addNewItem(tests);
            ArrayList<Test> tests = new ArrayList<>();
            tests.add(new Test("AA", 100, R.drawable.xyz_logo));
            tests.add(new Test("BB", 100, R.drawable.xyz_logo));
            tests.add(new Test("CC", 100, R.drawable.xyz_logo));
            tests.add(new Test("DD", 100, R.drawable.xyz_logo));
            tests.add(new Test("EE", 100, R.drawable.xyz_logo));
//            practiceRecyclerAdapter.addNewItem(new Test("AA", 100, R.drawable.xyz_logo));
            practiceRecyclerAdapter.addNewItemsRange(tests);
        } else if (view.getId() == R.id.delete_item) {
//            tests.clear();
//            tests.add(new Test())
//            practiceRecyclerAdapter.removeItem();
        }
    }

    private ArrayList<Test> updateTests() {
        tests.add(new Test("G", 7, R.drawable.xyz_logo));
        tests.add(new Test("H", 8, R.drawable.xyz_logo));
        tests.add(new Test("I", 9, R.drawable.xyz_logo));
        tests.add(new Test("J", 10, R.drawable.xyz_logo));
        return tests;
    }
}