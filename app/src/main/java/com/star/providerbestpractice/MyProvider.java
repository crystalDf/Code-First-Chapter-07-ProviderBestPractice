package com.star.providerbestpractice;


import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;

public class MyProvider extends ContentProvider {

    public static final int BOOK_DIR = 0;
    public static final int BOOK_ITEM = 1;
    public static final int CATEGORY_DIR = 2;
    public static final int CATEGORY_ITEM = 3;

    public static final String AUTHORITY = "com.star.providerbestpractice.provider";
    public static final Uri AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);

    public static final Uri CONTENT_BOOK_URI = Uri.withAppendedPath(AUTHORITY_URI,
            MyDatabaseHelper.TABLE_BOOK_NAME);
    public static final Uri CONTENT_CATEGORY_URI = Uri.withAppendedPath(AUTHORITY_URI,
            MyDatabaseHelper.TABLE_CATEGORY_NAME);

    private MyDatabaseHelper mMyDatabaseHelper;

    private static UriMatcher sUriMatcher;

    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        sUriMatcher.addURI(AUTHORITY, MyDatabaseHelper.TABLE_BOOK_NAME, BOOK_DIR);
        sUriMatcher.addURI(AUTHORITY, MyDatabaseHelper.TABLE_BOOK_NAME + "/#", BOOK_ITEM);
        sUriMatcher.addURI(AUTHORITY, MyDatabaseHelper.TABLE_CATEGORY_NAME, CATEGORY_DIR);
        sUriMatcher.addURI(AUTHORITY, MyDatabaseHelper.TABLE_CATEGORY_NAME + "/#", CATEGORY_ITEM);
    }

    @Override
    public boolean onCreate() {

        mMyDatabaseHelper = new MyDatabaseHelper(getContext(), MainActivity.DB_NAME, null, 2);

        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = mMyDatabaseHelper.getReadableDatabase();

        String newSelection;
        Cursor cursor = null;

        switch (sUriMatcher.match(uri)) {
            case BOOK_DIR:
                cursor = db.query(MyDatabaseHelper.TABLE_BOOK_NAME,
                        projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case BOOK_ITEM:
                String bookId = uri.getPathSegments().get(1);
                newSelection = MyDatabaseHelper.BOOK_COLUMN_ID + " = " + bookId +
                        (!TextUtils.isEmpty(selection) ? " AND " + "(" + selection + ")" : "");
                cursor = db.query(MyDatabaseHelper.TABLE_BOOK_NAME,
                        projection, newSelection, selectionArgs, null, null, sortOrder);
                break;
            case CATEGORY_DIR:
                cursor = db.query(MyDatabaseHelper.TABLE_CATEGORY_NAME,
                        projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case CATEGORY_ITEM:
                String categoryId = uri.getPathSegments().get(1);
                newSelection = MyDatabaseHelper.CATEGORY_COLUMN_ID + " = " + categoryId +
                        (!TextUtils.isEmpty(selection) ? " AND " + "(" + selection + ")" : "");
                cursor = db.query(MyDatabaseHelper.TABLE_CATEGORY_NAME,
                        projection, newSelection, selectionArgs, null, null, sortOrder);
                break;
            default:
                break;
        }

        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = mMyDatabaseHelper.getWritableDatabase();

        Uri uriReturn = null;

        switch (sUriMatcher.match(uri)) {
            case BOOK_DIR:
            case BOOK_ITEM:
                long newBookId = db.insert(MyDatabaseHelper.TABLE_BOOK_NAME, null, values);
                if (newBookId > -1) {
                    uriReturn = ContentUris.withAppendedId(CONTENT_BOOK_URI, newBookId);
                }
                break;
            case CATEGORY_DIR:
            case CATEGORY_ITEM:
                long newCategoryId = db.insert(MyDatabaseHelper.TABLE_CATEGORY_NAME, null, values);
                if (newCategoryId > -1) {
                    uriReturn = ContentUris.withAppendedId(CONTENT_CATEGORY_URI, newCategoryId);
                }
                break;
            default:
                break;
        }

        if (uriReturn != null) {
            getContext().getContentResolver().notifyChange(uriReturn, null);
        }

        return uriReturn;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mMyDatabaseHelper.getWritableDatabase();

        String newSelection;
        int updatedRows = 0;

        switch (sUriMatcher.match(uri)) {
            case BOOK_DIR:
                updatedRows = db.update(MyDatabaseHelper.TABLE_BOOK_NAME, values,
                        selection, selectionArgs);
                break;
            case BOOK_ITEM:
                String bookId = uri.getPathSegments().get(1);
                newSelection = MyDatabaseHelper.BOOK_COLUMN_ID + " = " + bookId +
                        (!TextUtils.isEmpty(selection) ? " AND " + "(" + selection + ")" : "");
                updatedRows = db.update(MyDatabaseHelper.TABLE_BOOK_NAME, values,
                        newSelection, selectionArgs);
                break;
            case CATEGORY_DIR:
                updatedRows = db.update(MyDatabaseHelper.TABLE_CATEGORY_NAME, values,
                        selection, selectionArgs);
                break;
            case CATEGORY_ITEM:
                String newCategoryId = uri.getPathSegments().get(1);
                newSelection = MyDatabaseHelper.CATEGORY_COLUMN_ID + " = " + newCategoryId +
                        (!TextUtils.isEmpty(selection) ? " AND " + "(" + selection + ")" : "");
                updatedRows = db.update(MyDatabaseHelper.TABLE_CATEGORY_NAME, values,
                        newSelection, selectionArgs);
                break;
            default:
                break;
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return updatedRows;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mMyDatabaseHelper.getWritableDatabase();

        String newSelection;
        int deletedRows = 0;

        switch (sUriMatcher.match(uri)) {
            case BOOK_DIR:
                deletedRows = db.delete(MyDatabaseHelper.TABLE_BOOK_NAME,
                        selection, selectionArgs);
                break;
            case BOOK_ITEM:
                String bookId = uri.getPathSegments().get(1);
                newSelection = MyDatabaseHelper.BOOK_COLUMN_ID + " = " + bookId +
                        (!TextUtils.isEmpty(selection) ? " AND " + "(" + selection + ")" : "");
                deletedRows = db.delete(MyDatabaseHelper.TABLE_BOOK_NAME,
                        newSelection, selectionArgs);
                break;
            case CATEGORY_DIR:
                deletedRows = db.delete(MyDatabaseHelper.TABLE_CATEGORY_NAME,
                        selection, selectionArgs);
                break;
            case CATEGORY_ITEM:
                String newCategoryId = uri.getPathSegments().get(1);
                newSelection = MyDatabaseHelper.CATEGORY_COLUMN_ID + " = " + newCategoryId +
                        (!TextUtils.isEmpty(selection) ? " AND " + "(" + selection + ")" : "");
                deletedRows = db.delete(MyDatabaseHelper.TABLE_CATEGORY_NAME,
                        newSelection, selectionArgs);
                break;
            default:
                break;
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return deletedRows;
    }



    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case BOOK_DIR:
                return "vnd.android.cursor.dir/vnd" +
                        "." + AUTHORITY +
                        "." + MyDatabaseHelper.TABLE_BOOK_NAME;
            case BOOK_ITEM:
                return "vnd.android.cursor.item/vnd" +
                        "." + AUTHORITY +
                        "." + MyDatabaseHelper.TABLE_BOOK_NAME;
            case CATEGORY_DIR:
                return "vnd.android.cursor.dir/vnd" +
                        "." + AUTHORITY +
                        "." + MyDatabaseHelper.TABLE_CATEGORY_NAME;
            case CATEGORY_ITEM:
                return "vnd.android.cursor.item/vnd" +
                        "." + AUTHORITY +
                        "." + MyDatabaseHelper.TABLE_CATEGORY_NAME;
        }
        return null;
    }
}
