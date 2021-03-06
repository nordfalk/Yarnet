package dk.michaelwestergaard.strikkehkleapp.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ListFragment;
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

    private ImageView backBtn;
    private ImageView drawerBtn;
    private RecyclerView recyclerView;

    private FirebaseAuth auth= FirebaseAuth.getInstance();
    private UserDAO userDAO = new UserDAO();
    private UserDTO user;

    private RecipeDAO recipeDAO = new RecipeDAO();
    private List<RecipeDTO> recipes = new ArrayList<>();
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_favourites);
            final Context myContext = this;

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

            recyclerView = findViewById(R.id.recyclerViewGrid);

            userDAO.getReference().child(auth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    user = dataSnapshot.getValue(UserDTO.class);

                    recipeDAO.getReference().addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            recipes.clear();
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                RecipeDTO recipe = snapshot.getValue(RecipeDTO.class);

                                for(String recipeID : user.getFavouritedRecipes()) {
                                    if(recipe.getRecipeID().equals(recipeID)) {
                                        recipes.add(recipe);
                                    }
                                }
                            }

                            RecipeAdapter adapter = new RecipeAdapter(recipes);
                            recyclerView.setAdapter(adapter);

                            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(myContext, 3);
                            recyclerView.setLayoutManager(layoutManager);
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

            public void setRecipes(List<RecipeDTO> recipes) {
                this.recipes = recipes;
            }
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

    public static class FavouritesListFragment extends Fragment {

        public FavouritesListFragment() {

        }

        public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
            final View view = inflater.inflate(R.layout.fragment_list, container, false);

            RecyclerView recyclerView = view.findViewById(R.id.recyclerViewGrid);

            RecipeAdapter adapter = new RecipeAdapter(FavouritesSingleton.getInstance().getRecipes());
            recyclerView.setAdapter(adapter);

            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);
            recyclerView.setLayoutManager(layoutManager);

            return view;
        }
    }
*/
}
