package dk.michaelwestergaard.strikkehkleapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import dk.michaelwestergaard.strikkehkleapp.DTO.UserDTO;
import dk.michaelwestergaard.strikkehkleapp.MainSingleton;
import dk.michaelwestergaard.strikkehkleapp.R;

public class Profile extends AppCompatActivity implements View.OnClickListener {

    Button logout;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;

    ImageView avatar;
    TextView name;
    Button btn;
    ImageView backBtn;
    ImageView drawerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

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

        btn = findViewById(R.id.editProfileBtn);
        btn.setOnClickListener(this);


        auth = FirebaseAuth.getInstance();

        avatar = findViewById(R.id.avatar);
        name = findViewById(R.id.profileName);

        UserDTO user = MainSingleton.getInstance().getUser();
        name.setText(user.getFirst_name() + " " + user.getLast_name());
        if(user.getAvatar().contains("https")) {
            RequestOptions requestOptions = new RequestOptions().transforms(new CenterCrop(), new RoundedCorners(50));
            Glide.with(Profile.this).load(user.getAvatar()).apply(requestOptions).into(avatar);
        }

        logout = findViewById(R.id.logout);
        logout.setOnClickListener(this);

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    Intent intent = new Intent(Profile.this, LogInActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
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
