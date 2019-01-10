package dk.michaelwestergaard.strikkehkleapp.activities;

import android.os.Bundle;
import android.view.View;

import dk.michaelwestergaard.strikkehkleapp.R;

public class Settings extends Drawer {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_settings);
        super.onCreate(savedInstanceState);

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
