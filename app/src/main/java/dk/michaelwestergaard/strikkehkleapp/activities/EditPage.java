package dk.michaelwestergaard.strikkehkleapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;

import dk.michaelwestergaard.strikkehkleapp.R;

public class EditPage extends Drawer {

    private DrawerLayout drawer;
    EditText navn, efternavn, mail, kode, gentagKode;
    Button gemEdits;
    TextView billedeKnap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_page);

        drawer = findViewById(R.id.drawer_layout);

        billedeKnap = findViewById(R.id.billedeKnap);
        navn = findViewById(R.id.navn);
        efternavn = findViewById(R.id.efternavn);
        mail = findViewById(R.id.mail);
        kode = findViewById(R.id.kode);
        gentagKode = findViewById(R.id.gentagKode);
        gemEdits = findViewById(R.id.gemEdits);


        gemEdits.setOnClickListener(this);
        billedeKnap.setOnClickListener(this);


        backBtn.setVisibility(View.VISIBLE);
    }
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.backButton:
                finish();
                break;
            case R.id.gemEdits:
                Toast.makeText(EditPage.this, "TEST1", Toast.LENGTH_LONG).show();
                break;
            case R.id.billedeKnap:
                Toast.makeText(EditPage.this, "TEST2", Toast.LENGTH_LONG).show();
                break;
        }
        //drawer.openDrawer(GravityCompat.END);

        /*if(view == gemEdits){
            Toast.makeText(EditPage.this, "TEST1", Toast.LENGTH_LONG).show();
        } else if (view == billedeKnap) {
            Toast.makeText(EditPage.this, "TEST2", Toast.LENGTH_LONG).show();
        }*/
    }
}