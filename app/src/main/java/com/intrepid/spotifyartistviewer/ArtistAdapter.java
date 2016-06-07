package com.intrepid.spotifyartistviewer;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.intrepid.spotifyartistviewer.ArtistInfo.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Memo on 6/7/16.
 */
public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ViewHolder> {
    List<Item> artists;
    public ArtistAdapter(){
        artists = new ArrayList<>();
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.artist_item,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Item artistItem = artists.get(position);
        holder.tvArtistName.setText(artistItem.getName());
        holder.itemView.setTag(artistItem);
    }

    @Override
    public int getItemCount() {
        return artists.size();
    }

    public void addArtistItem(Item i){
        artists.add(artists.size(),i);
    }

    public void newSearch(){
        artists.clear();
        notifyItemRangeRemoved(0,artists.size());
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvArtistName;
        public ViewHolder(View itemView) {
            super(itemView);
            tvArtistName = (TextView) itemView.findViewById(R.id.tvArtistName);
        }
    }
}
