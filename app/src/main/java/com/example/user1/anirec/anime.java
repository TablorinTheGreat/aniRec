package com.example.user1.anirec;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class anime implements Parcelable {
    String name, id;
    int numRec;
    float dis, rate;
    Bitmap img;
    type t;
    List<String> voters_list;
    List<String> genre;

    public anime(String name, String id, int numRec, float dis, float rate, byte[] img, type t,List<String> v,List<String> g) {
        this.name = name;
        this.id = id;
        this.numRec = numRec;
        this.dis = dis;
        this.rate = rate;
        if(img!=null)
        this.img = BitmapFactory.decodeByteArray(img, 0, img.length);
        this.t = t;
        voters_list=v;
        genre=g;

    }

    public anime(String name, String id, int numRec, float dis, float rate, Bitmap img, type t,List<String> v,List<String> g) {
        this.name = name;
        this.id = id;
        this.numRec = numRec;
        this.dis = dis;
        this.rate = rate;
        this.img = img;
        this.t = t;
        voters_list=v;
        genre=g;

    }

    public anime(String name, String id, int numRec, float dis, float rate, type t,List<String> v,List<String> g) {
        this.name = name;
        this.id = id;
        this.numRec = numRec;
        this.dis = dis;
        this.rate = rate;
        this.t = t;
        voters_list=v;
        genre=g;
    }

    public String getGenre() {
        return genre.toString().substring(1,genre.toString().length()-1);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNumRec(int numRec) {
        this.numRec = numRec;
    }

    public void setDis(float dis) {
        this.dis = dis;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public void setImg(Bitmap img) {
        this.img = img;
    }

    public void setT(type t) {
        this.t = t;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(id);
        parcel.writeInt(numRec);
        parcel.writeFloat(dis);
        parcel.writeFloat(rate);
        Bundle b = new Bundle();
        b.putParcelable("image", img);
        parcel.writeBundle(b);
        parcel.writeString(t.name());
        parcel.writeList(voters_list);
        parcel.writeList(genre);
    }

    public static final Parcelable.Creator<anime> CREATOR
            = new Parcelable.Creator<anime>() {
        public anime createFromParcel(Parcel in) {
            return new anime(in);
        }

        @Override
        public anime[] newArray(int i) {
            return new anime[i];
        }
    };

    public anime(Parcel p) {
        name=p.readString();
        id=p.readString();
        numRec=p.readInt();
        dis=p.readFloat();
        rate=p.readFloat();
        img=p.readBundle().getParcelable("image");
        t=type.valueOf(p.readString());
        voters_list = new ArrayList<String>();
        genre = new ArrayList<String>();
        p.readList(voters_list,null);
        p.readList(genre,null);
    }

    enum type {
        USER,
        ITEM;
    }
}
