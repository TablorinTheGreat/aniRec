package com.example.user1.anirec;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class actions extends AppCompatActivity {
anime_requests r;
    MAL m;
    ipDialog i;
    Button top50,anime;
private  static  boolean tryed2connect,tryed2auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actions);
        top50=(Button)findViewById(R.id.top50);
        anime=(Button)findViewById(R.id.anime_list);
        tryed2auth=false;
        tryed2connect=false;
        r=new anime_requests("connection",new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(!msg.getData().getString("error","").equals(""))
                    error(msg);
                else{
                    String s=msg.getData().getString("msg","");
                    if(!s.equals("")) {
                        if (s.equals("connected"))
                            connected();
                        else if (s.equals("authenticated"))
                            authenticated();
                        else Toast.makeText(actions.this,s,Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Intent i=new Intent(actions.this,MainActivity.class);
                        i.putExtra("data",msg.getData());
                        startActivity(i);
                        }
                }
            }
        });
        r.start();
        m=new MAL();
        i=new ipDialog();
        i.show(getFragmentManager(),"ipD");
        top50.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message m= r.h.obtainMessage();
                m.getData().putString("reqCode",anime_requests.request.TOP50.name());
                r.h.sendMessage(m);
            }
        });
        anime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message m= r.h.obtainMessage();
                m.getData().putString("reqCode",anime_requests.request.ANIME_RECOMENDATION.name());
                r.h.sendMessage(m);
            }
        });
    }
    public void try2connect(String ip){
        Message m= r.h.obtainMessage();
        m.getData().putString("ip",ip);
        r.h.sendMessage(m);
    }
    public void quit()
    {
       Message m= r.h.obtainMessage();
       m.getData().putString("quit","quit");
       r.h.sendMessage(m);
    }
    public void try2authenticate(String name,String pass){
            Message mg = r.h.obtainMessage();
            Bundle b = mg.getData();
            b.putString("name", name);
            b.putString("pass", pass);
            r.h.sendMessage(mg);
    }
    public void connected(){
        i.dismiss();
        m.show(getFragmentManager(),"mal");
    }
    public void authenticated(){
        m.dismiss();
        top50.setClickable(true);
        anime.setClickable(true);
    }
    public void error(Message msg){
        Toast.makeText(actions.this,msg.getData().getString("error"),Toast.LENGTH_SHORT).show();
        if(msg.getData().getString("cause","").equals("connection"))
        {
            tryed2connect=false;
            return;
        }
        else if(msg.getData().getString("cause","").equals("auth")){
            tryed2auth=false;
        }

    }
    public static class ipDialog extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());//creating dialog
            setCancelable(false);
           final View v = getActivity().getLayoutInflater().inflate(R.layout.ip_dialog, null);
            ((Button) v.findViewById(R.id.connect)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!tryed2connect) {
                        tryed2connect = true;
                        ((actions) getActivity()).try2connect(((EditText)v.findViewById(R.id.ip)).getText().toString());
                    }

                }
            });
            ((Button) v.findViewById(R.id.dismiss)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((actions) getActivity()).quit();
                    dismiss();
                    getActivity().finish();
                }
            });
            builder.setView(v);
            return builder.create();
        }
    }
    public static class MAL extends DialogFragment{
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());//creating dialog
            setCancelable(false);
            final View v = getActivity().getLayoutInflater().inflate(R.layout.mal_dailog, null);
            ((Button)v.findViewById(R.id.auth)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!tryed2auth) {
                        tryed2auth=true;
                        ((actions) getActivity()).try2authenticate(((EditText)v.findViewById(R.id.name)).getText().toString(), ((EditText)v.findViewById(R.id.pass)).getText().toString());
                    }
                }
            });
            ((Button)v.findViewById(R.id.dismiss)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((actions)getActivity()).quit();
                    dismiss();
                    getActivity().finish();
                }
            });
            builder.setView(v);
            return builder.create();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        quit();
    }
}
