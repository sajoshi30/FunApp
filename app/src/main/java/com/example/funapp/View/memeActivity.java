package com.example.funapp.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.funapp.Model.DBHelper;
import com.example.funapp.Model.Meme;
import com.example.funapp.Model.MySingleton;
import com.example.funapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashSet;

public class memeActivity extends AppCompatActivity implements GestureDetector.OnGestureListener{


    ImageView memeView;
    Button saveButton;
    ProgressBar pbar;
    String imgUrl="";
    DBHelper DB;

    private GestureDetector gestureDetector;
    int memesCounter = 0;
    int totalmemes = 0;
    LinkedHashSet<Meme> lodedUrls = new LinkedHashSet<Meme>();
    ArrayList<Meme> set;
    public static final int SWIPE_THRESHOLD = 100;
    public static final int SWIPE_VELOCITY_THRESHOLD = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meme);

        DB = new DBHelper(this);
        gestureDetector = new GestureDetector(this);

        memeView = findViewById(R.id.memeView);
        pbar = findViewById(R.id.pbar_meme);
        loadMeme();


    }


    public void loadMeme()
    {
        String url = "https://meme-api.herokuapp.com/gimme";
        Log.d("hello1","entered");

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            imgUrl = response.getString("url");
                            //  loadIntoGlide(imgUrl);
                            Log.d("hello1",""+imgUrl);
                            Meme meme = new Meme(imgUrl);
                            lodedUrls.add(meme);
                            meme.setMemeurl(imgUrl);

                            loadIntoGlide(imgUrl);
                            memesCounter++;
                            totalmemes++;

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
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }



    public void loadIntoGlide(String loadUrl)
    {

        RequestManager manager = Glide.with(memeView);
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
        }).into(memeView);
    }

    public void shareMeme(View view) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT,"Hey there!! Checkout this meme : \n\n"+imgUrl);
        startActivity(Intent.createChooser(intent,"Share using.."));

    }


    public void nextMeme() {
        pbar.setVisibility(View.VISIBLE);
        if(memesCounter == totalmemes)
            loadMeme();
        else
        {
            memesCounter++;
            set = new ArrayList<Meme>(lodedUrls);
            String imgLink = set.get(memesCounter-1).getMemeurl();
            loadIntoGlide(imgLink);
        }
    }


    public void saveMeme(View view)
    {

        try {
            Boolean checkinsertdata = DB.insertintosavedmeme(imgUrl);
            if (checkinsertdata == true)
                Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e)
        {
            Log.d( "errorsaveMeme: ",""+e);
        }
    }

    public void previousMeme() {
        Cursor res = DB.getmemes();
        if(memesCounter>1)
            memesCounter = memesCounter - 1;
        else
            memesCounter = 1;
        res.move(memesCounter);
        // String prevurl = ""+res.getString(0);
        //Log.d( "previousMeme: ",""+res.getPosition()+" "+prevurl);
        set = new ArrayList<Meme>(lodedUrls);
        String imgLink = set.get(memesCounter-1).getMemeurl();
        loadIntoGlide(imgLink);
    }


    //swipe activity
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

        Log.d("swipe: ","entered");
        boolean result = false;
        float diffY = moveEvent.getY() - downEvent.getY();
        float diffX = moveEvent.getX() - downEvent.getX();
        if (Math.abs(diffX) > Math.abs(diffY)) {
            // right or left swipe
            if (Math.abs(diffX)> SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                if (diffX > 0) {
                    Log.d("swipe","rigth");
                    previousMeme();
                } else {
                    Log.d("swipe","left");
                    nextMeme();

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