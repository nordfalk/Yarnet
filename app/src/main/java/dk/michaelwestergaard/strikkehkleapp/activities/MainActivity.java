package dk.michaelwestergaard.strikkehkleapp.activities;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import dk.michaelwestergaard.strikkehkleapp.fragments.CreateRecipe;
import dk.michaelwestergaard.strikkehkleapp.fragments.MyCollection;
import dk.michaelwestergaard.strikkehkleapp.R;
import dk.michaelwestergaard.strikkehkleapp.ViewPagerAdapter;
import dk.michaelwestergaard.strikkehkleapp.fragments.DiscoverFragment;
import dk.michaelwestergaard.strikkehkleapp.fragments.DiscoverStartFragment;
import dk.michaelwestergaard.strikkehkleapp.fragments.ListFragment;

public class MainActivity extends AppCompatActivity implements ListFragment.OnFragmentInteractionListener, DiscoverStartFragment.OnFragmentInteractionListener, MyCollection.OnFragmentInteractionListener, CreateRecipe.OnFragmentInteractionListener {

    private ViewPager viewPager;
    BottomNavigationView bottomNavigationView;

    MenuItem prevMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.mainViewPager);
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
                }
                else
                {
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

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new DiscoverFragment(), "Discover");
        adapter.addFragment(new MyCollection(), "Collection");
        adapter.addFragment(new CreateRecipe(), "Create");
        viewPager.setAdapter(adapter);


    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
