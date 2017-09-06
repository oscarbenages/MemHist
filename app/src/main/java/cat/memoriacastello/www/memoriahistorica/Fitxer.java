package cat.memoriacastello.www.memoriahistorica;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Coet on 30/08/2017.
 */

public class Fitxer {
    private Context c;
    private static String historial[];

    public Fitxer(Context c){
        this.c = c;
    }

    private boolean existix(String f) {
        for (String fitxer : c.fileList())
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
                        c.openFileInput(nomFitxer)
                );
                BufferedReader br = new BufferedReader(fitxer);
                String línia = br.readLine();
                while (línia != null) {
                    tot += línia + "\n";
                    línia = br.readLine();
                }
                br.close();
                fitxer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        return tot.split("\n");
    }

    public void actualitzaHistorial(){
        historial = lligFitxer("historial");
    }

    protected void esborraHistorial(){
        esborraFitxer("historial");
    }

    public void mostraContingut(String f){
        String nomFitxer = String.format("%s.txt", f);
        String text = "";
        if (existix(nomFitxer))
            try {
                InputStreamReader fitxer = new InputStreamReader(
                        c.openFileInput(nomFitxer)
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
        Intent i = new Intent(c, Notificacio.class);
        i.putExtra("text", text);
        c.startActivity(i);
    }

    public void mostraMissatge(String text){
        /*
        Mètode útil per a mostrar missatges extensos
        que no resultarien fàcils de llegir amb un Toast.
        -- Molt apropiat en mode depuració ;) --

        } catch (RuntimeException e) {
            f.mostraMissatge("#1" + e.toString());
        } catch (Exception e) {
            StackTraceElement ste[] = e.getStackTrace();
            String s = "";
            for (StackTraceElement el : ste)
                s += String.format("\n\t\t%s", el);
            f.mostraMissatge(s);
        }

        */
        Intent i = new Intent(c, Notificacio.class);
        i.putExtra("text", text);
        c.startActivity(i);
    }

    protected void desaFitxer(String f, String s) {
        String nomFitxer = String.format("%s.txt", f);
        try {
            OutputStreamWriter fitxer = new OutputStreamWriter(
                    c.openFileOutput(
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

    protected void esborraFitxer(String f) {
        String nomFitxer = String.format("%s.txt", f);
        try {
            OutputStreamWriter fitxer = new OutputStreamWriter(
                    c.openFileOutput(
                            nomFitxer, Activity.MODE_PRIVATE
                    )
            );
            fitxer.write("");
            fitxer.flush();
            fitxer.close();
        } catch (IOException e) {
            StackTraceElement stack[] = e.getStackTrace();
            String s = "";
            for(StackTraceElement t : stack)
                s +=String.format("\n\t\t%s",t);
            mostraMissatge(s);

        }
    }

    protected String obtéDarrerUsuari(){
        actualitzaHistorial();
        String usuari = "";
        for(String línia : historial)
            if (línia.startsWith("[u:")) {
                String s = "\\[u:(\\w+)\\]";
                Pattern p = Pattern.compile(s);
                Matcher m = p.matcher(línia);
                while(m.find()){
                    usuari = m.group(1);
                    break;
                }
                //break; //Amb break 'habilitat', obtenim el primer usuari que troba a l'historial.
            }
        return usuari;
    }

    protected void afegixHoraInici() {
        if (!historial[historial.length - 1].startsWith("[begin:")) {
            desaFitxer(
                    "historial",
                    String.format(
                            "[begin:%s]",
                            new SimpleDateFormat(
                                    "yyy/MM/dd HH:mm:ss"
                            ).format(new Date())
                    )
            );
            actualitzaHistorial();
        }
    }

    protected void afegixHoraFi(Date ara){
        desaFitxer(
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
        actualitzaHistorial();
    }

    protected void preguntesNoves(){
        desaFitxer(
                "historial",
                String.format(
                        "[end:%1$s]\n[begin:%1$s]",
                        new SimpleDateFormat("yyy/MM/dd HH:mm:ss").format(new Date())
                )
        );
        actualitzaHistorial();
    }

    protected void afegixUsuari(){
        if (historial[historial.length-1].startsWith("[begin:")) {
            desaFitxer("historial", String.format("[u:%s]", MainActivity.nomUsuari));
            actualitzaHistorial();
        }
    }

    protected void afegixHoraIniciIUsuari(Date ara){
        desaFitxer(
                "historial",
                String.format(
                        "[begin:%s]\n[u:%s]",
                        new SimpleDateFormat("yyy/MM/dd HH:mm:ss").format(ara),
                        MainActivity.nomUsuari
                )
        );
        actualitzaHistorial();
    }

    protected void actualitzaBenContestades(){
        String usuari = "";
        for (String línia : historial){
            if (línia.startsWith("[u:")) {
                String s = "\\[u:(\\w+)\\]";
                Pattern p = Pattern.compile(s);
                Matcher m = p.matcher(línia);
                while (m.find()) {
                    usuari = m.group(1);
                    break;
                }
            }
            if (usuari.equals(MainActivity.nomUsuari) && línia.startsWith("[id:")){
                String s = "\\[id:(\\d+)\\te:(-?1)\\]";
                Pattern p = Pattern.compile(s);
                Matcher m = p.matcher(línia);
                int estat=0, id=0;
                while (m.find()){
                    estat = Integer.parseInt(m.group(2));
                    id = Integer.parseInt(m.group(1));
                }
                if (estat==1 && !MainActivity.benContestades.contains(id))
                    MainActivity.benContestades.add(id);
            }
        }
    }
}
