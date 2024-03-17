package com.nguyenuyennhi.helloworld;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

public class FeedbackActivity extends AppCompatActivity {

    EditText edtContentFeedback;
    Button btnSubmitFeedback;
    RatingBar ratingBar;
    Intent previousIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        addViews();
        getDataFromPreviousActivity();
        addEvents();
    }

    private void addEvents() {
        btnSubmitFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = edtContentFeedback.getText().toString();
                float rating = ratingBar.getRating();
                previousIntent.putExtra("CONTENT_FEEDBACK",content);
                previousIntent.putExtra("RATING_FEEDBACK", rating);
                setResult(2,previousIntent);
                finish();
            }
        });
    }

    private void getDataFromPreviousActivity() {
        previousIntent = getIntent();
    }

    private void addViews() {
        edtContentFeedback = findViewById(R.id.edtContentFeedback);
        btnSubmitFeedback = findViewById(R.id.btnSubmitFeedback);
        ratingBar = findViewById(R.id.ratingBar);
    }
}