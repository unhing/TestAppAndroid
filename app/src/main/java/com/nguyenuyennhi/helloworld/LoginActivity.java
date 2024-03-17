package com.nguyenuyennhi.helloworld;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.Notification;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nguyenuyennhi.model.Account;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class LoginActivity extends AppCompatActivity {

    EditText edtUserName;
    EditText edtPassword;
    TextView txtMessage;
    int count_exit = 0;
    ImageView imgFooter;

    Dialog dialog = null;
    ImageView imgLogo;

    SharedPreferences sharedPreferences;
    String Key_Preference="LOGIN_PREFERENCE";
    CheckBox chkSaveInfo;

    MediaPlayer mediaPlayer;

    private static final int CALL_PERMISSION_CODE = 100;

    public static final String DATABASE_NAME = "HelloWorld.sqlite";
    public static final String DB_PATH_SUFFIX = "/databases/";
    public static SQLiteDatabase database = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        addViews();
        addEvents();
        readLoginInfo();
        copyDataBase();
        playAudio();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mediaPlayer.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mediaPlayer.start();
    }

    private void copyDataBase(){
        try {
            File dbFile = getDatabasePath(DATABASE_NAME);
            if (!dbFile.exists()) {
                if (CopyDBFromAsset()) {
                    Toast.makeText(LoginActivity.this,
                            "Copy database successful!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(LoginActivity.this,
                            "Copy database fail!", Toast.LENGTH_LONG).show();
                }
            }
        } catch (Exception e){
            Log.e("Error: ", e.toString());
        }
    }

    private boolean CopyDBFromAsset() {
        String dbPath = getApplicationInfo().dataDir + DB_PATH_SUFFIX + DATABASE_NAME;
        try {
            InputStream inputStream = getAssets().open(DATABASE_NAME);
            File f = new File(getApplicationInfo().dataDir + DB_PATH_SUFFIX);
            if(!f.exists()){
                f.mkdir();
            }
            OutputStream outputStream = new FileOutputStream(dbPath);
            byte[] buffer = new byte[1024]; int length;
            while((length=inputStream.read(buffer))>0){
                outputStream.write(buffer,0, length);
            }
            outputStream.flush();  outputStream.close(); inputStream.close();
            return  true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void addEvents() {
        imgFooter.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // process for deleting
                dialog.show();
                return false;
            }
        });
    }

    private void addViews() {
        edtUserName = findViewById(R.id.edtUserName);
        edtPassword = findViewById(R.id.edtPassword);
        txtMessage = findViewById(R.id.txtMessage);
        imgFooter = findViewById(R.id.imgFooter);

        dialog = new Dialog(LoginActivity.this);
        dialog.setContentView(R.layout.deleting_custom);

        ImageView imgRemove = dialog.findViewById(R.id.imgRemove);
        ImageView imgCancel = dialog.findViewById(R.id.imgCancel);

        imgRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgFooter.setVisibility(View.GONE);
                dialog.dismiss();
            }
        });

        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        imgLogo = findViewById(R.id.imgLogo);
        registerForContextMenu(imgLogo);

        chkSaveInfo = findViewById(R.id.chkSaveInfo);
    }

    public void exitApp(View view) {
        // finish(); // thoát phần mềm
        confirmExit();
    }

    void confirmExit() {
        // Create builder object (instance)
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);

        // Set title
        builder.setTitle("Confirm Exit!");

        // Set message
        builder.setMessage("Are you sure you want to exit?");

        // Set icon
        builder.setIcon(android.R.drawable.ic_dialog_alert);

        // Set actions button for user interactions
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        // Create dialog object
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);  // ngăn cái việc bấm ra ngoài = tắt dialog
        dialog.show();
    }

    public void openMainActivity(View view) {
        String userName = edtUserName.getText().toString();
        String password = edtPassword.getText().toString();

        // if (userName.equalsIgnoreCase("admin") && password.equals("123")) {
        if (loginSystem(userName, password) != null) {
            // Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            // Intent intent = new Intent(LoginActivity.this, SimpleListBookActivity.class);
            // Intent intent = new Intent(LoginActivity.this, SimpleListBookObjectActivity.class);
            // Intent intent = new Intent(LoginActivity.this, AdvancedListBookObjectActivity.class);
            // Intent intent = new Intent(LoginActivity.this, PublisherBookActivity.class);
            // Intent intent = new Intent(LoginActivity.this, PublisherBookSqliteActivity.class);
            Intent intent = new Intent(LoginActivity.this, PublisherBookSqliteCRUDActivity.class);
            // Intent intent = new Intent(LoginActivity.this, MyContactActivity.class);
            // Intent intent = new Intent(LoginActivity.this, MyContactAdvancedActivity.class);

            startActivity(intent);
            Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();


            // SAVE LOGIN INFORMATION
            sharedPreferences = getSharedPreferences(Key_Preference,MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("USER_NAME",userName);
            editor.putString("PASSWORD",password);
            editor.putBoolean("SAVED",chkSaveInfo.isChecked());
            editor.commit();
        } else {
            // Alarm login failed
            txtMessage.setText("Login failed, please check your account again");
            Toast.makeText(LoginActivity.this, "Login failed!", Toast.LENGTH_SHORT).show();
        }
    }

    void readLoginInfo() {
        // sharedPreferences không NULL
        sharedPreferences = getSharedPreferences(Key_Preference,MODE_PRIVATE);
        String userName = sharedPreferences.getString("USER_NAME","");
        String password = sharedPreferences.getString("PASSWORD","");
        boolean saved = sharedPreferences.getBoolean("SAVED",false);

        if (saved) {
            edtUserName.setText(userName);
            edtPassword.setText(password);
        } else {
            edtUserName.setText("");
            edtPassword.setText("");
        }
        chkSaveInfo.setChecked(saved);
    }

    @Override
    public void onBackPressed() {
        count_exit++;
        if (count_exit >= 3) {
            confirmExit();
            count_exit = 0;
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.contextmenu_logo, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.mnuUpdate) {
            
        } else if (item.getItemId() == R.id.mnuSharing) {

        } else if (item.getItemId() == R.id.mnuDirectCall) {
            checkPermission(Manifest.permission.CALL_PHONE, CALL_PERMISSION_CODE);
            Intent intent = new Intent(Intent.ACTION_CALL);
            Uri uri = Uri.parse("tel:0911855002");
            intent.setData(uri);
            startActivity(intent);

            // nếu chưa cấp quyền thì mở lên yêu cầu KH cấp quyền
            // confirm allow permission

        } else if (item.getItemId() == R.id.mnuDialUpCall) {
            checkPermission(Manifest.permission.CALL_PHONE, CALL_PERMISSION_CODE);
            Intent intent = new Intent(Intent.ACTION_DIAL);
            Uri uri = Uri.parse("tel:0911855002");
            intent.setData(uri);
            startActivity(intent);
        }
        return super.onContextItemSelected(item);
    }

    // Function to check and request permission.
    public void checkPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(LoginActivity.this, permission) == PackageManager.PERMISSION_DENIED) {

            // Requesting the permission
            ActivityCompat.requestPermissions(LoginActivity.this, new String[] { permission }, requestCode);
        }
        else {
            Toast.makeText(LoginActivity.this, "Permission already granted", Toast.LENGTH_SHORT).show();
        }
    }

    // This function is called when the user accepts or decline the permission.
    // Request Code is used to check which permission called this function.
    // This request code is provided when the user is prompt for permission.

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CALL_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(LoginActivity.this, "Camera Permission Granted", Toast.LENGTH_SHORT) .show();
            }
            else {
                Toast.makeText(LoginActivity.this, "Camera Permission Denied", Toast.LENGTH_SHORT) .show();
            }
        }
    }

    // Play music background
    private void playAudio() {
        try{
            AssetFileDescriptor assetFileDescriptor =
                    getAssets().openFd("musics/toothless_dance.mp3");
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(
                    assetFileDescriptor.getFileDescriptor(),
                    assetFileDescriptor.getStartOffset(),
                    assetFileDescriptor.getLength());
            assetFileDescriptor.close();
            mediaPlayer.prepare();
            mediaPlayer.start();
            mediaPlayer.setLooping(true);
        }catch (Exception e){
            Log.e("Error: " , e.toString());
        }
    }

    public Account loginSystem(String username, String password) {
        database = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null); // không bao giờ bị null (không có thì nó create)

        String sql = "SELECT * FROM Account WHERE username = '" + username + "' and password = '" + password + "'";
        Cursor cursor = database.rawQuery(sql, null);

        if (cursor.moveToNext()) {
            String usn = cursor.getString(1);
            String p = cursor.getString(2);
            Account acc = new Account(usn, p);

            cursor.close();
            return acc;
        }
        cursor.close();
        return null;
    }
}