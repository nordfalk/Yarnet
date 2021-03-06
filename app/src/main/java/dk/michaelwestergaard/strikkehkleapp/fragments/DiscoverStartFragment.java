package dk.michaelwestergaard.strikkehkleapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import dk.michaelwestergaard.strikkehkleapp.DAO.RecipeDAO;
import dk.michaelwestergaard.strikkehkleapp.DTO.RecipeDTO;
import dk.michaelwestergaard.strikkehkleapp.DTO.UserDTO;
import dk.michaelwestergaard.strikkehkleapp.MainSingleton;
import dk.michaelwestergaard.strikkehkleapp.ShowRecipe;
import dk.michaelwestergaard.strikkehkleapp.R;
import dk.michaelwestergaard.strikkehkleapp.activities.WatchMore;
import dk.michaelwestergaard.strikkehkleapp.adapters.RecipeAdapter;

public class DiscoverStartFragment extends Fragment {

        Button watchMore1;
        Button watchMore2;
        Button watchMore3;
        ViewPager viewPager;

    final List<RecipeDTO> recipesNewest = new ArrayList<RecipeDTO>();
    final List<RecipeDTO> recipesBought = new ArrayList<RecipeDTO>();
    final List<RecipeDTO> recipesFree = new ArrayList<RecipeDTO>();

    private RecipeDAO recipeDAO = new RecipeDAO();

    public DiscoverStartFragment() {
        // Required empty public constructor
    }

    public static DiscoverStartFragment newInstance(String param1, String param2) {
        DiscoverStartFragment fragment = new DiscoverStartFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }

    public void sortNewest(List<RecipeDTO> recipes) {
        Collections.sort(recipes, new Comparator<RecipeDTO>() {
            @Override
            public int compare(RecipeDTO recipe1, RecipeDTO recipe2) {
                return recipe2.getCreatedTimestamp().compareTo(recipe1.getCreatedTimestamp());

            }
        });
    }

    public void sortPop(List<RecipeDTO> recipes) {
        Collections.sort(recipes, new Comparator<RecipeDTO>() {
            @Override
            public int compare(RecipeDTO recipe1, RecipeDTO recipe2) {
                return recipe2.getFavouritedAmount() - recipe1.getFavouritedAmount();

            }
        });
    }
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_discover_start, container, false);

        watchMore1 = view.findViewById(R.id.watchMore1);
        watchMore1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                WatchMore.WatchMoreSingleton.getInstance().setRecipes(recipesNewest);
                startActivity( new Intent(v.getContext(), WatchMore.class));
            }
        });
        watchMore2 = view.findViewById(R.id.watchMore2);
        watchMore2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                WatchMore.WatchMoreSingleton.getInstance().setRecipes(recipesBought);
                startActivity( new Intent(v.getContext(), WatchMore.class));
            }
        });
        watchMore3 = view.findViewById(R.id.watchMore3);
        watchMore3.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                WatchMore.WatchMoreSingleton.getInstance().setRecipes(recipesFree);
                startActivity( new Intent(v.getContext(), WatchMore.class));
            }
        });

        Query query = FirebaseDatabase.getInstance().getReference().child("recipes");
        FirebaseRecyclerOptions<RecipeDTO> options = new FirebaseRecyclerOptions.Builder<RecipeDTO>()
                .setQuery(query, RecipeDTO.class)
                .build();

        FirebaseRecyclerAdapter recyclerAdapter = new FirebaseRecyclerAdapter<RecipeDTO, RecipeViewHolder>(options) {

            @Override
            public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_listing_item, parent, false);
                return new RecipeViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(RecipeViewHolder holder, int position, RecipeDTO recipe) {
                holder.recipeID = recipe.getRecipeID();
                holder.title.setText(recipe.getTitle());
            }


            @Override
            public void onError(DatabaseError e) {
            }
        };

        recipeDAO.getReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserDTO user = MainSingleton.getInstance().getUser();
                recipesNewest.clear();
                recipesBought.clear();
                recipesFree.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    RecipeDTO recipe = snapshot.getValue(RecipeDTO.class);
                    if(recipe.getRecipeType().toString().equals(user.getType()) || user.getType().equals("BOTH")) {
                        String recipeDifficulty = recipe.getRecipeDifficulty().toString();

                        switch (user.getDifficulty()) {
                            case "HARD":
                                recipesNewest.add(recipe);
                                if (recipe.getPrice() == 0) {
                                    recipesFree.add(recipe);
                                } else {
                                    recipesBought.add(recipe);
                                }
                                break;

                            case "MEDIUM":
                                if(recipeDifficulty.equals("MEDIUM") || recipeDifficulty.equals("EASY")) {
                                    recipesNewest.add(recipe);
                                    if (recipe.getPrice() == 0) {
                                        recipesFree.add(recipe);
                                    } else {
                                        recipesBought.add(recipe);
                                    }
                                }
                                break;

                            case "EASY":
                                if(recipeDifficulty.equals("EASY")) {
                                    recipesNewest.add(recipe);
                                    if (recipe.getPrice() == 0) {
                                        recipesFree.add(recipe);
                                    } else {
                                        recipesBought.add(recipe);
                                    }
                                }
                                break;
                        }
                    }
                }
                sortNewest(recipesNewest);
                sortPop(recipesBought);
                sortPop(recipesFree);
                RecipeAdapter adapterNewest = new RecipeAdapter(recipesNewest, 12);
                RecipeAdapter adapterBought = new RecipeAdapter(recipesBought, 12);
                RecipeAdapter adapterFree = new RecipeAdapter(recipesFree, 12);

                RecyclerView recyclerViewNew = view.findViewById(R.id.item_list_new);
                RecyclerView.LayoutManager layoutManagerNew = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                recyclerViewNew.setAdapter(adapterNewest);
                recyclerViewNew.setLayoutManager(layoutManagerNew);

                RecyclerView recyclerViewPaid = view.findViewById(R.id.item_list_paid);
                RecyclerView.LayoutManager layoutManagerPaid = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                recyclerViewPaid.setAdapter(adapterBought);
                recyclerViewPaid.setLayoutManager(layoutManagerPaid);

                RecyclerView recyclerViewFree = view.findViewById(R.id.item_list_free);
                RecyclerView.LayoutManager layoutManagerFree = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                recyclerViewFree.setAdapter(adapterFree);
                recyclerViewFree.setLayoutManager(layoutManagerFree);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }


        });
      
        return view;
    }

    class RecipeViewHolder extends RecyclerView.ViewHolder {

        String recipeID;
        private TextView title;
        private ImageView image;

        public RecipeViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.item_title);
            image = itemView.findViewById(R.id.item_image);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent (view.getContext(), ShowRecipe.class);
                    intent.putExtra("RecipeID", recipeID);
                    view.getContext().startActivity(intent);
                }
            });
        }
    }

}
