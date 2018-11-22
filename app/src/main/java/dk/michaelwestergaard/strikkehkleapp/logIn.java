package dk.michaelwestergaard.strikkehkleapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import dk.michaelwestergaard.strikkehkleapp.activities.MainActivity;

public class logIn extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth auth;

    private EditText inputEmail, inputPassword;

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

        Button loginBtn = findViewById(R.id.login_btn);
        loginBtn.setOnClickListener(this);

        Button signupBtn = findViewById(R.id.signup_btn);
        signupBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.login_btn:
                String email = inputEmail.getText().toString();
                String password = inputPassword.getText().toString();

                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(logIn.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            // tjek fejlen
                        } else {
                            Intent i = new Intent(logIn.this, MainActivity.class);
                            startActivity(i);
                            finish();
                        }
                    }
                });
                break;

            case R.id.signup_btn:
                Intent signupIntent = new Intent(this, signUp.class);
                startActivity(signupIntent);
                break;
            default:
                break;
        }
    }
}
