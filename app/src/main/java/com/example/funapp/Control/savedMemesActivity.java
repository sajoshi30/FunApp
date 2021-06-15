package com.example.funapp.Control;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

import com.example.funapp.Main.MainActivity;
import com.example.funapp.Model.DBHelper;
import com.example.funapp.R;

import java.util.ArrayList;

public class savedMemesActivity extends AppCompatActivity {

    ArrayList<String> savedurls;
    DBHelper DB;
    Adapter adapter;
    RecyclerView dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_memes);
        savedurls = new ArrayList<String>();
        dataList = findViewById(R.id.dataList);
        DB = new DBHelper(this);


        Cursor res = DB.getsavedmemes();

        while (res.moveToNext()) {
            if(!res.getString(0).equals(null)) {
                savedurls.add(res.getString(0));
                Log.d( "onCreate: ",res.getString(0));
            }
            else
                continue;

    }

        adapter = new Adapter(this,savedurls);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2,GridLayoutManager.VERTICAL,false);
        dataList.setLayoutManager(gridLayoutManager);
        dataList.setAdapter(adapter);


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            Intent intent = new Intent(getBaseContext(), MainActivity.class);
            startActivity(intent);
            return true; //I have tried here true also
        }
        return super.onKeyDown(keyCode, event);
    }
}