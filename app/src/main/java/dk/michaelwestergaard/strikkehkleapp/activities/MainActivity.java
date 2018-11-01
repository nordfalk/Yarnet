package dk.michaelwestergaard.strikkehkleapp.activities;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import dk.michaelwestergaard.strikkehkleapp.FavoritterFragment;
import dk.michaelwestergaard.strikkehkleapp.IndstillingerFragment;
import dk.michaelwestergaard.strikkehkleapp.OpretOpskriftFragment;
import dk.michaelwestergaard.strikkehkleapp.fragments.CreateRecipe;
import dk.michaelwestergaard.strikkehkleapp.fragments.MyCollection;
import dk.michaelwestergaard.strikkehkleapp.R;
import dk.michaelwestergaard.strikkehkleapp.ViewPagerAdapter;
import dk.michaelwestergaard.strikkehkleapp.VisKontoFragment;
import dk.michaelwestergaard.strikkehkleapp.fragments.DiscoverFragment;
import dk.michaelwestergaard.strikkehkleapp.fragments.DiscoverStartFragment;
import dk.michaelwestergaard.strikkehkleapp.fragments.ListFragment;

public class MainActivity extends AppCompatActivity implements ListFragment.OnFragmentInteractionListener, DiscoverStartFragment.OnFragmentInteractionListener,
        MyCollection.OnFragmentInteractionListener, CreateRecipe.OnFragmentInteractionListener {

    // NavigationView.OnNavigationItemSelectedListener,

    private DrawerLayout drawer;
    private ViewPager viewPager;
    BottomNavigationView bottomNavigationView;

    MenuItem prevMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       /* Toolbar toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();*/

        viewPager = findViewById(R.id.mainViewPager);
        viewPager.setOffscreenPageLimit(3);
        bottomNavigationView = findViewById(R.id.bottom_menu);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.discover:
                                viewPager.setCurrentItem(0);
                                return true;
                            case R.id.collection:
                                viewPager.setCurrentItem(1);
                                return true;
                            case R.id.create_recipe:
                                viewPager.setCurrentItem(2);
                                return true;
                        }
                        return false;
                    }
                });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                } else {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                prevMenuItem = bottomNavigationView.getMenu().getItem(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        setupViewPager(viewPager);
    }
    private void setupViewPager (ViewPager viewPager){
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new DiscoverFragment(), "Discover");
        adapter.addFragment(new MyCollection(), "Collection");
        adapter.addFragment(new CreateRecipe(), "Create");
        viewPager.setAdapter(adapter);


    }

    /* @Override
     public void onFragmentInteraction (Uri uri){

    } */

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }


    }

   /* @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_visKonto:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new VisKontoFragment()).commit();
                break;
            case R.id.nav_indstillinger:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new IndstillingerFragment()).commit();
                break;
            case R.id.nav_favoritter:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new FavoritterFragment()).commit();
                break;
            case R.id.nav_opretOpskrift:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new OpretOpskriftFragment()).commit();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }*/


    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
