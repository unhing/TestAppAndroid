package com.nguyenuyennhi.helloworld;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.nguyenuyennhi.model.Book;
import com.nguyenuyennhi.model.Publisher;

import java.util.ArrayList;

public class PublisherSqliteCRUDActivity extends AppCompatActivity {

    ArrayAdapter<Publisher> publisherArrayAdapter;
    ListView lvPublisher;
    EditText edtPublisherId, edtPublisherName;

    public static final String DATABASE_NAME = "HelloWorld.sqlite";
    public static final String DB_PATH_SUFFIX = "/databases/";
    public static SQLiteDatabase database = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publisher_sqlite_crud);
        addViews();
        loadPublisher();
        addEvents();
    }

    /*@Override
    protected void onResume() {
        super.onResume();
        loadPublisher();
    }*/

    private void addViews() {
        edtPublisherId = findViewById(R.id.edtPublisherId);
        edtPublisherName = findViewById(R.id.edtPublisherName);

        lvPublisher = findViewById(R.id.lvPublisher);
        publisherArrayAdapter = new ArrayAdapter<>(PublisherSqliteCRUDActivity.this, android.R.layout.simple_list_item_1);
        lvPublisher.setAdapter(publisherArrayAdapter);
    }

    private void loadPublisher() {
        ArrayList<Publisher> publishers = new ArrayList<>();

        database = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null); // không bao giờ bị null (không có thì nó create)

        Cursor cursor = database.rawQuery("SELECT * FROM Publisher", null);
        while(cursor.moveToNext()){
            String publisherId = cursor.getString(0);
            String publisherName = cursor.getString(1);

            Publisher publisher = new Publisher();
            //publisherArrayAdapter.add(publisher);
            publisher.setPublisherId(publisherId);
            publisher.setPublisherName(publisherName);

            publishers.add(publisher);
        }
        cursor.close();

        publisherArrayAdapter.clear();
        publisherArrayAdapter.addAll(publishers);
    }

    private void addEvents() {
        lvPublisher.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Publisher publisher = publisherArrayAdapter.getItem(position);
                edtPublisherId.setText(publisher.getPublisherId());
                edtPublisherName.setText(publisher.getPublisherName());
            }
        });
    }

    public void processNew(View view) {
        edtPublisherId.setText("");
        edtPublisherName.setText("");
        edtPublisherId.requestFocus();
    }

    public void processSave(View view) {
        ContentValues record = new ContentValues();
        record.put("publisherId",edtPublisherId.getText().toString());
        record.put("publisherName",edtPublisherName.getText().toString());

        long result = database.insert("Publisher",null, record);

        if (result > 0) {
            loadPublisher();
        }
    }

    public void processUpdate(View view) {
        ContentValues record = new ContentValues();
        record.put("publisherName",edtPublisherName.getText().toString());

        String publisherId = edtPublisherId.getText().toString();
        long result = database.update("Publisher",record,"publisherId=?",new String[]{publisherId});

        if (result > 0) {
            loadPublisher();
        }
    }

    public void processDelete(View view) {
        String publisherId = edtPublisherId.getText().toString();
        long result = database.delete("Publisher","publisherId=?",new String[]{publisherId});
        
        if (result > 0) {
            loadPublisher();
            processNew(view);
        }
    }

    public void confirmDelete(View view) {
        // Create builder object (instance)
        AlertDialog.Builder builder = new AlertDialog.Builder(PublisherSqliteCRUDActivity.this);

        // Set title
        builder.setTitle("Confirm Delete");

        // Set message
        builder.setMessage("Are you sure you want to delete?");

        // Set icon
        builder.setIcon(android.R.drawable.ic_dialog_alert);

        // Set actions button for user interactions
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                processDelete(view);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
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
}