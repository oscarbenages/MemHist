package cat.memoriacastello.www.memoriahistorica;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class Notificacio extends AppCompatActivity {
    private TextView p8tv1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificacio);

        p8tv1 = (TextView) findViewById(R.id.p8tv1);

        ompleText();
    }

    private void ompleText(){
        Bundle b = getIntent().getExtras();
        String text = b.getString("text");
        p8tv1.setText(text);
    }

    public void arrere(View v){
        finish();
    }
}
