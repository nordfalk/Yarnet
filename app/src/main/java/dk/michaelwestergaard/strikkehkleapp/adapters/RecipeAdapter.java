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
import dk.michaelwestergaard.strikkehkleapp.activities.Profile;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> implements View.OnClickListener {

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
        recipe = recipes.get(position);

        TextView titleView = holder.titleView;
        titleView.setText(recipe.getTitle());
        ImageView imageView = holder.imageView;
        //TODO: Set image source

        holder.itemView.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(v.getContext(), Opskrift.class);
        intent.putExtra("RecipeID", recipe.getRecipeID());
        intent.putExtra("UserID", recipe.getUserID());
        v.getContext().startActivity(intent);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView titleView;
        public ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);

            titleView = (TextView) itemView.findViewById(R.id.item_title);
            imageView = (ImageView) itemView.findViewById(R.id.item_image);
        }
    }
}