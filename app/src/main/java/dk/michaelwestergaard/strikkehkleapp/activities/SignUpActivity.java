package dk.michaelwestergaard.strikkehkleapp.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.UUID;
import java.util.regex.Pattern;

import dk.michaelwestergaard.strikkehkleapp.DAO.UserDAO;
import dk.michaelwestergaard.strikkehkleapp.DTO.UserDTO;
import dk.michaelwestergaard.strikkehkleapp.R;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener, OnCompleteListener<AuthResult> {

    private FirebaseAuth auth;


    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^"         +
                    "(?=.*[0-9])"       +
                    "(?=.*[a-z])"       +
                    "(?=.*[A-Z])"       +
                    "(?=.*[a-zA-Z])"    +
                    "(?=\\S+$)"         +
                    ".{8,16}"           +
                    "$");


    private EditText            inputFirstName, inputLastName, inputEmail, inputPassword, inputPasswordAgain;
    private Button              btnSignup, addProfilePic, delImage;
    private StorageReference    storageReference;
    private FirebaseStorage     storage;
    private ImageView           pictureholder;
    private RadioGroup          radioGroup;
    private Uri                 selImage;


    AlertDialog.Builder         builder;
    AlertDialog                 progressDialog;

    UserDAO                     userDAO = new UserDAO();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        auth = FirebaseAuth.getInstance();

        inputFirstName      = findViewById(R.id.input_first_name);
        inputLastName       = findViewById(R.id.input_first_last);
        inputEmail          = findViewById(R.id.input_email);
        inputPassword       = findViewById(R.id.input_password);
        inputPasswordAgain  = findViewById(R.id.input_password_again);
        addProfilePic       = findViewById(R.id.addProfilePic);
        pictureholder       = findViewById(R.id.pic1);
        delImage            = findViewById(R.id.delImage);
        radioGroup          = findViewById(R.id.radioJegEr);
        btnSignup           = findViewById(R.id.btn_signup);

        //Loading dialog
        builder = new AlertDialog.Builder(SignUpActivity.this);
        builder.setCancelable(false);
        builder.setView(R.layout.loading_dialog);
        progressDialog = builder.create();


        pictureholder   .setVisibility(View.GONE);
        delImage        .setVisibility(View.GONE);


        addProfilePic   .setOnClickListener(this);
        delImage        .setOnClickListener(this);
        btnSignup       .setOnClickListener(this);

        storage          = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

    }

    @Override
    public void onClick(View view) {
        if(view == btnSignup) {
            progressDialog.show();
            validateInputs();
        } else if (view == addProfilePic) {
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this);

        } else if (view == delImage) {
            pictureholder.setImageURI(null);
            pictureholder.setVisibility(View.GONE);
            addProfilePic.setVisibility(View.VISIBLE);
            delImage.setVisibility(view.GONE);
        }
    }

    private boolean firstNameReq() {
        String firstNameInput = inputFirstName.getText().toString().trim();

        if (firstNameInput.isEmpty()) {
            inputFirstName.setError("Fletet må ikke være tomt");
            return false;
        } else {
            inputFirstName.setError(null);
            return true;
        }
    }

    private boolean lastNameReq() {
        String lastNameInput = inputLastName.getText().toString().trim();

        if (lastNameInput.isEmpty()) {
            inputLastName.setError("Fletet må ikke være tomt");
            return false;
        } else {
            inputLastName.setError(null);
            return true;
        }
    }

    private boolean mailReq() {
        String emailInput = inputEmail.getText().toString().trim();

        if (emailInput.isEmpty()) {
            inputEmail.setError("Fletet må ikke være tomt");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            inputEmail.setError("E-mailadressen er ugyldig");
            return false;
        } else {
            inputEmail.setError(null);
            return true;
        }
    }

    private boolean passReq() {
        String passwordInput = inputPassword.getText().toString().trim();

        if (passwordInput.isEmpty()) {
            inputPassword.setError("Fletet må ikke være tomt");
            return false;
        } else if (!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
            inputPassword.setError("Adgangskode lever ikke op til kravene. Prøv igen");
            return false;
        } else {
            inputPassword.setError(null);
            return true;
        }
    }

    private boolean passReqTwo() {
        String passAgainInput = inputPasswordAgain.getText().toString().trim();

        if (passAgainInput.isEmpty()) {
            inputPasswordAgain.setError("Fletet må ikke være tomt");
            return false;
        } else {
            inputPasswordAgain.setError(null);
            return true;
        }
    }

    private boolean validateInputs(){

        if(passReqTwo() && passReq() && mailReq() && firstNameReq() && lastNameReq()){
            if(inputPassword.getText().toString().equals(inputPasswordAgain.getText().toString()) && passReq() && mailReq() && firstNameReq() && lastNameReq()){
                // TODO: Gem brugeroplysningerne sammen med det userid der bliver lavet
                auth.createUserWithEmailAndPassword(inputEmail.getText().toString(), inputPassword.getText().toString()).addOnCompleteListener(this);
            } else {
                passReq();
                mailReq();
                if (passReq() && mailReq()) {
                    inputPasswordAgain.setError("Koderne stemmer ikke overens");
                }
            }
        } else {
            firstNameReq();
            lastNameReq();
            passReq();
            passReqTwo();
            mailReq();
        }
        progressDialog.dismiss();
        return false;
    }


    @Override
    public void onComplete(@NonNull final Task<AuthResult> task) {

        progressDialog.dismiss();

        Toast.makeText(SignUpActivity.this, "Du er hermed oprettet som bruger!", Toast.LENGTH_SHORT).show();

        if (!task.isSuccessful()) {
            Toast.makeText(SignUpActivity.this, "Kunne ikke oprette, prøv igen.", Toast.LENGTH_SHORT).show();
        } else {

            String type = "";

            if (radioGroup.getCheckedRadioButtonId() == R.id.radioS) {
                type = "KNITTING";
            } else if (radioGroup.getCheckedRadioButtonId() == R.id.radioH) {
                type = "CROCHETING";
            } else if (radioGroup.getCheckedRadioButtonId() == R.id.radioBegge) {
                type = "BOTH";
            }

            if (selImage != null) {

                UUID picRandomID = UUID.randomUUID();

                StorageReference ref = storageReference.child("users/" + picRandomID);
                ref.putFile(selImage);

                UserDTO userDTO = new UserDTO(task.getResult().getUser().getUid(),
                        inputEmail.getText().toString(), inputFirstName.getText().toString(),
                        inputLastName.getText().toString(), picRandomID.toString(),
                        type, 1);
                userDAO.insert(userDTO);
                finish();

            } else {

                UserDTO userDTO = new UserDTO(task.getResult().getUser().getUid(),
                        inputEmail.getText().toString(), inputFirstName.getText().toString(),
                        inputLastName.getText().toString(), "defaultPic112233445566",
                        type, 1);
                userDAO.insert(userDTO);
                finish();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {

                selImage = result.getUri();
                pictureholder.setImageURI(selImage);

                addProfilePic.setVisibility(View.GONE);
                pictureholder.setVisibility(View.VISIBLE);
                delImage.setVisibility(View.VISIBLE);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}
