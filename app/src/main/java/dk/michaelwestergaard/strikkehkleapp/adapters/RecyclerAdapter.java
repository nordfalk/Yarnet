package dk.michaelwestergaard.strikkehkleapp.adapters;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import dk.michaelwestergaard.strikkehkleapp.DTO.RecipeDTO;
import dk.michaelwestergaard.strikkehkleapp.Opskrift;
import dk.michaelwestergaard.strikkehkleapp.R;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecipeViewHolder> {

    List<RecipeDTO> recipeList;

    public RecyclerAdapter(List<RecipeDTO> recipeList) {
        Log.d("Test", "test");
        this.recipeList = recipeList;
        Log.d("Test", "recipeList");

    }

    @Override
    public RecyclerAdapter.RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d("onCreateViewHolder", "created");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_listing_item, parent, false);

        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerAdapter.RecipeViewHolder holder, int position) {
        Log.d("Binding", "Binding position " + position);
        RecipeDTO recipe = recipeList.get(position);
        holder.recipeID = recipe.getRecipeID();
        holder.title.setText(recipe.getTitle());
        //TODO: Tilføj billede
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
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
                    System.out.println("Clicked " + recipeID);
                    Intent intent = new Intent (view.getContext(), Opskrift.class);
                    intent.putExtra("RecipeID", recipeID);
                    view.getContext().startActivity(intent);
                }
            });
        }
    }
}
