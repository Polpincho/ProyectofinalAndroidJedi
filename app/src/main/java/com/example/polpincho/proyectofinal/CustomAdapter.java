package com.example.polpincho.proyectofinal;

import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.polpincho.proyectofinal.persistencia.BDUser;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;

/**
 * Created by polpincho on 01/02/2016.
 */
public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.AdapterViewHolder>{


    ArrayList<User> users;
    private BDUser bduser;

    CustomAdapter(ArrayList<User> data){
            this.users = data;
    }


    @Override
    public CustomAdapter.AdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        //Instancia un layout XML en la correspondiente vista.
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        //Inflamos en la vista el layout para cada elemento
        View view = inflater.inflate(R.layout.rank, viewGroup, false);
        return new AdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomAdapter.AdapterViewHolder adapterViewholder, int position) {
        //int iconLayout = users.get(position).getPuntuation();
        Log.v("haha","user name: " + users.get(position).getUsername());
        Log.v("haha", "user mail: " + users.get(position).getMail());
        Log.v("haha", "user mail: " + users.get(position).getPuntuation());
        String aux = users.get(position).getUri();
        if (aux.equals("NULL")){
            adapterViewholder.icon.setImageDrawable(adapterViewholder.v.getResources().getDrawable(R.drawable.default_user));
        }
        else {
            adapterViewholder.icon.setImageURI(Uri.parse(aux));
        }
        adapterViewholder.username.setText(users.get(position).getUsername());
        adapterViewholder.mail.setText(users.get(position).getMail());
        adapterViewholder.punt.setText(users.get(position).getPuntuation() + "");

    }

    @Override
    public int getItemCount() {
        //Debemos retornar el tamaño de todos los elementos contenidos en el viewholder
        //Por defecto es return 0 --> No se mostrará nada.
        return users.size();
    }



    //Definimos una clase viewholder que funciona como adapter para
    public class AdapterViewHolder extends RecyclerView.ViewHolder {
    /*
    *  Mantener una referencia a los elementos de nuestro ListView mientras el usuario realiza
    *  scrolling en nuestra aplicación. Así que cada vez que obtenemos la vista de un item,
    *  evitamos las frecuentes llamadas a findViewById, la cuál se realizaría únicamente la primera vez y el resto
    *  llamaríamos a la referencia en el ViewHolder, ahorrándonos procesamiento.
    */

        public ImageView icon;
        public TextView username;
        public TextView mail;
        public TextView punt;
        public View v;
        public AdapterViewHolder(View itemView) {
            super(itemView);
            this.v = itemView;
            this.icon = (ImageView) itemView.findViewById(R.id.iconrank);
            this.username = (TextView) itemView.findViewById(R.id.username);
            this.mail = (TextView) itemView.findViewById(R.id.mail);
            this.punt = (TextView) itemView.findViewById(R.id.punt);
        }
    }
}