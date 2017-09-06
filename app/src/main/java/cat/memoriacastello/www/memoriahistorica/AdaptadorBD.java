package cat.memoriacastello.www.memoriahistorica;

/**
 * Created by coet on 04/09/2017.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class AdaptadorBD {
    Cadenes cad = new Cadenes();
    public static final String CLAU_DATA = "data";
    public static final String CLAU_MT_DATA = "mt_data";
    public static final String CLAU_USUARI = "usuari";
    public static final String CLAU_PUNTS = "puntuació";
    public static final String CLAU_TEMPS = "temps";
    public static final String CLAU_CAD_TEMPS = "cad_temps";

    private  static final String ETIQUETA = "AdaptadorBD";
    private AssistentBD assistentBD;
    private SQLiteDatabase baseDeDades;

    private static final  String NOM_BASE_DE_DADES = "BaseDades";
    private static final String NOM_TAULA = "taula_qualificacions";

    private final Context mCtx;

    private static final String CREA_BASE_DE_DADES = "CREATE TABLE IF NOT EXISTS taula_qualificacions(data INT PRIMARY KEY, mt_data TEXT, usuari TEXT, puntuació INT, temps INT, cad_temps TEXT)";

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


    public long nouRegistre(long data, String usuari, int puntuació, long temps) {
        ContentValues initValues = new ContentValues();
        initValues.put(CLAU_MT_DATA, data);
        initValues.put(CLAU_DATA, cad.formataDataCurta(String.valueOf(data)));
        initValues.put(CLAU_USUARI, usuari);
        initValues.put(CLAU_PUNTS, puntuació);
        initValues.put(CLAU_TEMPS, temps);
        initValues.put(CLAU_CAD_TEMPS, cad.obtéDifTemps(Long.valueOf(temps), 0 ,true));
        return baseDeDades.insert(NOM_TAULA, null, initValues);
    }

    public boolean esborraRegistresTaula(){
        int fet = 0;
        fet = baseDeDades.delete(NOM_TAULA, null, null);
        Log.w(ETIQUETA, Integer.toString(fet));
        return fet > 0;
    }

    public boolean esborraRegistesUsuari(String usuari){
        String [] usuaris = usuari.split(", ?");
        int done = 0;
        done = baseDeDades.delete(NOM_TAULA, "usuari=?", usuaris);
        Log.w(ETIQUETA, Integer.toString(done));
        return done > 0;
    }

    public Cursor classificació(String usuari){
        String[] camps = new String[] {"rowid as _id", "data", "mt_data", "usuari", "puntuació", "temps", "cad_temps"};
        Cursor cursor = baseDeDades.query(
                NOM_TAULA,
                camps,
                "usuari like ?",
                new String[] {String.format("%%%s%%", usuari)},
                null,
                null,
                "puntuació DESC, temps ASC",
                "10"
        );
        if (cursor != null) cursor.moveToFirst();
        return cursor;
    }

    public Cursor classificació(){
        String[] camps = new String[] {"rowid as _id", "data", "mt_data", "usuari", "puntuació", "temps", "cad_temps"};
        Cursor cursor = baseDeDades.query(
                NOM_TAULA,
                camps,
                null,
                null,
                null,
                null,
                "puntuació DESC, temps ASC",
                "10"
        );
        if (cursor != null) cursor.moveToFirst();
        return cursor;
    }

    public Cursor mostraBaseDeDades() {
        String camps[] = new String[] {"rowid as _id", "data", "mt_data", "usuari", "puntuació", "temps", "cad_temps"};
        Cursor cursor = baseDeDades.query(
                NOM_TAULA,
                camps,
                null,
                null,
                null,
                null,
                null
        );
        return cursor;
    }
}