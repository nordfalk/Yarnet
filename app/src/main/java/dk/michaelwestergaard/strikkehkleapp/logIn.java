package dk.michaelwestergaard.strikkehkleapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;

import dk.michaelwestergaard.strikkehkleapp.activities.MainActivity;

public class logIn extends AppCompatActivity implements View.OnClickListener {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

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
                System.out.println("FB Error: " + error);
            }
        });

        //Loading dialog
        builder = new AlertDialog.Builder(logIn.this);
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
                            UserDTO userDTO = new UserDTO(user.getUid(), user.getEmail(), name[0], name[1], avatar, 1);
                            createUser(userDTO);
                        }
                        System.out.println("UserID: " + user.getUid());
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
                Intent i = new Intent(this, MainActivity.class);
                startActivity(i);
                break;
            default:
                break;
        }
    }
}
