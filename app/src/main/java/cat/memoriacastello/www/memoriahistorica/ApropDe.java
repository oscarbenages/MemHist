package cat.memoriacastello.www.memoriahistorica;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ApropDe extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aprop_de);
    }

    public void inici (View v){
        finish();
    }

    public void professorat (View v) {
        startActivity(new Intent(this, Professor.class));
    }
}
