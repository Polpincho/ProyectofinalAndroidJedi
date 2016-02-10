package com.example.polpincho.proyectofinal;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Login extends AppCompatActivity implements OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Creamos el primer fragment, y no le pasamos argumentos!
        setTitle("Fragment 1");
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =
                fragmentManager.beginTransaction();
        //Reemplazamos el Frame Layout de la Activity por el nuevo fragment.
        //El Frame Layout es el contenedor
        fragmentTransaction.replace(R.id.frameLayout, new IniFragment());
        fragmentTransaction.commit();
    }


    @Override
    public void onFragmentInteraction(String text, Integer from) {

        Fragment f = null;

        if (from == 2 ) {
            //from 4: sign up
            f = new SignFragment();
            setTitle("Sign Up");
        }
        else {
            if (from == 1) {
                //from 1: log in
                f = new LogFragment();
                setTitle("Log In");
            } else {
                if (from == 3) {
                    //from 3: back
                    f = new IniFragment();
                }else {
                    if (from == 4) {
                        //from 4: Logged in
                        finish();
                    }
                    if (from == 666) {
                        //from 4: Error on login
                        f = new BadLogFragment();
                    }
                }
            }
        }
        if (from != 4){
            //Creamos un bundle con el text recibido del fragment
            Bundle b = new Bundle();
            b.putString("message", text);
            //Añadimos el Bundle al nuevo fragment
            f.setArguments(b);

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction =
                    fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frameLayout, f);
            fragmentTransaction.commit();
        }

    }
}
