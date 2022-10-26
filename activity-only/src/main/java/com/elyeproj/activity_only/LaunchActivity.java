package com.elyeproj.activity_only;

import static com.elyeproj.activity_only.MainActivity.KEY;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class LaunchActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        findViewById(R.id.btn_click).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(KEY, "From Launch");
        startActivity(intent);
    }
}
