package dk.michaelwestergaard.strikkehkleapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import dk.michaelwestergaard.strikkehkleapp.activities.MainActivity;

public class logIn extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth auth;

    private EditText inputEmail, inputPassword;

    AlertDialog.Builder builder;
    AlertDialog progressDialog;
    private CallbackManager mCallbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

        inputEmail = findViewById(R.id.input_email);
        inputPassword = findViewById(R.id.input_password);

        Button loginBtn = findViewById(R.id.login_btn);
        loginBtn.setOnClickListener(this);

        Button signupBtn = findViewById(R.id.signup_btn);
        signupBtn.setOnClickListener(this);

        mCallbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = findViewById(R.id.buttonFacebookLogin);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException error) {
                System.out.println(error);
                System.out.println("ererer");
            }
        });

        //Loading dialog
        builder = new AlertDialog.Builder(logIn.this);
        builder.setCancelable(false); // if you want user to wait for some process to finish,
        builder.setView(R.layout.loading_dialog);
        progressDialog = builder.create();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void handleFacebookAccessToken(AccessToken token) {
        //TODO: Hvis en email er oprettet og man logger ind med fb virker det ikke. Skal linkes under profil indstillinger eller noget
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    System.out.println(task.getException());
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        Intent i = new Intent(logIn.this, MainActivity.class);
                        startActivity(i);
                    } else {
                        Toast.makeText(logIn.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.login_btn:
                progressDialog.show();

                String email = inputEmail.getText().toString();
                String password = inputPassword.getText().toString();

                if(!isEmpty(inputEmail) && !isEmpty(inputPassword)) {
                    auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(logIn.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                // tjek fejlen
                            } else {
                                Intent i = new Intent(logIn.this, MainActivity.class);
                                startActivity(i);

                                progressDialog.dismiss();
                                finish();
                            }
                        }

                    });
                } else {
                    Toast.makeText(this, "Indtast venligst email og adgangskode", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }
                break;

            case R.id.signup_btn:
                Intent signupIntent = new Intent(this, signUp.class);
                startActivity(signupIntent);
                break;
            default:
                break;
        }
    }

    private boolean isEmpty(EditText editText){
        return editText.getText().toString().equals("");
    }
}
