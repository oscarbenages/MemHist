package cat.memoriacastello.www.memoriahistorica;

/**
 * Created by coet on 04/09/2017.
 * www.mysamplecode.com/2012/07/android-listview-cursoradapter-sqlite.html
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class dbAdapter {
    public static final String KEY_DATE = "data";
    public static final String KEY_USER = "usuari";
    public static final String KEY_POINTS = "puntuació";
    public static final String KEY_TIME = "temps";

    private  static final String TAG = "dbAdapter";
    private  DatabaseHelper mDbHelper;
    private  SQLiteDatabase mDb;

    private  static final  String DATABASE_NAME = "BDqualificacions";
    private static final String SQLITE_TABLE = "qualificacions";

    private final Context mCtx;

    private static final String DATABASE_CREATE = "create table qualificacions(data int primary key,usuari text,puntuació int,temps int)";

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, 1);
        }


        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.w(TAG, DATABASE_CREATE);
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Actualitzant base de dades a la versió...");
            //db.execSQL("DROP TABLE IF EXISTS " + SQLITE_TABLE);
        }
    }

    public dbAdapter(Context ctx){
        this.mCtx = ctx;
    }

    public dbAdapter open() throws SQLException {
        mDbHelper= new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        if (mDbHelper != null) mDbHelper.close();
    }

    public long nouRegistre(long data, String usuari, int puntuació, long temps) {
        ContentValues initValues = new ContentValues();
        initValues.put(KEY_DATE, data);
        initValues.put(KEY_USER, usuari);
        initValues.put(KEY_POINTS, puntuació);
        initValues.put(KEY_TIME, temps);
        return mDb.insert(SQLITE_TABLE, null, initValues);
    }

    public boolean esborraRegistresTaula(){
        int done = 0;
        done = mDb.delete(SQLITE_TABLE, null, null);
        Log.w(TAG, Integer.toString(done));
        return done > 0;
    }

    public boolean esborraRegistesUsuari(String usuaris[]){
        int done = 0;
        done = mDb.delete(SQLITE_TABLE, "usuari=?", usuaris);
        Log.w(TAG, Integer.toString(done));
        return done > 0;
    }

    public Cursor classificació(String usuaris[]){
        String[] camps = new String[] {"data", "usuari", "puntuació", "temps"};
        Cursor cursor = mDb.query(
                "qualificacions",
                camps, "usuari=?",
                usuaris,
                    /*groupBy*/ null,
                    /*having*/ null,
                    /*orderBy*/ "puntuació DESC, temps ASC",
                    /*limit*/ null
        );
        if (cursor != null) cursor.moveToFirst();
        return cursor;
    }

}
