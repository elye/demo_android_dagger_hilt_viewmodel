package com.elyeproj.activity_only;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    static Repository repository = new Repository();
    final static String KEY = "Key";
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.text_view);
        if (savedInstanceState != null) {
            textView.setText(savedInstanceState.getString(KEY));
        } else {
            textView.setText(getIntent().getExtras().getString(KEY));
        }

        Button button = findViewById(R.id.btn_fetch);
        button.setOnClickListener(this);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY, textView.getText().toString());
    }

    @Override
    public void onClick(View v) {
        textView.setText(repository.getMessage());
    }
}
