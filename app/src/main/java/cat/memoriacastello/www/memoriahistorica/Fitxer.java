package cat.memoriacastello.www.memoriahistorica;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by Coet on 30/08/2017.
 */

public class Fitxer {
    Context c;

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
}
