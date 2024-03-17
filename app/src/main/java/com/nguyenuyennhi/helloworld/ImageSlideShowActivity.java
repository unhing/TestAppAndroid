package com.nguyenuyennhi.helloworld;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class ImageSlideShowActivity extends AppCompatActivity {

    ImageView imgSmall1, imgSmall2, imgLarge;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_slide_show);
        addViews();
        addEvents();
    }

    private void addEvents() {
        imgSmall1.setOnClickListener(new MyClassEvent());
        imgSmall2.setOnClickListener(new MyClassEvent());
    }

    private void addViews() {
        imgSmall1 = findViewById(R.id.imgSmall1);
        imgSmall2 = findViewById(R.id.imgSmall2);
        imgLarge = findViewById(R.id.imgLarge);
    }

    class MyClassEvent implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (v.equals(imgSmall1)) {
                imgLarge.setImageResource(R.mipmap.ic_book_large_1);
            } else if (v.equals(imgSmall2)) {
                imgLarge.setImageResource(R.mipmap.ic_book_large_2);
            }
        }
    }
}