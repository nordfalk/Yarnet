package dk.michaelwestergaard.strikkehkleapp.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.UUID;
import java.util.regex.Pattern;

import dk.michaelwestergaard.strikkehkleapp.DAO.UserDAO;
import dk.michaelwestergaard.strikkehkleapp.DTO.UserDTO;
import dk.michaelwestergaard.strikkehkleapp.MainSingleton;
import dk.michaelwestergaard.strikkehkleapp.R;

public class EditPage extends AppCompatActivity implements View.OnClickListener {

    EditText name;
    EditText lastName;
    EditText mail;
    EditText code;
    EditText repeatCode;
    Button saveEdits;
    TextView imageButton;
    ImageView backBtn;
    ImageView drawerBtn, profileImage;

    UserDTO user;
    UserDAO userDAO = new UserDAO();
    Uri newImage;

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +
                    "(?=.*[a-z])" +
                    "(?=.*[A-Z])" +
                    "(?=.*[a-zA-Z])" +
                    "(?=\\S+$)" +
                    ".{8,16}" +
                    "$");

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

        imageButton = findViewById(R.id.billedeKnap);
        profileImage = findViewById(R.id.profile_image);
        name = findViewById(R.id.navn);
        lastName = findViewById(R.id.efternavn);
        mail = findViewById(R.id.mail);
        code = findViewById(R.id.kode);
        repeatCode = findViewById(R.id.gentagKode);
        saveEdits = findViewById(R.id.gemEdits);

        name.setText(user.getFirst_name());
        lastName.setText(user.getLast_name());
        mail.setText(user.getEmail());
        if(user.getAvatar().contains("https")) {
            RequestOptions requestOptions = new RequestOptions().transforms(new CenterCrop(), new RoundedCorners(50));
            Glide.with(EditPage.this).load(user.getAvatar()).apply(requestOptions).into(profileImage);
        }

        saveEdits.setOnClickListener(this);
        imageButton.setOnClickListener(this);

        backBtn.setVisibility(View.VISIBLE);
    }

    private boolean mailReq() {
        String emailInput = mail.getText().toString().trim();

        if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            mail.setError("E-mailadressen er ugyldig");
            return false;
        } else {
            mail.setError(null);
            return true;
        }
    }
    private boolean passReq() {
        String passInput = code.getText().toString().trim();

        if (!PASSWORD_PATTERN.matcher(passInput).matches()) {
            code.setError("Adgangskode lever ikke op til kravene. Prøv igen");
            return false;
        } else {
            code.setError(null);
            return true;
        }
    }

    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.backButton:
                finish();
                break;
        }

        if(view == saveEdits){

            if(!user.getEmail().equals(mail.getText().toString())){
                if(mailReq())
                    FirebaseAuth.getInstance().getCurrentUser().updateEmail(mail.getText().toString());
            }
            user.setFirst_name(name.getText().toString());
            user.setLast_name(lastName.getText().toString());
            user.setEmail(mail.getText().toString());
            if(code.getText().equals("") && repeatCode.getText().equals("")){
                if(code.getText().equals(repeatCode.getText())){
                    if(passReq())
                        FirebaseAuth.getInstance().getCurrentUser().updatePassword(code.getText().toString());
                }
            }
            userDAO.update(user);
            Toast.makeText(this,"Ændringerne blev gemt", Toast.LENGTH_LONG).show();
        } else if (view == imageButton) {
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this);
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {

                newImage = result.getUri();
                UUID picRandomID = UUID.randomUUID();

                final StorageReference ref = FirebaseStorage.getInstance().getReference().child("users/" + picRandomID);

                ref.putFile(newImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                user.setAvatar(uri.toString());
                                userDAO.update(user);
                                RequestOptions requestOptions = new RequestOptions().transforms(new CenterCrop(), new RoundedCorners(50));
                                Glide.with(EditPage.this).load(uri.toString()).apply(requestOptions).into(profileImage);
                            }
                        });
                    }
                });

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}
