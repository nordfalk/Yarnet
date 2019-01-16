package dk.michaelwestergaard.strikkehkleapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import dk.michaelwestergaard.strikkehkleapp.DTO.RecipeDTO;
import dk.michaelwestergaard.strikkehkleapp.Opskrift;
import dk.michaelwestergaard.strikkehkleapp.R;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    private List<RecipeDTO> recipes;
    private RecipeDTO recipe;

    public RecipeAdapter(List<RecipeDTO> recipes) {
        this.recipes = recipes;
    }

    @Override
    public RecipeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View recipeView = inflater.inflate(R.layout.recipe_listing_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(recipeView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecipeAdapter.ViewHolder holder, int position) {
        holder.bindView(position);
        System.out.println("binding " + position);
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private RecipeDTO recipe;
        public TextView titleView;
        public ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            titleView = itemView.findViewById(R.id.item_title);
            imageView = itemView.findViewById(R.id.item_image);
            itemView.setOnClickListener(this);
        }

        public void bindView(int position){
            recipe = recipes.get(position);
            titleView.setText(recipe.getTitle());
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), Opskrift.class);
            System.out.println("clicking on " + recipe.getRecipeID());
            System.out.println("clicking on " + titleView.getText().toString());
            intent.putExtra("RecipeID", recipe.getRecipeID());
            v.getContext().startActivity(intent);
        }
    }
}