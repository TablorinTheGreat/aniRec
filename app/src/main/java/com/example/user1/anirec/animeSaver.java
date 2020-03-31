package com.example.user1.anirec;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class animeSaver extends SQLiteOpenHelper {
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE anime ( id TEXT PRIMARY KEY, name TEXT, numRec INTEGER, dis REAL, rate REAL,img BLOB,type TEXT)";
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS anime";
    private static final String SQL_CREATE_ENTRIES_voters =
            "CREATE TABLE voters ( id TEXT , name TEXT)";
    private static final String SQL_DELETE_ENTRIES_voters =
            "DROP TABLE IF EXISTS voters";
    private static final String SQL_CREATE_ENTRIES_genres =
            "CREATE TABLE genres ( id TEXT , name TEXT)";
    private static final String SQL_DELETE_ENTRIES_genres =
            "DROP TABLE IF EXISTS genres";

    public animeSaver(Context context) {
        super(context, "anime", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
        db.execSQL(SQL_CREATE_ENTRIES_voters);
        db.execSQL(SQL_CREATE_ENTRIES_genres);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
    }
    public Bundle getAnime(){
        SQLiteDatabase db=getReadableDatabase();
        Cursor c=db.rawQuery("SELECT * FROM anime",null);
        if(c==null||c.getCount()==0) {
            Bundle b=new Bundle();
            b.putBoolean("exists",false);
            return b;
        }
        ArrayList<anime> users=new ArrayList<>();
        ArrayList<anime> items=new ArrayList<>();
        while(c.moveToNext()) {
            anime a = new anime(c.getString(c.getColumnIndex("name")), c.getString(c.getColumnIndex("id")), c.getInt(c.getColumnIndex("numRec")), c.getFloat(c.getColumnIndex("dis")), c.getFloat(c.getColumnIndex("rate")), c.getBlob(c.getColumnIndex("img")), anime.type.valueOf(c.getString(c.getColumnIndex("type"))),getVoters(c.getString(c.getColumnIndex("id"))),getGenres(c.getString(c.getColumnIndex("id"))));
            if (a.t == anime.type.USER)
                users.add(a);
            else items.add(a);
        }
        Bundle b =new Bundle();
        b.putParcelableArrayList("users", users);
        b.putParcelableArrayList("items", items);
        b.putBoolean("exists",true);
        return b;
    }
    public void putAnime(List<anime> u,List<anime> t){
        u.addAll(t);
        SQLiteDatabase db=getWritableDatabase();
        db.execSQL(SQL_DELETE_ENTRIES);
        db.execSQL(SQL_CREATE_ENTRIES);
        db.execSQL(SQL_DELETE_ENTRIES_voters);
        db.execSQL(SQL_CREATE_ENTRIES_voters);
        db.execSQL(SQL_DELETE_ENTRIES_genres);
        db.execSQL(SQL_CREATE_ENTRIES_genres);
        for(int i=0;i<u.size();i++){
            ContentValues values = new ContentValues();
            values.put("name",u.get(i).name);
            values.put("id", u.get(i).id);
            values.put("numRec",u.get(i).numRec);
            values.put("dis", u.get(i).dis);
            values.put("rate",u.get(i).rate);
            if(u.get(i).img!=null){
            ByteBuffer dst= ByteBuffer.allocate(u.get(i).img.getByteCount());
            u.get(i).img.copyPixelsToBuffer( dst);
            values.put("img",dst.array());}
            values.put("type",u.get(i).t.name());
            if(u.get(i).t.equals(anime.type.ITEM)){
            putVoters(u.get(i).voters_list,u.get(i).id);
            putGenres(u.get(i).genre,u.get(i).id);}
            db.insert("anime", null, values);
        }
    }
    public void deleteAnime(String id){
        SQLiteDatabase db=getWritableDatabase();
        db.delete("anime", "id = "+id, null);
        db.delete("voters", "id = "+id, null);
        db.delete("genres", "id = "+id, null);
    }
    public void putVoters(List<String> v,String id){
        SQLiteDatabase db=getWritableDatabase();
        for(int i=0;i<v.size();i++) {
            ContentValues values = new ContentValues();
            values.put("name",v.get(i));
            values.put("id",id);
            db.insert("voters", null, values);
        }
    }
    public List<String> getVoters(String id){
        SQLiteDatabase db=getReadableDatabase();
        Cursor c=db.rawQuery("SELECT name FROM voters WHERE id="+id,null);
        List<String> v=new ArrayList<>();
        while(c.moveToNext()) {
            v.add(c.getString(c.getColumnIndex("name")));
        }
        return v;
    }
    public void putGenres(List<String> g,String id){
        SQLiteDatabase db=getWritableDatabase();
        for(int i=0;i<g.size();i++) {
            ContentValues values = new ContentValues();
            values.put("name",g.get(i));
            values.put("id",id);
            db.insert("genres", null, values);
        }
    }
    public List<String> getGenres(String id){
        SQLiteDatabase db=getReadableDatabase();
        Cursor c=db.rawQuery("SELECT name FROM genres WHERE id="+id,null);
        List<String> g=new ArrayList<>();
        while(c.moveToNext()) {
            g.add(c.getString(c.getColumnIndex("name")));
        }
        return g;
    }
    public List<String> getGenreList(){
        SQLiteDatabase db=getReadableDatabase();
        Cursor c=db.rawQuery("SELECT DISTINCT name FROM genres",null);
        List<String> g=new ArrayList<>();
        while(c.moveToNext()) {
            g.add(c.getString(c.getColumnIndex("name")));
        }
        return g;
    }
}
