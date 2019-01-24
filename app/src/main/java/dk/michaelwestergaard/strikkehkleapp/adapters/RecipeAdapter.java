package dk.michaelwestergaard.strikkehkleapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.DecimalFormat;
import java.util.List;

import dk.michaelwestergaard.strikkehkleapp.DTO.RecipeDTO;
import dk.michaelwestergaard.strikkehkleapp.ShowRecipe;
import dk.michaelwestergaard.strikkehkleapp.R;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    private List<RecipeDTO> recipes;
    private RecipeDTO recipe;
    private int maxCount;
    Context context;

    public RecipeAdapter(List<RecipeDTO> recipes, int maxCount) {
        this.recipes = recipes;
        this.maxCount = maxCount;
    }

    public RecipeAdapter(List<RecipeDTO> recipes) {
        this.recipes = recipes;
        maxCount = 0;
    }
    @Override
    public RecipeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View recipeView = inflater.inflate(R.layout.recipe_listing_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(recipeView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecipeAdapter.ViewHolder holder, int position) {
        holder.bindView(position);
    }

    @Override
    public int getItemCount() {

        if (maxCount != 0) {
            if(recipes.size() > maxCount) {
                return maxCount;
            }
            return recipes.size();
        }
        return recipes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private RecipeDTO recipe;
        public TextView titleView;
        public ImageView imageView;
        public ImageView imageHeartCount;
        public TextView favouriteCount1;
        public TextView listPrice;

        public ViewHolder(View itemView) {
            super(itemView);
            titleView = itemView.findViewById(R.id.item_title);
            imageView = itemView.findViewById(R.id.item_image);
            imageHeartCount = itemView.findViewById(R.id.imageHeartCount);
            favouriteCount1 = itemView.findViewById(R.id.favoriteCount1);
            listPrice = itemView.findViewById(R.id.listPrice);
            itemView.setOnClickListener(this);
        }

        public void bindView(int position){
            recipe = recipes.get(position);
            titleView.setText(recipe.getTitle());
            favouriteCount1.setText(String.valueOf(recipe.getFavouritedAmount()));
            if(recipe.getPrice() == 0){
                listPrice.setText("Gratis");
            } else {
                listPrice.setText(new DecimalFormat("0.#").format(recipe.getPrice()) + " kr");
            }

            if(recipe.getImageList() != null){
                final RequestOptions requestOptions = new RequestOptions().transforms(new CenterCrop(), new RoundedCorners(16));
                String firstImage = recipe.getImageList().get(0);
                System.out.println(firstImage);
                if(!firstImage.contains("https")){
                    StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("recipeImages/" + firstImage);
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Glide.with(context).load(uri.toString()).apply(requestOptions).into(imageView);
                            //Glide.with(context).load(uri.toString()).apply(RequestOptions.circleCropTransform()).into(imageView);
                        }
                    });
                } else {
                    Glide.with(context).load(firstImage).apply(requestOptions).into(imageView);
                    //Glide.with(context).load(firstImage).apply(RequestOptions.circleCropTransform()).into(imageView);
                }
            }
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), ShowRecipe.class);
            intent.putExtra("RecipeID", recipe.getRecipeID());
            v.getContext().startActivity(intent);
        }
    }
}