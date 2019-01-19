package dk.michaelwestergaard.strikkehkleapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import dk.michaelwestergaard.strikkehkleapp.DTO.RecipeInstructionDTO;

import static com.facebook.FacebookSdk.getApplicationContext;

public class InstructionAdapter extends RecyclerView.Adapter {

    List<RecipeInstructionDTO> recipeInstructionDTO;
    String recipeID;
    Context context;
    SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
    SharedPreferences.Editor editor = pref.edit();

    public InstructionAdapter(String recipeID, List<RecipeInstructionDTO> recipeInstructionDTO) {
        this.recipeInstructionDTO = recipeInstructionDTO;
        this.recipeID = recipeID;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_recipe_instruction_top_element, parent,false);

        context = parent.getContext();
        System.out.println("childcount: "+parent.getChildCount());
        return new InstructionViewHolder(view,parent.getChildCount());
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((InstructionViewHolder) holder).bindView(position);
    }

    @Override
    public int getItemCount() {
        if(recipeInstructionDTO != null)
            return recipeInstructionDTO.size();

        return 0;
    }

    private class InstructionViewHolder extends RecyclerView.ViewHolder{
        TextView headlineElement, instructionpoint, underlineElement, instructionpoint2;
        RecyclerView underlineCycleview;
        int position;

        public InstructionViewHolder(View view, final int position) {
            super(view);
            this.position = position;
            headlineElement = view.findViewById(R.id.headlineElement);
            underlineElement = view.findViewById(R.id.underlineElement);
            instructionpoint = view.findViewById(R.id.instructionPoint);
            instructionpoint2=view.findViewById(R.id.instructionPoint2);
            underlineCycleview = view.findViewById(R.id.underlineCycleView);
            editor.putBoolean("done",true);
            editor.commit();



            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<View> views = ((InstructionUnderAdapter)underlineCycleview.getAdapter()).getViews();
                    RecipeChecklist recipeChecklist = RecipeChecklist.getInstance();
                    String state = recipeChecklist.getChecklistState(recipeID, position);
                    System.out.println("State:"+state);


                    if (state != null){
                        if(state.equals("true")){
                            instructionpoint.setBackgroundResource(0);
                            headlineElement.setTextColor(Color.parseColor("#DCDCDC"));
                            for (int i = 0; i < views.size();i++) {
                                views.get(i).findViewById(R.id.instructionPoint2).setBackgroundResource(0);
                                ((TextView) views.get(i).findViewById(R.id.underlineElement)).setTextColor(Color.parseColor("#DCDCDC"));
                            }
                            recipeChecklist.updateChecklist(recipeID,position,false);
                        }else if(state.equals("false")){
                            instructionpoint.setBackgroundResource(R.drawable.done);
                            headlineElement.setTextColor(Color.parseColor("#43b05c"));
                            for (int i = 0; i < views.size();i++) {
                                views.get(i).findViewById(R.id.instructionPoint2).setBackgroundResource(R.drawable.done);
                                ((TextView) views.get(i).findViewById(R.id.underlineElement)).setTextColor(Color.parseColor("#43b05c"));
                            }
                            recipeChecklist.updateChecklist(recipeID,position,true);
                        }
                    } else{
                        System.out.println("State"+state);
                        recipeChecklist.addChecklist(recipeID, recipeInstructionDTO.size());
                        instructionpoint.setBackgroundResource(0);
                        headlineElement.setTextColor(Color.parseColor("#DCDCDC"));
                        for (int i = 0; i < views.size();i++) {
                            views.get(i).findViewById(R.id.instructionPoint2).setBackgroundResource(0);
                            ((TextView) views.get(i).findViewById(R.id.underlineElement)).setTextColor(Color.parseColor("#DCDCDC"));
                        }
                        recipeChecklist.updateChecklist(recipeID,position,true);
                    }
                    editor.commit();
                }
            });
        }

        public void bindView(int position){
            headlineElement.setText(recipeInstructionDTO.get(position).getTitle());
            instructionpoint.setText(position+1+"");
            underlineCycleview.setAdapter(new InstructionUnderAdapter(recipeInstructionDTO.get(position).getInstructions()));
            underlineCycleview.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

            List<View> views = ((InstructionUnderAdapter)underlineCycleview.getAdapter()).getViews();
            boolean test = pref.getBoolean("done",true);
            if( test ){
                instructionpoint.setBackgroundResource(R.drawable.done);
                headlineElement.setTextColor(Color.parseColor("#43b05c"));
                //instructionpoint2.setBackgroundResource(R.drawable.done);
                //underlineElement.setTextColor(Color.parseColor("#43b05c"));
            }else if(test!= true){
                instructionpoint.setBackgroundResource(0);
                headlineElement.setTextColor(Color.parseColor("#DCDCDC"));
            }

            for (int i = 0; i < views.size();i++) {
                views.get(i).findViewById(R.id.instructionPoint2).setBackgroundResource(R.drawable.done);
                ((TextView) views.get(i).findViewById(R.id.underlineElement)).setTextColor(Color.parseColor("#43b05c"));
            }
        }

    }


}
