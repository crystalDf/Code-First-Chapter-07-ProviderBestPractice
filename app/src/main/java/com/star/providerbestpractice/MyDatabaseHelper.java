package com.star.providerbestpractice;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    public static final String TABLE_BOOK_NAME = "book";

    public static final String BOOK_COLUMN_ID = "id";
    public static final String BOOK_COLUMN_AUTHOR = "author";
    public static final String BOOK_COLUMN_PRICE = "price";
    public static final String BOOK_COLUMN_PAGES = "pages";
    public static final String BOOK_COLUMN_NAME = "name";

    public static final String BOOK_COLUMN_CATEGORY_ID = "category_id";

    public static final String TABLE_CATEGORY_NAME = "category";

    public static final String CATEGORY_COLUMN_ID = "id";
    public static final String CATEGORY_COLUMN_NAME = "category_name";
    public static final String CATEGORY_COLUMN_CODE = "category_code";

    public static final String CREATE_BOOK = "CREATE TABLE " + TABLE_BOOK_NAME + "(" +
            BOOK_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            BOOK_COLUMN_AUTHOR + " TEXT, " +
            BOOK_COLUMN_PRICE + " REAL, " +
            BOOK_COLUMN_PAGES + " INTEGER, " +
            BOOK_COLUMN_NAME + " TEXT)";

    public static final String CREATE_CATEGORY = "CREATE TABLE " + TABLE_CATEGORY_NAME + "(" +
            CATEGORY_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            CATEGORY_COLUMN_NAME + " TEXT, " +
            CATEGORY_COLUMN_CODE + " INTEGER)";

    public static final String DROP_BOOK = "DROP TABLE IF EXISTS " + TABLE_BOOK_NAME;

    public static final String DROP_CATEGORY = "DROP TABLE IF EXISTS " + TABLE_CATEGORY_NAME;

    public static final String ALTER_BOOK = "ALTER TABLE " + TABLE_BOOK_NAME +
            " ADD COLUMN " + BOOK_COLUMN_CATEGORY_ID + " INTEGER";

    private Context mContext;

    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_BOOK);
        db.execSQL(CREATE_CATEGORY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion) {
            case 1:
                db.execSQL(CREATE_CATEGORY);
            case 2:
                db.execSQL(ALTER_BOOK);
            default:
        }
    }
}
