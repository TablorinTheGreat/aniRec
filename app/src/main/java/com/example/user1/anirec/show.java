package com.example.user1.anirec;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class show extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        anime a=(anime)getIntent().getParcelableExtra("anime");
        if(a.t==anime.type.ITEM)
            ((TextView)findViewById(R.id.txt1)).setText("mean distance");
        ((TextView)findViewById(R.id.name)).setText(String.valueOf(a.name));
        ((TextView)findViewById(R.id.rating)).setText(String.valueOf(a.rate));
        ((TextView)findViewById(R.id.predRat)).setText(String.valueOf(a.dis));
        ((TextView)findViewById(R.id.RecNum)).setText(String.valueOf(a.numRec));
        ((TextView)findViewById(R.id.genre)).setText(String.valueOf(a.getGenre()));
        if(a.img!=null)
            ((ImageView)findViewById(R.id.image)).setImageBitmap(a.img);
        ListView l=(ListView)findViewById(R.id.voters);
        l.setAdapter(new ArrayAdapter<String>(show.this,android.R.layout.simple_list_item_1,a.voters_list));

    }
}
