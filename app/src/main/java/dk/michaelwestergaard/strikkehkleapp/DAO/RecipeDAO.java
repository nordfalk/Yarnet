package dk.michaelwestergaard.strikkehkleapp.DAO;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import dk.michaelwestergaard.strikkehkleapp.DTO.RecipeDTO;

public class RecipeDAO implements DAO<RecipeDTO> {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference("recipes");

    @Override
    public String insert(RecipeDTO recipe) {
        String recipeID = databaseReference.push().getKey();
        recipe.setRecipeID(recipeID);
        databaseReference.child(recipeID).setValue(recipe);
        return recipeID;
    }

    @Override
    public boolean update(RecipeDTO recipe)  {
        databaseReference.child(recipe.getRecipeID()).setValue(recipe);
        return true;
    }

    @Override
    public boolean delete(RecipeDTO recipe) {
        databaseReference.child(recipe.getRecipeID()).removeValue();
        return true;
    }

    //Skal fjernes
    public RecipeDTO get(final String recipeID) {
        final RecipeDTO[] recipeDTO = new RecipeDTO[1];
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    if(snapshot.getKey().equals(recipeID)){
                        System.out.println("Recipe found! " + snapshot.getKey());
                        recipeDTO[0] = snapshot.getValue(RecipeDTO.class);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return recipeDTO[0];
    }

    public List<RecipeDTO> getAll() {
        final List<RecipeDTO> recipes = new ArrayList<RecipeDTO>();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    recipes.add(snapshot.getValue(RecipeDTO.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Log.d("Recipes", recipes.toString());
        return recipes;

    }

    @Override
    public DatabaseReference getReference() {
        return databaseReference;
    }
}
