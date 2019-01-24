package dk.michaelwestergaard.strikkehkleapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class Kortoplysninger extends AppCompatActivity implements View.OnClickListener {

    Button approveButton,backButton;

    Opskrift opskrift;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kortoplysninger);

        approveButton=findViewById(R.id.godkendKnap);
        backButton=findViewById(R.id.kortTilbageKnap);

        approveButton.setOnClickListener(this);
        backButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v==approveButton){
            Intent godkend = new Intent(this, Opskrift.class);
            godkend.putExtra("TEST","1");
            opskrift.bought = true;
            startActivity(godkend);
            finish();
        }
        else if(v==backButton){
            Intent tilbage = new Intent(this, OpskriftKoeb.class);
            startActivity(tilbage);
            finish();
        }
    }
}
