package dk.michaelwestergaard.strikkehkleapp.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import dk.michaelwestergaard.strikkehkleapp.DAO.UserDAO;
import dk.michaelwestergaard.strikkehkleapp.DTO.UserDTO;
import dk.michaelwestergaard.strikkehkleapp.MainSingleton;
import dk.michaelwestergaard.strikkehkleapp.R;
import dk.michaelwestergaard.strikkehkleapp.ViewPagerAdapter;
import dk.michaelwestergaard.strikkehkleapp.fragments.CreateRecipe;
import dk.michaelwestergaard.strikkehkleapp.fragments.CreateRecipeStepOne;
import dk.michaelwestergaard.strikkehkleapp.fragments.DiscoverFragment;
import dk.michaelwestergaard.strikkehkleapp.fragments.ListFragment;
import dk.michaelwestergaard.strikkehkleapp.fragments.MyCollection;

public class MainActivity extends Drawer implements NavigationView.OnNavigationItemSelectedListener, ListFragment.OnFragmentInteractionListener, CreateRecipe.OnFragmentInteractionListener, CreateRecipeStepOne.OnFragmentInteractionListener {

    private ViewPager viewPager;
    BottomNavigationView bottomNavigationView;
    MenuItem prevMenuItem;
    DrawerLayout drawer;
    TextView editProfileBtn2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);

        drawer = findViewById(R.id.drawer_layout);
        editProfileBtn2 = headerView.findViewById(R.id.editProfileBtn2);
        editProfileBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EditPage.class);
                startActivity(intent);
            }
        });

        drawerBtn = findViewById(R.id.drawerBtn);
        drawerBtn.setOnClickListener(this);

        UserDAO userDAO = new UserDAO();
        userDAO.getReference().child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserDTO user = dataSnapshot.getValue(UserDTO.class);
                MainSingleton.getInstance().setUser(user);

                String userAvatar = MainSingleton.getInstance().getUser().getAvatar();
                ((TextView)findViewById(R.id.drawer_profile_name)).setText("Hej, " + user.getFirst_name());
                if(userAvatar.contains("https")) {
                    RequestOptions requestOptions = new RequestOptions().transforms(new CenterCrop(), new RoundedCorners(50));
                    Glide.with(MainActivity.this).load(userAvatar).apply(requestOptions).into(drawerBtn);
                    ImageView drawerHeaderImage = findViewById(R.id.drawer_image);
                    Glide.with(MainActivity.this).load(userAvatar).apply(requestOptions).into(drawerHeaderImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        viewPager = findViewById(R.id.mainViewPager);
        viewPager.setOffscreenPageLimit(10);
        viewPager.setVisibility(View.VISIBLE);
        bottomNavigationView = findViewById(R.id.bottom_menu);
        bottomNavigationView.setItemIconTintList(null);
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

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
    public void onClick (View view){

        drawer.openDrawer(GravityCompat.END);
    }
}
