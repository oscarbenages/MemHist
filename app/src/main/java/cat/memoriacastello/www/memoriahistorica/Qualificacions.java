package cat.memoriacastello.www.memoriahistorica;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.content.CursorLoader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

/* view 9 */

public class Qualificacions extends AppCompatActivity {
    ListView p9lv1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qualificacions);

        String[] camps = new String[] {"data", "usuari", "puntuació", "temps"};
        String[] args = new String[] {"Oscar"};
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,
                "BDqualificacions", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();

        Cursor c = bd.query("qualificacions", camps, "usuari=?", args, null, null, null);

        //Nos aseguramos de que existe al menos un registro
        if (c.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            do {
                long data = c.getLong(0);
                String usuari = c.getString(1);
                int puntuació = c.getInt(2);
                int temps = c.getInt(3);
            } while(c.moveToNext());
        }

        int ids[] = new int[]{R.id.p9lv1};

        p9lv1 = (ListView) findViewById(R.id.p9lv1);
        SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(
                this,
                R.layout.activity_qualificacions,
                c,
                camps,
                ids,
                0
        );
        setListAdapter(cursorAdapter);
    }

}
