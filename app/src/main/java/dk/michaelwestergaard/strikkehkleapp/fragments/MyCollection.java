package dk.michaelwestergaard.strikkehkleapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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
import dk.michaelwestergaard.strikkehkleapp.activities.WatchMore;
import dk.michaelwestergaard.strikkehkleapp.adapters.RecipeAdapter;

public class MyCollection extends Fragment {

    Button watchMore4;
    Button watchMore5;
    Button watchMore6;

    List<RecipeDTO> savedRecipes;
    List<RecipeDTO> boughtRecipes;
    List<RecipeDTO> myRecipes;

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

        watchMore4 = view.findViewById(R.id.watchMore4);
        watchMore4.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                WatchMore.WatchMoreSingleton.getInstance().setRecipes(savedRecipes);
                startActivity( new Intent(v.getContext(), WatchMore.class));
            }
        });
        watchMore5 = view.findViewById(R.id.watchMore5);
        watchMore5.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                WatchMore.WatchMoreSingleton.getInstance().setRecipes(boughtRecipes);
                startActivity( new Intent(v.getContext(), WatchMore.class));
            }
        });
        watchMore6 = view.findViewById(R.id.watchMore6);
        watchMore6.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                WatchMore.WatchMoreSingleton.getInstance().setRecipes(myRecipes);
                startActivity( new Intent(v.getContext(), WatchMore.class));
            }
        });

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

                        savedRecipes = sortRecipes("saved", recipes, actualUser);
                        boughtRecipes = sortRecipes("bought", recipes, actualUser);
                        myRecipes = sortRecipes("my", recipes, actualUser);

                        RecipeAdapter adapterSaved = new RecipeAdapter(savedRecipes, 12);
                        RecipeAdapter adapterBought = new RecipeAdapter(boughtRecipes, 12);
                        RecipeAdapter adapterMy = new RecipeAdapter(myRecipes, 12);

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
                    List<String> savedRecipeIDs = null;//user.getSavedRecipes();

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