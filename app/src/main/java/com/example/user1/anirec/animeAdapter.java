package com.example.user1.anirec;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class animeAdapter extends ArrayAdapter<anime> {
    List<anime> animeList;
    anime.type t;

    public animeAdapter(@NonNull Context context,List<anime> animeList, anime.type t) {
        super(context,0,animeList);
        this.animeList = animeList;
        this.t = t;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(getContext()).inflate(R.layout.anime_item,parent,false);
        if(t==anime.type.ITEM)
            ((TextView)listItem.findViewById(R.id.txt1)).setText("mean distance");
        ((TextView)listItem.findViewById(R.id.name)).setText(String.valueOf(animeList.get(position).name));
        ((TextView)listItem.findViewById(R.id.rating)).setText(String.valueOf(animeList.get(position).rate));
        ((TextView)listItem.findViewById(R.id.predRat)).setText(String.valueOf(animeList.get(position).dis));
        ((TextView)listItem.findViewById(R.id.RecNum)).setText(String.valueOf(animeList.get(position).numRec));
        if(animeList.get(position).img!=null)
        ((ImageView)listItem.findViewById(R.id.image)).setImageBitmap(animeList.get(position).img);
        return listItem;
    }


}
