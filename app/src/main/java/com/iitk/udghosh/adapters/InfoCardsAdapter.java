package com.iitk.udghosh.adapters;

/**
 * Created by hiteshkr on 11/10/17.
 */

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.iitk.udghosh.R;
import com.iitk.udghosh.models.InfoCard;

import java.io.File;
import java.util.List;


public class InfoCardsAdapter extends RecyclerView.Adapter<InfoCardsAdapter.MyViewHolder> {

    private Context mContext;
    private List<InfoCard> albumList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.titleoth);

            thumbnail = (ImageView) view.findViewById(R.id.thumbnailoth);
        }
    }


    public InfoCardsAdapter(Context mContext, List<InfoCard> albumList) {
        this.mContext = mContext;
        this.albumList = albumList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_info, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        InfoCard album = albumList.get(position);
        holder.title.setText(album.getName());


        // loading album cover using Glide library
//        Glide.with(mContext).load(album.getThumbnail()).into(holder.thumbnail);

        Glide.with(mContext).load(Uri.parse(album.getThumbnail())).into(holder.thumbnail);

    }


    @Override
    public int getItemCount() {
        return albumList.size();
    }
}