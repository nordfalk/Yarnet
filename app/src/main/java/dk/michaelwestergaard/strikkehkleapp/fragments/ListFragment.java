package dk.michaelwestergaard.strikkehkleapp.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import dk.michaelwestergaard.strikkehkleapp.DAO.RecipeDAO;
import dk.michaelwestergaard.strikkehkleapp.DTO.RecipeDTO;
import dk.michaelwestergaard.strikkehkleapp.DTO.UserDTO;
import dk.michaelwestergaard.strikkehkleapp.MainSingleton;
import dk.michaelwestergaard.strikkehkleapp.R;
import dk.michaelwestergaard.strikkehkleapp.adapters.RecipeAdapter;

public class ListFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private RecipeDAO recipeDAO = new RecipeDAO();
    private String categoryID;
    private String subCategoryID;
    private String searchValue;

    public ListFragment() {
    }

    public static ListFragment newInstance(String param1, String param2) {
        ListFragment fragment = new ListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle arguments = this.getArguments();
            categoryID = arguments.getString("categoryID");
            subCategoryID = arguments.getString("subCategoryID");
            searchValue = arguments.getString("searchValue");
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_list, container, false);

        final List<RecipeDTO> recipes = new ArrayList<RecipeDTO>();

        final String searchValue = MainSingleton.getInstance().getSearchValue();

        recipeDAO.getReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserDTO user = MainSingleton.getInstance().getUser();
                recipes.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    if(snapshot.getValue(RecipeDTO.class).getRecipeType().toString().equals(user.getType()) || user.getType().equals("BOTH")) {
                        String recipeDifficulty = snapshot.getValue(RecipeDTO.class).getRecipeDifficulty().toString();

                        switch (user.getDifficulty()) {
                            case "HARD":
                                recipes.add(snapshot.getValue(RecipeDTO.class));
                                break;

                            case "MEDIUM":
                                if(recipeDifficulty.equals("MEDIUM") || recipeDifficulty.equals("EASY")) {
                                    recipes.add(snapshot.getValue(RecipeDTO.class));
                                }
                                break;

                            case "LOW":
                                if(recipeDifficulty.equals("EASY")) {
                                    recipes.add(snapshot.getValue(RecipeDTO.class));
                                }
                                break;
                        }
                    }
                }

                if(searchValue == null) {
                    for (int i = 0; i < recipes.size(); i++) {
                        if (!(recipes.get(i).getCategoryID().equals(categoryID))) {
                            recipes.remove(i);
                            i = i - 1;
                        }
                    }

                    if (subCategoryID != null) {
                        for (int i = 0; i < recipes.size(); i++) {
                            if (!(recipes.get(i).getSubcategoryID().equals(subCategoryID))) {
                                recipes.remove(i);
                                i = i - 1;
                            }
                        }
                    }
                } else {
                    for (int i = 0; i < recipes.size(); i++) {
                        if (!(recipes.get(i).getTitle().toLowerCase().contains(searchValue.toLowerCase()))) {
                            recipes.remove(i);
                            i = i - 1;
                        }
                    }
                }


                RecyclerView recyclerView = view.findViewById(R.id.recyclerViewGrid);

                RecipeAdapter adapter = new RecipeAdapter(recipes);
                recyclerView.setAdapter(adapter);

                RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);
                recyclerView.setLayoutManager(layoutManager);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Log.d("Recipes", recipes.toString());

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
