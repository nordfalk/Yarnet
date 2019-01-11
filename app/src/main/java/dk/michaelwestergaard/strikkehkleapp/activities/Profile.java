package dk.michaelwestergaard.strikkehkleapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import dk.michaelwestergaard.strikkehkleapp.R;

public class Profile extends Drawer implements View.OnClickListener {

    Button logout;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;

    ImageView avatar;
    TextView name;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        btn = findViewById(R.id.editProfileBtn);
        btn.setOnClickListener(this);


        auth = FirebaseAuth.getInstance();

        avatar = findViewById(R.id.avatar);
        name = findViewById(R.id.profileName);

/*
        Picasso.get().load(user.getAvatar()).transform(new RoundedCornersTransformation(50, 50)).into(avatar);
        name.setText(user.getFirst_name() + " " + user.getFirst_name());
*/
        logout = findViewById(R.id.logout);
        logout.setOnClickListener(this);

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    startActivity(new Intent(Profile.this, LogInActivity.class));
                    finish();
                }
            }
        };

        auth.addAuthStateListener(authListener);
    }

    @Override
    public void onClick(View view) {
        if(view == logout){
            auth.signOut();
            LoginManager.getInstance().logOut();
        } else if (view == btn) {
            Intent intent = new Intent(Profile.this, EditPage.class);
            startActivity(intent);
        }
    }
}
