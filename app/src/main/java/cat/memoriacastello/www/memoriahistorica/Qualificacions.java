package cat.memoriacastello.www.memoriahistorica;

import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.Toast;

/* view 7 */

public class Qualificacions extends AppCompatActivity {
    //Atributs
    private Fitxer f = new Fitxer(this);
    private Cadenes cad = new Cadenes();
    private Resultats res = new Resultats();
    private AdaptadorBD assistentBD;
    private SimpleCursorAdapter adaptadorDades;
    private ListView p7lv1;
    private EditText p7et1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qualificacions);
        assistentBD = new AdaptadorBD(this);
        assistentBD.obre();
        try {
            mostraLlista();
        } catch (Exception e) {
            StackTraceElement ste[] = e.getStackTrace();
            String s = "";
            for (StackTraceElement el : ste)
                s += String.format("\n\t\t%s", el);
            f.mostraMissatge(s);
        }

}

    private void mostraLlista() {
        Cursor cursor = assistentBD.classificació();
        String columnes[] = new String[]{
                AdaptadorBD.CLAU_DATA,
                AdaptadorBD.CLAU_USUARI,
                AdaptadorBD.CLAU_PUNTS,
                AdaptadorBD.CLAU_CAD_TEMPS
        };

        int columnesXML[] = new int[]{
                R.id.data,
                R.id.usuari,
                R.id.puntuació,
                R.id.cad_temps
        };

        adaptadorDades = new SimpleCursorAdapter(
                this,
                R.layout.activity_columnes,
                cursor,
                columnes,
                columnesXML,
                0
        );

        p7lv1 = (ListView) findViewById(R.id.p7lv1);
        p7lv1.setAdapter(adaptadorDades);

        p7lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Cursor cursor = (Cursor) p7lv1.getItemAtPosition(i);

                String msg = String.format(
                        "%s:\n\t%s\n\t%s\n\t%s",
                        cursor.getString(cursor.getColumnIndexOrThrow("usuari")),
                        cursor.getString(cursor.getColumnIndexOrThrow("data")),
                        cursor.getString(cursor.getColumnIndexOrThrow("puntuació")),
                        cursor.getString(cursor.getColumnIndexOrThrow("cad_temps"))
                );
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });

            p7et1 = (EditText) findViewById(R.id.p7et1);
            p7et1.addTextChangedListener(new TextWatcher() {
                public void afterTextChanged(Editable s) {

                }

                public void beforeTextChanged(CharSequence s, int start,
                                              int count, int after) {
                }

                public void onTextChanged(CharSequence s, int start,
                                          int before, int count) {
                    adaptadorDades.getFilter().filter(s.toString());
                }
            });

            adaptadorDades.setFilterQueryProvider(new FilterQueryProvider() {
                @Override
                public Cursor runQuery(CharSequence seq) {
                    return assistentBD.classificació(seq.toString());
                }
            });

    }

    private void mostraBaseDeDades(View v){

    }
}