package com.example.polpincho.proyectofinal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import android.os.Bundle;

import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.polpincho.proyectofinal.persistencia.BDUser;

public class InMenu extends AppCompatActivity implements View.OnClickListener {

    private ImageButton clc;
    private ImageButton mpl;
    private ImageButton pro;
    private ImageButton rnk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_menu);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Main menu");
        //Creamos el primer fragment, y no le pasamos argumentos!
        clc = (ImageButton) findViewById(R.id.clc);
        rnk = (ImageButton) findViewById(R.id.rnk);
        mpl = (ImageButton) findViewById(R.id.med);
        pro = (ImageButton) findViewById(R.id.prof);

        clc.setOnClickListener(this);
        rnk.setOnClickListener(this);
        mpl.setOnClickListener(this);
        pro.setOnClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_in_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.MenuLogOut:
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


    @Override
    public void onClick(View v) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =
                fragmentManager.beginTransaction();
        //Reemplazamos el Frame Layout de la Activity por el nuevo fragment.
        //El Frame Layout es el contenedor

        switch (v.getId()) {
            case R.id.clc:
                Intent i = new Intent(getApplicationContext(), Calculator.class);
                startActivity(i);
                break;
            case R.id.rnk:
                i = new Intent(getApplicationContext(), PagerHolder.class);
                startActivity(i);
                break;
            case R.id.med:
                i = new Intent(getApplicationContext(), Reproductor.class);
                startActivity(i);
                break;
            case R.id.prof:

                i = new Intent(getApplicationContext(), Perfil.class);
                startActivity(i);

                break;
        }
    }
}


