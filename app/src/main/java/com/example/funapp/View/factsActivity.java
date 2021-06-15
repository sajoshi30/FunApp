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
import com.example.funapp.Model.Fact;
import com.example.funapp.Model.MySingleton;
import com.example.funapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashSet;

public class factsActivity extends AppCompatActivity implements GestureDetector.OnGestureListener{


    TextView facttv;
    ProgressBar pbar;
    String fact;
    LinkedHashSet<Fact> factsLoaded = new LinkedHashSet<Fact>();
    int currentFact;
    int factCount;
    ArrayList<Fact> set;
    public static final int SWIPE_THRESHOLD = 100;
    public static final int SWIPE_VELOCITY_THRESHOLD = 100;
    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facts);

        pbar = findViewById(R.id.pbar_joke);
        facttv = findViewById(R.id.setup);
        gestureDetector = new GestureDetector(this);
        loadFact();

    }


    public void loadFact() {
        pbar.setVisibility(View.VISIBLE);
        String url = "https://useless-facts.sameerkumar.website/api";
        //String url = "http://my-json-feed";


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("loadjokeerror", "entered1");
                        try {
                            //  Log.d("loadjokeerror", response.getString("setup"));
                            fact = response.getString("data");
                            facttv.setText("" + fact);
                            pbar.setVisibility(View.INVISIBLE);

                            Fact funFact = new Fact(fact);
                            funFact.setFact(fact);
                            factsLoaded.add(funFact);
                            factCount++;
                            currentFact++;
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

    public void loadNextFact() {
        Log.d( "jokeNext: ","jc= "+currentFact);
        if (factCount == currentFact)
            loadFact();
        else {
            currentFact++;
            Log.d( "jokeNext: ","jc= "+currentFact);
            set = new ArrayList<Fact>(factsLoaded);
            String tempSet = set.get(currentFact-1).getFact();

            Log.d( "jokeNext: ","js= "+tempSet);

            facttv.setText("" + tempSet);

        }
    }

    public void loadPreviousFact() {
        Log.d( "jokePrev: ","jc= "+currentFact);
        if (currentFact > 1)
            currentFact = currentFact - 1;
        else
            currentFact = 1;
        Log.d( "jokePrev: ","jc= "+currentFact);
        set = new ArrayList<Fact>(factsLoaded);
        String tempSet = set.get(currentFact-1).getFact();

        Log.d( "jokePrev: ","js= "+tempSet);
        facttv.setText("" + tempSet);



    }

    public void shareFact(View view) {

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT,"Hey! checkout this fact by FunApp..\n\n"+fact);
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
                    loadPreviousFact();
                } else {
                    Log.d("swipe", "left");
                    loadNextFact();

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