package dk.michaelwestergaard.strikkehkleapp.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import dk.michaelwestergaard.strikkehkleapp.ListAdapter;
import dk.michaelwestergaard.strikkehkleapp.R;
import dk.michaelwestergaard.strikkehkleapp.adapters.RecipeAdapter;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyCollection.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyCollection#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyCollection extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private RecipeDAO recipeDAO = new RecipeDAO();
    private UserDAO userDAO = new UserDAO();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public MyCollection() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyCollection.
     */
    // TODO: Rename and change types and number of parameters
    public static MyCollection newInstance(String param1, String param2) {
        MyCollection fragment = new MyCollection();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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

    private List<RecipeDTO> sortRecipes(String sortStyle, List<RecipeDTO> recipeSource, UserDTO user) {
        List<RecipeDTO> recipes = new ArrayList<>();

        for(RecipeDTO recipe : recipeSource) {
            recipes.add(recipe);
        }

        switch(sortStyle) {
            case "saved":
                if(user != null) {
                    List<String> savedRecipeIDs = user.getSavedRecipes();

                    for (int i = 0; i < recipes.size(); i++) {
                        boolean keepRecipe = false;

                        for(String savedRecipeID : savedRecipeIDs) {
                            if(recipes.get(i).getRecipeID().equals(savedRecipeID)) {
                                keepRecipe = true;
                                break;
                            }
                        }

                        if(!keepRecipe) {
                            recipes.remove(i);
                            i = i - 1;
                        }
                    }
                } else {
                    System.out.println("Error sorting recipes: User not found!");
                }
                break;

            case "bought":
                if(user != null) {
                    List<String> boughtRecipeIDs = user.getBoughtRecipes();

                    for (int i = 0; i < recipes.size(); i++) {
                        boolean keepRecipe = false;

                        for(String boughtRecipeID : boughtRecipeIDs) {
                            if(recipes.get(i).getRecipeID().equals(boughtRecipeID)) {
                                keepRecipe = true;
                                break;
                            }
                        }

                        if(!keepRecipe) {
                            recipes.remove(i);
                            i = i - 1;
                        }
                    }
                } else {
                    System.out.println("Error sorting recipes: User not found!");
                }
                break;

            case "My":
                if(user != null) {
                    for (int i = 0; i < recipes.size(); i++) {
                        if(!recipes.get(i).getUserID().equals(user.getUserID())) {
                            recipes.remove(i);
                            i = i - 1;
                        }
                    }
                } else {
                    System.out.println("Error sorting recipes: User not found!");
                }
                break;

            default:
                System.out.print("Error sorting recipes: Unknown sortStyle!");
                break;
        }

        return recipes;
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
