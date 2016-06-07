package com.intrepid.spotifyartistviewer;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.intrepid.spotifyartistviewer.ArtistDetails.ArtistDetails;
import com.intrepid.spotifyartistviewer.ArtistInfoPojo.Image;
import com.intrepid.spotifyartistviewer.ArtistInfoPojo.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Memo on 6/7/16.
 */
public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ViewHolder> {
    List<Item> mArtists;
    List<com.intrepid.spotifyartistviewer.ArtistDetails.ArtistAlbumPojo.Item> mAlbums;
    Context mContext;
    boolean isMainAct;
    public static final String NAME = "NAME";
    public static final String POPULARITY = "POPULARITY";
    public static final String ID = "ID";
    public ArtistAdapter(Context context, boolean iMA){
        isMainAct = iMA;
        if(iMA)
            mArtists = new ArrayList<>();
        else
            mAlbums = new ArrayList<>();
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
        if(isMainAct){
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
                        Intent artistDeets = new Intent(mContext, ArtistDetails.class);
                        artistDeets.putExtra(NAME, artistItem.getName());
                        artistDeets.putExtra(ID, artistItem.getId());
                        artistDeets.putExtra(POPULARITY, artistItem.getFollowers().getTotal());
                        mContext.startActivity(artistDeets);
                    }
                });
            }
        else if(!isMainAct){
            com.intrepid.spotifyartistviewer.ArtistDetails.ArtistAlbumPojo.Item albumItem = mAlbums.get(position);
            holder.tvArtistName.setText(albumItem.getName());
            holder.itemView.setTag(albumItem);
            List<com.intrepid.spotifyartistviewer.ArtistDetails.ArtistAlbumPojo.Image> images =
                    albumItem.getImages();
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
        }
    }

    @Override
    public int getItemCount() {
        if(isMainAct)
            return mArtists.size();
        else
            return mAlbums.size();
    }

    public void addArtistItem(Item i){
        mArtists.add(mArtists.size(),i);
    }

    public void addAlbumItem(com.intrepid.spotifyartistviewer.ArtistDetails.ArtistAlbumPojo.Item i){
        mAlbums.add(mAlbums.size(), i);
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
