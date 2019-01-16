package dk.michaelwestergaard.strikkehkleapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class InstructionUnderAdapter extends RecyclerView.Adapter{
    TextView instructionPoint,underlineElement;

    List<String> instruction;

    public InstructionUnderAdapter(List<String> instructions) {
        this.instruction = instructions;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_recipe_instruction_bottom_element, parent, false);
        return new InstructionBottomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((InstructionBottomViewHolder) holder).bindView(position);
    }

    @Override
    public int getItemCount() {
        if(instruction != null)
            return instruction.size();

        return 0;
    }

    private class InstructionBottomViewHolder extends RecyclerView.ViewHolder{

        private TextView textView;

        public InstructionBottomViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.underlineElement);
        }

        public void bindView(int position){
            textView.setText(instruction.get(position));
        }

    }

}