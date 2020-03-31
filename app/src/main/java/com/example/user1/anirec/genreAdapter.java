package com.example.user1.anirec;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.util.ArrayList;
import java.util.List;

public class genreAdapter extends BaseAdapter {
    List<String> genre;
   public List<String> selected;
    Context mcontext;

    public genreAdapter(List<String> genre, Context mcontext) {
        this.genre = genre;
        this.mcontext = mcontext;
        selected=new ArrayList<>();
    }

    @Override
    public int getCount() {
        return genre.size();
    }

    @Override
    public Object getItem(int position) {
        return genre.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final CheckBox c=new CheckBox(mcontext);
        c.setText(genre.get(position));
        c.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    selected.add(c.getText().toString());
                else
                    selected.remove(c.getText().toString());
            }
        });
        return c;
    }
}
