package cat.memoriacastello.www.memoriahistorica;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
    //Atributs
    protected static String nomUsuari;
    protected static int edatUsuari;
      //Vector amb totes les preguntes.
    protected static final int maxPreguntes = 42;
    protected static DadesPregunta preguntes[] = new DadesPregunta[maxPreguntes];
      //Vector amb només 20 preg. del joc en curs.
    protected static final int maxPregPerPartida = 20;
    protected static DadesPregunta test[] = new DadesPregunta[maxPregPerPartida];
    protected static long horaInici;
    protected static long horaFi;
    protected static int contestades;
    protected static int benContestades[] = new int[maxPreguntes];
    protected static boolean reset = true;

    private EditText p1et1, p1et2;

    //Metodes
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Codi per a tancar l'aplicació (relacionada amb el botó 'IX DEL JOC'
        //de la funció surt() al fitxer Resultats.java).
        if (getIntent().getBooleanExtra("LOGOUT", false))
            finish();

        p1et1 = (EditText) findViewById(R.id.p1et1);
        p1et2 = (EditText) findViewById(R.id.p1et2);

        lligDades();

        mostraContingut("historial");

        String vector[] = lligFitxer("historial");
        for(String s : vector)
            if (s.startsWith("[u:")) {
                String usuari = s.substring(3, s.indexOf("\t"));
                String edat = s.substring(s.indexOf("\t")+3, s.indexOf("]"));
                p1et1.setText(usuari);
                p1et2.setText(edat);
                break;
            }
        if (!vector[0].startsWith("[begin:"))
            desaFitxer(
                    "historial",
                    String.format(
                            "[begin:%s]",
                            new SimpleDateFormat(
                                    "yyy/MM/dd HH:mm:ss"
                            ).format(new Date())
                    )
            );
    }

    private boolean existix(String f) {
        for (String fitxer : fileList())
            if (f.equals(fitxer))
                return true;
        return false;
    }

    public String[] lligFitxer(String f){
        String nomFitxer = String.format("%s.txt", f);
        String tot = "";
        if (existix(nomFitxer))
            try {
                InputStreamReader fitxer = new InputStreamReader(
                        openFileInput(nomFitxer)
                );
                BufferedReader br = new BufferedReader(fitxer);
                String línia;
                do {
                    línia = br.readLine();
                    tot += línia + "\n";
                } while (línia != null);
                br.close();
                fitxer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        return tot.split("\n");
    }

    public void mostraContingut(String f){
        String nomFitxer = String.format("%s.txt", f);
        String text = "";
        if (existix(nomFitxer))
            try {
                InputStreamReader fitxer = new InputStreamReader(
                        openFileInput(nomFitxer)
                );
                BufferedReader br = new BufferedReader(fitxer);
                String línia = br.readLine();
                while (línia != null) {
                    text += línia + "\n";
                    línia = br.readLine();
                }
                br.close();
                fitxer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        Intent i = new Intent(this, Notificacio.class);
        i.putExtra("text", text);
        startActivity(i);
        //Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    protected void desaFitxer(String f, String s) {
        String nomFitxer = String.format("%s.txt", f);
        try {
            OutputStreamWriter fitxer = new OutputStreamWriter(
                    openFileOutput(
                            nomFitxer, Activity.MODE_APPEND
                    )
            );
            fitxer.write(s+"\n");
            fitxer.flush();
            fitxer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void lligDades() {
        /*
        TODO: fer que després de llegir les dades canvie l'ordre tant de les preguntes com
        de les respostes. Estaria molt bé que n'hi haja més de 20 preguntes, jo en posaria
        40, encara que cada test només en mostraria 20. Quan es reiniciara el joc canviarien
        les 20 preguntes. L'únic que sabria les preguntes seria el profe.

        TODO: Estaria bé que el profe tinguera un accés especial per a conéixer la resposta a la
        pregunta.
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
                    preguntes[i++] = new DadesPregunta(
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
                preguntes[i++] = dp;
            }
            //msg = "S'ha llegit el fitxer amb èxit.";
        } catch (Exception e) {
            msg = "No s'ha pogut accedir al fitxer.";
        }
        if (msg.length()>0) Toast.makeText(this, msg, Toast.LENGTH_LONG).show();

    }

    public void inicia(View v){
        nomUsuari = p1et1.getText().toString();
        String s = p1et2.getText().toString();
        if (s.length() == 0) {
            String msg = String.format(
                    "El camp %sde l'edat no %s quedar buid.",
                    nomUsuari.length()==0 ?
                            "de l'usuari i el camp " :
                            "",
                    nomUsuari.length()==0 ? "poden" :
                            "pot"
            );
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        } else {

            edatUsuari = Integer.parseInt(s);
            boolean cond1 = nomUsuari.length() != 0;
            boolean cond2 = edatUsuari > 13 && edatUsuari < 19;

            if (!cond1 || !cond2){
                String msg = "";
                if (!cond1) msg = "El camp de l'usuari no pot quedar buid.";
                if (!cond2) {
                    if (msg.length()!=0) msg += "\n";
                    msg += "L'edat ha de ser entre 14 i 18 anys.";
                }
                Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
            } else {
                Intent i = new Intent(this, PaginaPrincipal.class);
                startActivity(i);
            }
        }
    }

    public void apropDe(View v) {
        startActivity(new Intent(this, ApropDe.class));
    }
}
