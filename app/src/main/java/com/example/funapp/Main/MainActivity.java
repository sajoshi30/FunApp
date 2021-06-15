package com.example.funapp.Main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.funapp.Control.savedMemesActivity;
import com.example.funapp.R;
import com.example.funapp.View.factsActivity;
import com.example.funapp.View.jokesActivity;
import com.example.funapp.View.memeActivity;
import com.example.funapp.View.quotesActivity;


public class MainActivity extends AppCompatActivity {

    Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void viewNewMemes(View view) {
        Intent intent = new Intent(getBaseContext(), memeActivity.class);
        startActivity(intent);
    }

    public void viewNewJokes(View view) {
        Intent intent = new Intent(getBaseContext(), jokesActivity.class);
        startActivity(intent);
    }

    public void viewSavedMemes(View view) {
        Intent intent = new Intent(getBaseContext(), savedMemesActivity.class);
        startActivity(intent);
    }

    public void viewNewQuotes(View view) {
        Intent intent = new Intent(getBaseContext(), quotesActivity.class);
        startActivity(intent);
    }

    public void viewNewFacts(View view) {
        Intent intent = new Intent(getBaseContext(), factsActivity.class);
        startActivity(intent);
    }

    public void onBackPressed(){
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);

    }

}