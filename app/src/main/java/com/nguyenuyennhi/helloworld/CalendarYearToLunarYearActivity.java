package com.nguyenuyennhi.helloworld;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class CalendarYearToLunarYearActivity extends AppCompatActivity
implements View.OnClickListener
{

    EditText edtCalendarYear;
    Button btnLunarYear;
    TextView txtLunarYear;
    ImageView imgReturn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_year_to_lunar_year);
        addViews();
        addEvents();
    }

    private void addEvents() {
        btnLunarYear.setOnClickListener(this);
        imgReturn.setOnClickListener(this);
    }

    private void addViews() {
        edtCalendarYear = findViewById(R.id.edtCalendarYear);
        btnLunarYear = findViewById(R.id.btnLunarYear);
        txtLunarYear = findViewById(R.id.txtLunarYear);
        imgReturn = findViewById(R.id.imgReturn);
    }

    String convertCalendarToLunar(int calendarYear) {
        String result = "";
        String[] arr_can = {"Canh", "Tân", "Nhâm", "Quý", "Giáp", "Ất", "Bính", "Đinh", "Mậu", "Kỳ"};
        String[] arr_chi = {"Thân", "Dậu", "Tuất", "Hợi", "Tý", "Sửu", "Dần", "Mão", "Thìn", "Tị", "Ngọ", "Mùi"};

        int can = calendarYear % 10;
        String name_can = arr_can[can];

        int chi = calendarYear % 12;
        String name_chi = arr_chi[chi];

        result = name_can + " " + name_chi;
        return result;
    }

    @Override
    public void onClick(View v) {
        if (v.equals(btnLunarYear)) {
            int calendarYear = Integer.parseInt(edtCalendarYear.getText().toString());
            String result = convertCalendarToLunar(calendarYear);
            txtLunarYear.setText(result);
        } else if (v.equals(imgReturn)) {
            finish();
        }
    }
}