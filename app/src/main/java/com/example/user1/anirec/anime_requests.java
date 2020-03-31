package com.example.user1.anirec;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.widget.Switch;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class anime_requests extends HandlerThread {
    public Handler h;
    Handler mh;
    BufferedReader in;
    Socket s;
    String ip;
    boolean authenticated = false, connected = false;
    DataOutputStream out;
    public anime_requests(String name, Handler mh) {
        super(name);
        this.mh = mh;
    }

    @Override
    public void run() {
        super.run();
    }
    public void connect(String ip){
        try {
             s = new Socket(ip, 777);
            in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            out = new DataOutputStream(s.getOutputStream());
            connected = true;
            sendToActivty("connected",false,null);
        } catch (IOException e) {
            sendToActivty(e.toString(), true,"connection");
        }
    }
    public void sendToActivty(String s, boolean error,String cause) {
        Message msg = mh.obtainMessage();
        Bundle data = msg.getData();
        String m = "msg";
        if (error){
            m = "error";
            data.putString("cause",cause);
        }
        data.putString(m, s);
        mh.sendMessage(msg);
    }

    @Override
    protected void onLooperPrepared() {
        super.onLooperPrepared();
        h = new Handler(getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                if (msg.getData().getString("quit","").equals("quit")) {
                    try {
                        out.writeUTF(",2");
                        out.close();
                        in.close();
                        s.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    catch (NullPointerException e){ e.printStackTrace();}
                    quitSafely();
                    return;
                }
                String ip = msg.getData().getString("ip", "");
                if (!ip.equals("")) {
                    connect(ip);
                    return;
                }
                String name = msg.getData().getString("name", "");
                if (name!=null&&!name.equals("")) {
                    String pass = msg.getData().getString("pass");
                    authenticate(name, pass);
                    return;
                }
                String reqCode = msg.getData().getString("reqCode");
                request(request.valueOf(reqCode));

            }
        };
    }

    public void authenticate(String name, String pass) {
        if (out != null) {
            try {
                out.writeUTF(","+name + "," + pass);
                String anime = in.readLine();
                if(anime.equals("error"))
                    throw new Exception();
                sendToActivty("authenticated",false,null);
                authenticated = true;
            } catch (IOException e) {
                sendToActivty(e.toString(), true,"auth");
            } catch (Exception e) {
                sendToActivty(e.toString()+"couldnt authenticate", true,"auth");
            }
        }
    }

    public void request(request r) {
        String anime = "";
        if (authenticated) {
            if (out != null) {
                try {
                    out.writeUTF(String.valueOf(","+r.requestCode));
                    switch (r.requestCode){
                        case 0:
                            sendToActivty(in.readLine(),false,"");
                            break;
                        case 1:
                            boolean user=true;
                            ArrayList<anime> users=new ArrayList<>();
                            ArrayList<anime> items=new ArrayList<>();
                            while(!(anime=in.readLine()).equals("0")){
                                if(anime.equals("%%")) {
                                    user = false;
                                    continue;
                                }
                                if(user)
                                    users.add(str2anime(anime, com.example.user1.anirec.anime.type.USER));
                                else
                                    items.add(str2anime(anime, com.example.user1.anirec.anime.type.ITEM));

                            }
                            Message msg = mh.obtainMessage();
                            Bundle data = msg.getData();
                            data.putParcelableArrayList("users", users);
                            data.putParcelableArrayList("items", items);
                            mh.sendMessage(msg);
                            break;
                    }
                } catch (IOException e) {
                    anime = e.toString();
                    sendToActivty(anime, true,"request");
                    return;
                }
            }
        }
    }
public anime str2anime(String s,anime.type t){
    String[] entries =s.split(" ,");
    List<String> v=new ArrayList<>();
    for(int i=6;i<entries.length;i++)
        v.add(entries[i]);
    return new anime(entries[1],entries[0],Integer.valueOf(entries[4]),Float.valueOf(entries[3]),Float.valueOf(entries[2]),t,v,Arrays.asList(entries[5].split(", ")));
}

    enum request {
        TOP50(0),
        ANIME_RECOMENDATION(1);
        int requestCode;

        request(int requestCode) {
            this.requestCode = requestCode;
        }
    }
}
