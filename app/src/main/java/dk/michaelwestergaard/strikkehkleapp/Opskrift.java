package dk.michaelwestergaard.strikkehkleapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import dk.michaelwestergaard.strikkehkleapp.DAO.RecipeDAO;
import dk.michaelwestergaard.strikkehkleapp.DAO.UserDAO;
import dk.michaelwestergaard.strikkehkleapp.DTO.RecipeDTO;
import dk.michaelwestergaard.strikkehkleapp.DTO.UserDTO;


public class Opskrift extends AppCompatActivity implements View.OnClickListener {

    private RecipeDTO recipe = null;
    private RecipeDAO recipeDAO = new RecipeDAO();
    private String recipeID = "";

    private UserDTO user;
    private UserDAO userDAO = new UserDAO();

    private TextView title, creator;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Button købKnap;
    public boolean bought;
    ImageView backgroundPicture;
    CardView købContainer;
    int price = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opskrift);

        recipeID = getIntent().getStringExtra("RecipeID");

        String test = getIntent().getStringExtra("TEST");

        title = findViewById(R.id.recipe_title);
        creator = findViewById(R.id.recipe_creator);

        backgroundPicture = findViewById(R.id.baggrundsBillede);

        købContainer = findViewById(R.id.købContainer);

        tabLayout = findViewById(R.id.indholdvinduer);
        viewPager = findViewById(R.id.indholdscontainer);
        købKnap = findViewById(R.id.købKnap);

        købKnap.setOnClickListener(this);

        if(test == "1"){
            bought = true;
        }

        showRecipe();

    }

    public void showRecipe(){
        if(bought){
            købContainer.setVisibility(View.GONE);
            insertRecipe();
        }else{
            if(price == 0){
                købContainer.setVisibility(View.GONE);
                insertRecipe();
            }
        }
    }

    private void insertRecipe(){


        recipeDAO.getReference().child(recipeID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                System.out.println("Recipe found! " + dataSnapshot.getKey());
                recipe = dataSnapshot.getValue(RecipeDTO.class);
                title.setText(recipe.getTitle());

                userDAO.getReference().child(recipe.getUserID()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        System.out.println("User found! " + dataSnapshot.getKey());
                        user = dataSnapshot.getValue(UserDTO.class);

                        creator.setText(user.getFirst_name() + " " + user.getLast_name());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                setupViewPager(viewPager);
                tabLayout.setupWithViewPager(viewPager);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        RecipeViewPagerAdapter adapter = new RecipeViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new fragment_recipe_information().newInstance(recipe.getRecipeInformationDTO()), "Information");
        adapter.addFragment(new fragment_recipe_instruction().newInstance(recipe.getRecipeInstructionDTO()), "Vejledning");
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

