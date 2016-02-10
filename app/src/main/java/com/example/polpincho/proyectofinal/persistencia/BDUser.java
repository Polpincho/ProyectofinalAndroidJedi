package com.example.polpincho.proyectofinal.persistencia;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class BDUser extends SQLiteOpenHelper {


    //Declaracion del nombre de la base de datos
    public static final int DATABASE_VERSION = 1;

    //Declaracion global de la version de la base de datos
    public static final String DATABASE_NAME = "users1";

    //Declaracion del nombre de la tabla
    public static final String USER_TABLE ="Users";

    //sentencia global de cracion de la base de datos
    public static final String USER_TABLE_CREATE = "CREATE TABLE " + USER_TABLE + " (username TEXT PRIMARY KEY UNIQUE, pass TEXT, mail TEXT UNIQUE, bstpunt INTEGER, firstlog BOOLEAN, keep BOOLEAN, notis INTEGER, uri TEXT);";



    public BDUser(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(USER_TABLE_CREATE);

    }


    //obtener una lista de users
    public Cursor getAllUsersPunt() {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {"username","mail","bstpunt","uri"};
        Cursor c = db.query(
                USER_TABLE,  // The table to query
                columns,    // The columns to return
                "bstpunt!=0",       // The columns for the WHERE clause
                null,       // The values for the WHERE clause
                null,       // don't group the rows
                null,       // don't filter by row groups
                "bstpunt ASC"        // The sort order
        );
        return c;
    }

    public Cursor getAllUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {"username","mail","bstpunt","uri"};
        Log.d("BDUser", "get All Users");
        Cursor c = db.query(
                USER_TABLE,  // The table to query
                columns,    // The columns to return
                null,       // The columns for the WHERE clause
                null,       // The values for the WHERE clause
                null,       // don't group the rows
                null,       // don't filter by row groups
                null        // The sort order
        );
        return c;
    }

    public Cursor getUri(String user) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {"uri"};
        String[] colum = {user};
        Cursor c = db.query(
                USER_TABLE,  // The table to query
                columns,    // The columns to return
                "username=?",       // The columns for the WHERE clause
                colum,       // The values for the WHERE clause
                null,       // don't group the rows
                null,       // don't filter by row groups
                null        // The sort order
        );
        return c;
    }

    public Cursor getfirstLog(String user) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {"firstlog"};
        String[] colum = {user};
        Log.d("BDUser", "Get first" + " " + user);
        Cursor c = db.query(
                USER_TABLE,  // The table to query
                columns,    // The columns to return
                "username=?",       // The columns for the WHERE clause
                colum,       // The values for the WHERE clause
                null,       // don't group the rows
                null,       // don't filter by row groups
                null        // The sort order
        );
        return c;
    }

    public Cursor getKeepLog(String user) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {"keep"};
        String[] colum = {user};
        Log.d("BDUser", "Get keep Log" + " " + user);
        Cursor c = db.query(
                USER_TABLE,  // The table to query
                columns,    // The columns to return
                "username=?",       // The columns for the WHERE clause
                colum,       // The values for the WHERE clause
                null,       // don't group the rows
                null,       // don't filter by row groups
                null        // The sort order
        );
        return c;
    }

    public Cursor getNotis(String user) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {"notis"};
        String[] colum = {user};
        Cursor c = db.query(
                USER_TABLE,  // The table to query
                columns,    // The columns to return
                "username=?",       // The columns for the WHERE clause
                colum,       // The values for the WHERE clause
                null,       // don't group the rows
                null,       // don't filter by row groups
                null        // The sort order
        );
        return c;
    }


    public Cursor getPassByUsername(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {"pass"};
        String[] where = {username};
        Cursor c = db.query(
                USER_TABLE,  // The table to query
                columns,    // The columns to return
                "username=?",   // The columns for the WHERE clause
                where,      // The values for the WHERE clause
                null,       // don't group the rows
                null,       // don't filter by row groups
                null        // The sort order
        );
        return c;
    }

    public Cursor getPointsByUsername(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {"bstpunt"};
        String[] where = {username};
        Cursor c = db.query(
                USER_TABLE,  // The table to query
                columns,    // The columns to return
                "username=?",   // The columns for the WHERE clause
                where,      // The values for the WHERE clause
                null,       // don't group the rows
                null,       // don't filter by row groups
                null        // The sort order
        );
        return c;
    }


    public boolean createUser (ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase();
        values.put("uri","NULL");
        long e = db.insert(
                USER_TABLE,
                null,
                values);
        Log.d("BDUser", "createUser " +e);
        if (e == -1){
            return false;
        }
        return true;

    }
//TODO:
    public boolean updatePoints (ContentValues values,String user) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] val = {user};
        long f = db.update(USER_TABLE, values, "username=?", val);

        Log.d("BDUser", "updatePoints " + f + " " + user);
        //db.execSQL("UPDATE Users SET bstpunt="+val[1]+" WHERE username='"+user +"' AND (bstpunt>"+ val[1] + " OR bstpunt=0)");
        //Log.wtf("Update", f + "" + values.getAsInteger("bstpunt").toString());

        if (f == -1) {
            return false;
        }

        return true;
    }

    public boolean ResetPoints () {
        ContentValues values = new ContentValues();
        values.put("bstpunt", 0);
        SQLiteDatabase db = this.getWritableDatabase();
        long f = db.update(USER_TABLE, values, "bstpunt!=0", null);

        Log.d("BDUser", "ResetPoints " +f);
        if (f == -1) {
            return false;
        }

        return true;
    }

    public boolean updatekeeplogged (Boolean aux,String user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("keep", aux);
        String[] val = {user};
        long f = db.update(USER_TABLE, values, "username=?", val);
        Log.d("BDUser", "update keep " +f  + " " + user);
        if (f == -1) {
            return false;
        }

        return true;
    }

    public boolean updatenotis (int not,String user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("notis",not);
        String[] val = {user};
        long f = db.update(USER_TABLE, values, "username=?", val);
        Log.d("BDUser", "update notis " + f  + " " + user);
        if (f == -1) {
            return false;
        }
        return true;
    }

    public boolean FirstLog (String user) {
        SQLiteDatabase db = this.getWritableDatabase();
        boolean aux = false;
        ContentValues values = new ContentValues();
        values.put("firstlog",aux);
        String[] val = {user};
        long f = db.update(USER_TABLE, values, "username=?", val);
        Log.d("BDUser", "FirstLog " +f  + " " + user);
        if (f == -1) {
            return false;
        }
        return true;
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }


    public Cursor getUser(String use) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {"username","mail","bstpunt","firstlog","keep","notis","uri"};
        String[] where = {use};
        Cursor c = db.query(
                USER_TABLE,  // The table to query
                columns,    // The columns to return
                "username=?",   // The columns for the WHERE clause
                where,      // The values for the WHERE clause
                null,       // don't group the rows
                null,       // don't filter by row groups
                null        // The sort order
        );
        return c;
    }

    public boolean updateEdit(ContentValues values, String user) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] val = {user};
        long f = db.update(USER_TABLE, values, "username=?", val);
        Log.d("BDUser", "update edit " +f + " " + user);
        if (f == -1) {
            return false;
        }
        return true;
    }

    public boolean ResetPointsUser(String user) {
        ContentValues values = new ContentValues();
        values.put("bstpunt", 0);
        SQLiteDatabase db = this.getWritableDatabase();
        String[] val = {user};
        long f = db.update(USER_TABLE, values, "username=?", val);

        Log.d("BDUser", "ResetPointsUser " +f);
        if (f == -1) {
            return false;
        }

        return true;
    }
}
