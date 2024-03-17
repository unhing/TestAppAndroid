package com.nguyenuyennhi.helloworld;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.nguyenuyennhi.adapter.AdvancedBookAdapter;
import com.nguyenuyennhi.model.Book;
import com.nguyenuyennhi.model.Publisher;

import java.util.ArrayList;

public class PublisherBookSqliteCRUDActivity extends AppCompatActivity {
    Spinner spinnerPublisher;
    ArrayAdapter<Publisher> publisherArrayAdapter;
    ListView lvBook;
    AdvancedBookAdapter advancedBookAdapter;
    EditText edtBookId, edtBookName, edtUnitPrice;

    public static final String DATABASE_NAME = "HelloWorld.sqlite";
    public static final String DB_PATH_SUFFIX = "/databases/";
    public static SQLiteDatabase database = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publisher_book_sqlite_crud_activity);
        addViews();
        loadPublisher();
        addEvents();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPublisher();
    }

    private void addEvents() {
        spinnerPublisher.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Publisher selectedPublisher = publisherArrayAdapter.getItem(position);
                loadBookByPublisher(selectedPublisher);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        lvBook.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Book book = advancedBookAdapter.getItem(position);
                edtBookId.setText(book.getBookId());
                edtBookName.setText(book.getBookName());
                edtUnitPrice.setText(book.getUnitPrice() + "");
            }
        });
    }

    private void loadBookByPublisher(Publisher selectedPublisher) {
        // 1 Publisher has many books
        // 1 Book belongs to 1 Publisher
        ArrayList<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM Book WHERE publisherId='"+selectedPublisher.getPublisherId()+"'";

        database = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null); // không bao giờ bị null (không có thì nó create)

        Cursor cursor = database.rawQuery(sql, null);
        while(cursor.moveToNext()) {
            String bookId = cursor.getString(0);
            String bookName = cursor.getString(1);
            float unitPrice = cursor.getFloat(2);
            String description = cursor.getString(3);

            Book book = new Book();
            book.setBookId(bookId);
            book.setBookName(bookName);
            book.setUnitPrice(unitPrice);
            book.setDescription(description);
            book.setPublisher(selectedPublisher);

            books.add(book);
        }
        cursor.close();
        selectedPublisher.setBooks(books);
        advancedBookAdapter.clear();
        advancedBookAdapter.addAll(books);
    }

    private void loadPublisher() {
        ArrayList<Publisher> publishers = new ArrayList<>();

        database = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null); // không bao giờ bị null (không có thì nó create)

        Cursor cursor = database.rawQuery("SELECT * FROM Publisher", null);
        while(cursor.moveToNext()){
            String publisherId = cursor.getString(0);
            String publisherName = cursor.getString(1);

            Publisher publisher = new Publisher(publisherId, publisherName);
            publisherArrayAdapter.add(publisher);

            publishers.add(publisher);
        }
        cursor.close();

        publisherArrayAdapter.clear();
        publisherArrayAdapter.addAll(publishers);
    }

    private void addViews() {
        spinnerPublisher = findViewById(R.id.spinnerPublisher);
        publisherArrayAdapter = new ArrayAdapter<>(PublisherBookSqliteCRUDActivity.this, android.R.layout.simple_list_item_1);
        publisherArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPublisher.setAdapter(publisherArrayAdapter);

        lvBook = findViewById(R.id.lvBook);
        advancedBookAdapter = new AdvancedBookAdapter(PublisherBookSqliteCRUDActivity.this, R.layout.advancedbook_item);
        lvBook.setAdapter(advancedBookAdapter);

        edtBookId = findViewById(R.id.edtBookId);
        edtBookName = findViewById(R.id.edtBookName);
        edtUnitPrice = findViewById(R.id.edtUnitPrice);
    }

    public void processNew(View view) {
        edtBookId.setText("");
        edtBookName.setText("");
        edtUnitPrice.setText("");
        edtBookId.requestFocus();
    }

    public void processSave(View view) {
        ContentValues record = new ContentValues();
        record.put("bookId",edtBookId.getText().toString());
        record.put("bookName",edtBookName.getText().toString());
        record.put("unitPrice",Float.parseFloat(edtUnitPrice.getText().toString()));
        Publisher publisher = (Publisher) spinnerPublisher.getSelectedItem();
        record.put("publisherId", publisher.getPublisherId());

        long result = database.insert("Book",null, record);

        if (result > 0) {
            loadBookByPublisher(publisher);
        }
    }

    public void processUpdate(View view) {
        ContentValues record = new ContentValues();
        record.put("bookName",edtBookName.getText().toString());
        record.put("unitPrice",Float.parseFloat(edtUnitPrice.getText().toString()));
        Publisher publisher = (Publisher) spinnerPublisher.getSelectedItem();
        record.put("publisherId", publisher.getPublisherId());

        String bookId = edtBookId.getText().toString();
        long result = database.update("Book",record,"bookId=?",new String[]{bookId});

        if (result > 0) {
            loadBookByPublisher(publisher);
        }
    }

    public void processDelete(View view) {
        String bookId = edtBookId.getText().toString();
        long result = database.delete("Book","bookId=?",new String[]{bookId});

        Publisher publisher = (Publisher) spinnerPublisher.getSelectedItem();

        if (result > 0) {
            loadBookByPublisher(publisher);
            processNew(view);
        }
    }

    public void confirmDelete(View view) {
        // Create builder object (instance)
        AlertDialog.Builder builder = new AlertDialog.Builder(PublisherBookSqliteCRUDActivity.this);

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

    public void moveToPublisher(View view) {
        Intent intent = new Intent(PublisherBookSqliteCRUDActivity.this, PublisherSqliteCRUDActivity.class);
        startActivity(intent);
    }
}