package com.example.polpincho.proyectofinal;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MediaPlayerServ extends Service {

    // Binder given to clients

    private final IBinder mBinder = new MediaPlayerBinder();
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private Boolean muted = false;
    private String songlist[];
    File Music;
    private int pos = 0;
    private boolean directory = false;

    public boolean play() {
        if (mediaPlayer.isPlaying()){
            mediaPlayer.pause();
            return false;
        }
        else{
            mediaPlayer.start();
            return true;
        }
    }

    public boolean SetVolume() {
        if (muted){
            mediaPlayer.setVolume(1, 1);
            muted = false;
            return false;
        }
        else{
            mediaPlayer.setVolume(0, 0);
            muted = true;
            return true;
        }
    }

    public String next(){
        if (directory) {
            ++pos;
            pos = pos % songlist.length;
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer = null;
            assigna();
            mediaPlayer.start();
            return songlist[pos];
        }
        else{
            Replay();
            return "demo";
        }
    }

    public String previous() {
        if (directory) {
            if (pos != 0) {
                --pos;
                pos = pos % songlist.length;
            } else pos = songlist.length - 1;
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer = null;
            assigna();
            mediaPlayer.start();
            return songlist[pos];
        }
        else{
            Replay();
            return "data0";
        }
    }

    public boolean Replay() {
        boolean playing = mediaPlayer.isPlaying();
        mediaPlayer.stop();

        mediaPlayer.reset();
        mediaPlayer = null;

        assigna();
        mediaPlayer.start();
        return playing;
    }

    public boolean Stop() {
        boolean playing = mediaPlayer.isPlaying();
        mediaPlayer.stop();
        mediaPlayer.reset();
        mediaPlayer = null;
        assigna();
        return playing;
    }

    public class MediaPlayerBinder extends Binder {
        MediaPlayerServ getService() {
            return MediaPlayerServ.this;
        }
    }


    public MediaPlayerServ() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = MediaPlayer.create(this, R.raw.data0);
        File SDcard = Environment.getExternalStorageDirectory();
        Music = new File(SDcard.getAbsolutePath()+"/Music");
        if (Music.exists()) directory = true;
        songlist = Music.list();
        assigna();



    }

   private void assigna(){
       if (directory) {
           mediaPlayer = new MediaPlayer();
           MediaPlayer.OnCompletionListener li = new MediaPlayer.OnCompletionListener() {
               @Override
               public void onCompletion(android.media.MediaPlayer mp) {
                   next();
                   //setSong();
               }
           };


           String aux = (Music.getAbsolutePath() + "/" + songlist[pos]);
           try {
               mediaPlayer.setDataSource(aux);
               mediaPlayer.prepare();
               mediaPlayer.setOnCompletionListener(li);
           } catch (IOException e) {
               e.printStackTrace();
           }
       }
       else mediaPlayer = MediaPlayer.create(this, R.raw.data0);
   }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
}
