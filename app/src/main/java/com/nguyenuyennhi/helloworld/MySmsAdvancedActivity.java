package com.nguyenuyennhi.helloworld;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.nguyenuyennhi.model.MyContact;

public class MySmsAdvancedActivity extends AppCompatActivity {

    EditText edtMessageBody;
    TextView txtContactName, txtPhoneNumber;
    RadioButton radSms1, radSms2, radSms3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_sms_advanced);
        addViews();
        getDataFromPrevActivity();
    }

    private void getDataFromPrevActivity() {
        Intent intent = getIntent();
        MyContact contact = (MyContact) intent.getSerializableExtra("SELECTED_CONTACT");

        txtContactName.setText(contact.getContactName());
        txtPhoneNumber.setText(contact.getPhoneNumber());
    }

    private void addViews() {
        txtContactName = findViewById(R.id.txtContactName);
        txtPhoneNumber = findViewById(R.id.txtPhoneNumber);
        edtMessageBody = findViewById(R.id.edtMessageBody);
        radSms1 = findViewById(R.id.radSms1);
        radSms2 = findViewById(R.id.radSms2);
        radSms3 = findViewById(R.id.radSms3);
    }

    public void doSendSms1(View view) {
        sendSmsMethod1(txtPhoneNumber.getText().toString(), edtMessageBody.getText().toString());
    }

    public void doSendSms2(View view) {
        sendSmsMethod2(txtPhoneNumber.getText().toString(), edtMessageBody.getText().toString());
    }

    public void doSendSms3(View view) {
        sendSmsMethod3(txtPhoneNumber.getText().toString(), edtMessageBody.getText().toString());
    }

    // không quan tâm gửi thành công hay thất bại
    private void sendSmsMethod1(String phoneNumber, String message) {
        final SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, null, null);
    }

    // quan tâm gửi thành công hay thất bại
    private void sendSmsMethod2(String phoneNumber, String message) {
        final SmsManager sms = SmsManager.getDefault();
        Intent msgSent = new Intent("ACTION_MSG_SENT");
        final PendingIntent sentIntent = PendingIntent.getBroadcast(this, 0, msgSent, PendingIntent.FLAG_IMMUTABLE);
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int result = getResultCode();
                String msg = "Send OK";
                if (result != Activity.RESULT_OK) {
                    msg = "Send failed";
                }
                Toast.makeText(MySmsAdvancedActivity.this, msg, Toast.LENGTH_LONG).show();
            }
        }, new IntentFilter("ACTION_MSG_SENT"));
        sms.sendTextMessage(phoneNumber, null, message, sentIntent, null);
    }

    // quan tâm gửi thành công hay thất bại + nhận thành công hay thất bại
    private void sendSmsMethod3(String phoneNumber, String message) {
        String SENT_ACTION = "SMS_SENT_ACTION";
        String DELIVERY_ACTION = "SMS_DELIVERED_ACTION";

        PendingIntent sentIntent = PendingIntent.getBroadcast(this, 100, new Intent(SENT_ACTION), PendingIntent.FLAG_IMMUTABLE);
        PendingIntent deliveryIntent = PendingIntent.getBroadcast(this, 200, new Intent(DELIVERY_ACTION), PendingIntent.FLAG_IMMUTABLE);

        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int result = getResultCode();
                String msg = "Send OK";
                if (result != Activity.RESULT_OK) {
                    msg = "Send failed";
                }
                Toast.makeText(MySmsAdvancedActivity.this, msg, Toast.LENGTH_LONG).show();
            }
        }, new IntentFilter(SENT_ACTION));

        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int result = getResultCode();
                String msg = "Send OK";
                if (result != Activity.RESULT_OK) {
                    msg = "Send failed";
                }
                Toast.makeText(MySmsAdvancedActivity.this, msg, Toast.LENGTH_LONG).show();
            }
        }, new IntentFilter(DELIVERY_ACTION));

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, sentIntent, deliveryIntent);
    }

    public void doSendMessage(View view) {
        String phoneNumber = txtPhoneNumber.getText().toString();
        String messageBody = edtMessageBody.getText().toString();

        if (radSms1.isChecked()) {
            sendSmsMethod1(phoneNumber, messageBody);
        } else if (radSms2.isChecked()) {
            sendSmsMethod2(phoneNumber, messageBody);
        } else if (radSms3.isChecked()) {
            sendSmsMethod3(phoneNumber, messageBody);
        }
    }
}