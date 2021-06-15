package com.example.funapp.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.funapp.Model.MySingleton;
import com.example.funapp.Model.Quote;
import com.example.funapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashSet;

public class quotesActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {

    String quote;
    String author;
    TextView quotetv;
    TextView authortv;
    ProgressBar pbar_joke;
    LinkedHashSet<Quote> quotesLoaded = new LinkedHashSet<Quote>();
    int currentQuote;
    int quoteCount;
    public static final int SWIPE_THRESHOLD = 100;
    public static final int SWIPE_VELOCITY_THRESHOLD = 100;
    private GestureDetector gestureDetector;
    ArrayList<Quote> set;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quotes);

        quotetv = findViewById(R.id.setup);
        authortv = findViewById(R.id.punchline);
        pbar_joke = findViewById(R.id.pbar_joke);
        gestureDetector = new GestureDetector(this);

        loadQuote();
    }

    public void loadQuote() {
        pbar_joke.setVisibility(View.VISIBLE);
        String url = "https://freequote.herokuapp.com/";
        //String url = "http://my-json-feed";


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("loadjokeerror", "entered1");
                        try {

                            quote = response.getString("quote");
                            author = response.getString("author");
                            quotetv.setText("Quote : " + quote);
                            authortv.setText("Author : " + author);
                            pbar_joke.setVisibility(View.INVISIBLE);

                            Quote quotes = new Quote(quote, author);
                            quotes.setQuote(quote);
                            quotes.setAuthor(author);
                            quotesLoaded.add(quotes);
                            quoteCount++;
                            currentQuote++;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error

                    }
                });

// Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    public void loadNextQuote() {
        Log.d( "jokeNext: ","jc= "+currentQuote);
        if (quoteCount == currentQuote)
            loadQuote();
        else {
            currentQuote++;
            Log.d( "jokeNext: ","jc= "+currentQuote);
            set = new ArrayList<Quote>(quotesLoaded);
            String tempSet = set.get(currentQuote-1).getQuote();
            String tempPunch = set.get(currentQuote-1).getAuthor();
            Log.d( "jokeNext: ","js= "+tempSet);

            quotetv.setText("" + tempSet);
            authortv.setText("" + tempPunch);
        }
    }

    public void loadPreviousQuote() {
        Log.d( "jokePrev: ","jc= "+currentQuote);
        if (currentQuote > 1)
            currentQuote = currentQuote - 1;
        else
            currentQuote = 1;
        Log.d( "jokePrev: ","jc= "+currentQuote);
        set = new ArrayList<Quote>(quotesLoaded);
        String tempSet = set.get(currentQuote-1).getQuote();
        String tempPunch = set.get(currentQuote-1).getAuthor();
        Log.d( "jokeNext: ","js= "+tempSet);

        quotetv.setText("" + tempSet);
        authortv.setText("" + tempPunch);


    }


    public void shareJoke(View view) {

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT,"Hey! checkout this quote by FunApp..\n\n"+quote+"\n\nBy : "+author);
        startActivity(Intent.createChooser(intent,"Share using.."));
    }


    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent downEvent, MotionEvent moveEvent, float velocityX, float velocityY) {

        Log.d("swipe: ", "entered");
        boolean result = false;
        float diffY = moveEvent.getY() - downEvent.getY();
        float diffX = moveEvent.getX() - downEvent.getX();
        if (Math.abs(diffX) > Math.abs(diffY)) {
            // right or left swipe
            if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                if (diffX > 0) {
                    Log.d("swipe", "rigth");
                    loadPreviousQuote();
                } else {
                    Log.d("swipe", "left");
                    loadNextQuote();

                }
                result = true;
            }
        }

        return result;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }






}