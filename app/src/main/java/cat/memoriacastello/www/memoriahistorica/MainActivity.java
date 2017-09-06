package cat.memoriacastello.www.memoriahistorica;

/* view 1 */

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
    //Atributs
    private Fitxer f = new Fitxer(this);
    protected static String nomUsuari;
      //Vector amb totes les preguntes.
    protected static final int MAX_PREGUNTES = 42;
    protected static DadesPregunta preguntesJoc[] = new DadesPregunta[MAX_PREGUNTES];
      //Vector amb només 20 preg. del joc en curs.
    protected static final int MAX_PREG_PER_PARTIDA = 20;
    protected static DadesPregunta preguntesPartida[] = new DadesPregunta[MAX_PREG_PER_PARTIDA];
    protected static long horaInici;
    protected static long horaFi;
    protected static int contestades;
    protected static ArrayList<Integer> benContestades = new ArrayList<>();
    protected static int puntuació;
    protected static boolean reset = true;
    private EditText p1et1;
    private static final String ETIQ = "Main";

    //Metodes
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(ETIQ+"_onCreate", String.format(
                "usuari %s\npreguntesJoc %s\npreguntesPartida %s\nhoraInici %s\nhoraFi %s\n" +
                "contestades %s\nbenContestades %s\npuntuació %s\nreset %s",
                nomUsuari,
                preguntesJoc.length,
                preguntesPartida.length,
                horaInici,
                horaFi,
                contestades,
                benContestades.toString(),
                puntuació,
                reset
                )
        );

        setContentView(R.layout.activity_main);

        //Codi per a tancar l'aplicació (relacionada amb el botó 'IX DEL JOC'
        //de la funció surt() al fitxer Resultats.java).
        if (getIntent().getBooleanExtra("surt", false)){
            finish();
            return;
        }

        p1et1 = (EditText) findViewById(R.id.p1et1);

        lligDades();

        String darrerUsuari = f.obtéDarrerUsuari();
        p1et1.setText(darrerUsuari);

        f.afegixHoraInici();
    }

    private void lligDades() {
        /*
        TODO: fer que després de llegir les dades canvie l'ordre tant de les preguntes com
        de les respostes. Estaria molt bé que n'hi haja més de 20 preguntes, jo en posaria
        40, encara que cada preguntesPartida només en mostraria 20. Quan es reiniciara el joc canviarien
        les 20 preguntes. L'únic que sabria les preguntes seria el profe.

        DONE: Estaria bé que el profe tinguera un accés especial per a conéixer la resposta a la
        pregunta. -Fet: Vista professorat.-
        */

        String msg = "";
        Scanner fitxer;
        try {
            fitxer = new Scanner(getResources().openRawResource(R.raw.preguntes));
            String línia;
            int i = 0;
            while (fitxer.hasNextLine()) {
                línia = fitxer.nextLine();
                if (línia.equals("") || línia.charAt(0) == '#') continue;
                String vector[] = línia.split("\t");
                if (vector.length < 5 || vector.length > 6) {
                    //Hi ha hagut un error en la construcció de la pregunta
                    //falten o sobren tabulacions.
                    preguntesJoc[i++] = new DadesPregunta(
                            i+1,
                            String.format("#ERROR DE LECTURA ([#%d] %d tabs)", i, vector.length),
                            "#resp1",
                            "#resp2",
                            "#resp3",
                            1
                    );
                    continue;
                }
                String enunciat = vector[0];
                String resp1 = vector[1];
                String resp2 = vector[2];
                String resp3 = vector[3];
                int iRespCorr = Integer.parseInt(vector[4]);
                DadesPregunta dp;
                if (vector.length==5)
                    dp = new DadesPregunta(
                            i+1, enunciat, resp1, resp2, resp3, iRespCorr
                    );
                else {
                    String imatge = vector[5];
                    dp = new DadesPregunta(
                            i+1, enunciat, resp1, resp2, resp3, iRespCorr, imatge
                    );
                }
                preguntesJoc[i++] = dp;
            }
            Log.d(ETIQ+"_lligDades()", String.format(
                    "usuari %s\npreguntesJoc %s\npreguntesPartida %s\nhoraInici %s\nhoraFi %s\n" +
                            "contestades %s\nbenContestades %s\npuntuació %s\nreset %s",
                    nomUsuari,
                    preguntesJoc.length,
                    preguntesPartida.length,
                    horaInici,
                    horaFi,
                    contestades,
                    benContestades.toString(),
                    puntuació,
                    reset
                    )
            );

        } catch (Exception e) {
            msg = "No s'ha pogut accedir al fitxer.";
        }
        if (msg.length()>0) Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    protected void reinicialitzaPreguntesPartida(){
        for (int i = 0; i < preguntesPartida.length; i++) {
            if (preguntesPartida[i] != null) preguntesPartida[i].neteja();
            preguntesPartida[i] = null;
        }
        Log.d(ETIQ+"_reiniPregPart()", String.format(
                "usuari %s\npreguntesJoc %s\npreguntesPartida %s\nhoraInici %s\nhoraFi %s\n" +
                        "contestades %s\nbenContestades %s\npuntuació %s\nreset %s",
                nomUsuari,
                preguntesJoc.length,
                preguntesPartida.length,
                horaInici,
                horaFi,
                contestades,
                benContestades.toString(),
                puntuació,
                reset
                )
        );

    }

    public void inicia(View v){
        nomUsuari = p1et1.getText().toString();
        if (nomUsuari.length() == 0){
            String msg = "El camp de l'usuari no pot quedar buid.";
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        } else {
            Log.d(ETIQ+"_inicia", String.format(
                    "usuari %s\npreguntesJoc %s\npreguntesPartida %s\nhoraInici %s\nhoraFi %s\n" +
                            "contestades %s\nbenContestades %s\npuntuació %s\nreset %s",
                    nomUsuari,
                    preguntesJoc.length,
                    preguntesPartida.length,
                    horaInici,
                    horaFi,
                    contestades,
                    benContestades.toString(),
                    puntuació,
                    reset
                    )
            );

            startActivity(new Intent(this, PaginaPrincipal.class));
        }
    }

    public void apropDe(View v) {
        startActivity(new Intent(this, ApropDe.class));
    }

    public void qualificacions (View v){
        startActivity( new Intent(this, Qualificacions.class));
    }


}
