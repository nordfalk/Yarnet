package dk.michaelwestergaard.strikkehkleapp;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class InstructionUnderAdapter extends RecyclerView.Adapter{
    TextView instructionPoint,underlineElement;
    List<String> instruction;
    List<View> views = new ArrayList<View>();
    String state;

    public InstructionUnderAdapter(List<String> instructions, String state) {
        this.instruction = instructions;
        this.state = state;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_recipe_instruction_bottom_element, parent, false);
        views.add(view);
        return new InstructionBottomViewHolder(view);
    }

    public List<View> getViews() {
        return views;
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

        private TextView instructionPoint,underlineElement;

        public InstructionBottomViewHolder(View itemView) {
            super(itemView);
            underlineElement = itemView.findViewById(R.id.underlineElement);
            instructionPoint= itemView.findViewById(R.id.instructionPoint2);
        }

        public void bindView(int position){
            underlineElement.setText(instruction.get(position));
            if(state != null){
                if (state.equals("true")) {
                    instructionPoint.setBackgroundResource(R.drawable.done);
                    underlineElement.setTextColor(Color.parseColor("#B7B7B7"));
                }
            }
        }

    }

}
