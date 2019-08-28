package com.example.user.druberapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

public class SampleActivity extends AppCompatActivity {
    int count = 1;
    TextView textView;
    Button click;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);
        textView = findViewById(R.id.count);
//        click = findViewById(R.id.click);

        if (savedInstanceState != null) {
            count = (int) savedInstanceState.get("value");
            textView.setText(String.valueOf(count));
        } else
            textView.setText(String.valueOf(count));

        /*click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                textView.setText(String.valueOf(count));
            }
        })*/
        ;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("value", count);
    }
}