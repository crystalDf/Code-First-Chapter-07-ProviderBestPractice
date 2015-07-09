package com.star.providerbestpractice;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

    public static final String DB_NAME = "BookStore.db";

    public static final int DB_VERSION = 2;

    private MyDatabaseHelper mMyDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMyDatabaseHelper = new MyDatabaseHelper(this, DB_NAME, null, DB_VERSION);

        Button createDatabase = (Button) findViewById(R.id.create_database);
        createDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMyDatabaseHelper.getWritableDatabase();
            }
        });

        Button addData = (Button) findViewById(R.id.add_data);
        addData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = mMyDatabaseHelper.getWritableDatabase();

                ContentValues values = new ContentValues();

                values.put(MyDatabaseHelper.BOOK_COLUMN_NAME, "The Da Vinci Code");
                values.put(MyDatabaseHelper.BOOK_COLUMN_AUTHOR, "Dan Brown");
                values.put(MyDatabaseHelper.BOOK_COLUMN_PAGES, 454);
                values.put(MyDatabaseHelper.BOOK_COLUMN_PRICE, 16.96);
                db.insert(MyDatabaseHelper.TABLE_BOOK_NAME, null, values);

                values.clear();

                values.put(MyDatabaseHelper.BOOK_COLUMN_NAME, "The Lost Symbol");
                values.put(MyDatabaseHelper.BOOK_COLUMN_AUTHOR, "Dan Brown");
                values.put(MyDatabaseHelper.BOOK_COLUMN_PAGES, 510);
                values.put(MyDatabaseHelper.BOOK_COLUMN_PRICE, 19.95);
                db.insert(MyDatabaseHelper.TABLE_BOOK_NAME, null, values);

            }
        });

        Button updateData = (Button) findViewById(R.id.update_data);
        updateData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = mMyDatabaseHelper.getWritableDatabase();

                ContentValues values = new ContentValues();

                values.put(MyDatabaseHelper.BOOK_COLUMN_PRICE, 10.99);
                db.update(MyDatabaseHelper.TABLE_BOOK_NAME, values,
                        MyDatabaseHelper.BOOK_COLUMN_NAME + " = ?",
                        new String[]{"The Da Vinci Code"});
            }
        });

        Button deleteData = (Button) findViewById(R.id.delete_data);
        deleteData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = mMyDatabaseHelper.getWritableDatabase();

                db.delete(MyDatabaseHelper.TABLE_BOOK_NAME,
                        MyDatabaseHelper.BOOK_COLUMN_PAGES + " > ?",
                        new String[]{"500"});
            }
        });

        Button queryData = (Button) findViewById(R.id.query_data);
        queryData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = mMyDatabaseHelper.getWritableDatabase();

                Cursor cursor = db.query(MyDatabaseHelper.TABLE_BOOK_NAME,
                        null, null, null, null, null, null);

                while (cursor.moveToNext()) {
                    String name = cursor.getString(cursor.getColumnIndex(
                            MyDatabaseHelper.BOOK_COLUMN_NAME));
                    String author = cursor.getString(cursor.getColumnIndex(
                            MyDatabaseHelper.BOOK_COLUMN_AUTHOR));
                    int pages = cursor.getInt(cursor.getColumnIndex(
                            MyDatabaseHelper.BOOK_COLUMN_PAGES));
                    double price = cursor.getDouble(cursor.getColumnIndex(
                            MyDatabaseHelper.BOOK_COLUMN_PRICE));

                    Log.d("MainActivity", "book name is " + name);
                    Log.d("MainActivity", "book author is " + author);
                    Log.d("MainActivity", "book pages is " + pages);
                    Log.d("MainActivity", "book price is " + price);
                }

                cursor.close();
            }
        });

        Button replaceData = (Button) findViewById(R.id.replace_data);
        replaceData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = mMyDatabaseHelper.getWritableDatabase();

                db.beginTransaction();

                try {
                    db.delete(MyDatabaseHelper.TABLE_BOOK_NAME, null, null);

                    if (true) {
                        throw new NullPointerException();
                    }

                    ContentValues values = new ContentValues();
                    values.put(MyDatabaseHelper.BOOK_COLUMN_NAME, "Game of Thrones");
                    values.put(MyDatabaseHelper.BOOK_COLUMN_AUTHOR, "George Martin");
                    values.put(MyDatabaseHelper.BOOK_COLUMN_PAGES, 720);
                    values.put(MyDatabaseHelper.BOOK_COLUMN_PRICE, 20.85);
                    db.insert(MyDatabaseHelper.TABLE_BOOK_NAME, null, values);

                    db.setTransactionSuccessful();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    db.endTransaction();
                }
            }
        });
    }

}
