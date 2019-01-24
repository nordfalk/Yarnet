package dk.michaelwestergaard.strikkehkleapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class Kortoplysninger extends AppCompatActivity implements View.OnClickListener {

    Button godkendKnap,tilbageKnap;

    ShowRecipe showRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kortoplysninger);

        godkendKnap=findViewById(R.id.godkendKnap);
        tilbageKnap=findViewById(R.id.kortTilbageKnap);

        godkendKnap.setOnClickListener(this);
        tilbageKnap.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v==godkendKnap){
            Intent godkend = new Intent(this, ShowRecipe.class);
            godkend.putExtra("TEST","1");
            showRecipe.bought = true;
            startActivity(godkend);
            finish();
        }
        else if(v==tilbageKnap){
            Intent tilbage = new Intent(this, OpskriftKoeb.class);
            startActivity(tilbage);
            finish();
        }
    }
}
