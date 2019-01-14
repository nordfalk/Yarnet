package dk.michaelwestergaard.strikkehkleapp.activities;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;

import dk.michaelwestergaard.strikkehkleapp.R;

public class Favourites extends Drawer {

    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_favourites);
        super.onCreate(savedInstanceState);

        drawerBtn = findViewById(R.id.drawerBtn);
        drawerBtn.setOnClickListener(this);

        backBtn.setVisibility(View.VISIBLE);
    }
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.backButton:
                finish();
                break;

        }

      //  drawer.openDrawer(GravityCompat.END);

        }
    }
