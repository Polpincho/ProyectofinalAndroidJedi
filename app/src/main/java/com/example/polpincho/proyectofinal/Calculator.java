package com.example.polpincho.proyectofinal;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.polpincho.proyectofinal.persistencia.BDUser;


public class Calculator extends AppCompatActivity implements View.OnClickListener{

    private int Notis = 1;
    private TextView visor;
    private Button button0;
    private Button button1;
    private Button button2;
    private Button button3;
    private Button button4;
    private Button button5;
    private Button button6;
    private Button button7;
    private Button button8;
    private Button button9;
    private Button borrar;
    private Button equal;
    private Button sum;
    private Button minus;
    private Button multi;
    private Button divide;
    private Button dot;
    private Button answer;

    private String valor;
    private double val = 0;
    private double ans = 0;
    private double dec = 0;
    private double ant = 0;
    private int op = 0;
    private boolean negative = false;
    private boolean decimal = false;
    private boolean errores = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("");

        BDHelper bdHelper = new BDHelper();
        bdHelper.execute(5);

        visor = (TextView) findViewById(R.id.visor);
        button0 = (Button) findViewById(R.id.button0);
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
        button4 = (Button) findViewById(R.id.button4);
        button5 = (Button) findViewById(R.id.button5);
        button6 = (Button) findViewById(R.id.button6);
        button7 = (Button) findViewById(R.id.button7);
        button8 = (Button) findViewById(R.id.button8);
        button9 = (Button) findViewById(R.id.button9);
        borrar = (Button) findViewById(R.id.borrar);
        equal = (Button) findViewById(R.id.equal);
        sum = (Button) findViewById(R.id.sum);
        minus = (Button) findViewById(R.id.minus);
        multi = (Button) findViewById(R.id.multi);
        divide = (Button) findViewById(R.id.divide);
        dot = (Button) findViewById(R.id.dot);
        answer = (Button) findViewById(R.id.ans);

        button0.setOnClickListener(this);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button5.setOnClickListener(this);
        button6.setOnClickListener(this);
        button7.setOnClickListener(this);
        button8.setOnClickListener(this);
        button9.setOnClickListener(this);
        borrar.setOnClickListener(this);
        equal.setOnClickListener(this);
        sum.setOnClickListener(this);
        minus.setOnClickListener(this);
        multi.setOnClickListener(this);
        divide.setOnClickListener(this);
        dot.setOnClickListener(this);
        answer.setOnClickListener(this);
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_calculator, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.calccall:
                Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + visor.getText().toString()));
                startActivity(i);
                return true;
            case R.id.calcbrowser:
                i = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://"));
                startActivity(i);
                return true;
            case R.id.CalcLogOut:
                SharedPreferences settings = getApplicationContext().getSharedPreferences("MEMORYAPK", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("SessionActiva", false);
                editor.apply();
                i = new Intent(getApplicationContext(), Login.class);
                startActivity(i);
                finish();
                return true;
            case R.id.submenu1:
                Toast.makeText(getApplicationContext(), "Toast Notifications", Toast.LENGTH_SHORT).show();
                Notis = 1;
                savenotis();
                return true;
            case R.id.submenu2:
                Toast.makeText(getApplicationContext(), "SnackBar Notifications", Toast.LENGTH_SHORT).show();
                Notis = 2;
                savenotis();
                return true;
            case R.id.subsubmenu2:
                Toast.makeText(getApplicationContext(), "Sticky State Notifications", Toast.LENGTH_SHORT).show();
                Notis = 4;
                savenotis();
                return true;
            case R.id.subsubmenu1:
                Toast.makeText(getApplicationContext(), "No Sticky State Notifications", Toast.LENGTH_SHORT).show();
                Notis = 3;
                savenotis();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void savenotis() {
        BDHelper bdhelp = new BDHelper();
        bdhelp.execute(Notis);
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
            SharedPreferences settings = getApplicationContext().getSharedPreferences("MEMORYAPK", Context.MODE_PRIVATE);
            //Consultamos
            String User = settings.getString("UserActivo", "Guest");
            if (params[0]==5){
                BDUser bdUser = new BDUser(getApplicationContext());
                Cursor c = bdUser.getNotis(User);
                if (c.moveToFirst()) {
                    Notis = c.getInt(c.getColumnIndex("notis"));
                }
                c.close();
                bdUser.close();
            }
            else {
                BDUser bdUser = new BDUser(getApplicationContext());
                boolean aux = bdUser.updatenotis(params[0], User);
                bdUser.close();
                Log.e("Puto update", "" + aux);
            }
            return null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putDouble("val", val);
        outState.putDouble("ant", ant);
        outState.putDouble("ans", ans);
        outState.putDouble("dec", dec);
        outState.putInt("op", op);
        outState.putBoolean("errores", errores);
        outState.putBoolean("negative", negative);
        outState.putBoolean("decimal", decimal);
        outState.putString("valor", valor);
        outState.putInt("Notis", Notis);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        val = savedInstanceState.getDouble("val");
        ant = savedInstanceState.getDouble("ant");
        ans = savedInstanceState.getDouble("ans");
        dec = savedInstanceState.getDouble("dec");
        op = savedInstanceState.getInt("op");
        errores = savedInstanceState.getBoolean("errores");
        negative = savedInstanceState.getBoolean ("negative");
        decimal = savedInstanceState.getBoolean("decimal");
        valor = savedInstanceState.getString("valor");
        Notis = savedInstanceState.getInt("Notis");
        visor.setText(valor);


        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button0:
                if(val  < 1.0E10) {
                    if (decimal) {
                        dec /= 10;
                    } else {
                        val *= 10;
                    }
                    if (decimal) {
                        valor = String.valueOf(val);
                    } else {
                        valor = String.format("%.0f", val);
                    }
                    visor.setText(valor);
                }
                break;
            case R.id.button1:
                if(val  < 1.0E10) {
                    if (decimal) {
                        if (negative) {
                            val -= dec;
                        } else {
                            val += dec;
                        }
                        dec /= 10;
                    } else {
                        if (negative) {
                            val *= 10;
                            val -= 1;
                        } else {
                            val *= 10;
                            val += 1;
                        }
                    }
                    if (decimal) {
                        valor = String.valueOf(val);
                    } else {
                        valor = String.format("%.0f", val);
                    }
                    visor.setText(valor);
                }
                break;
            case R.id.button2:
                if(val  < 1.0E10) {
                    if (decimal) {
                        if (negative) {
                            val -= dec * 2;
                        } else {
                            val += dec * 2;
                        }
                        dec /= 10;
                    } else {
                        if (negative) {
                            val *= 10;
                            val -= 2;
                        } else {
                            val *= 10;
                            val += 2;
                        }
                    }
                    if (decimal) {
                        valor = String.valueOf(val);
                    } else {
                        valor = String.format("%.0f", val);
                    }
                    visor.setText(valor);
                }
                break;
            case R.id.button3:
                if(val  < 1.0E10) {
                    if (decimal) {
                        if (negative) {
                            val -= dec * 3;
                        } else {
                            val += dec * 3;
                        }
                        dec /= 10;
                    } else {
                        if (negative) {
                            val *= 10;
                            val -= 3;
                        } else {
                            val *= 10;
                            val += 3;
                        }
                    }
                    if (decimal) {
                        valor = String.valueOf(val);
                    } else {
                        valor = String.format("%.0f", val);
                    }
                    visor.setText(valor);
                }
                break;
            case R.id.button4:
                if(val  < 1.0E10) {
                    if (decimal) {
                        if (negative) {
                            val -= dec * 4;
                        } else {
                            val += dec * 4;
                        }
                        dec /= 10;
                    } else {
                        if (negative) {
                            val *= 10;
                            val -= 4;
                        } else {
                            val *= 10;
                            val += 4;
                        }
                    }
                    if (decimal) {
                        valor = String.valueOf(val);
                    } else {
                        valor = String.format("%.0f", val);
                    }
                    visor.setText(valor);
                }
                break;
            case R.id.button5:
                if(val  < 1.0E10) {
                    if (decimal) {
                        if (negative) {
                            val -= dec * 5;
                        } else {
                            val += dec * 5;
                        }
                        dec /= 10;
                    } else {
                        if (negative) {
                            val *= 10;
                            val -= 5;
                        } else {
                            val *= 10;
                            val += 5;
                        }
                    }
                    if (decimal) {
                        valor = String.valueOf(val);
                    } else {
                        valor = String.format("%.0f", val);
                    }
                    visor.setText(valor);
                }
                break;
            case R.id.button6:
                if(val  < 1.0E10) {
                    if (decimal) {
                        if (negative) {
                            val -= dec * 6;
                        } else {
                            val += dec * 6;
                        }
                        dec /= 10;
                    } else {
                        if (negative) {
                            val *= 10;
                            val -= 6;
                        } else {
                            val *= 10;
                            val += 6;
                        }
                    }
                    if (decimal) {
                        valor = String.valueOf(val);
                    } else {
                        valor = String.format("%.0f", val);
                    }
                    visor.setText(valor);
                }
                break;
            case R.id.button7:
                if(val  < 1.0E10) {
                    if (decimal) {
                        if (negative) {
                            val -= dec * 7;
                        } else {
                            val += dec * 7;
                        }
                        dec /= 10;
                    } else {
                        if (negative) {
                            val *= 10;
                            val -= 7;
                        } else {
                            val *= 10;
                            val += 7;
                        }
                    }
                    if (decimal) {
                        valor = String.valueOf(val);
                    } else {
                        valor = String.format("%.0f", val);
                    }
                    visor.setText(valor);
                }
                break;
            case R.id.button8:
                if(val  < 1.0E10) {
                    if (decimal) {
                        if (negative) {
                            val -= dec * 8;
                        } else {
                            val += dec * 8;
                        }
                        dec /= 10;
                    } else {
                        if (negative) {
                            val *= 10;
                            val -= 8;
                        } else {
                            val *= 10;
                            val += 8;
                        }
                    }
                    if (decimal) {
                        valor = String.valueOf(val);
                    } else {
                        valor = String.format("%.0f", val);
                    }
                    visor.setText(valor);
                }
                break;
            case R.id.button9:
                if(val  < 1.0E10) {
                    if (decimal) {
                        if (negative) {
                            val -= dec * 9;
                        } else {
                            val += dec * 9;
                        }
                        dec /= 10;
                    } else {
                        if (negative) {
                            val *= 10;
                            val -= 9;
                        } else {
                            val *= 10;
                            val += 9;
                        }
                    }
                    if (decimal) {
                        valor = String.valueOf(val);
                    } else {
                        valor = String.format("%.0f", val);
                    }
                    visor.setText(valor);
                }
                break;
            case R.id.borrar:
                decimal = false;
                negative = false;
                val = 0;
                valor = String.format("%.0f", val);
                visor.setText(valor);
                break;
            case R.id.sum:
                decimal = false;
                negative = false;
                op = 1;
                if (val != 0) {
                    ant = val;
                }
                val = 0;
                if (decimal){
                    valor = String.valueOf(ant);
                }
                else {
                    valor = String.format("%.0f", ant);
                }
                decimal = false;
                visor.setText(valor);
                break;

            case R.id.minus:
                if (val != 0) {
                    negative = false;
                    op = 2;
                    if (val != 0) {
                        ant = val;
                    }
                    val = 0;
                    if (decimal){
                        valor = String.valueOf(ant);
                    }
                    else {
                        valor = String.format("%.0f", ant);
                    }
                    decimal = false;
                    visor.setText(valor);
                }
                else {
                    negative = true;
                    valor = String.format("%.0f", ant);
                    visor.setText(valor);
                }
                break;
            case R.id.multi:

                negative = false;
                op = 3;
                if (val != 0) {
                    ant = val;
                }
                val = 0;
                if (decimal){
                    valor = String.valueOf(ant);
                }
                else {
                    valor = String.format("%.0f", ant);
                }
                decimal = false;
                visor.setText(valor);
                break;
            case R.id.divide:

                negative = false;
                op = 4;
                if (val != 0) {
                    ant = val;
                }
                val = 0;
                if (decimal){
                    valor = String.valueOf(ant);
                }
                else {
                    valor = String.format("%.0f", ant);
                }
                decimal = false;
                visor.setText(valor);
                break;
            case R.id.dot:
                if (!decimal) {
                    decimal = true;
                    dec = 0.1;
                }
                break;
            case R.id.ans:
                val = ans;
                valor = String.valueOf(val);
                visor.setText(valor);
                break;
            case R.id.equal:
                decimal = false;
                negative = false;
                switch (op) {
                    case 1:
                        val += ant;
                        break;
                    case 2:
                        val = ant - val;
                        break;
                    case 3:
                        val *= ant;
                        break;
                    case 4:
                        if (0 == val){
                            errores = true;

                        }
                        else {
                            val = ant / val;
                        }
                        break;
                }
                if (errores == false){
                    ans = val;
                    valor = String.valueOf(val);
                    visor.setText(valor);
                }
                else{
                    switch (Notis) {
                        case 1:
                            Toast.makeText(getApplicationContext(), "Operacion Ilegal: Division entre 0", Toast.LENGTH_SHORT).show();
                            break;
                        case 2:
                            Snackbar.make(v, "Operacion Ilegal: Division entre 0", Snackbar.LENGTH_LONG)
                                    .show();
                            break;
                        case 3:
                            //Entero que nos permite identificar la notificaci�n
                            int mId = 1;
                            //Instanciamos Notification Manager
                            NotificationManager mNotificationManager =
                                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                            NotificationCompat.Builder mBuilder =
                                    new NotificationCompat.Builder(getApplicationContext())
                                            .setSmallIcon(R.drawable.ic_warning_amber_48dp)
                                            .setContentTitle("Operacion ilegal")
                                            .setContentText("Has intentado dividir entre 0...");


                            // Creamos un intent explicito, para abrir la app desde nuestra notificaci�n
                            Intent resultIntent = new Intent(getApplicationContext(), Calculator.class);

                            //El objeto stack builder contiene una pila artificial para la Acitivty empezada.
                            //De esta manera, aseguramos que al navegar hacia atr�s
                            //desde la Activity nos lleve a la home screen.

                            //Desde donde la creamos
                            TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
                            // A�ade la pila para el Intent,pero no el intent en s�
                            stackBuilder.addParentStack(Calculator.class);
                            // A�adimos el intent que empieza la activity que est� en el top de la pila
                            stackBuilder.addNextIntent(resultIntent);

                            //El pending intent ser� el que se ejecute cuando la notificaci�n sea pulsada
                            PendingIntent resultPendingIntent =
                                    stackBuilder.getPendingIntent(
                                            0,
                                            PendingIntent.FLAG_UPDATE_CURRENT
                                    );
                            mBuilder.setContentIntent(resultPendingIntent);
                            mNotificationManager.notify(mId, mBuilder.build());
                            break;
                        case 4:
                            mId = 2;
                            //Instanciamos Notification Manager
                            mNotificationManager =
                                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


                            // Para la notificaciones, en lugar de crearlas directamente, lo hacemos mediante
                            // un Builder/contructor.
                            mBuilder =
                                    new NotificationCompat.Builder(getApplicationContext())
                                            .setSmallIcon(R.drawable.ic_warning_amber_48dp)
                                            .setContentTitle("Holi")
                                            .setContentText("No dividas entre 0 :)");

                            // Creamos un intent explicito, para abrir la app desde nuestra notificaci�n
                            resultIntent = new Intent(getApplicationContext(), Calculator.class);

                            //El objeto stack builder contiene una pila artificial para la Acitivty empezada.
                            //De esta manera, aseguramos que al navegar hacia atr�s
                            //desde la Activity nos lleve a la home screen.

                            stackBuilder = TaskStackBuilder.create(getApplicationContext());
                            // Adds the back stack for the Intent (but not the Intent itself)
                            stackBuilder.addParentStack(Calculator.class);
                            // Adds the Intent that starts the Activity to the top of the stack
                            stackBuilder.addNextIntent(resultIntent);

                            //El pending intent ser� el que se ejecute cuando la notificaci�n sea pulsada
                            resultPendingIntent =
                                    stackBuilder.getPendingIntent(
                                            1,
                                            PendingIntent.FLAG_UPDATE_CURRENT
                                    );
                            mBuilder.setContentIntent(resultPendingIntent);

                            // mId nos permite actualizar las notificaciones en un futuro
                            // Notificamos
                            Notification noti = mBuilder.build();
                            noti.flags |= Notification.FLAG_INSISTENT;
                            noti.flags |= Notification.FLAG_NO_CLEAR;
                            noti.flags |= Notification.FLAG_SHOW_LIGHTS;
                            noti.flags |= Notification.FLAG_NO_CLEAR;
                            mNotificationManager.notify(mId, noti);
                            break;
                    }
                    errores = false;
                }
                op = 0;
                ant = val;
                ans = val;
                val = 0;
                break;

        }
        Log.e("ans" , ""+ans);
    }
}
