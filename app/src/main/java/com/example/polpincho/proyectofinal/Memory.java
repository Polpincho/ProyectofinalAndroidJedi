package com.example.polpincho.proyectofinal;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.polpincho.proyectofinal.persistencia.BDUser;

import java.util.Arrays;
import java.util.Random;


public class Memory extends Fragment implements View.OnClickListener {


    private int[] value;
    private ImageButton[] button;
    private TextView punt;
    private CardView[] card;
    private boolean[] filled;
    private int i;
    private boolean first = false;
    private boolean second = false;
    private int pri = -1;
    private int seg = -1;
    private int clicks = 0;
    private int tryes = 0;

    View rootView;
    private OnFragmentInteractionListener mListener;

    public Memory() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_memory, container, false);


        button = new ImageButton[16];
        card = new CardView[16];

        setHasOptionsMenu(true);

        punt = (TextView) rootView.findViewById(R.id.punt);
        button[0] = (ImageButton) rootView.findViewById(R.id.button1);
        button[1] = (ImageButton) rootView.findViewById(R.id.button2);
        button[2] = (ImageButton) rootView.findViewById(R.id.button3);
        button[3] = (ImageButton) rootView.findViewById(R.id.button4);
        button[4] = (ImageButton) rootView.findViewById(R.id.button5);
        button[5] = (ImageButton) rootView.findViewById(R.id.button6);
        button[6] = (ImageButton) rootView.findViewById(R.id.button7);
        button[7] = (ImageButton) rootView.findViewById(R.id.button8);
        button[8] = (ImageButton) rootView.findViewById(R.id.button9);
        button[9] = (ImageButton) rootView.findViewById(R.id.button10);
        button[10] = (ImageButton) rootView.findViewById(R.id.button11);
        button[11] = (ImageButton) rootView.findViewById(R.id.button12);
        button[12] = (ImageButton) rootView.findViewById(R.id.button13);
        button[13] = (ImageButton) rootView.findViewById(R.id.button14);
        button[14] = (ImageButton) rootView.findViewById(R.id.button15);
        button[15] = (ImageButton) rootView.findViewById(R.id.button16);

        card[0] = (CardView) rootView.findViewById(R.id.card_view1);
        card[1]= (CardView) rootView.findViewById(R.id.card_view2);
        card[2]= (CardView) rootView.findViewById(R.id.card_view3);
        card[3]= (CardView) rootView.findViewById(R.id.card_view4);
        card[4]= (CardView) rootView.findViewById(R.id.card_view5);
        card[5]= (CardView) rootView.findViewById(R.id.card_view6);
        card[6]= (CardView) rootView.findViewById(R.id.card_view7);
        card[7]= (CardView) rootView.findViewById(R.id.card_view8);
        card[8]= (CardView) rootView.findViewById(R.id.card_view9);
        card[9] = (CardView) rootView.findViewById(R.id.card_view10);
        card[10] = (CardView) rootView.findViewById(R.id.card_view11);
        card[11] = (CardView) rootView.findViewById(R.id.card_view12);
        card[12] = (CardView) rootView.findViewById(R.id.card_view13);
        card[13] = (CardView) rootView.findViewById(R.id.card_view14);
        card[14] = (CardView) rootView.findViewById(R.id.card_view15);
        card[15] = (CardView) rootView.findViewById(R.id.card_view16);

        if (mListener != null) {
            mListener.onFragmentInteraction(getTag(),1);
        }else {
            Log.v("Memory", "No listener attached");
        }

        for (int i =0; i<16; ++i){
            button[i].setOnClickListener(this);
        }

        initialize();
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_pager_holder, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.HolderRestart:
                initialize();
                return true;
            case R.id.HolderLogOut:
                SharedPreferences settings = getContext().getSharedPreferences("MEMORYAPK", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("SessionActiva", false);
                editor.apply();
                Intent i = new Intent(getContext(), Login.class);
                startActivity(i);
                if (mListener != null) {
                    mListener.onFragmentInteraction("end",4);
                }else {
                    Log.v("Fragment1", "No listener attached");
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void initialize(){

        for (int i =0; i<16; ++i){
            button[i].setBackgroundResource(R.drawable.poke);
            button[i].setBackground(getResources().getDrawable(R.drawable.poke));
        }
        first = false;
        second = false;
        pri = -1;
        seg = -1;
        clicks = 0;
        tryes = 0;
        value = new int[16];
        filled = new boolean[16];
        Arrays.fill(filled, Boolean.FALSE);
        Arrays.fill(value, -1);

        Random r = new Random();
        int k = 16;
        while (k > 0) {
            int rand = r.nextInt(16);

            if (!filled[rand]){
                value[rand] = k;
                filled[rand] = true;
                --k;
            }
        }
        tryes = 0;
        punt.setText(tryes +" intentos");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onClick(View v) {
        if (clicks < 2) {
            switch (v.getId()) {
                case R.id.button1:
                    i = 0;
                    break;
                case R.id.button2:
                    i = 1;
                    break;
                case R.id.button3:
                    i = 2;
                    break;
                case R.id.button4:
                    i = 3;
                    break;
                case R.id.button5:
                    i = 4;
                    break;
                case R.id.button6:
                    i = 5;
                    break;
                case R.id.button7:
                    i = 6;
                    break;
                case R.id.button8:
                    i = 7;
                    break;
                case R.id.button9:
                    i = 8;
                    break;
                case R.id.button10:
                    i = 9;
                    break;
                case R.id.button11:
                    i = 10;
                    break;
                case R.id.button12:
                    i = 11;
                    break;
                case R.id.button13:
                    i = 12;
                    break;
                case R.id.button14:
                    i = 13;
                    break;
                case R.id.button15:
                    i = 14;
                    break;
                case R.id.button16:
                    i = 15;
                    break;
            }
            int aux = i;
            turn(v, aux);

            punt.setText(tryes +" intentos");
        }

    }

    public void setimagen(int i) {

        switch (value[i] % 8) {
            case 0:
                button[i].setBackgroundResource(R.drawable.sylv);
                break;
            case 1:
                button[i].setBackgroundResource(R.drawable.jolt);
                break;
            case 2:
                button[i].setBackgroundResource(R.drawable.vapo);
                break;
            case 3:
                button[i].setBackgroundResource(R.drawable.flar);
                break;
            case 4:
                button[i].setBackgroundResource(R.drawable.leaf);
                break;
            case 5:
                button[i].setBackgroundResource(R.drawable.umb);
                break;
            case 6:
                button[i].setBackgroundResource(R.drawable.glac);
                break;
            case 7:
                button[i].setBackgroundResource(R.drawable.espe);
                break;
        }
    }


    private void turn(View v, int j) {
        if (filled[j]) {
            MyTask task = new MyTask();
            if (first == false){
                ++tryes;
                pri = j;
                first = true;
                task.execute(j);
                ++clicks;
            }
            else {
                if (pri != j) {
                    ++clicks;
                    ++tryes;
                    seg = j;
                    second = true;
                    task.execute(j);

                }
            }
            task.onCancelled();
        }
        else finalized();
    }


    private class MyTask extends AsyncTask<Integer, Integer, String> {

        @Override
        protected void onPreExecute() {
            //TODO: Cambia imagen
            setimagen(i);
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            //TODO: Restaurar
            if (second) {
                if (value[pri] % 8 == value[seg] % 8) {
                    filled[pri] = false;
                    filled[seg] = false;
                } else {
                    button[pri].setBackgroundResource(R.drawable.poke);
                    button[seg].setBackgroundResource(R.drawable.poke);
                }
                pri = -1;
                seg = -1;
                first = false;
                second= false;
                clicks-=2;
            }
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(Integer... params) {
            //TODO: Pausa
            if (first) {
                while (!second);
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }

    private void finalized() {
        boolean end = false;
        for(int k = 0; k< filled.length; ++k) end = end | filled[k];
        end = !end;
        if (end){
            BDHelper bdhelp = new BDHelper();
            bdhelp.execute(tryes);
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Partida finalizada");
            builder.setMessage("Enhorabuena, tu puntuacion es " + tryes +".");
            builder.setPositiveButton("Ok",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (mListener != null) {
                                mListener.onFragmentInteraction("end",4);
                            }else {
                                Log.v("Fragment1", "No listener attached");
                            }
                        }
                    });
            builder.setNegativeButton("Replay",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            initialize();
                            mListener.onFragmentInteraction(getTag(), 5);
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    private class BDHelper extends AsyncTask<Integer, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {

            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(Integer... params) {
            //Instanciamos el SharedPreferences
            SharedPreferences settings = getContext().getSharedPreferences("MEMORYAPK", Context.MODE_PRIVATE);
            //Consultamos
            String User = settings.getString("UserActivo", "Guest");
            BDUser bdUser = new BDUser(getContext());

            Cursor c= bdUser.getPointsByUsername(User);
            if (c.moveToFirst()){
                int bestpunt = c.getInt(c.getColumnIndex("bstpunt"));
                if (bestpunt == 0 || bestpunt> params[0]) {
                    ContentValues values = new ContentValues();
                    values.put("bstpunt", params[0]);
                    boolean aux = bdUser.updatePoints(values, User);
                    Log.e("Puto update", "" + aux);

                }
            }
            c.close();
            bdUser.close();
            return null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }


}
