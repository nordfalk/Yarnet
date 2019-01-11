package dk.michaelwestergaard.strikkehkleapp.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import dk.michaelwestergaard.strikkehkleapp.R;

public class EditPage extends Drawer {

    EditText navn;
    EditText efternavn;
    EditText mail;
    EditText kode;
    EditText gentagKode;
    Button gemEdits;
    TextView billedeKnap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_edit_page);
        super.onCreate(savedInstanceState);

        billedeKnap = findViewById(R.id.billedeKnap);
        navn = findViewById(R.id.navn);
        efternavn = findViewById(R.id.efternavn);
        mail = findViewById(R.id.mail);
        kode = findViewById(R.id.kode);
        gentagKode = findViewById(R.id.gentagKode);
        gemEdits = findViewById(R.id.gemEdits);

        gemEdits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "TEST2", Toast.LENGTH_LONG).show();
            }
        });

        billedeKnap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "TEST2", Toast.LENGTH_LONG).show();
            }
        });

        backBtn.setVisibility(View.VISIBLE);
    }
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.backButton:
                finish();
                break;
        }
    }
    }
