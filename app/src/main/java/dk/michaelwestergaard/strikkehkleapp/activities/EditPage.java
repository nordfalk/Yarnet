package dk.michaelwestergaard.strikkehkleapp.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;

import dk.michaelwestergaard.strikkehkleapp.DAO.UserDAO;
import dk.michaelwestergaard.strikkehkleapp.DTO.UserDTO;
import dk.michaelwestergaard.strikkehkleapp.MainSingleton;
import dk.michaelwestergaard.strikkehkleapp.R;

public class EditPage extends AppCompatActivity implements View.OnClickListener {

    EditText navn;
    EditText efternavn;
    EditText mail;
    EditText kode;
    EditText gentagKode;
    Button gemEdits;
    TextView billedeKnap;
    ImageView backBtn;
    ImageView drawerBtn, profileImage;

    UserDTO user;
    UserDAO userDAO = new UserDAO();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_page);

        user = MainSingleton.getInstance().getUser();

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

        billedeKnap = findViewById(R.id.billedeKnap);
        profileImage = findViewById(R.id.profile_image);
        navn = findViewById(R.id.navn);
        efternavn = findViewById(R.id.efternavn);
        mail = findViewById(R.id.mail);
        kode = findViewById(R.id.kode);
        gentagKode = findViewById(R.id.gentagKode);
        gemEdits = findViewById(R.id.gemEdits);

        navn.setText(user.getFirst_name());
        efternavn.setText(user.getLast_name());
        mail.setText(user.getEmail());
        if(user.getAvatar().contains("https")) {
            RequestOptions requestOptions = new RequestOptions().transforms(new CenterCrop(), new RoundedCorners(50));
            Glide.with(EditPage.this).load(user.getAvatar()).apply(requestOptions).into(profileImage);
        }

        gemEdits.setOnClickListener(this);
        billedeKnap.setOnClickListener(this);

        backBtn.setVisibility(View.VISIBLE);
    }
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.backButton:
                finish();
                break;
        }

        if(view == gemEdits){
            if(!user.getEmail().equals(mail.getText().toString())){
                FirebaseAuth.getInstance().getCurrentUser().updateEmail(mail.getText().toString());
            }
            user.setFirst_name(navn.getText().toString());
            user.setLast_name(efternavn.getText().toString());
            user.setEmail(mail.getText().toString());
            if(kode.getText().equals("") && gentagKode.getText().equals("")){
                if(kode.getText().equals(gentagKode.getText())){
                    FirebaseAuth.getInstance().getCurrentUser().updatePassword(kode.getText().toString());
                }
            }
            userDAO.update(user);
        } else if (view == billedeKnap) {
            Toast.makeText(getApplicationContext(), "TEST2", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
