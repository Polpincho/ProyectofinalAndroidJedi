package com.example.polpincho.proyectofinal;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.polpincho.proyectofinal.persistencia.BDUser;


public class IniFragment extends Fragment implements View.OnClickListener{
    private OnFragmentInteractionListener mListener;
    EditText userna;
    Button log;
    Button sign;
    String use;

    public IniFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_ini, container, false);

        userna = (EditText) rootView.findViewById(R.id.usernameini);
        log = (Button) rootView.findViewById(R.id.log);
        log.setOnClickListener(this);
        sign = (Button) rootView.findViewById(R.id.sign);
        sign.setOnClickListener(this);

        //Instanciamos el SharedPreferences
        SharedPreferences settings = getActivity().getSharedPreferences("MEMORYAPK", Context.MODE_PRIVATE);
        //Consultamos
        use = settings.getString("UserActivo", "");
        Boolean active = settings.getBoolean("SessionActiva", false);
        if (active){
            Intent i = new Intent(getContext(),InMenu.class);
            startActivity(i);
            if (mListener != null) {
                mListener.onFragmentInteraction("", 4);
            } else {
            }
        }
        else {
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("UserActivo", "");
            editor.apply();
            userna.setText(use);


        }
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
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    //Importante! Aquí no se puede usar el método del "onClick" en xml
    // por que referencia a la Activity y no al fragment!!
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sign:
                String use = userna.getText().toString();
                if (use.length() >0) {
                    BDUser bdUser = new BDUser(getContext());
                    Cursor c = bdUser.getUser(use);
                    if (!c.moveToFirst()) {
                        if (mListener != null) {
                            mListener.onFragmentInteraction(userna.getText().toString(), 2);
                        } else {
                        }
                    } else {
                        Toast.makeText(getContext(), "User already exists", Toast.LENGTH_SHORT).show();
                    }
                    c.close();
                    bdUser.close();
                }
                else Toast.makeText(getContext(), "Introduce your username", Toast.LENGTH_SHORT).show();
                break;
            case R.id.log:
                use = userna.getText().toString();
                BDUser bdUser = new BDUser(getContext());
                Cursor c = bdUser.getKeepLog(use);
                if (c.moveToFirst()) {
                    boolean aux = c.getInt(c.getColumnIndex("keep")) > 0;
                    Log.e("keep", aux+"");
                    if (aux){
                        SharedPreferences settings = getActivity().getSharedPreferences("MEMORYAPK", 0);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString("UserActivo", use);
                        editor.putBoolean("SessionActiva", true);
                        editor.apply();
                        Intent i = new Intent(getContext(),InMenu.class);
                        startActivity(i);
                        if (mListener != null) {
                            mListener.onFragmentInteraction("", 4);
                        } else {
                        }
                    }
                    else{
                        if (mListener != null) {
                            mListener.onFragmentInteraction(userna.getText().toString(), 1);
                        } else {
                        }
                    }
                }
                else {
                    Toast.makeText(getContext(), "User don't exists", Toast.LENGTH_SHORT).show();
                }
                c.close();
                bdUser.close();
                break;
            default:
                Log.v("Fragment1","Not a button");
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
            boolean kip = false;
            if (params[0]==1)kip= true;
            boolean aux = bdUser.updatekeeplogged(kip,User);
            bdUser.close();
            return null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }

}