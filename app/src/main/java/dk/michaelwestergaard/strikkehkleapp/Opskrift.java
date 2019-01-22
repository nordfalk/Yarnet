package dk.michaelwestergaard.strikkehkleapp;

import android.app.AlertDialog;
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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import dk.michaelwestergaard.strikkehkleapp.DAO.CategoryDAO;
import dk.michaelwestergaard.strikkehkleapp.DAO.RecipeDAO;
import dk.michaelwestergaard.strikkehkleapp.DAO.UserDAO;
import dk.michaelwestergaard.strikkehkleapp.DTO.CategoryDTO;
import dk.michaelwestergaard.strikkehkleapp.DTO.RecipeDTO;
import dk.michaelwestergaard.strikkehkleapp.DTO.UserDTO;
import dk.michaelwestergaard.strikkehkleapp.activities.EditRecipe;
import dk.michaelwestergaard.strikkehkleapp.adapters.RecipeImageSliderAdapter;


public class Opskrift extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth auth= FirebaseAuth.getInstance();

    private RecipeDTO recipe = null;
    private RecipeDAO recipeDAO = new RecipeDAO();
    private String recipeID = "";

    private UserDTO userBrowsing;
    private UserDTO createdByUser;
    private UserDAO userDAO = new UserDAO();
    private CategoryDAO categoryDAO = new CategoryDAO();

    private TextView title, creator, categoriTextView, priceTextView, favoriteCount, stepsCount, difficulty, editTxt, deleteTxt;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Button købKnap;
    public boolean bought;
    private ImageView backgroundPicture, favoriteBtn, saveBtn, creatorImage, editImg, deleteImg;
    private CardView købContainer;
    private ImageView backBtn;
    private ImageView drawerBtn;


    private AlertDialog.Builder alertBuilder;
    private AlertDialog alertDialog;
    private ViewPager imageSliderViewPager;
    final List<String> imageUrls = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opskrift);

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

        editTxt = findViewById(R.id.editTxt);
        editImg = findViewById(R.id.editImg);
        deleteTxt = findViewById(R.id.deleteTxt);
        deleteImg = findViewById(R.id.deleteImg);

        editTxt.setVisibility(View.GONE);
        editImg.setVisibility(View.GONE);
        deleteTxt.setVisibility(View.GONE);
        deleteImg.setVisibility(View.GONE);

        recipeID = getIntent().getStringExtra("RecipeID");

        title = findViewById(R.id.recipe_title);
        creator = findViewById(R.id.recipe_creator);

        backgroundPicture = findViewById(R.id.baggrundsBillede);
        favoriteBtn = findViewById(R.id.recipe_favorite_btn);
        saveBtn = findViewById(R.id.recipe_save_btn);
        creatorImage = findViewById(R.id.creator_image);

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
        saveBtn.setOnClickListener(this);
        købKnap.setOnClickListener(this);
        backgroundPicture.setOnClickListener(this);
        editTxt.setOnClickListener(this);
        editImg.setOnClickListener(this);

        showRecipe();
    }

    private void showRecipe(){
        recipeDAO.getReference().child(recipeID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                recipe = dataSnapshot.getValue(RecipeDTO.class);
                title.setText(recipe.getTitle());

                if(recipe.getImageList() != null){
                    imageUrls.clear();
                    String firstImage = recipe.getImageList().get(0);
                    if(!firstImage.contains("https")){
                        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("recipeImages/" + firstImage);
                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Glide.with(Opskrift.this).load(uri.toString()).apply(RequestOptions.fitCenterTransform()).into(backgroundPicture);
                            }
                        });
                    } else {
                        Glide.with(Opskrift.this).load(firstImage).apply(RequestOptions.fitCenterTransform()).into(backgroundPicture);
                    }

                    int count = 0;
                    for (String recipeImage : recipe.getImageList()) {
                        count++;
                        if(recipeImage.contains("https:")){
                            imageUrls.add(recipeImage);
                            if(count == recipe.getImageList().size())
                                createImageSlider(imageUrls);
                        } else {
                            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("recipeImages/" + recipeImage);
                            final int finalCount = count;
                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    imageUrls.add(uri.toString());
                                    if(finalCount == recipe.getImageList().size())
                                        createImageSlider(imageUrls);
                                }
                            });
                        }
                    }
                }
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
                        if(createdByUser.getAvatar() != null){
                            String avatar = createdByUser.getAvatar();
                            if(avatar.contains("http") || avatar.contains("https")){
                                Glide.with(Opskrift.this).load(avatar).into(creatorImage);
                            } else {
                                StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("users/" + avatar);
                                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Glide.with(Opskrift.this).load(uri.toString()).apply(RequestOptions.circleCropTransform()).into(creatorImage);
                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                userBrowsing = MainSingleton.getInstance().getUser();

                if(userBrowsing.getFavouritedRecipes() != null){
                    if(userBrowsing.getFavouritedRecipes().contains(recipe.getRecipeID())){
                        favoriteBtn.setImageDrawable(getDrawable(R.drawable.ic_baseline_favorite));
                    }
                }

                if(userBrowsing.getSavedRecipes() != null){
                    if(userBrowsing.getSavedRecipes().contains(recipe.getRecipeID())){
                        saveBtn.setImageDrawable(getDrawable(R.drawable.save_color));
                    }
                }

                if(recipe.getPrice() == 0){
                    købContainer.setVisibility(View.GONE);
                    priceTextView.setVisibility(View.GONE);
                } else if(userBrowsing.getBoughtRecipes() != null){
                    if(userBrowsing.getBoughtRecipes().contains(recipe.getRecipeID())) {
                        købContainer.setVisibility(View.GONE);
                        priceTextView.setVisibility(View.GONE);
                    }
                } else {
                    priceTextView.setText("Pris: " + new DecimalFormat("0.#").format(recipe.getPrice()) + " kr");
                    købContainer.setVisibility(View.VISIBLE);
                }
              
                if(userBrowsing.getUserID().equals(recipe.getUserID())) {
                    editTxt.setVisibility(View.VISIBLE);
                    editImg.setVisibility(View.VISIBLE);
                }

                favoriteCount.setText(String.valueOf(recipe.getFavouritedAmount()));

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

    private void createImageSlider(List<String> images){

        alertBuilder = new AlertDialog.Builder(this);

        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.image_dialog_slider, null, false);

        TabLayout tabLayoutSlider = dialogView.findViewById(R.id.viewPagerTabs);
        imageSliderViewPager = dialogView.findViewById(R.id.image_slider_view_pager);
        imageSliderViewPager.setAdapter(new RecipeImageSliderAdapter(this, images));
        tabLayoutSlider.setupWithViewPager(imageSliderViewPager,true);

        Button closeBtn = dialogView.findViewById(R.id.close_image_slider);

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        alertBuilder.setView(dialogView);

        alertDialog = alertBuilder.create();
    }

    private void setupViewPager(ViewPager viewPager) {
        RecipeViewPagerAdapter adapter = new RecipeViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new fragment_recipe_information().newInstance(recipe.getRecipeInformationDTO()), "Information");
        adapter.addFragment(new fragment_recipe_instruction().newInstance(recipeID,recipe.getRecipeInstructionDTO()), "Vejledning");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        if(v==købKnap){
            if(userBrowsing.getBoughtRecipes() == null) {
                List<String> boughtRecipes = new ArrayList<>();
                boughtRecipes.add(recipe.getRecipeID());

                userBrowsing.setBoughtRecipes(boughtRecipes);

                købContainer.setVisibility(View.GONE);
                priceTextView.setVisibility(View.GONE);
            } else {
                List<String> boughtRecipes = userBrowsing.getBoughtRecipes();
                boughtRecipes.add(recipe.getRecipeID());

                userBrowsing.setBoughtRecipes(boughtRecipes);

                købContainer.setVisibility(View.GONE);
                priceTextView.setVisibility(View.GONE);
            }

            userDAO.update(userBrowsing);
        } else if(v.equals(favoriteBtn)){
            if(userBrowsing.getFavouritedRecipes() == null){
                List<String> favouritedRecipes = new ArrayList<String>();
                favouritedRecipes.add(recipe.getRecipeID());
                userBrowsing.setFavouritedRecipes(favouritedRecipes);
                favoriteCount.setText((recipe.getFavouritedAmount()+1)+"");
                recipe.increaseFavouritedAmount();
                favoriteBtn.setImageDrawable(getDrawable(R.drawable.ic_baseline_favorite));
            } else {
                if(userBrowsing.getFavouritedRecipes().contains(recipe.getRecipeID())){
                    favoriteBtn.setImageDrawable(getDrawable(R.drawable.ic_baseline_favorite_border));
                    favoriteCount.setText((recipe.getFavouritedAmount()-1)+"");
                    recipe.decreaseFavouritedAmount();
                    userBrowsing.getFavouritedRecipes().remove(recipe.getRecipeID());
                } else {
                    favoriteBtn.setImageDrawable(getDrawable(R.drawable.ic_baseline_favorite));
                    favoriteCount.setText((recipe.getFavouritedAmount()+1)+"");
                    recipe.increaseFavouritedAmount();
                    userBrowsing.getFavouritedRecipes().add(recipe.getRecipeID());
                }
            }

            recipeDAO.update(recipe);
            userDAO.update(userBrowsing);
        } else if(v.equals(saveBtn)){
            if(userBrowsing.getSavedRecipes() == null){
                List<String> savedRecipes = new ArrayList<>();
                savedRecipes.add(recipe.getRecipeID());
                userBrowsing.setSavedRecipes(savedRecipes);
                saveBtn.setImageDrawable(getDrawable(R.drawable.save_color));
            } else {
                if(userBrowsing.getSavedRecipes().contains(recipe.getRecipeID())){
                    saveBtn.setImageDrawable(getDrawable(R.drawable.save_white));
                    userBrowsing.getSavedRecipes().remove(recipe.getRecipeID());
                } else {
                    saveBtn.setImageDrawable(getDrawable(R.drawable.save_color));
                    userBrowsing.getSavedRecipes().add(recipe.getRecipeID());
                }
            }

            userDAO.update(userBrowsing);
        } else if (v.equals(backgroundPicture)) {
          if(imageUrls != null && alertDialog != null)
                alertDialog.show();
        } else if(v == editTxt || v == editImg){
            Intent intent = new Intent(this, EditRecipe.class);
            intent.putExtra("recipeID", recipeID);
            startActivity(intent);
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

