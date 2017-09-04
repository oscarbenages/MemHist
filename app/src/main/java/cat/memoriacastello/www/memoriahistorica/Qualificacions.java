package cat.memoriacastello.www.memoriahistorica;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.CursorLoader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.Calendar;
import java.util.Date;

/* view 9 */

public class Qualificacions extends AppCompatActivity {
    //Atributs
    Fitxer f = new Fitxer(this);
    ListView p9lv1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Resultats res = new Resultats();
        setContentView(R.layout.activity_qualificacions);

                String[] camps = new String[] {"data", "usuari", "puntuació", "temps"};
            String[] args = new String[] {"Oscar"};
            AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,
                    "BDqualificacions", null, 1);
            SQLiteDatabase bd = admin.getWritableDatabase();

            Cursor cursor = bd.query(
                    "qualificacions",
                    camps, "usuari=?",
                    args,
                    /*groupBy*/ null,
                    /*having*/ null,
                    /*orderBy*/ "puntuació DESC, temps ASC",
                    /*limit*/ null
            );

            //Nos aseguramos de que existe al menos un registro
            String s1 = "";

            if (cursor.moveToFirst()) {
                //Recorremos el cursor hasta que no haya más registros
                do {
                    long data = cursor.getLong(0);
                    String usuari = cursor.getString(1);
                    int puntuació = cursor.getInt(2);
                    int temps = cursor.getInt(3);
                    s1 += String.format(
                            "%s\t%s\t%d p.\t%s\n",
                            data,
                            usuari,
                            puntuació,
                            res.obtéDifTemps((long) temps, 0, true)
                    );
                } while(cursor.moveToNext());
            }
            //f.mostraMissatge(s1);
            int ids[] = new int[]{R.id.p10tv1, R.id.p10tv2, R.id.p10tv3, R.id.p10tv4};

            p9lv1 = (ListView) findViewById(R.id.p9lv1);

            SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(
                    this,
                    R.layout.activity_columnes,
                    cursor,
                    camps,
                    ids,
                    0
            );
            p9lv1.setAdapter(cursorAdapter);

    }

}
