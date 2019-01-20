package dk.michaelwestergaard.strikkehkleapp;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class ListAdapter extends RecyclerView.Adapter {

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_listing_item, parent, false);

        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ListViewHolder) holder).bindView(position);
    }

    @Override
    public int getItemCount() {
        return TestData.title.length;
    }

    private class ListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView textView;
        private ImageView imageView;

        public ListViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.item_title);
            imageView = itemView.findViewById(R.id.item_image);
            itemView.setOnClickListener(this);
        }

        public void bindView(int position){
            textView.setText(TestData.title[position]);

        }

        @Override
        public void onClick(View view) {
            System.out.println("Clicked " + textView.getText().toString());
            Intent intent = new Intent (view.getContext(), Opskrift.class);
            intent.putExtra("Title", textView.getText().toString());
            view.getContext().startActivity(intent);
        }
    }
}
