package com.example.polpincho.proyectofinal;

import android.content.ContentValues;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.polpincho.proyectofinal.persistencia.BDUser;

public class Perfil extends AppCompatActivity implements OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_perfil);
        setSupportActionBar(toolbar);

        boolean aux = false;
        Intent i = getIntent();
        if (i.hasExtra("Username")){
            String user = i.getStringExtra("Username");
            ContentValues cv = new ContentValues();
            cv.put("firstlog",false);
            BDUser bdUser = new BDUser(this);
            bdUser.FirstLog(user);
            aux = true;
            bdUser.close();
        }
        //Creamos el primer fragment, y no le pasamos argumentos!
        setTitle("My Profile");
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =
                fragmentManager.beginTransaction();
        //Reemplazamos el Frame Layout de la Activity por el nuevo fragment.
        //El Frame Layout s el contenedor
        if (aux) {
            fragmentTransaction.replace(R.id.frameperfil, new EditPerfil());
        }
        else {
            fragmentTransaction.replace(R.id.frameperfil, new ViewPerfil());
        }
        fragmentTransaction.commit();
    }


    @Override
    public void onFragmentInteraction(String text, Integer from) {

        Fragment f = null;

        if (from == 1 ) {
            f = new ViewPerfil();
            //Podemos hacer directamente setTitle por que nuestra activity
            //es AppCompatActivity
        }
        else {
            if(from == 2) {
                f = new EditPerfil();
            }
            else{
                if (from == 4 && text.equals("end")) {
                    Intent i = new Intent(getApplicationContext(), Login.class);
                    startActivity(i);
                    finish();
                }
                else{
                    if (from == 5 && text.equals("end")){
                        Intent i = new Intent(getApplicationContext(), Login.class);
                        startActivity(i);
                        finish();
                    }
                }
            }
        }

        if (!(from == 4 || from == 5)) {

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction =
                    fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frameperfil, f);
            fragmentTransaction.commit();
        }

    }
}
