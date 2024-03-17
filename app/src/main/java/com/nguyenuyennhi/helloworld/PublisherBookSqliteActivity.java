package com.nguyenuyennhi.helloworld;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.nguyenuyennhi.adapter.AdvancedBookAdapter;
import com.nguyenuyennhi.model.Book;
import com.nguyenuyennhi.model.Publisher;

import java.util.ArrayList;

public class PublisherBookSqliteActivity extends AppCompatActivity {
    Spinner spinnerPublisher;
    ArrayAdapter<Publisher> publisherArrayAdapter;
    ListView lvBook;
    AdvancedBookAdapter advancedBookAdapter;

    public static final String DATABASE_NAME = "HelloWorld.sqlite";
    public static final String DB_PATH_SUFFIX = "/databases/";
    public static SQLiteDatabase database = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publisher_book_sqlite);
        addViews();
        loadPublisher();
        addEvents();
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
        database = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null); // không bao giờ bị null (không có thì nó create)

        Cursor cursor = database.rawQuery("SELECT * FROM Publisher", null);
        while(cursor.moveToNext()){
            String publisherId = cursor.getString(0);
            String publisherName = cursor.getString(1);

            Publisher publisher = new Publisher(publisherId, publisherName);
            publisherArrayAdapter.add(publisher);
        }
        cursor.close();
    }

    private void addViews() {
        spinnerPublisher = findViewById(R.id.spinnerPublisher);
        publisherArrayAdapter = new ArrayAdapter<>(PublisherBookSqliteActivity.this, android.R.layout.simple_list_item_1);
        publisherArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPublisher.setAdapter(publisherArrayAdapter);

        lvBook = findViewById(R.id.lvBook);
        advancedBookAdapter = new AdvancedBookAdapter(PublisherBookSqliteActivity.this, R.layout.advancedbook_item);
        lvBook.setAdapter(advancedBookAdapter);
    }
}