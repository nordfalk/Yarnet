package dk.michaelwestergaard.strikkehkleapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        //https://developer.android.com/guide/topics/ui/controls/spinner brug dette link til at implementerer adapter til dropdown
    }
}
