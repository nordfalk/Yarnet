package dk.michaelwestergaard.strikkehkleapp.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import dk.michaelwestergaard.strikkehkleapp.adapters.RecipeAdapter;

public class MyCollection extends Fragment {

    private RecipeDAO recipeDAO = new RecipeDAO();
    private UserDAO userDAO = new UserDAO();

    public MyCollection() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_my_collection, container, false);

        final List<RecipeDTO> recipes = new ArrayList<RecipeDTO>();

        recipeDAO.getReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                recipes.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    recipes.add(snapshot.getValue(RecipeDTO.class));
                }

                final FirebaseAuth auth = FirebaseAuth.getInstance();
                final List<UserDTO> users = new ArrayList<>();

                userDAO.getReference().addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        users.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                            users.add(snapshot.getValue(UserDTO.class));
                        }

                        UserDTO actualUser = new UserDTO();
                        for(UserDTO user : users) {
                            if(user.getUserID().equals(auth.getCurrentUser().getUid())) {
                                actualUser = user;
                            }
                        }

                        List<RecipeDTO> savedRecipes = sortRecipes("saved", recipes, actualUser);
                        List<RecipeDTO> boughtRecipes = sortRecipes("bought", recipes, actualUser);
                        List<RecipeDTO> myRecipes = sortRecipes("my", recipes, actualUser);

                        RecipeAdapter adapterSaved = new RecipeAdapter(savedRecipes);
                        RecipeAdapter adapterBought = new RecipeAdapter(boughtRecipes);
                        RecipeAdapter adapterMy = new RecipeAdapter(myRecipes);

                        RecyclerView recyclerViewSaved = view.findViewById(R.id.SavedPatternsView);
                        RecyclerView.LayoutManager layoutManagerNew = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                        recyclerViewSaved.setAdapter(adapterSaved);
                        recyclerViewSaved.setLayoutManager(layoutManagerNew);

                        RecyclerView recyclerViewBought = view.findViewById(R.id.BoughtPatternsView);
                        RecyclerView.LayoutManager layoutManagerPaid = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                        recyclerViewBought.setAdapter(adapterBought);
                        recyclerViewBought.setLayoutManager(layoutManagerPaid);

                        RecyclerView recyclerViewMy = view.findViewById(R.id.MyPatternsView);
                        RecyclerView.LayoutManager layoutManagerFree = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                        recyclerViewMy.setAdapter(adapterMy);
                        recyclerViewMy.setLayoutManager(layoutManagerFree);
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

        return view;
    }

    private List<RecipeDTO> sortRecipes(String sortStyle, List<RecipeDTO> recipes, UserDTO user) {
        List<RecipeDTO> recipesToShow = new ArrayList<RecipeDTO>();

        switch(sortStyle) {
            case "saved":
                if(user != null) {
                    List<String> savedRecipeIDs = user.getSavedRecipes();

                    if(savedRecipeIDs != null) {

                        for (RecipeDTO recipe : recipes) {
                            for (String savedRecipeID : savedRecipeIDs) {
                                if (recipe.getRecipeID().equals(savedRecipeID)) {
                                    recipesToShow.add(recipe);
                                }
                            }
                        }
                    }
                } else {
                    System.out.println("Error sorting recipes: User not found!");
                }
                break;

            case "bought":
                if(user != null) {
                    List<String> boughtRecipeIDs = user.getBoughtRecipes();

                    if(boughtRecipeIDs != null) {

                        for (RecipeDTO recipe : recipes) {
                            for (String boughtRecipeID : boughtRecipeIDs) {
                                if (recipe.getRecipeID().equals(boughtRecipeID)) {
                                    recipesToShow.add(recipe);
                                }
                            }
                        }
                    }
                } else {
                    System.out.println("Error sorting recipes: User not found!");
                }
                break;

            case "my":
                if(user != null) {
                    for(RecipeDTO recipe : recipes){
                        if(recipe.getUserID().equals(user.getUserID()))
                            recipesToShow.add(recipe);
                    }
                } else {
                    System.out.println("Error sorting recipes: User not found!");
                }
                break;

            default:
                System.out.print("Error sorting recipes: Unknown sortStyle!");
                break;
        }

        return recipesToShow;
    }
}