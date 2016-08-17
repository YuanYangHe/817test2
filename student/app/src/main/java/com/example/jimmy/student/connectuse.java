package com.example.jimmy.student;

import android.app.Application;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class connectuse extends Application {
    private Socket soc;
    private BufferedReader brt;
    private BufferedWriter bwt;
     //String accountname,email;
    //String net="192.168.100.2";
    Bitmap b;
    public  void init()
    {
        try {
            this.soc=new Socket();
            SharedPreferences settings = getSharedPreferences("studentuse_pref", 0);
            SocketAddress addr=new InetSocketAddress(settings.getString("net","192.168.100.5"),599);
            soc.connect(addr,2000);
            brt=new BufferedReader(new InputStreamReader(soc.getInputStream(), "UTF-8"));
            bwt = new BufferedWriter(new OutputStreamWriter(soc.getOutputStream(), "UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public Socket getSocket() {
        return soc;
    }

    public BufferedWriter getwrite()
    {
        return bwt;
    }

    public BufferedReader getread()
    {
        return brt;
    }


}
