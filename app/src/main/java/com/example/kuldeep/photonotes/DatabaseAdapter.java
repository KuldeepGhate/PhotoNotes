package com.example.kuldeep.photonotes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Message;
import android.widget.Toast;

import java.io.File;

/**
 * Created by Kuldeep on 2/6/2015.
 */
public class DatabaseAdapter {

    VivzHelper helper;

    public DatabaseAdapter(Context context) {
        helper = new VivzHelper(context);
    }

    public long insertData(String path, String name) {

        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(VivzHelper.NAME, name);
        contentValues.put(VivzHelper.PATH, path);
        long id = db.insert(VivzHelper.TABLE_NAME, null, contentValues);
        return id;
    }


    public String getAllData() {
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] columns = {VivzHelper.UID, VivzHelper.NAME, VivzHelper.PATH};
        Cursor cursor = db.query(VivzHelper.TABLE_NAME, columns, null, null, null, null, null);
        StringBuffer buffer = new StringBuffer();

        while (cursor.moveToNext()) {

            int cid = cursor.getInt(0);
            String name = cursor.getString(1);
            String password = cursor.getString(2);
            buffer.append(cid + " " + name + " " + password + "\n");
        }
        return buffer.toString();
    }
}
       class VivzHelper extends SQLiteOpenHelper {
       protected static final String DATABASE_NAME = "vivzdatabase.db";
       protected static final String TABLE_NAME = "VIVZTABLE";
       protected static final String UID = "_id";
       protected static final String NAME = "name";
       protected static final String PATH = "path";
       protected static final int DATABASE_VERSION = 9;
     //  protected static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("+ NAME + " VARCHAR(255), " + PATH + " VARCHAR(255));";
     private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("+ UID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+ NAME + " VARCHAR(255), " + PATH + " VARCHAR(255));";
           protected static final String DROP_TABLE ="DROP TABLE IF EXISTS " +TABLE_NAME;
       protected Context context;
       protected Message m = new Message();

       VivzHelper(Context context){
           super(context, DATABASE_NAME,null,DATABASE_VERSION);
           this.context = context;
      //     Toast.makeText(context,"Constructor called",Toast.LENGTH_LONG).show();
       }

       @Override
       public void onCreate(SQLiteDatabase db) {
           try {
               db.execSQL(CREATE_TABLE);
           } catch (android.database.SQLException e) {
               Toast.makeText(context, "onCreate Called", Toast.LENGTH_LONG).show();
           }
       }

       @Override
       public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
           try {
               db.execSQL(DROP_TABLE);
               onCreate(db);
           } catch (android.database.SQLException e) {
               Toast.makeText(context,"onUpgrade Called",Toast.LENGTH_LONG).show();
           }
       }
   }

