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
    public static final String NAME = "NAME";
    public static final String POPULARITY = "POPULARITY";
    public static final String ID = "ID";
    List<Item> artists;
    List<com.intrepid.spotifyartistviewer.ArtistDetails.ArtistAlbumPojo.Item> albums;
    Context context;
    boolean isMainAct;

    public ArtistAdapter(Context context, boolean iMA) {
        isMainAct = iMA;
        if (iMA)
            artists = new ArrayList<>();
        else
            albums = new ArrayList<>();
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.artist_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (isMainAct) {
            final Item artistItem = artists.get(position);
            holder.tvArtistName.setText(artistItem.getName());
            holder.itemView.setTag(artistItem);
            List<Image> images = artistItem.getImages();
            if (images.size() > 0) {
                Glide.with(context)
                        .load(images.get(0).getUrl())
                        .centerCrop()
                        .crossFade()
                        .into(holder.ivArtist);
            } else {
                holder.ivArtist.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_error_outline_black_24dp));
            }
            holder.llArtistHolder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent artistDeets = new Intent(context, ArtistDetails.class);
                    artistDeets.putExtra(NAME, artistItem.getName());
                    artistDeets.putExtra(ID, artistItem.getId());
                    artistDeets.putExtra(POPULARITY, artistItem.getFollowers().getTotal());
                    context.startActivity(artistDeets);
                }
            });
        } else if (!isMainAct) {
            com.intrepid.spotifyartistviewer.ArtistDetails.ArtistAlbumPojo.Item albumItem = albums.get(position);
            holder.tvArtistName.setText(albumItem.getName());
            holder.itemView.setTag(albumItem);
            List<com.intrepid.spotifyartistviewer.ArtistDetails.ArtistAlbumPojo.Image> images =
                    albumItem.getImages();
            if (images.size() > 0) {
                Glide.with(context)
                        .load(images.get(0).getUrl())
                        .centerCrop()
                        .crossFade()
                        .into(holder.ivArtist);
            } else {
                holder.ivArtist.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_error_outline_black_24dp));
            }
        }
    }

    @Override
    public int getItemCount() {
        if (isMainAct)
            return artists.size();
        else
            return albums.size();
    }

    public void addArtistItem(Item i) {
        artists.add(artists.size(), i);
    }

    public void addAlbumItem(com.intrepid.spotifyartistviewer.ArtistDetails.ArtistAlbumPojo.Item i) {
        albums.add(albums.size(), i);
    }

    public void newSearch() {
        artists.clear();
        notifyItemRangeRemoved(0, artists.size());
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
