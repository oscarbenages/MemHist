package cat.memoriacastello.www.memoriahistorica;

/**
 * Created by coet on 04/09/2017.
 * Adaptat de: www.mysamplecode.com/2012/07/android-listview-cursoradapter-sqlite.html
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class AdaptadorBD {
    public static final String CLAU_DATA = "data";
    public static final String CLAU_USUARI = "usuari";
    public static final String CLAU_PUNTS = "puntuació";
    public static final String CLAU_TEMPS = "temps";

    private  static final String ETIQUETA = "AdaptadorBD";
    private AssistentBD assistentBD;
    private SQLiteDatabase baseDeDades;

    private static final  String NOM_BASE_DE_DADES = "BaseDades";
    private static final String NOM_TAULA = "taula_qualificacions";

    private final Context mCtx;

    private static final String CREA_BASE_DE_DADES = "CREATE TABLE IF NOT EXISTS taula_qualificacions(data INT PRIMARY KEY, usuari TEXT, puntuació INT, temps INT)";

    private static class AssistentBD extends SQLiteOpenHelper {
        AssistentBD(Context context) {
            super(context, NOM_BASE_DE_DADES, null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.w(ETIQUETA, CREA_BASE_DE_DADES);
            db.execSQL(CREA_BASE_DE_DADES);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(ETIQUETA, "Actualitzant base de dades a la versió...");
            //db.execSQL("DROP TABLE IF EXISTS " + NOM_TAULA);
        }
    }

    public AdaptadorBD(Context ctx){
        this.mCtx = ctx;
    }

    public AdaptadorBD obre() throws SQLException {
        assistentBD = new AssistentBD(mCtx);
        baseDeDades = assistentBD.getWritableDatabase();
        return this;
    }

    public void tanca() {
        if (assistentBD != null) assistentBD.close();
    }

    public long nouRegistre(String data, String usuari, String puntuació, String temps) {
        ContentValues initValues = new ContentValues();
        initValues.put(CLAU_DATA, data);
        initValues.put(CLAU_USUARI, usuari);
        initValues.put(CLAU_PUNTS, puntuació);
        initValues.put(CLAU_TEMPS, temps);
        return baseDeDades.insert(NOM_TAULA, null, initValues);
    }

    public long nouRegistre(long data, String usuari, int puntuació, long temps) {
        String cadData = String.valueOf(data);
        cadData = String.format(
                "%3$s/%2$s/%1$s %4$s:%5$s",
                cadData.substring(0, 4), //any
                cadData.substring(3, 6), //mes
                cadData.substring(5, 8), //dia
                cadData.substring(7, 10), //hora
                cadData.substring(9, 12) //minut

        );
        ContentValues initValues = new ContentValues();
        initValues.put(CLAU_DATA, data);
        initValues.put(CLAU_USUARI, usuari);
        initValues.put(CLAU_PUNTS, puntuació);
        initValues.put(CLAU_TEMPS, temps);
        return baseDeDades.insert(NOM_TAULA, null, initValues);
    }

    public boolean esborraRegistresTaula(){
        int fet = 0;
        fet = baseDeDades.delete(NOM_TAULA, null, null);
        Log.w(ETIQUETA, Integer.toString(fet));
        return fet > 0;
    }

    public boolean esborraRegistesUsuari(String usuaris[]){
        int done = 0;
        done = baseDeDades.delete(NOM_TAULA, "usuari=?", usuaris);
        Log.w(ETIQUETA, Integer.toString(done));
        return done > 0;
    }

    public Cursor classificació(String usuari){
        String usuaris[] = new String[] {usuari};
        String[] camps = new String[] {"data", "usuari", "puntuació", "temps"};
        Cursor cursor = baseDeDades.rawQuery("SELECT data, usuari, puntuació, temps FROM taula_qualificacions WHERE usuari='Igor' GROUPBY puntuació DESC, temps ASC LIMIT 10;", null);
        if (cursor != null) cursor.moveToFirst();
        return cursor;
    }

    public Cursor classificació(){
        Cursor cursor = baseDeDades.rawQuery("SELECT data, usuari, puntuació, temps FROM taula_qualificacions WHERE usuari='Igor' ORDER BY puntuació DESC, temps ASC LIMIT 10;", null);
        if (cursor != null) cursor.moveToFirst();
        return cursor;
    }

}

/*
Cursor cursor = baseDeDades.query(
                NOM_TAULA,
                camps,
                "usuari=?",
                usuaris,
                null,
                null,
                "puntuació DESC, temps ASC",
                null
        );
 */
