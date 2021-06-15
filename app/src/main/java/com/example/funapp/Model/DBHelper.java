package com.example.funapp.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context) {
        super(context, "funapp.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase DB) {
        DB.execSQL("create Table memes( urls TEXT NOT NULL UNIQUE)");
        DB.execSQL("create Table savedmemes(urls TEXT NOT NULL UNIQUE )");
        DB.execSQL("create Table jokes( setup TEXT NOT NULL UNIQUE, punchline TEXT NOT NULL UNIQUE)");
        DB.execSQL("create Table savedjokes( setup TEXT NOT NULL UNIQUE, punchline TEXT NOT NULL UNIQUE)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int i, int i1) {
        Log.d( "onUpgrade: ","entered on upgrade");
        DB.execSQL("drop Table if exists memes");
        DB.execSQL("drop Table if exists savedmemes");
        DB.execSQL("drop Table if exists jokes");
        DB.execSQL("drop Table if exists savedjokes");

        onCreate(DB);


    }


    public Boolean insertintomemes(String url)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("urls", url);
        long result=DB.insert("memes", null, contentValues);
        if(result==-1){
            return false;
        }else{
            return true;
        }
    }
    public Boolean insertintosavedmeme(String url)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("urls", url);
        long result=DB.insert("savedmemes", null, contentValues);
        if(result==-1){
            return false;
        }else{
            return true;
        }
    }
    public Boolean insertintosavedjokes(String set,String punch)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("setup", set);
        contentValues.put("punchline", punch);

        long result=DB.insert("savedjokes", null, contentValues);
        if(result==-1){
            return false;
        }else{
            return true;
        }
    }


    public Boolean deleteFromSaved (String url)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from savedmemes where urls = ?", new String[]{url});
        if (cursor.getCount() > 0) {
            long result = DB.delete("savedmemes", "urls=?", new String[]{url});
            if (result == -1) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }

    }



    public Cursor getmemes ()
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from memes", null);
        return cursor;

    }
    public Cursor getsavedmemes ()
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from savedmemes", null);
        return cursor;

    }
    public void clearmemes()
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        DB.execSQL("DELETE FROM memes");

    }
}




