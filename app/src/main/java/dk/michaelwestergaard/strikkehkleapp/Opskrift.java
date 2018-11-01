package dk.michaelwestergaard.strikkehkleapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class Opskrift extends AppCompatActivity implements View.OnClickListener {

    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opskrift);
        title = findViewById(R.id.recipe_title);

        title.setText(getIntent().getStringExtra("Title"));
    }

    @Override
    public void onClick(View v) {

    }
}
