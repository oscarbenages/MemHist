package cat.memoriacastello.www.memoriahistorica;

/* view 4 */

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Resultats extends AppCompatActivity {
    //Atributs
    Fitxer f = new Fitxer(this);
    private TextView p4tv1;

    //Mètodes
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultats);

        p4tv1 = (TextView) findViewById(R.id.p4tv1);
    }

    @Override
    protected void onStart(){
        super.onStart();

        if (MainActivity.contestades == MainActivity.MAX_PREG_PER_PARTIDA && MainActivity.horaFi == 0)
            MainActivity.horaFi = java.util.Calendar.getInstance().getTimeInMillis();
        long tempsFinal = MainActivity.horaFi == 0 ?
                java.util.Calendar.getInstance().getTimeInMillis() :
                MainActivity.horaFi;
        String temps = obtéDifTemps(tempsFinal, MainActivity.horaInici);
        p4tv1.setText(
                String.format("Heu obtingut una puntuació de %d punts\nen %s", MainActivity.puntuació, temps)
        );
    }

    public void seguix(View v){
        finish();
    }

    public void nova(View v){
        /*
        S'ha de reiniciar:
        - l'hora d'inici (camp horaInici del MainActivity).
        - el nombre de preguntes contestades (camp contestades del MainActivity).

        S'ha de modificar la variable reset que indica si estem reiniciat.
         */

        MainActivity.horaInici = java.util.Calendar.getInstance().getTimeInMillis();
        MainActivity.contestades = 0;
        MainActivity.reset = true;
        String ara = new SimpleDateFormat("yyy/MM/dd HH:mm:ss").format(new Date());

        f.desaFitxer(
                "historial",
                String.format(
                        "[end:%1$s]\n[begin:%1$s]",
                        ara
                )
        );
        finish();
    }

    public void surt(View v) throws ParseException {

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("surt", true);

        f.desaFitxer(
                "historial",
                String.format(
                        "[end:%s]",
                        new SimpleDateFormat(
                                "yyy/MM/dd HH:mm:ss"
                        ).format(
                                new Date()
                        )
                )
        );

        String usuari = MainActivity.nomUsuari;
        Calendar c = new GregorianCalendar();
        int dia = c.get(Calendar.DAY_OF_MONTH);
        int mes = c.get(Calendar.MONTH);
        int any = c.get(Calendar.YEAR);
        int hora = c.get(Calendar.HOUR_OF_DAY);
        int minut = c.get(Calendar.MINUTE);
        int segon = c.get(Calendar.SECOND);
        long marcaTemps = (long) (any * Math.pow(10,10) + mes * Math.pow(10,8) + dia * Math.pow(10,6)
                + hora * Math.pow(10,4) + minut * Math.pow(10,2) + segon);


        int puntuació = MainActivity.puntuació;
        long temps = java.util.Calendar.getInstance().getTimeInMillis() - MainActivity.horaInici;


        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,
                "BDqualificacions", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        ContentValues registro = new ContentValues();
        registro.put("data", marcaTemps);
        registro.put("usuari", usuari);
        registro.put("puntuació", puntuació);
        registro.put("temps", temps);
        bd.insert("qualificacions", null, registro);
        bd.close();

        startActivity(intent);
        Frases.acomiada(this);
    }

    public void reinicia(View v){
        MainActivity.horaInici = java.util.Calendar.getInstance().getTimeInMillis();
        MainActivity.contestades = 0;
        for (int i =0; i < MainActivity.partida.length; i++) {
            if (MainActivity.partida[i] != null)
                MainActivity.partida[i].neteja();
            MainActivity.partida[i] = null;
        }
        MainActivity.reset = true;
        MainActivity.benContestades.clear();
        MainActivity.puntuació = 0;
        f.esborraFitxer("historial");
        String ara = new SimpleDateFormat("yyy/MM/dd HH:mm:ss").format(new Date());

        f.desaFitxer(
                "historial",
                String.format(
                        "[begin:%s]\n[u:%s]",
                        ara,
                        MainActivity.nomUsuari
                )
        );
        finish();
    }

    protected String concatena(String vector[]) {
        /*
         *  Este mètode permet concatenar els elements d'una llista amb la coma
         *  de separador i la conjunció entre el darrer i el penúltim element si s'escau.
         */
        StringBuilder sb = new StringBuilder();
        int i = 0, e = 0; //'i' es l'index dels vectors i 'e' és el nombre d'elements no nuls.

        for (String s: vector) {
            if (s == null) {
                i++;
                continue;
            }
            e++;

            if (i + 1 == vector.length && e > 1)
                sb.append(" i ");

            sb.append(s);
            i++;
            if (i < vector.length-1)
                sb.append(", ");
            else if (i == vector.length)
                sb.append(".");
        }

        return sb.toString();
    }

    protected String obtéDifTemps(long t1, long t2){
        long diff = t1 - t2;
        long vector[] = {
                diff / (60 * 60 * 1000) % 24,
                diff / (60 * 1000) % 60,
                diff / 1000 % 60
        };
        String unitats_sg[] = {"hora", "minut", "segon"};
        String unitats_pl[] = {"hores", "minuts", "segons"};
        String resultat[] = {null, null, null};
        for (int i = 0; i < vector.length; i++)
            if (vector[i]>0) resultat[i] = String.format("%d %s", vector[i], vector[i] != 1 ? unitats_pl[i] : unitats_sg[i]);
        return concatena(resultat);
    }

    protected String obtéDifTemps(long t1, long t2, boolean abreujat){
        long diff = t1 - t2;
        long vector[] = {
                diff / (60 * 60 * 1000) % 24,
                diff / (60 * 1000) % 60,
                diff / 1000 % 60
        };
        String unitats_sg[] = {"hora", "minut", "segon"};
        String unitats_pl[] = {"hores", "minuts", "segons"};
        String unitats_abr[] = {"h", "m", "s"};
        String resultat[] = {null, null, null};
        for (int i = 0; i < vector.length; i++)
            if (vector[i]>0) resultat[i] = String.format("%d %s", vector[i], !abreujat ? vector[i] != 1 ? unitats_pl[i] : unitats_sg[i] : unitats_abr[i]);
        return concatena(resultat);
    }

    public void mostraBD(View v){
            AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,
                    "BDqualificacions", null, 1);
            SQLiteDatabase bd = admin.getWritableDatabase();
            Cursor fila = bd.rawQuery(
                    "select data,usuari,puntuació,temps from qualificacions where usuari='Oscar'", null);
            if (fila.moveToLast()) {
                String u = fila.getString(0);
                int p = fila.getInt(1);
                Toast.makeText(this, String.format("%s: %d", u, p), Toast.LENGTH_LONG).show();
            } else
                Toast.makeText(this, "No s'ha trobat cap coincidència",
                        Toast.LENGTH_SHORT).show();
            bd.close();
    }

    public void qualificacions (View v){
        startActivity( new Intent(this, Qualificacions.class));
    }
}

