package com.example.polpincho.proyectofinal;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.polpincho.proyectofinal.persistencia.BDUser;

import java.util.ArrayList;

public class Ranking extends Fragment implements View.OnClickListener{

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayout;

    private OnFragmentInteractionListener mListener;

    public Ranking() {
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
        View rootView = inflater.inflate(R.layout.fragment_ranking, container, false);
        //findViewById del layout activity_main
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.mRecyclerView);

        //LinearLayoutManager necesita el contexto de la Activity.
        //El LayoutManager se encarga de posicionar los items dentro del recyclerview
        //Y de definir la politica de reciclaje de los items no visibles.
        mLinearLayout = new LinearLayoutManager(rootView.getContext());

        //Asignamos el LinearLayoutManager al recycler:
        mRecyclerView.setLayoutManager(mLinearLayout);

        setHasOptionsMenu(true);

        BDUser bduser = new BDUser(getContext());
        Cursor cursor = bduser.getAllUsersPunt();
        ArrayList<User> data = new ArrayList<User>();
        if (cursor.moveToFirst()) {
            do {
                String user = cursor.getString(cursor.getColumnIndex("username"));
                String mail =cursor.getString(cursor.getColumnIndex("mail"));
                int punt = cursor.getInt(cursor.getColumnIndex("bstpunt"));
                String uri = cursor.getString(cursor.getColumnIndex("uri"));
                User u = new User(user,mail, uri, punt);
                Log.wtf("Update", cursor.getInt(cursor.getColumnIndex("bstpunt")) + " " + cursor.getColumnIndex("bstpunt"));
                data.add(u);
            } while (cursor.moveToNext());
        }
        bduser.close();
        mRecyclerView.setAdapter(new CustomAdapter(data));

        if (mListener != null) {
            mListener.onFragmentInteraction(getTag(),2);
        }else {
            Log.v("ranking", "No listener attached");
        }

        return rootView;
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
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Resetear puntuaciones");
                builder.setMessage("Estas seguro que quieres borrar Todos los registros?");
                builder.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (mListener != null) {
                                    BDUser bdUser = new BDUser(getContext());
                                    boolean aux = bdUser.ResetPoints();
                                    bdUser.close();
                                    mListener.onFragmentInteraction(getTag(), 5);
                                } else {
                                    Log.v("Fragment1", "No listener attached");
                                }
                            }
                        });
                builder.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
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
            BDUser bdUser = new BDUser(getContext());
            ContentValues values = new ContentValues();
            boolean aux = bdUser.ResetPoints();
            bdUser.close();
            return null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
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

    }
}
