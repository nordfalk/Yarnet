package dk.michaelwestergaard.strikkehkleapp.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import dk.michaelwestergaard.strikkehkleapp.DAO.UserDAO;
import dk.michaelwestergaard.strikkehkleapp.DTO.UserDTO;
import dk.michaelwestergaard.strikkehkleapp.MainSingleton;
import dk.michaelwestergaard.strikkehkleapp.R;

public class LogInActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth auth;

    private TextView passWordReset;
    private EditText inputEmail, inputPassword;


    private AlertDialog.Builder builder, resetPasswordBuilder;
    private AlertDialog progressDialog, resetPasswordDialog;
    private CallbackManager mCallbackManager;

    UserDAO userDAO = new UserDAO();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(LogInActivity.this, MainActivity.class));
            finish();
        }

        passWordReset = findViewById(R.id.passwordReset);
        passWordReset.setOnClickListener(this);

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
            }
        });

        //Loading dialog
        builder = new AlertDialog.Builder(LogInActivity.this);
        builder.setCancelable(false);
        builder.setView(R.layout.loading_dialog);
        progressDialog = builder.create();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
                            boolean newUser = task.getResult().getAdditionalUserInfo().isNewUser();
                            if(newUser){
                                String[] name = user.getDisplayName().split(" ");
                                String avatar = user.getPhotoUrl().toString();
                                avatar = avatar + "?height=500";
                                UserDTO userDTO = new UserDTO(user.getUid(), user.getEmail(), name[0], name[1], avatar, "BOTH", 1, "ALL");
                                createUser(userDTO);
                            }
                            System.out.println("UserID: " + user.getUid());
                            userDAO.getReference().child(auth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    UserDTO user = dataSnapshot.getValue(UserDTO.class);
                                    MainSingleton.getInstance().setUser(user);

                                    startActivity(new Intent(LogInActivity.this, MainActivity.class));
                                    finish();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                }
                            });
                        } else {
                            Toast.makeText(LogInActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
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
                    auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LogInActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                // tjek fejlen
                                Toast.makeText(LogInActivity.this, "Kunne ikke logge ind, prøv igen", Toast.LENGTH_LONG).show();
                                progressDialog.dismiss();
                            } else {
                                userDAO.getReference().child(auth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        UserDTO user = dataSnapshot.getValue(UserDTO.class);
                                        MainSingleton.getInstance().setUser(user);

                                        startActivity(new Intent(LogInActivity.this, MainActivity.class));
                                        progressDialog.dismiss();
                                        finish();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                    }
                                });
                            }
                        }

                    });
                } else {
                    Toast.makeText(this, "Indtast venligst email og adgangskode", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }
                break;

            case R.id.signup_btn:
                Intent signupIntent = new Intent(this, SignUpActivity.class);
                startActivity(signupIntent);
                break;

            case R.id.passwordReset:
                resetPasswordBuilder = new AlertDialog.Builder(this);
                resetPasswordBuilder.setTitle("Glemt adgangskode?");

                View dialogView = this.getLayoutInflater().inflate(R.layout.reset_password_prompt, null);

                final EditText emailText = dialogView.findViewById(R.id.promptEdit);

                resetPasswordBuilder.setView(dialogView);

                resetPasswordBuilder.setPositiveButton("Nulstil", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String emailInput = emailText.getText().toString().trim();

                        boolean verified;
                        if (emailInput.isEmpty()) {
                            emailText.setError("Feltet må ikke være tomt");
                            verified = false;
                        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
                            emailText.setError("E-mailadressen er ugyldig");
                            verified = false;
                        } else {
                            emailText.setError(null);
                            verified = true;
                        }

                        if(verified) {
                            FirebaseAuth.getInstance().sendPasswordResetEmail(emailText.getText().toString());
                        }
                    }
                });
                resetPasswordBuilder.setNegativeButton("Annuller", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                resetPasswordDialog = resetPasswordBuilder.create();
                resetPasswordBuilder.show();
                break;

            default:
                break;
        }
    }

    private void createUser(UserDTO user){
        if(!userDAO.insert(user).matches("")){
            //YAY
            System.out.println("created");
        } else {
            //gg

            System.out.println("createdn't");
        }
    }

    private boolean isEmpty(EditText editText){
        return editText.getText().toString().equals("");
    }

}