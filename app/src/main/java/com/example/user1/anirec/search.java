package com.example.user1.anirec;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class search extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        final genreAdapter ga=new genreAdapter(getIntent().getStringArrayListExtra("genre"),this);
        ((GridView)findViewById(R.id.genres)).setAdapter(ga);
        Integer[] i=new Integer[50];
        for(int j=1;j<=50;j++)
            i[j-1]=j;
        final Spinner user_rec= (Spinner)findViewById(R.id.user_rec);
        user_rec.setAdapter(new ArrayAdapter<Integer>(this,android.R.layout.simple_spinner_item,i));
        final Spinner types= (Spinner)findViewById(R.id.types);
        final Spinner score= (Spinner)findViewById(R.id.score);
        final Spinner pre_score= (Spinner)findViewById(R.id.predicted_score);
        final EditText dis=(EditText)findViewById(R.id.dis);
        types.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TableRow user_only=(TableRow)findViewById(R.id.user_only);
                TextView pre=(TextView)findViewById(R.id.pre);
                if(position==1){
                    user_only.setVisibility(View.GONE);
                pre.setText("mean distance");
                dis.setVisibility(View.VISIBLE);
                pre_score.setVisibility(View.GONE);
                }
                else {user_only.setVisibility(View.VISIBLE);
                    pre.setText("predicted score");
                    dis.setVisibility(View.GONE);
                    pre_score.setVisibility(View.VISIBLE);}
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        try {
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);
            ((android.widget.ListPopupWindow) popup.get(user_rec)).setHeight(500);
            ((android.widget.ListPopupWindow) popup.get(score)).setHeight(500);
            ((android.widget.ListPopupWindow) popup.get(pre_score)).setHeight(500);
        }
        catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {
            // silently fail...
        }
        final SearchView sv=(SearchView)findViewById(R.id.query);
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                List<anime> iterate;
                List<anime> result=new ArrayList<>();
                boolean isuser=types.getSelectedItem().toString().equals("user");
                if(isuser)
                    iterate=getIntent().getParcelableArrayListExtra("user");
                else
                    iterate=getIntent().getParcelableArrayListExtra("item");
                for (anime a:iterate) {
                    if(a.name.toLowerCase().contains(sv.getQuery().toString().toLowerCase()))
                        result.add(a);
                }
                Intent i=new Intent(search.this,result.class);
                i.putExtra("result", (ArrayList) result);
                i.putExtra("isuser",isuser);
                startActivity(i);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        ((ImageButton)findViewById(R.id.search)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<anime> iterate;
                List<anime> result=new ArrayList<>();
                boolean isuser=types.getSelectedItem().toString().equals("user");
                if(isuser)
                    iterate=getIntent().getParcelableArrayListExtra("user");
                else
                    iterate=getIntent().getParcelableArrayListExtra("item");
                for (anime a:iterate) {
                    if(a.rate>=Integer.valueOf(score.getSelectedItem().toString())
                    &&a.voters_list.size()>=Integer.valueOf(((TextView)findViewById(R.id.voter_num)).getText().toString())
                        &&(((!isuser)&&a.dis<=Integer.valueOf(pre_score.getSelectedItem().toString()))
                            ||(isuser&&a.numRec>=Integer.valueOf(user_rec.getSelectedItem().toString()) &&a.dis>=Integer.valueOf(pre_score.getSelectedItem().toString())))
                            &&((((Spinner)findViewById(R.id.genre_mode)).getSelectedItem().toString().equals("include all")&&a.genre.containsAll(ga.selected))||
                            (((Spinner)findViewById(R.id.genre_mode)).getSelectedItem().toString().equals("exclude all")&&doesExcludeGenre(a,ga.selected))))
                        result.add(a);
                }
                Intent i=new Intent(search.this,result.class);
                i.putExtra("result", (ArrayList) result);
                i.putExtra("isuser",isuser);
                startActivity(i);
            }
        });
    }
    public boolean doesExcludeGenre(anime a,List g){
        for (String s:
             a.genre)
            if(g.contains(s))return false;
        return true;
    }
}
