package com.example.polpincho.proyectofinal;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.polpincho.proyectofinal.persistencia.BDUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SignFragment extends Fragment implements View.OnClickListener{
    TextView usernamed;
    EditText passone;
    EditText passtwo;
    EditText emaile;
    Button ndlog;
    Button ndexit;
    Button back;
    private OnFragmentInteractionListener mListener;

    public SignFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_sign, container, false);

        usernamed = (TextView) rootView.findViewById(R.id.usernamesign);
        passone = (EditText) rootView.findViewById(R.id.password);
        passtwo = (EditText) rootView.findViewById(R.id.passwordconfirmation);
        emaile = (EditText) rootView.findViewById(R.id.maile);
        ndexit = (Button) rootView.findViewById(R.id.signndback);
        ndlog = (Button) rootView.findViewById(R.id.signndlog);
        back = (Button) rootView.findViewById(R.id.backsign);
        back.setOnClickListener(this);
        ndlog.setOnClickListener(this);
        ndexit.setOnClickListener(this);

        Bundle args = this.getArguments();
        if (args != null){
            usernamed.setText(args.getString("message"));
        }else{
            usernamed.setText("");
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

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static boolean validatemail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    public static final Pattern VALID_PASS_ADDRESS_REGEX =
            Pattern.compile("((?=.*[a-z])(?=.*\\d)(?=.*[A-Z]).{8,40})", Pattern.CASE_INSENSITIVE);

    public static boolean validatepass(String passStr) {
        Matcher matcher = VALID_PASS_ADDRESS_REGEX.matcher(passStr);
        return matcher.find();
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.signndback:
                String username = usernamed.getText().toString();
                String passwan = passone.getText().toString();
                String passchu = passtwo.getText().toString();
                String imail = emaile.getText().toString();
                boolean pass1 = false;
                boolean pass2 = false;
                boolean mail1 = false;

                if (validatepass(passwan)) {
                    pass1 = true;
                }
                else passone.setText("");

                if (pass1){
                    if (passwan.equals(passchu)){
                        pass2 = true;
                    }
                    else{
                        passtwo.setText("");
                    }
                }
                else{
                    passtwo.setText("");
                }

                if (validatemail(imail)) {
                    mail1 = true;
                }

                if (pass1 && pass2 && mail1) {
                    BDUser bduser = new BDUser(getContext());
                    ContentValues valuesToStore = new ContentValues();
                    valuesToStore.put("username", String.valueOf(username));
                    valuesToStore.put("pass", String.valueOf(passwan));
                    valuesToStore.put("mail", String.valueOf(imail));
                    valuesToStore.put("keep", String.valueOf(false));
                    valuesToStore.put("firstlog", String.valueOf(true));
                    if (bduser.createUser(valuesToStore)) {
                        Toast.makeText(getContext(), "insertion done", Toast.LENGTH_SHORT).show();
                        if (mListener != null) {
                            mListener.onFragmentInteraction(usernamed.getText().toString(), 3);
                        } else {
                        }
                    } else
                        Toast.makeText(getContext(), "error on insertion", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getContext(), "incorrect data", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.signndlog:
                username = usernamed.getText().toString();
                passwan = passone.getText().toString();
                passchu = passtwo.getText().toString();
                imail = emaile.getText().toString();
                pass1 = false;
                pass2 = false;
                mail1 = false;

                if (validatepass(passwan)) {
                    pass1 = true;
                }
                else passone.setText("");

                if (pass1){
                    if (passwan.equals(passchu)){
                        pass2 = true;
                    }
                    else{
                        passtwo.setText("");
                    }
                }
                else{
                    passtwo.setText("");
                }

                if (validatemail(imail)) {
                    mail1 = true;
                }

                if (pass1 && pass2 && mail1) {
                    BDUser bduser = new BDUser(getContext());
                    ContentValues valuesToStore = new ContentValues();
                    valuesToStore.put("username", String.valueOf(username));
                    valuesToStore.put("pass", String.valueOf(passwan));
                    valuesToStore.put("mail", String.valueOf(imail));
                    valuesToStore.put("keep", String.valueOf(false));
                    valuesToStore.put("firstlog", String.valueOf(true));
                    if (bduser.createUser(valuesToStore)) {
                        Toast.makeText(getContext(), "insertion done", Toast.LENGTH_SHORT).show();
                        SharedPreferences settings = getActivity().getSharedPreferences("MEMORYAPK", 0);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString("UserActivo",username);
                        editor.apply();

                        Toast.makeText(getContext(), "Loggin done", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getContext(), Perfil.class);
                        intent.putExtra("Username",username);
                        startActivity(intent);
                        if (mListener != null) {
                            mListener.onFragmentInteraction("", 4);
                        } else {
                        }
                    } else
                        Toast.makeText(getContext(), "error on insertion", Toast.LENGTH_SHORT).show();
                    bduser.close();
                }
                else {
                    Toast.makeText(getContext(), "incorrect data", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.backsign:
                if (mListener != null) {
                    //En este caso, si no hay nada escrito en el EditText,
                    //pondremos el String vació ""
                    mListener.onFragmentInteraction(usernamed.getText().toString(),3);
                }else {
                    Log.v("Fragment1", "No listener attached");
                }
                break;
            default:
                Log.v("Fragment2", "Not a button");
        }
    }
}
