package cat.memoriacastello.www.memoriahistorica;

/* view 2 */

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

public class PaginaPrincipal extends AppCompatActivity {
    //Atributs
    private MainActivity m = new MainActivity();
    private TextView p2tv2;
    private Button p2b1, p2b2, p2b3, p2b4, p2b5, p2b6, p2b7, p2b8, p2b9, p2b10,
            p2b11, p2b12, p2b13, p2b14, p2b15, p2b16, p2b17, p2b18, p2b19, p2b20;

    //Instància
    Fitxer f = new Fitxer(this);

    //Mètodes
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagina_principal);

        p2tv2 = (TextView) findViewById(R.id.p2tv2);

        p2b1 = (Button) findViewById(R.id.p2b1);
        p2b2 = (Button) findViewById(R.id.p2b2);
        p2b3 = (Button) findViewById(R.id.p2b3);
        p2b4 = (Button) findViewById(R.id.p2b4);
        p2b5 = (Button) findViewById(R.id.p2b5);
        p2b6 = (Button) findViewById(R.id.p2b6);
        p2b7 = (Button) findViewById(R.id.p2b7);
        p2b8 = (Button) findViewById(R.id.p2b8);
        p2b9 = (Button) findViewById(R.id.p2b9);
        p2b10 = (Button) findViewById(R.id.p2b10);
        p2b11 = (Button) findViewById(R.id.p2b11);
        p2b12 = (Button) findViewById(R.id.p2b12);
        p2b13 = (Button) findViewById(R.id.p2b13);
        p2b14 = (Button) findViewById(R.id.p2b14);
        p2b15 = (Button) findViewById(R.id.p2b15);
        p2b16 = (Button) findViewById(R.id.p2b16);
        p2b17 = (Button) findViewById(R.id.p2b17);
        p2b18 = (Button) findViewById(R.id.p2b18);
        p2b19 = (Button) findViewById(R.id.p2b19);
        p2b20 = (Button) findViewById(R.id.p2b20);

        MainActivity.horaInici = Calendar.getInstance().getTimeInMillis();
        if (MainActivity.contestades == 0) Frases.saluda(this);
        else if (MainActivity.contestades != MainActivity.MAX_PREG_PER_PARTIDA) Frases.continua(this);
        else Frases.finalitza(this);

        f.afegixUsuari();
    }

    @Override
    protected void onStart(){
        super.onStart();

        //Omple el vector de preguntes de forma aleatòria quan comença el joc
        // i en les successives vegades que es prem el botó "REINICIA EL JOC".
        if (MainActivity.reset == true) {
            omplePartida();
            MainActivity.reset = false;
        }

        //Anem a assignar un color a cada botó de pregunta contestada.
        Button botons[] = {
                p2b1, p2b2, p2b3, p2b4, p2b5, p2b6, p2b7, p2b8, p2b9, p2b10,
                p2b11, p2b12, p2b13, p2b14, p2b15, p2b16, p2b17, p2b18, p2b19, p2b20
        };

        int suma = 0, i = 0;
        for(DadesPregunta pregunta : MainActivity.preguntesPartida) {
            if (pregunta == null) {
                botons[i++].setEnabled(false);
                continue;
            }
            int punt = pregunta.getEstat();
            int color = Color.WHITE;
            if (punt == -1) {
                color = Color.RED;

            } else if (punt == 0) {
                color = Color.LTGRAY;
            }
            else if (punt == 1) {
                suma += punt;
                color = Color.GREEN;
            }
            botons[i++].setBackgroundColor(color);
        }
        p2tv2.setText(String.format("puntuació: %d", MainActivity.puntuació));

        f.afegixUsuari();
    }

    private void omplePartida(){
        /*
        Este procediment ens permet omplir el vector de les preguntes que eixen
        al joc en curs. En total tenim 42 preguntes, però al joc només n'eixen
        20, així cada vegada l'usuari té la sensació de jugar a un nou joc, les
        preguntes no seran les mateixes i l'ordre en que apareixen serà diferent.
         */

        m.reinicialitzaPreguntesPartida();
        f.actualitzaBenContestades();
        reomplePreguntesPartida();
    }

    private void reomplePreguntesPartida(){

        Random random = new Random();
        Integer alea;
        int idx = 0;
        ArrayList<Integer> llista = new ArrayList<>();
        int forat = MainActivity.MAX_PREGUNTES - MainActivity.benContestades.size();
        while (idx < MainActivity.MAX_PREG_PER_PARTIDA && idx < forat) {
            alea = random.nextInt(MainActivity.MAX_PREGUNTES);
            if (!llista.contains(alea) && !MainActivity.benContestades.contains(alea)) {
                llista.add(alea);
                MainActivity.preguntesPartida[idx++] = new DadesPregunta(MainActivity.preguntesJoc[alea]);
            }
        }

    }

    public void obre_pregunta(View v, int idx){
        DadesPregunta pregunta = MainActivity.preguntesPartida[idx];
        if (pregunta.getEstat()==0) {
            Intent i = new Intent(this, Pregunta.class);
            i.putExtra("index", String.valueOf(idx));
            startActivity(i);
        } else {
            Frases.prohibeix(this);
        }
    }

    public void pregunta1(View v){
        obre_pregunta(v, 0);
    }

    public void pregunta2(View v){
        obre_pregunta(v, 1);
    }

    public void pregunta3(View v){
        obre_pregunta(v, 2);
    }

    public void pregunta4(View v){
        obre_pregunta(v, 3);
    }

    public void pregunta5(View v){
        obre_pregunta(v, 4);
    }

    public void pregunta6(View v){
        obre_pregunta(v, 5);
    }

    public void pregunta7(View v){
        obre_pregunta(v, 6);
    }

    public void pregunta8(View v){
        obre_pregunta(v, 7);
    }

    public void pregunta9(View v){
        obre_pregunta(v, 8);
    }

    public void pregunta10(View v){
        obre_pregunta(v, 9);
    }

    public void pregunta11(View v){
        obre_pregunta(v, 10);
    }

    public void pregunta12(View v){
        obre_pregunta(v, 11);
    }

    public void pregunta13(View v){
        obre_pregunta(v, 12);
    }

    public void pregunta14(View v){
        obre_pregunta(v, 13);
    }

    public void pregunta15(View v){
        obre_pregunta(v, 14);
    }

    public void pregunta16(View v){
        obre_pregunta(v, 15);
    }

    public void pregunta17(View v){
        obre_pregunta(v, 16);
    }

    public void pregunta18(View v){
        obre_pregunta(v, 17);
    }

    public void pregunta19(View v){
        obre_pregunta(v, 18);
    }

    public void pregunta20(View v){
        obre_pregunta(v, 19);
    }

    public void inici (View v){
        finish();
    }

    public void resultat(View v){
        startActivity(new Intent(this, Resultats.class));
    }

    public void mostraContingut(View v){
        //TODO: depuració
        f.mostraContingut("historial");
    }

    public void mostraVars(View v){
        //TODO: depuració
        String s = MainActivity.benContestades.toString();
        s += "\n\n preguntesPartida: [" ;
        for(DadesPregunta p : MainActivity.preguntesPartida)
            if(p != null)
                s += " " +String.valueOf(p.getId()-1);
        s +="]";
        f.mostraMissatge(s);
    }
}
