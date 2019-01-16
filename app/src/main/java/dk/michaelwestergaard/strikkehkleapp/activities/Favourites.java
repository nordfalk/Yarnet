package dk.michaelwestergaard.strikkehkleapp.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import dk.michaelwestergaard.strikkehkleapp.R;

public class Favourites extends AppCompatActivity {

    ImageView backBtn;
    ImageView drawerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);
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
    }
}
