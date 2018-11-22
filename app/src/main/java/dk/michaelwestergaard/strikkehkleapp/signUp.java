package dk.michaelwestergaard.strikkehkleapp;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class signUp extends AppCompatActivity implements View.OnClickListener, OnCompleteListener<AuthResult> {

    private FirebaseAuth auth;

    private EditText inputFirstName, inputLastName, inputEmail, inputPassword, inputPasswordAgain;
    private Button btnSignup;

    AlertDialog.Builder builder;
    AlertDialog progressDialog;

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

        btnSignup = findViewById(R.id.btn_signup);
        btnSignup.setOnClickListener(this);

        //Loading dialog
        builder = new AlertDialog.Builder(signUp.this);
        builder.setCancelable(false); // if you want user to wait for some process to finish,
        builder.setView(R.layout.loading_dialog);
        progressDialog = builder.create();
    }

    @Override
    public void onClick(View view) {
        if(view == btnSignup) {
            progressDialog.show();
            validateInputs();
        }
    }

    private boolean validateInputs(){

        if(!isEmpty(inputFirstName) && !isEmpty(inputLastName) && !isEmpty(inputEmail) && !isEmpty(inputPassword) && !isEmpty(inputPasswordAgain)){
            if(inputPassword.getText().toString().equals(inputPasswordAgain.getText().toString())){
                //All good?
                // TODO: Gem brugeroplysningerne sammen med det userid der bliver lavet
                auth.createUserWithEmailAndPassword(inputEmail.getText().toString(), inputPassword.getText().toString()).addOnCompleteListener(this);
            }
        }
        progressDialog.dismiss();
        return false;
    }

    private boolean isEmpty(EditText editText){
        return editText.getText().toString().equals("");
    }

    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {

        progressDialog.dismiss();

        Toast.makeText(signUp.this, "Du er hermed oprettet som bruger!", Toast.LENGTH_SHORT).show();

        if (!task.isSuccessful()) {
            Toast.makeText(signUp.this, "Kunne ikke oprette, prøv igen.", Toast.LENGTH_SHORT).show();
        } else {
            finish();
        }
    }
}
