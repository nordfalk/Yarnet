package dk.michaelwestergaard.strikkehkleapp.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import dk.michaelwestergaard.strikkehkleapp.DAO.UserDAO;
import dk.michaelwestergaard.strikkehkleapp.DTO.UserDTO;
import dk.michaelwestergaard.strikkehkleapp.MainSingleton;
import dk.michaelwestergaard.strikkehkleapp.R;

public class Settings extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    ImageView backBtn;
    ImageView drawerBtn;
    Spinner difficultySpinner, recipeTypeSpinner;
    UserDAO userDAO = new UserDAO();
    UserDTO userDTO = MainSingleton.getInstance().getUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        difficultySpinner = findViewById(R.id.difficultySpinner);
        recipeTypeSpinner = findViewById(R.id.recipeSpinner);

        ArrayAdapter<CharSequence> recAdapter = ArrayAdapter.createFromResource(this,
                R.array.RecipeTypes, android.R.layout.simple_spinner_item);

        ArrayAdapter<CharSequence> difAdapter = ArrayAdapter.createFromResource(this,
                R.array.NewRecipeDifficulty, android.R.layout.simple_spinner_item);

        recipeTypeSpinner.setAdapter(recAdapter);
        difficultySpinner.setAdapter(difAdapter);

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

        recipeTypeSpinner.setOnItemSelectedListener(this);
        difficultySpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        parent.getItemAtPosition(position);

        if (difficultySpinner.getSelectedItemPosition()==0){
            userDTO.setDifficulty("EASY");
        }else if(difficultySpinner.getSelectedItemPosition()==1){
            userDTO.setDifficulty("MEDIUM");
        }else if(difficultySpinner.getSelectedItemPosition()==2) {
            userDTO.setDifficulty("HARD");
        }

        if (recipeTypeSpinner.getSelectedItemPosition()==0) {
            userDTO.setType("BOTH");
        } else if (recipeTypeSpinner.getSelectedItemPosition()==1) {
            userDTO.setType("CROCHETING");
        } else if (recipeTypeSpinner.getSelectedItemPosition()==2) {
            userDTO.setType("KNITTING");
        }
        userDAO.update(userDTO);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
