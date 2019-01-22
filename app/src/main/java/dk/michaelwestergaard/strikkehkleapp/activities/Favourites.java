package dk.michaelwestergaard.strikkehkleapp.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.support.v4.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
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
import dk.michaelwestergaard.strikkehkleapp.ViewPagerAdapter;
import dk.michaelwestergaard.strikkehkleapp.adapters.RecipeAdapter;

public class Favourites extends AppCompatActivity {

    ViewPager viewpager;
    ImageView backBtn;
    ImageView drawerBtn;

    List<RecipeDTO> favouriteRecipes;

    private RecipeDAO recipeDAO = new RecipeDAO();
    private UserDAO userDAO = new UserDAO();

    public static class FavouritesListFragment extends Fragment {

        public FavouritesListFragment() {

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);

        drawerBtn = findViewById(R.id.drawerBtn);
        backBtn = findViewById(R.id.backButton);
        backBtn.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }));

        backBtn.setVisibility(View.VISIBLE);
        drawerBtn.setVisibility(View.GONE);

        ViewPager viewPager = findViewById(R.id.favouritesViewPager);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new FavouritesListFragment(), "FavouriteList");
        viewPager.setAdapter(adapter);

    final List<RecipeDTO> recipes = new ArrayList<RecipeDTO>();

            recipeDAO.getReference().addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            recipes.clear();
            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                recipes.add(snapshot.getValue(RecipeDTO.class));
            }

            final FirebaseAuth auth = FirebaseAuth.getInstance();
            final List<UserDTO> users = new ArrayList<>();

            userDAO.getReference().addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    users.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        users.add(snapshot.getValue(UserDTO.class));
                    }

                    UserDTO actualUser = new UserDTO();
                    for (UserDTO user : users) {
                        if (user.getUserID().equals(auth.getCurrentUser().getUid())) {
                            actualUser = user;
                        }
                    }

                    favouriteRecipes = sortRecipes("favourited", recipes, actualUser);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
        }
    });
}

    private List<RecipeDTO> sortRecipes(String sortStyle, List<RecipeDTO> recipes, UserDTO user) {
        List<RecipeDTO> recipesToShow = new ArrayList<RecipeDTO>();

        switch (sortStyle) {
            case "favourited":
                if (user != null) {
                    List<String> FavouritedRecipeIDs = user.getFavouritedRecipes();

                    if (FavouritedRecipeIDs != null) {

                        for (RecipeDTO recipe : recipes) {
                            for (String favouritedRecipeID : FavouritedRecipeIDs) {
                                if (recipe.getRecipeID().equals(favouritedRecipeID)) {
                                    recipesToShow.add(recipe);
                                }
                            }
                        }
                    }
                } else {
                    System.out.println("Error sorting recipes: User not found!");
                }
                break;
        }
        return recipesToShow;
    }
    /*
    public static class FavouritesSingleton {
        private List<RecipeDTO> recipes;
        private static FavouritesSingleton instance = null;
        public static FavouritesSingleton getInstance() {
            if (instance == null) {
                instance = new FavouritesSingleton();
            }
            return instance;
        }

        public List<RecipeDTO> getRecipes() {
            return recipes;
        }

        public void setRecipes(List<RecipeDTO> recipes) { this.recipes = recipes;}
    }

    public static class FavouritesListFragment extends Fragment {
        public FavouritesListFragment() {

        }
*/

}
