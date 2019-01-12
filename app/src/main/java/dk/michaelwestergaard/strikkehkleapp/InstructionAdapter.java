package dk.michaelwestergaard.strikkehkleapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class InstructionAdapter extends RecyclerView.Adapter {

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_recipe_instruction_top_element, parent,false);

        return new InstructionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((InstructionViewHolder) holder).bindView(position);
        System.out.println("Onkel3");
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    private class InstructionViewHolder extends RecyclerView.ViewHolder{
        TextView headlineElement, instructionpoint;
        RecyclerView underlineCycleview;

        public InstructionViewHolder(View view) {
            super(view);
            headlineElement = view.findViewById(R.id.headlineElement);
            instructionpoint = view.findViewById(R.id.instructionPoint);
            underlineCycleview = view.findViewById(R.id.underlineCycleView);
            System.out.println("Onkel2");
        }

        public void bindView(int position){
            headlineElement.setText("test"+position);
            instructionpoint.setText("test"+position);
            underlineCycleview.setAdapter(new InstructionUnderAdapter());
            System.out.println("Onkel1");
        }

    }


}
