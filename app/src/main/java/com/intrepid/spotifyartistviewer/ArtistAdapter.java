package com.intrepid.spotifyartistviewer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.intrepid.spotifyartistviewer.ArtistInfo.Image;
import com.intrepid.spotifyartistviewer.ArtistInfo.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Memo on 6/7/16.
 */
public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ViewHolder> {
    List<Item> mArtists;
    Context mContext;
    public static final String NAME = "NAME";
    public static final String POPULARITY = "POPULARITY";
    public static final String ID = "ID";
    public ArtistAdapter(Context context){
        mArtists = new ArrayList<>();
        mContext = context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.artist_item,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Item artistItem = mArtists.get(position);
        holder.tvArtistName.setText(artistItem.getName());
        holder.itemView.setTag(artistItem);
        List<Image> images = artistItem.getImages();
        if(images.size()>0) {
            Glide.with(mContext)
                    .load(images.get(0).getUrl())
                    .centerCrop()
                    .crossFade()
                    .into(holder.ivArtist);
        }
        else {
            holder.ivArtist.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_error_outline_black_24dp));
        }
        holder.llArtistHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent artistDeets = new Intent();
                artistDeets.putExtra(NAME, artistItem.getName());
                artistDeets.putExtra(ID,artistItem.getId());
                artistDeets.putExtra(POPULARITY, artistItem.getPopularity());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mArtists.size();
    }

    public void addArtistItem(Item i){
        mArtists.add(mArtists.size(),i);
    }

    public void newSearch(){
        mArtists.clear();
        notifyItemRangeRemoved(0, mArtists.size());
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvArtistName;
        ImageView ivArtist;
        LinearLayout llArtistHolder;
        public ViewHolder(View itemView) {
            super(itemView);
            tvArtistName = (TextView) itemView.findViewById(R.id.tvArtistName);
            ivArtist = (ImageView) itemView.findViewById(R.id.ivArtist);
            llArtistHolder = (LinearLayout) itemView.findViewById(R.id.llArtistHolder);
        }
    }
}
