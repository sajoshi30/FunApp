package com.example.funapp.Control;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.funapp.Model.DBHelper;
import com.example.funapp.R;


public class enlargeSavedActivity extends AppCompatActivity {

    ImageView savedMemeView;
    ProgressBar pbar;
    String loadUrl;
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enlarge_saved);
        db = new DBHelper(this);
        savedMemeView = findViewById(R.id.savedMemeView);
        pbar = findViewById(R.id.pbar_meme);

        if (getIntent().hasExtra("image_url")) {
            loadUrl = getIntent().getStringExtra("image_url");
        }
        Log.d("hello",loadUrl);

        try {
            loadIntoGlide(loadUrl);
        }
        catch (Exception e)
        {
            Log.d("hello",""+e);
        }


    }



    public void loadIntoGlide (String loadUrl)
    {
        RequestManager manager = Glide.with(savedMemeView);
        manager.load(loadUrl).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                pbar.setVisibility(View.INVISIBLE);
                return false;
            }
        }).into(savedMemeView);

    }


    public void shareMeme(View view) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT,"Hey there! Checkout this meme from FunApp.. \n\n"+loadUrl);
        startActivity(Intent.createChooser(intent,"Share using.."));
    }

    public void deleteFromSaved(View view) {

        boolean res =  db.deleteFromSaved(loadUrl);
        if (res == true)
            Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, "Not Deleted", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getBaseContext(), savedMemesActivity.class);
        startActivity(intent);

    }
}