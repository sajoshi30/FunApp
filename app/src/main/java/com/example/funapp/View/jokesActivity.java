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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.funapp.Model.DBHelper;
import com.example.funapp.Model.Joke;
import com.example.funapp.Model.MySingleton;
import com.example.funapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashSet;

public class jokesActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {


    DBHelper DB ;
    String setup;
    String punchline;
    TextView setuptv;
    TextView punchlinetv;
    ProgressBar pbar_joke;
    LinkedHashSet<Joke> jokesLoaded = new LinkedHashSet<Joke>();
    int currentJoke;
    int jokeCount;
    public static final int SWIPE_THRESHOLD = 100;
    public static final int SWIPE_VELOCITY_THRESHOLD = 100;
    private GestureDetector gestureDetector;
    ArrayList<Joke> set;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jokes);

        setuptv = findViewById(R.id.setup);
        punchlinetv = findViewById(R.id.punchline);
        pbar_joke = findViewById(R.id.pbar_joke);
        gestureDetector = new GestureDetector(this);
        DB = new DBHelper(this);
        loadJoke();

    }


    public void loadJoke() {
        pbar_joke.setVisibility(View.VISIBLE);
        String url = "https://official-joke-api.appspot.com/random_joke";
        //String url = "http://my-json-feed";


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("loadjokeerror", "entered1");
                        try {
                            Log.d("loadjokeerror", response.getString("setup"));
                            setup = response.getString("setup");
                            punchline = response.getString("punchline");
                            setuptv.setText("" + setup);
                            punchlinetv.setText("" + punchline);
                            pbar_joke.setVisibility(View.INVISIBLE);

                            Joke joke = new Joke(setup, punchline);
                            joke.setSetup(setup);
                            joke.setPunchline(punchline);
                            jokesLoaded.add(joke);
                            jokeCount++;
                            currentJoke++;
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

    public void loadNextJoke() {
        Log.d( "jokeNext: ","jc= "+currentJoke);
        if (jokeCount == currentJoke)
            loadJoke();
        else {
            currentJoke++;
            Log.d( "jokeNext: ","jc= "+currentJoke);
            set = new ArrayList<Joke>(jokesLoaded);
            String tempSet = set.get(currentJoke-1).getSetup();
            String tempPunch = set.get(currentJoke-1).getPunchline();
            Log.d( "jokeNext: ","js= "+tempSet);

            setuptv.setText("" + tempSet);
            punchlinetv.setText("" + tempPunch);
        }
    }

    public void loadPreviousJoke() {
        Log.d( "jokePrev: ","jc= "+currentJoke);
        if (currentJoke > 1)
            currentJoke = currentJoke - 1;
        else
            currentJoke = 1;
        Log.d( "jokePrev: ","jc= "+currentJoke);
        set = new ArrayList<Joke>(jokesLoaded);
        String tempSet = set.get(currentJoke-1).getSetup();
        String tempPunch = set.get(currentJoke-1).getPunchline();
        Log.d( "jokePrev: ","js= "+tempSet);
        setuptv.setText("" + tempSet);
        punchlinetv.setText("" + tempPunch);


    }

    public void saveJoke(View view){
        set = new ArrayList<Joke>(jokesLoaded);
        String tempSet = set.get(currentJoke-1).getSetup();
        String tempPunch = set.get(currentJoke-1).getPunchline();
        Boolean checkinsertdata =  DB.insertintosavedjokes(tempSet,tempPunch);

        try {
            if (checkinsertdata == true)
                Toast.makeText(this, "Joke Saved", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "Joke not saved", Toast.LENGTH_SHORT).show();
        }
        catch(Exception e)
        {
            Log.d( "saveJoke: ",""+e);
        }
    }

    public void shareJoke(View view) {

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT,"Hey! checkout this joke by FunApp..\n\n"+setup+"\n\n"+punchline);
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
                    loadPreviousJoke();
                } else {
                    Log.d("swipe", "left");
                    loadNextJoke();

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