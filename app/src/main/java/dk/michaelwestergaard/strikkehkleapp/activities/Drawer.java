package dk.michaelwestergaard.strikkehkleapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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

import dk.michaelwestergaard.strikkehkleapp.R;

public class Drawer extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

        private DrawerLayout drawer;
        ImageButton drawerBtn;
        Toolbar toolbar;
        ImageButton backBtn;
        TextView editProfileBtn2;

        @Override
        protected void onCreate (Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            setContentView(R.layout.drawer);

            drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            NavigationView navigationView = findViewById(R.id.nav_view);
            navigationView.setItemIconTintList(null);
            navigationView.setNavigationItemSelectedListener(this);


            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                    R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();


            drawerBtn = findViewById(R.id.drawerBtn);
            drawerBtn.setOnClickListener(this);

            backBtn = findViewById(R.id.backButton);
            backBtn.setOnClickListener(this);

          //  editProfileBtn2 = findViewById(R.id.editProfileBtn2);
           // editProfileBtn2.setOnClickListener(this);

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

                case R.id.editProfileBtn2:
                    Intent intent4 = new Intent(this, EditPage.class);
                    startActivity(intent4);
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
