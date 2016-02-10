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
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.polpincho.proyectofinal.persistencia.BDUser;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditPerfil extends Fragment implements View.OnClickListener{

    private OnFragmentInteractionListener mListener;
    Button editback, editsave, editselectnewicon, editdeleteicon, editrestartpoints, editlogout;
    CheckBox sessionk;
    ImageView editicon;
    private String uri, passwordconfdialog;
    private EditText passworduno, passworddos, newemail;
    private String passwordunostr, passworddosstr, newemailstr;
    private Boolean pas1,mai1,uri1,keepSes,checked;

    String User;
    View rootView;

    public EditPerfil() {
        // Required empty public constructor
    }

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX1 =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static boolean validatemail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX1.matcher(emailStr);
        return matcher.find();
    }

    public static final Pattern VALID_PASS_ADDRESS_REGEX1 =
            Pattern.compile("((?=.*[a-z])(?=.*\\d)(?=.*[A-Z]).{8,40})", Pattern.CASE_INSENSITIVE);

    public static boolean validatepass(String passStr) {
        Matcher matcher = VALID_PASS_ADDRESS_REGEX1.matcher(passStr);
        return matcher.find();
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_edit_perfil, container, false);

        setHasOptionsMenu(true);

        editicon = (ImageView) rootView.findViewById(R.id.EditIcon);
        editselectnewicon = (Button) rootView.findViewById(R.id.EditSelectNewIcon);
        editdeleteicon = (Button) rootView.findViewById(R.id.EditDeleteIcon);
        editsave = (Button) rootView.findViewById(R.id.EditSaveButton);
        editback = (Button) rootView.findViewById(R.id.EditBackButton);
        editlogout = (Button) rootView.findViewById(R.id.EditLoggout);
        editrestartpoints = (Button) rootView.findViewById(R.id.EditRestPunt);
        sessionk = (CheckBox) rootView.findViewById(R.id.EditKeepSes);
        newemail = (EditText) rootView.findViewById(R.id.EditMail);
        passworduno = (EditText) rootView.findViewById(R.id.EditNewPassword);
        passworddos = (EditText) rootView.findViewById(R.id.EditNewPasswordConf);

        editselectnewicon.setOnClickListener(this);
        editdeleteicon.setOnClickListener(this);
        editsave.setOnClickListener(this);
        editback.setOnClickListener(this);
        editrestartpoints.setOnClickListener(this);
        editlogout.setOnClickListener(this);

        SharedPreferences settings = getContext().getSharedPreferences("MEMORYAPK", Context.MODE_PRIVATE);
        //Consultamos
        User = settings.getString("UserActivo", "Guest");
        keepSes = false;
        BDUser bdUser = new BDUser(getContext());
        Cursor c = bdUser.getUser(User);
        if (c.moveToFirst()) {
            sessionk.setChecked(c.getInt(c.getColumnIndex("keep"))>0);
            String uri = c.getString(c.getColumnIndex("uri"));
            Log.e("uri",""+uri);
            if (uri != null) {
                Uri imageway = Uri.parse(uri);
                try {
                    editicon.setImageBitmap(MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageway));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        c.close();
        bdUser.close();

        sessionk.setOnClickListener(this);
        uri = "";
        uri1= false;

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_perfil_edit, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.PerfilEditExit:
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
            case R.id.EditSelectNewIcon:
                Intent getImageAsContent = new Intent(Intent.ACTION_GET_CONTENT, null);
                getImageAsContent.setType("image/*");
                startActivityForResult(getImageAsContent, 1);
                break;
            case R.id.EditDeleteIcon:
                uri1 = true;
                editicon.setImageDrawable(getResources().getDrawable(R.drawable.default_user));
                break;
            case R.id.EditRestPunt:
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    final EditText quantity = new EditText(getActivity());
                    final EditText lot = new EditText(getActivity());

                builder.setTitle("Resetear puntuacion");
                builder.setMessage("Estas seguro que quieres resetear tu puntuacion?");
                builder.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                BDHelper bdHelper = new BDHelper();
                                bdHelper.execute(2);
                                if (mListener != null) {
                                    mListener.onFragmentInteraction("end", 4);
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
                break;
            case R.id.EditKeepSes:
                keepSes = !keepSes;
                break;
            case R.id.EditSaveButton:
                newemailstr = newemail.getText().toString();
                passwordunostr = passworduno.getText().toString();
                passworddosstr = passworddos.getText().toString();
                pas1 = validatepass(passwordunostr);
                mai1 = validatemail(newemailstr);
                BDHelper bdHelper = new BDHelper();
                bdHelper.execute(1);
                break;
            case R.id.EditBackButton:
                if (mListener != null) {
                    //En este caso, si no hay nada escrito en el EditText,
                    //pondremos el String vació ""
                    mListener.onFragmentInteraction("",1);
                }else {
                    Log.v("Fragment1", "No listener attached");
                }
                break;
            case R.id.EditLoggout:
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
                break;
            default:
                Log.v("editperfil","Not a button");
        }
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode < 0){
            if(requestCode >= 1 && requestCode <= 3){
                data.getData();
                Uri selectedImage = data.getData();
                uri = selectedImage.toString();

                Log.v("PICK","Selected image uri" + selectedImage);
                try {
                    editicon.setImageBitmap(MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage));
                    uri1 = true;
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }else{
            Log.v("Result","Something happened");
        }
    }

    private class BDHelper extends AsyncTask<Integer, Integer, String> {

        @Override
        protected void onPreExecute() {
            checked = sessionk.isChecked();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {

            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(Integer... params) {
            if (params[0]==1) {
                ContentValues values = new ContentValues();
                if (mai1) {
                    Log.e("Thread", "mail ok");
                    values.put("mail", newemailstr);
                }
                if (pas1) {
                    Log.e("Thread", "pass1 ok");
                    if (passworddosstr.equals(passwordunostr)) {
                        Log.e("Thread", "pass2 ok");
                        values.put("pass", passworddosstr);
                    }
                }
                Log.e("uri", "" + uri);
                if (uri1) {
                    if (uri.length() != 0) {
                        Log.e("Thread", "uri ok");
                        values.put("uri", uri);

                    } else {
                        values.put("uri", "NULL");
                    }
                }
                if (keepSes) {
                    values.put("keep", checked);
                }
                if (uri1 || mai1 || pas1 || keepSes) {
                    BDUser bdUser = new BDUser(getContext());
                    boolean aux = bdUser.updateEdit(values, User);
                    bdUser.close();
                    Log.e("Puto update", "" + aux);
                }
            }
            if (params[0]== 2){
                BDUser bdUser = new BDUser(getContext());
                bdUser.ResetPointsUser(User);
            }
            if (params[0] == 3){
                BDUser bdUser = new BDUser(getContext());
                Cursor c = bdUser.getPassByUsername(User);
                if (c.moveToFirst()){
                    passwordconfdialog = c.getString(c.getColumnIndex("pass"));
                }
                c.close();
                bdUser.close();
            }
            return null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }

}
