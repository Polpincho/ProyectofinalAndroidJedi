package com.example.polpincho.proyectofinal;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.support.design.widget.NavigationView;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class Reproductor extends AppCompatActivity{

    private Button play;
    private Button stop;
    private Button next;
    private Button ante;
    private Button repla;
    private Button mute;
    private Toolbar toolbar;
    private TextView titulo;

    MediaPlayerServ mService;
    boolean bound;

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            MediaPlayerServ.MediaPlayerBinder binder = (MediaPlayerServ.MediaPlayerBinder) service;

            mService = binder.getService();

            bound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            bound = false;

        }
    };





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reproductor);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Media Player");
        /*

        File sdCard = Environment.getExternalStorageDirectory();
        File song = new File(sdCard.getAbsolutePath() + "/media/music/data-0.mp3");

        Log.d ("path",song.getAbsolutePath()+"");
        Log.d ("path",song.exists()+"");
        mediaPlayer = MediaPlayer.create(this, R.raw.data0);
        */
        File SDcard = Environment.getExternalStorageDirectory();
        File Music = new File(SDcard.getAbsolutePath()+"/Music");
        String[] songlist = Music.list();

        titulo = (TextView) findViewById(R.id.titlesong);
        play = (Button) findViewById(R.id.playea);
        stop = (Button) findViewById(R.id.stopea);
        repla = (Button) findViewById(R.id.replay);
        ante = (Button) findViewById(R.id.ante);
        next = (Button) findViewById(R.id.nexta);
        mute = (Button) findViewById(R.id.mutea);


        if (Music.exists()) titulo.setText(songlist[0]);
        else titulo.setText("demo");
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean playing = false;
                playing = mService.play();
                if (!playing){
                    play.setBackground(v.getResources().getDrawable(R.drawable.ic_play_arrow_white_48dp));
                } else{
                    play.setBackground(v.getResources().getDrawable(R.drawable.ic_pause_white_48dp));
                }
            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mService.Stop();
                play.setBackground(v.getResources().getDrawable(R.drawable.ic_play_arrow_white_48dp));
            }
        });
        repla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mService.Replay();
                play.setBackground(v.getResources().getDrawable(R.drawable.ic_pause_white_48dp));
            }
        });
        ante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String aux = mService.previous();
                titulo.setText(aux);
                play.setBackground(v.getResources().getDrawable(R.drawable.ic_pause_white_48dp));
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String aux = mService.next();
                titulo.setText(aux);
                play.setBackground(v.getResources().getDrawable(R.drawable.ic_pause_white_48dp));
            }
        });

        mute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean muted = false;
                muted = mService.SetVolume();
                if (!muted){
                    mute.setBackground(v.getResources().getDrawable(R.drawable.ic_volume_mute_white_48dp));
                }
                else{
                    mute.setBackground(v.getResources().getDrawable(R.drawable.ic_volume_up_white_48dp));
                }
            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = new Intent(Reproductor.this, MediaPlayerServ.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (bound) {
            unbindService(mConnection);
            bound = false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_reproductor, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.MediaLogOut:
                SharedPreferences settings = getApplicationContext().getSharedPreferences("MEMORYAPK", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("SessionActiva", false);
                editor.apply();
                Intent i = new Intent(getApplicationContext(), Login.class);
                startActivity(i);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
