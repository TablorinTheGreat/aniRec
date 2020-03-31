package com.example.user1.anirec;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private SectionsPagerAdapter mSectionsPagerAdapter;
    List<anime> usersort= new ArrayList<>();
    List<anime> itemsort= new ArrayList<>();
    animeAdapter u,i;
    animeSaver a;
    animeSaver as;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        as=new animeSaver(MainActivity.this);
        Bundle b=getIntent().getBundleExtra("data");
        if(b!=null&&!b.isEmpty()){
            usersort=b.getParcelableArrayList("users");
            itemsort=b.getParcelableArrayList("items");
            as.putAnime(usersort,itemsort);
            if(u!=null)
                u.notifyDataSetChanged();
            if(i!=null)
                i.notifyDataSetChanged();
        }
        else{
            Bundle b2=as.getAnime();
            if(b2.getBoolean("exists")){
                usersort=b2.getParcelableArrayList("users");
                itemsort=b2.getParcelableArrayList("items");
                if(u!=null)
                    u.notifyDataSetChanged();
                if(i!=null)
                    i.notifyDataSetChanged();
            }
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               startActivity(new Intent(MainActivity.this,actions.class));
            }
        });
        FloatingActionButton fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(MainActivity.this,search.class);
                i.putExtra("genre",(ArrayList)as.getGenreList());
                i.putExtra("user", (ArrayList) usersort);
                i.putExtra("item", (ArrayList) itemsort);
                startActivity(i);
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public static class AnimeFragment extends Fragment {
        animeAdapter aa;
        public AnimeFragment() throws Exception {
            throw new Exception();
        }


        public AnimeFragment(animeAdapter aa) {
            this.aa = aa;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
                ListView l=(ListView)rootView.findViewById(R.id.anime_list);
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
                        Intent i=new Intent(getContext(),show.class);
                        i.putExtra("anime",aa.getItem(position));
                        startActivity(i);
                    }
                });
            return rootView;
        }
    }
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if(position==0){
                u=new animeAdapter(MainActivity.this,usersort,anime.type.USER);
            return new AnimeFragment(u);}
            i=new animeAdapter(MainActivity.this,itemsort,anime.type.ITEM);
            return new AnimeFragment(i);

        }
        @Override
        public int getCount() {
            return 2;
        }
    }
}
