package dk.michaelwestergaard.strikkehkleapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;

public class OpskriftKoeb extends AppCompatActivity implements View.OnClickListener {

    RadioGroup radioGroup;
    Button annulere,fortsæt;
    ShowRecipe showRecipe;
    ImageView backBtn;
    ImageView drawerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opskrift_koeb);

        drawerBtn = findViewById(R.id.drawerBtn);
        backBtn = findViewById(R.id.backButton);
        backBtn.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        } ));

        backBtn.setVisibility(View.VISIBLE);
        drawerBtn.setVisibility(View.GONE);

        radioGroup = findViewById(R.id.radioGroup);
        annulere = findViewById(R.id.AnnulereKnap);
        fortsæt = findViewById(R.id.FortsætKnap);

        annulere.setOnClickListener(this);
        fortsæt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v==annulere){
            Intent tilbage = new Intent(this, ShowRecipe.class);
            startActivity(tilbage);
            finish();
        }
        else if(v==fortsæt){
            Intent tilbage = new Intent(this, Kortoplysninger.class);
            startActivity(tilbage);
            finish();
        }
    }
}
