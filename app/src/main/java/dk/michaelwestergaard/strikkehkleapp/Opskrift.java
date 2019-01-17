package dk.michaelwestergaard.strikkehkleapp;

import android.content.Intent;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import dk.michaelwestergaard.strikkehkleapp.DAO.CategoryDAO;
import dk.michaelwestergaard.strikkehkleapp.DAO.RecipeDAO;
import dk.michaelwestergaard.strikkehkleapp.DAO.UserDAO;
import dk.michaelwestergaard.strikkehkleapp.DTO.CategoryDTO;
import dk.michaelwestergaard.strikkehkleapp.DTO.RecipeDTO;
import dk.michaelwestergaard.strikkehkleapp.DTO.UserDTO;


public class Opskrift extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth auth= FirebaseAuth.getInstance();

    private RecipeDTO recipe = null;
    private RecipeDAO recipeDAO = new RecipeDAO();
    private String recipeID = "";

    private UserDTO userBrowsing;
    private UserDTO createdByUser;
    private UserDAO userDAO = new UserDAO();
    private CategoryDAO categoryDAO = new CategoryDAO();

    private TextView title, creator, categoriTextView, priceTextView, favoriteCount, stepsCount, difficulty;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Button købKnap;
    public boolean bought;
    ImageView backgroundPicture, favoriteBtn;
    CardView købContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opskrift);

        recipeID = getIntent().getStringExtra("RecipeID");

        title = findViewById(R.id.recipe_title);
        creator = findViewById(R.id.recipe_creator);

        backgroundPicture = findViewById(R.id.baggrundsBillede);
        favoriteBtn = findViewById(R.id.recipe_favorite_btn);

        købContainer = findViewById(R.id.købContainer);

        tabLayout = findViewById(R.id.indholdvinduer);
        viewPager = findViewById(R.id.indholdscontainer);
        købKnap = findViewById(R.id.købKnap);

        categoriTextView = findViewById(R.id.categoriTextView);
        priceTextView = findViewById(R.id.priceTextView);

        favoriteCount = findViewById(R.id.recipe_rating);
        stepsCount = findViewById(R.id.recipe_steps);
        difficulty = findViewById(R.id.recipe_difficulty);

        favoriteBtn.setOnClickListener(this);
        købKnap.setOnClickListener(this);

        showRecipe();
    }

    private void showRecipe(){
        recipeDAO.getReference().child(recipeID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                recipe = dataSnapshot.getValue(RecipeDTO.class);
                title.setText(recipe.getTitle());

                if(recipe.getRecipeInstructionDTO() != null)
                    stepsCount.setText(recipe.getRecipeInstructionDTO().size() + " trin");

                categoryDAO.getReference().child(recipe.getCategoryID()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        categoriTextView.setText("Kategori: " + dataSnapshot.getValue(CategoryDTO.class).getName());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                userDAO.getReference().child(recipe.getUserID()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        createdByUser = dataSnapshot.getValue(UserDTO.class);
                        creator.setText(createdByUser.getFirst_name() + " " + createdByUser.getLast_name());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                if(recipe.getPrice() == 0){
                    købContainer.setVisibility(View.GONE);
                    priceTextView.setVisibility(View.GONE);
                } else {
                    priceTextView.setText("Pris: " + recipe.getPrice() + " DKK");
                    købContainer.setVisibility(View.VISIBLE);
                }

                userDAO.getReference().child(auth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        userBrowsing = dataSnapshot.getValue(UserDTO.class);
                        if(userBrowsing.getSavedRecipes() != null){
                            if(userBrowsing.getSavedRecipes().contains(recipe.getRecipeID())){
                                favoriteBtn.setImageDrawable(getDrawable(R.drawable.ic_baseline_favorite));
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                favoriteCount.setText(String.valueOf(recipe.getSavedAmount()));

                String[] difficulties = getResources().getStringArray(R.array.NewRecipeDifficulty);

                String difficultyText = "";
                switch(recipe.getRecipeDifficulty()){
                    case EASY:
                        difficultyText = difficulties[0];
                        break;

                    case MEDIUM:
                        difficultyText = difficulties[1];
                        break;

                    case HARD:
                        difficultyText = difficulties[2];
                        break;

                    default:
                        difficultyText = "Ikke opgivet";
                        break;
                }

                difficulty.setText(difficultyText);

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
        } else if(v.equals(favoriteBtn)){
            if(userBrowsing.getSavedRecipes() == null){
                List<String> savedRecipes = new ArrayList<String>();
                savedRecipes.add(recipe.getRecipeID());
                userBrowsing.setSavedRecipes(savedRecipes);
                recipe.increaseSavedAmount();
                favoriteBtn.setImageDrawable(getDrawable(R.drawable.ic_baseline_favorite));
            } else {
                if(userBrowsing.getSavedRecipes().contains(recipe.getRecipeID())){
                    favoriteBtn.setImageDrawable(getDrawable(R.drawable.ic_baseline_favorite_border));
                    recipe.decreaseSavedAmount();
                    userBrowsing.getSavedRecipes().remove(recipe.getRecipeID());
                } else {
                    favoriteBtn.setImageDrawable(getDrawable(R.drawable.ic_baseline_favorite));
                    recipe.increaseSavedAmount();
                    userBrowsing.getSavedRecipes().add(recipe.getRecipeID());
                }
            }
            recipeDAO.update(recipe);
            userDAO.update(userBrowsing);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
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

