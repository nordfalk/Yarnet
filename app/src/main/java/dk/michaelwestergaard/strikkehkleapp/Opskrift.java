package dk.michaelwestergaard.strikkehkleapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;


public class Opskrift extends AppCompatActivity implements View.OnClickListener, fragment_recipe_information.OnFragmentInteractionListener, fragment_recipe_instruction.OnFragmentInteractionListener {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Button købKnap;
    public boolean bought;
    ImageView backgroundPicture;
    CardView købContainer;
    int price = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opskrift);

        String test = getIntent().getStringExtra("TEST");

        backgroundPicture = findViewById(R.id.baggrundsBillede);

        købContainer = findViewById(R.id.købContainer);

        tabLayout = findViewById(R.id.indholdvinduer);
        viewPager = findViewById(R.id.indholdscontainer);
        købKnap = (Button) findViewById(R.id.købKnap);

        købKnap.setOnClickListener(this);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        if(test == "1"){
            bought = true;
        }

        System.out.println(test);

        showRecipe();

    }

    public void showRecipe(){
        if(bought){
            købContainer.setVisibility(View.GONE);
        }else{
            if(price == 0){
                købContainer.setVisibility(View.GONE);
            }
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        RecipeViewPagerAdapter adapter = new RecipeViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new fragment_recipe_information(), "Information");
        adapter.addFragment(new fragment_recipe_instruction(), "Vejledning");
        viewPager.setAdapter(adapter);
    }



    @Override
    public void onClick(View v) {
        if(v==købKnap){
        Intent koeb = new Intent(this, OpskriftKoeb.class);
        startActivity(koeb);

     }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public void onFragmentInteraction(Uri uri) {

    }


    class RecipeViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public RecipeViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}

