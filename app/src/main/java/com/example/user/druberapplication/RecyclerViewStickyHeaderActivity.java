package com.example.user.druberapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.user.druberapplication.adapter.Adapter;
import com.example.user.druberapplication.utils.RecyclerSectionItemDecoration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecyclerViewStickyHeaderActivity extends AppCompatActivity {
    private RecyclerView recyclerView_sticky_header;
    private Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view_sticky_header);

        recyclerView_sticky_header = findViewById(R.id.recyclerView_sticky_header);
        recyclerView_sticky_header.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter(this, getData());
        recyclerView_sticky_header.setAdapter(adapter);

        RecyclerSectionItemDecoration sectionItemDecoration =
                new RecyclerSectionItemDecoration(getResources().getDimensionPixelSize(R.dimen.header),
                        true,
                        getSectionCallback(getData()));
        recyclerView_sticky_header.addItemDecoration(sectionItemDecoration);

    }

    private RecyclerSectionItemDecoration.SectionCallback getSectionCallback(final List<String> people) {
        return new RecyclerSectionItemDecoration.SectionCallback() {
            @Override
            public boolean isSection(int position) {
                return position == 0
                        || people.get(position)
                        .charAt(0) != people.get(position - 1)
                        .charAt(0);
            }

            @Override
            public CharSequence getSectionHeader(int position) {
                return people.get(position)
                        .subSequence(0,
                                1);
            }
        };
    }

    public ArrayList<String> getData() {
        String[] strings = getResources().getStringArray(R.array.animals);
        ArrayList<String> list = new ArrayList<String>(Arrays.asList(strings));
        return list;
    }
}
