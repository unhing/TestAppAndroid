package com.nguyenuyennhi.helloworld;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.Random;

public class RuntimeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_runtime);
    }

    public void drawButton(View view) {
        LinearLayout llButton = findViewById(R.id.llButton);
        int n = 20;
        Random random = new Random();
        llButton.removeAllViews();

        for (int i = 0; i < n; i++) {
            Button btn = new androidx.appcompat.widget.AppCompatButton(this) {
                @Override
                public boolean performClick() {
                    setTextColor(Color.RED);
                    return super.performClick();
                }

                @Override
                public boolean performLongClick() {
                    llButton.removeView(this);
                    return super.performLongClick();
                }
            };

            String text = random.nextInt(100) + "";
            btn.setText(text);
            btn.setWidth(300);
            btn.setHeight(50);
            llButton.addView(btn);
        }
    }
}