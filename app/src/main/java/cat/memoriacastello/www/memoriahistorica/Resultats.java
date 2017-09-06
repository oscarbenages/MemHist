package cat.memoriacastello.www.memoriahistorica;

/* view 4 */

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Resultats extends AppCompatActivity {
    //Atributs
    Fitxer f = new Fitxer(this);
    Cadenes cad = new Cadenes();
    private TextView p4tv1;
    private long tempsFinal;

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
        tempsFinal = MainActivity.horaFi == 0 ?
                java.util.Calendar.getInstance().getTimeInMillis() :
                MainActivity.horaFi;
        String temps = cad.obtéDifTemps(tempsFinal, MainActivity.horaInici);
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

        //MainActivity.horaInici = java.util.Calendar.getInstance().getTimeInMillis();
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

    public void surt(View v)  {

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("surt", true);

        Date ara = new Date();
        f.desaFitxer(
                "historial",
                String.format(
                        "[end:%s]",
                        new SimpleDateFormat(
                                "yyy/MM/dd HH:mm:ss"
                        ).format(
                                ara
                        )
                )
        );

        String usuari = MainActivity.nomUsuari;
        long marcaTemporal = Long.valueOf(new SimpleDateFormat("yyyyMMddHHmmss").format(ara));
        int puntuació = MainActivity.puntuació;
        long temps = tempsFinal - MainActivity.horaInici;
        //Date data = new SimpleDateFormat("yyyyMMddHHmmss").parse(String.valueOf(marcaTemporal));
        AdaptadorBD baseDeDades = new AdaptadorBD(this);
        baseDeDades.obre();
        baseDeDades.nouRegistre(marcaTemporal, usuari, puntuació, temps);
        baseDeDades.tanca();
        startActivity(intent);
        Frases.acomiada(this);
    }

    public void reinicia(View v){
        //TODO: Necessitem que afegisca registres a la BdD!
        try {
            Date ara = new Date();
            String usuari = MainActivity.nomUsuari;
            long marcaTemporal = Long.valueOf(new SimpleDateFormat("yyyyMMddHHmmss").format(ara));
            int puntuació = MainActivity.puntuació;
            long temps = tempsFinal - MainActivity.horaInici;
            AdaptadorBD baseDeDades = new AdaptadorBD(this);
            baseDeDades.obre();
            baseDeDades.nouRegistre(marcaTemporal, usuari, puntuació, temps);
            baseDeDades.tanca();

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

            f.desaFitxer(
                    "historial",
                    String.format(
                            "[begin:%s]\n[u:%s]",
                            new SimpleDateFormat("yyy/MM/dd HH:mm:ss").format(ara),
                            MainActivity.nomUsuari
                    )
            );
        } catch (Exception e) {
            StackTraceElement ste[] = e.getStackTrace();
            String s = "";
            for (StackTraceElement el : ste)
                s += String.format("\n\t\t%s", el);
            f.mostraMissatge(s);
        }

        finish();
    }

    public void qualificacions (View v){
        startActivity( new Intent(this, Qualificacions.class));
    }

    public void mostraTemps (View v){
        Toast.makeText(this, String.valueOf(MainActivity.horaFi), Toast.LENGTH_LONG).show();
    }
}

