package com.example.funapp.Control;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.funapp.R;

import java.util.List;


public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    List<String> savedUrls;
    private Context mContext;
    LayoutInflater inflater;


    public Adapter(Context ctx, List<String> savedUrls){

        this.savedUrls = savedUrls;
        this.inflater = LayoutInflater.from(ctx);
        mContext = ctx;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.cardimage,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RequestManager manager = Glide.with(holder.savedMeme);
        manager.load(savedUrls.get(position)).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                //pbar.setVisibility(View.INVISIBLE);
                return false;
            }
        }).into(holder.savedMeme);
    }

    @Override
    public int getItemCount() {
        return savedUrls.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView savedMeme;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            savedMeme = itemView.findViewById(R.id.savedMeme);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //  Toast.makeText(v.getContext(), "Clicked -> " + getAdapterPosition(), Toast.LENGTH_SHORT).show();
                    try {
                        Intent intent = new Intent(mContext, enlargeSavedActivity.class);
                        intent.putExtra("image_url", savedUrls.get(getAdapterPosition()));
                        mContext.startActivity(intent);
                    }
                    catch (Exception e)
                    {
                        Log.d( "adapter: ",""+e);
                    }

                }
            });
        }
    }
}
