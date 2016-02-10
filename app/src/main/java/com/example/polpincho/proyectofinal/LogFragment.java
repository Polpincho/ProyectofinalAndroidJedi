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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.polpincho.proyectofinal.persistencia.BDUser;

public class LogFragment extends Fragment implements View.OnClickListener{
    TextView usernamed;
    EditText pass;
    Button into;
    Button back;
    CheckBox keep;

    private OnFragmentInteractionListener mListener;

    public LogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_log, container, false);

        usernamed = (TextView) rootView.findViewById(R.id.usernamelog);
        pass = (EditText) rootView.findViewById(R.id.password);
        into = (Button) rootView.findViewById(R.id.enter);
        into.setOnClickListener(this);
        back = (Button) rootView.findViewById(R.id.backlog);
        back.setOnClickListener(this);
        keep = (CheckBox) rootView.findViewById(R.id.session);

        Bundle args = this.getArguments();
        if (args != null){
            usernamed.setText(args.getString("message"));
        }else{
            usernamed.setText("Guest");
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.enter:
                BDUser bduser = new BDUser(getContext());
                ContentValues valuesToStore = new ContentValues();
                String password = pass.getText().toString();
                String username = usernamed.getText().toString();
                Cursor cursor = bduser.getPassByUsername(username);
                if (cursor.moveToFirst()) {
                    String s = cursor.getString(cursor.getColumnIndex("pass"));
                    if (s.equals(password)) {
                        cursor.close();
                        SharedPreferences settings = getActivity().getSharedPreferences("MEMORYAPK", 0);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString("UserActivo", username);
                        editor.putBoolean("SessionActiva", true);
                        editor.apply();

                        if (keep.isChecked()) {
                            BDHelper bdhelp = new BDHelper();
                            bdhelp.execute(1);
                        } else {
                            BDHelper bdhelp = new BDHelper();
                            bdhelp.execute(0);
                        }

                        Toast.makeText(getContext(), "Loggin done", Toast.LENGTH_SHORT).show();
                        BDUser bdUser = new BDUser(getContext());
                        Cursor c = bdUser.getfirstLog(username);
                        if (c.moveToFirst()) {
                            boolean aux = c.getInt(c.getColumnIndex("firstlog")) > 0;
                            Log.e("first", aux+"");
                            if (aux) {
                                Intent intent = new Intent(getContext(), Perfil.class);
                                startActivity(intent);
                                if (mListener != null) {
                                    mListener.onFragmentInteraction("", 4);
                                } else {
                                }
                            }
                            else{
                                Intent intent = new Intent(getContext(), InMenu.class);
                                startActivity(intent);
                                if (mListener != null) {
                                    mListener.onFragmentInteraction("", 4);
                                } else {
                                }
                            }
                        }
                    } else {
                        if (mListener != null) {
                            mListener.onFragmentInteraction("", 666);
                        } else {
                        }
                    }
                }

                pass.setText("");
                break;
            case R.id.backlog:
                if (mListener != null) {
                    //En este caso, si no hay nada escrito en el EditText,
                    //pondremos el String vació ""
                    mListener.onFragmentInteraction(usernamed.getText().toString(),3);
                }else {
                    Log.v("Fragment1", "No listener attached");
                }
                break;
            default:
                Log.v("Fragment2","Not a button");
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
            Log.e("Puto update", ""+aux);
            return null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }

}
