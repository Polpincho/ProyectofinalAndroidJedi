package com.example.polpincho.proyectofinal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.polpincho.proyectofinal.persistencia.BDUser;

import java.io.IOException;
import java.util.List;


public class ViewPerfil extends Fragment implements View.OnClickListener{

    private OnFragmentInteractionListener mListener;
    Button viewback, viewedit,viewlogout;
    TextView viewuser, viewmail, viewscore;
    ImageView viewicon;
    List<Address> l;
    LocationManager lm;
    LocationListener lis;
    View rootView;

    public ViewPerfil() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_view_perfil, container, false);
        viewuser = (TextView) rootView.findViewById(R.id.ViewUsername);
        viewmail = (TextView) rootView.findViewById(R.id.ViewEmail);
        viewscore = (TextView) rootView.findViewById(R.id.ViewBestScore);
        viewicon = (ImageView) rootView.findViewById(R.id.ViewIcon);


        viewback = (Button) rootView.findViewById(R.id.ViewBackButton);
        viewback.setOnClickListener(this);
        viewedit = (Button) rootView.findViewById(R.id.ViewEditButton);
        viewedit.setOnClickListener(this);
        viewlogout = (Button) rootView.findViewById(R.id.ViewLogOut);
        viewlogout.setOnClickListener(this);

        setHasOptionsMenu(true);

        //Instanciamos el SharedPreferences
        SharedPreferences settings = getActivity().getSharedPreferences("MEMORYAPK", Context.MODE_PRIVATE);
        //Consultamos
        String useractivo = settings.getString("UserActivo", "Guest");
        viewuser.setText(useractivo);
        BDUser bdUser = new BDUser(getContext());
        Cursor c = bdUser.getUser(useractivo);
        if (c.moveToFirst()) {
            viewscore.setText(""+c.getInt(c.getColumnIndex("bstpunt")));
            viewmail.setText(""+c.getString(c.getColumnIndex("mail")));
            viewmail.setText("" + c.getString(c.getColumnIndex("mail")));
            String uri = c.getString(c.getColumnIndex("uri"));
            Log.e("uri",""+uri);
            if (uri != null) {
                Uri imageway = Uri.parse(uri);
                try {
                    viewicon.setImageBitmap(MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageway));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        c.close();
        bdUser.close();
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
    }

    @Override
    public void onPause() {
        //lm.removeGpsStatusListener((GpsStatus.Listener) lis);
        if (lis!= null) lm.removeUpdates(lis);
        super.onPause();
    }

    //Importante! Aquí no se puede usar el método del "onClick" en xml
    // por que referencia a la Activity y no al fragment!!
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ViewEditButton:
                if (mListener != null) {
                    //En este caso, si no hay nada escrito en el EditText,
                    //pondremos el String vació ""
                    mListener.onFragmentInteraction("",2);
                }else {
                    Log.v("Fragment1", "No listener attached");
                }
                break;
            case R.id.ViewBackButton:
                Intent i = new Intent(getContext(),InMenu.class);
                startActivity(i);
                break;
            case R.id.ViewLogOut:
                SharedPreferences settings = getContext().getSharedPreferences("MEMORYAPK", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("SessionActiva", false);
                editor.apply();
                i = new Intent(getContext(), Login.class);
                startActivity(i);
                if (mListener != null) {
                    mListener.onFragmentInteraction("end",4);
                }else {
                    Log.v("Fragment1", "No listener attached");
                }
                break;
            default:
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_perfil_view, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.PerfilViewGps:
                l = null;
                lm = (LocationManager) getActivity()
                        .getSystemService(Context.LOCATION_SERVICE);
                lis = new LocationListener() {

                    @Override
                    public void onStatusChanged(String provider, int status,
                                                Bundle extras) {
                    }

                    @Override
                    public void onProviderEnabled(String provider) {
                    }

                    @Override
                    public void onProviderDisabled(String provider) {
                    }

                    @Override
                    public void onLocationChanged(Location location) {
                        Geocoder gc = new Geocoder(getContext());
                        try {
                            l = gc.getFromLocation(location.getLatitude(),
                                    location.getLongitude(), 5);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        Log.e("GPS", ""+location.getLatitude()+ " " +location.getLongitude());
                        for (int i = 0; i < l.size(); ++i) {
                            Log.v("LOG", l.get(i).getAddressLine(0).toString());
                            TextView t = (TextView) rootView.findViewById(R.id.GPS1);
                            if (i == 0) t.setText("");
                            t.setText(t.getText() + "\n" + l.get(i).getAddressLine(0).toString());
                        }

                    }
                };


                if (lm.isProviderEnabled(lm.GPS_PROVIDER)) {
                    try{
                        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, lis);
                    }
                    catch (Exception e) {
                    }
                    Log.e("GPS", "Satelite");
                }
                if (lm.isProviderEnabled(lm.NETWORK_PROVIDER)) {
                    try{
                        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, lis);
                    }
                    catch (Exception e) {
                    }
                    Log.e("GPS", "Network");
                }
                return true;
            case R.id.PerfilViewExit:
                SharedPreferences settings = getContext().getSharedPreferences("MEMORYAPK", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("SessionActiva", false);
                editor.apply();
                if (mListener != null) {
                    mListener.onFragmentInteraction("end", 5);
                }else {
                    Log.v("Fragment1", "No listener attached");
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}

