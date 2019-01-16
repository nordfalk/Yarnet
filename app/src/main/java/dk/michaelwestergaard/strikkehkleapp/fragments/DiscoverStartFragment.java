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
import java.util.List;

import dk.michaelwestergaard.strikkehkleapp.DAO.RecipeDAO;
import dk.michaelwestergaard.strikkehkleapp.DTO.RecipeDTO;
import dk.michaelwestergaard.strikkehkleapp.Opskrift;
import dk.michaelwestergaard.strikkehkleapp.R;
import dk.michaelwestergaard.strikkehkleapp.adapters.RecipeAdapter;

public class DiscoverStartFragment extends Fragment {

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

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_discover_start, container, false);

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

        final List<RecipeDTO> recipes = new ArrayList<RecipeDTO>();

        recipeDAO.getReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    recipes.add(snapshot.getValue(RecipeDTO.class));
                }

                RecipeAdapter adapter = new RecipeAdapter(recipes);

                RecyclerView recyclerViewNew = view.findViewById(R.id.item_list_new);
                RecyclerView.LayoutManager layoutManagerNew = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                recyclerViewNew.setAdapter(adapter);
                recyclerViewNew.setLayoutManager(layoutManagerNew);

                RecyclerView recyclerViewPaid = view.findViewById(R.id.item_list_paid);
                RecyclerView.LayoutManager layoutManagerPaid = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                recyclerViewPaid.setAdapter(adapter);
                recyclerViewPaid.setLayoutManager(layoutManagerPaid);

                RecyclerView recyclerViewFree = view.findViewById(R.id.item_list_free);
                RecyclerView.LayoutManager layoutManagerFree = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                recyclerViewFree.setAdapter(adapter);
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
                    Intent intent = new Intent (view.getContext(), Opskrift.class);
                    intent.putExtra("RecipeID", recipeID);
                    view.getContext().startActivity(intent);
                }
            });
        }
    }

}
