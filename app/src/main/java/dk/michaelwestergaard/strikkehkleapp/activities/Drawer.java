package dk.michaelwestergaard.strikkehkleapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import dk.michaelwestergaard.strikkehkleapp.DAO.RecipeDAO;
import dk.michaelwestergaard.strikkehkleapp.DAO.UserDAO;
import dk.michaelwestergaard.strikkehkleapp.DTO.RecipeDTO;
import dk.michaelwestergaard.strikkehkleapp.DTO.UserDTO;
import dk.michaelwestergaard.strikkehkleapp.R;

public class Drawer extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;

    private DrawerLayout drawer;
    protected ImageButton drawerBtn;
    private Toolbar toolbar;
    private ImageButton backBtn;
    private TextView editProfileBtn2;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        setContentView(R.layout.drawer);
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    Intent intent = new Intent(Drawer.this, LogInActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        };
        auth.addAuthStateListener(authListener);


        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        drawerBtn = findViewById(R.id.drawerBtn);
        drawerBtn.setOnClickListener(this);

        backBtn = findViewById(R.id.backButton);
        backBtn.setOnClickListener(this);


        editProfileBtn2 = (TextView) headerView.findViewById(R.id.editProfileBtn2);
        editProfileBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Drawer.this, EditPage.class);
                startActivity(intent);
            }
        });

    }

    public boolean onNavigationItemSelected (MenuItem item){
        switch (item.getItemId()) {
            case R.id.nav_visKonto:
                Intent intent1 = new Intent(this, Profile.class);
                startActivity(intent1);
                break;

            case R.id.nav_favoritter:
                Intent intent2 = new Intent(this, Favourites.class);
                startActivity(intent2);
                break;

            case R.id.nav_indstillinger:
                Intent intent3 = new Intent(this, Settings.class);
                startActivity(intent3);
                break;

            case R.id.nav_logout:
                auth.signOut();
                LoginManager.getInstance().logOut();
                break;

     /*   case R.id.nav_favoritter:
            Intent intent3 = new Intent(this, IndstillingerFragment.class);
            startActivity(intent3);
            //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
            //        new FavoritterFragment()).commit();
            break;
            */
        }
        drawer.closeDrawer(GravityCompat.END);
        return true;
    }

    public void onClick (View view){

        drawer.openDrawer(GravityCompat.END);

    }
}
