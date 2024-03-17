package com.nguyenuyennhi.helloworld;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class PTB2Activity extends AppCompatActivity {

    EditText edtA, edtB, edtC;
    Button btnGiai, btnTiep, btnTroVe;
    TextView txtKetQua;
    View.OnClickListener myOnClick;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ptb2);
        addViews();
        addEvents();
    }

    private void addEvents() {
        myOnClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.btnTroVe) {
                    finish();
                } else if (v.getId() == R.id.btnTiep) {
                    edtA.setText("");
                    edtB.setText("");
                    edtC.setText("");
                    txtKetQua.setText("");
                    edtA.requestFocus();
                } else if (v.getId() == R.id.btnGiai) {
                    giaiPhuongTrinh();
                }
            }
        };

        btnGiai.setOnClickListener(myOnClick);
        btnTiep.setOnClickListener(myOnClick);
        btnTroVe.setOnClickListener(myOnClick);
    }

    private void giaiPhuongTrinh() {
        double hsa = Double.parseDouble(edtA.getText().toString());
        double hsb = Double.parseDouble(edtB.getText().toString());
        double hsc = Double.parseDouble(edtC.getText().toString());

        if (hsa == 0) {
            // ptb1: bx + c = 0
            if (hsb == 0 && hsc == 0) {
                txtKetQua.setText("Vô số nghiệm");
            } else if (hsb == 0 && hsc != 0) {
                txtKetQua.setText("Vô nghiệm");
            } else {
                double x = -hsc/hsb;
                txtKetQua.setText("x = " + x);
            }
        } else {
            double delta = Math.pow(hsb, 2) - 4 * hsa * hsc;

            if (delta < 0) {
                txtKetQua.setText("Vô nghiệm");
            } else if (delta == 0) {
                double x = -hsb/2 * hsa;
                txtKetQua.setText("Nghiệm kép x1 = x2 = " + x);
            } else {
                double x1 = (-hsb - Math.sqrt(delta)) / (2 * hsa);
                double x2 = (-hsb + Math.sqrt(delta)) / (2 * hsa);
                txtKetQua.setText("x1 = " + x1 + "; x2 = " + x2);
            }
        }
    }

    private void addViews() {
        edtA = findViewById(R.id.edtA);
        edtB = findViewById(R.id.edtB);
        edtC = findViewById(R.id.edtC);
        btnGiai = findViewById(R.id.btnGiai);
        btnTiep = findViewById(R.id.btnTiep);
        btnTroVe = findViewById(R.id.btnTroVe);
        txtKetQua = findViewById(R.id.txtKetQua);
    }
}