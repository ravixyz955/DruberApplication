package com.example.user.druberapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class ListenerActivity extends AppCompatActivity {
    private Button childClick;
    private ClickListener clickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listener);

        childClick = findViewById(R.id.clildClick);
        childClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null)
                    clickListener.clicked();
            }
        });
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
        if (clickListener != null)
            clickListener.clicked();
    }

    public interface ClickListener {
        void clicked();
    }
}