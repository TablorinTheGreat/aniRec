package com.example.user1.anirec;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class result extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main);
        ListView l=(ListView)findViewById(R.id.anime_list);
        anime.type t;
        if(getIntent().getBooleanExtra("isuser",true))
        t=anime.type.USER;
        else
            t=anime.type.ITEM;
        final animeAdapter aa= new animeAdapter(this,getIntent().<anime>getParcelableArrayListExtra("result"),t);
        l.setAdapter(aa);
        l.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://myanimelist.net/anime/"+aa.getItem(position).id)));
                return true;
            }
        });
        l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i=new Intent(result.this,show.class);
                i.putExtra("anime",aa.getItem(position));
                startActivity(i);
            }
        });
    }
}
