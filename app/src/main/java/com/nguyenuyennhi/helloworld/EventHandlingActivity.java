package com.nguyenuyennhi.helloworld;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class EventHandlingActivity extends AppCompatActivity {

    Button btnChangeImage;
    ImageView imgStudent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_handling);
        addViews();
        addEvents();
    }

    // Anonymous Listener
    private void addEvents() {
        btnChangeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imgStudent.getTag() != null) {
                    if (imgStudent.getTag().equals("man")) {
                        imgStudent.setImageResource(R.mipmap.student_female);
                        imgStudent.setTag("woman");
                    } else {
                        imgStudent.setImageResource(R.mipmap.student_male);
                        imgStudent.setTag("man");
                    }
                } else {
                    imgStudent.setImageResource(R.mipmap.student_female);
                    imgStudent.setTag("woman");
                }
            }
        });
    }

    private void addViews() {
        btnChangeImage = findViewById(R.id.btnChangeImage);
        imgStudent = findViewById(R.id.imgStudent);
    }

    public void openPtb2Activity(View view) {
        Intent intent = new Intent(EventHandlingActivity.this, PTB2Activity.class);
        startActivity(intent);
    }

    public void openToLunarYear(View view) {
        Intent intent = new Intent(EventHandlingActivity.this, CalendarYearToLunarYearActivity.class);
        startActivity(intent);
    }

    public void openImageSlideShow(View view) {
        Intent intent = new Intent(EventHandlingActivity.this, ImageSlideShowActivity.class);
        startActivity(intent);
    }

    public void openRuntimeViewActivity(View view) {
        Intent intent = new Intent(EventHandlingActivity.this, RuntimeActivity.class);
        startActivity(intent);
    }
}