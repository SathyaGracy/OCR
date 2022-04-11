package com.zeyaly.extractor.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.zeyaly.extractor.model.PhotoModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import java.util.List;
import java.util.Locale;


public class DatabaseHandler extends SQLiteOpenHelper implements InterfacePhotoModel {
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());


    private static final String TABLE_NAME = "PhotosDB";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_PHOTO = "_data";
    private static final String COLUMN_HEAD = "_title";
    private static final String COLUMN_TEXTVALUE = "_textvalue";
    private static final String COLUMN_CREATED_AT = "created_at";


    public DatabaseHandler(Context context) {
        super(context, "OCRData", null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_MODEL_TABLE_ocrdata = "CREATE TABLE " + TABLE_NAME + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY, "
                + COLUMN_PHOTO + " BLOB, "
                + COLUMN_HEAD + " TEXT, "
                + COLUMN_TEXTVALUE + " TEXT, "
                + COLUMN_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP);";
        db.execSQL(CREATE_MODEL_TABLE_ocrdata);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }


    @Override
    public void add(PhotoModel photoModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PHOTO, photoModel.getByteBuffer());
        values.put(COLUMN_HEAD, photoModel.getTitle());
        values.put(COLUMN_TEXTVALUE, photoModel.getTextValue());
        values.put(COLUMN_CREATED_AT, System.currentTimeMillis());

        // Inserting Row
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    @Override
    public PhotoModel get(int index) {

        String query = "SELECT " + COLUMN_ID + ","
                + COLUMN_PHOTO + ","
                + COLUMN_HEAD + ","
                + COLUMN_TEXTVALUE + ","
                + COLUMN_CREATED_AT +
                " FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = " + index;


        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        PhotoModel ocrdata = null;

        if (cursor.moveToFirst()) {
            ocrdata = new PhotoModel();
            ocrdata.setId(Integer.parseInt(cursor.getString(0)));
//            ocrdata.setByteBuffer(cursor.getBlob(1));
            ocrdata.setCreated_at(new Date(cursor.getLong(2)));


        }

        return ocrdata;
    }

    @Override
    public ArrayList<PhotoModel> all() {

        SQLiteDatabase db = this.getWritableDatabase();

        ArrayList<PhotoModel> ocrdatas = new ArrayList<>();

        String query = "SELECT  * FROM " + TABLE_NAME + " ORDER BY " + COLUMN_CREATED_AT + " DESC";


        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                PhotoModel ocrdata = new PhotoModel();
                ocrdata.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                ocrdata.setByteBuffer(cursor.getBlob(cursor.getColumnIndex(COLUMN_PHOTO)));
                ocrdata.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_HEAD)));
                ocrdata.setTextValue(cursor.getString(cursor.getColumnIndex(COLUMN_TEXTVALUE)));
                ocrdata.setByteBuffer(cursor.getBlob(cursor.getColumnIndex(COLUMN_PHOTO)));
                ocrdata.setCreated_at(new Date(cursor.getColumnIndex(COLUMN_CREATED_AT)));
                ocrdatas.add(ocrdata);
            } while (cursor.moveToNext());
        }
        return ocrdatas;
    }

    @Override
    public void remove(int index) {

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = " + index;

        db.execSQL(query);
        db.close();

    }

    @Override
    public void update(PhotoModel photoModel, int index) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_HEAD, photoModel.getTitle());
        values.put(COLUMN_TEXTVALUE, photoModel.getTextValue());
        //  String query = "UPDATE " + TABLE_NAME + " SET " + COLUMN_HEAD + "=" + photoModel.getTitle() + COLUMN_TEXTVALUE + "=" + photoModel.getTextValue() + " WHERE " + COLUMN_ID + " = " + index;
        db.update(TABLE_NAME,values, COLUMN_ID + " = " + index,null);
        db.close();
    }


}
