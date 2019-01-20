package dk.michaelwestergaard.strikkehkleapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.List;

import dk.michaelwestergaard.strikkehkleapp.DTO.RecipeInstructionDTO;

import static com.facebook.FacebookSdk.getApplicationContext;

public class InstructionAdapter extends RecyclerView.Adapter {

    List<RecipeInstructionDTO> recipeInstructionDTO;
    String recipeID;
    Context context;

    RecipeChecklist recipeChecklist = RecipeChecklist.getInstance();

    SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
    SharedPreferences.Editor editor = pref.edit();

    public InstructionAdapter(String recipeID, List<RecipeInstructionDTO> recipeInstructionDTO) {
        this.recipeInstructionDTO = recipeInstructionDTO;
        this.recipeID = recipeID;
    }

    public void loadCheckLists(){
        Gson gson = new Gson();
        String json = pref.getString("RecipeCheckList", null);
        System.out.println(json);
        if(json != null) {
            this.recipeChecklist = gson.fromJson(json, RecipeChecklist.class);
            recipeChecklist.setChecklist(recipeChecklist.getChecklist());
        }
    }

    public void saveChecklists(){
        Gson gson = new Gson();
        String json = gson.toJson(recipeChecklist);
        editor.putString("RecipeCheckList", json);
        editor.commit();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_recipe_instruction_top_element, parent,false);

        context = parent.getContext();

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
            loadCheckLists();

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<View> views = ((InstructionUnderAdapter)underlineCycleview.getAdapter()).getViews();

                    String state = recipeChecklist.getChecklistState(recipeID, position);
                    System.out.println(state);

                    if (state != null){
                        if(state.equals("true")){
                            instructionpoint.setTextColor(Color.parseColor("#696969"));
                            instructionpoint.setText(""+(position+1));
                            instructionpoint.setBackgroundResource(R.drawable.rounded_corner);
                            headlineElement.setTextColor(Color.parseColor("#696969"));
                            for (int i = 0; i < views.size();i++) {
                                views.get(i).findViewById(R.id.instructionPoint2).setBackgroundResource(R.drawable.rounded_corner);
                                ((TextView) views.get(i).findViewById(R.id.underlineElement)).setTextColor(Color.parseColor("#696969"));
                            }
                            recipeChecklist.updateChecklist(recipeID,position,false);
                        }else if(state.equals("false")){
                            instructionpoint.setText("\u2713");
                            instructionpoint.setTextColor(Color.parseColor("#ffffff"));
                            instructionpoint.setBackgroundResource(R.drawable.rounded_corner_green);
                            headlineElement.setTextColor(Color.parseColor("#B7B7B7"));
                            for (int i = 0; i < views.size();i++) {
                                views.get(i).findViewById(R.id.instructionPoint2).setBackgroundResource(R.drawable.done);
                                ((TextView) views.get(i).findViewById(R.id.underlineElement)).setTextColor(Color.parseColor("#B7B7B7"));
                            }
                            recipeChecklist.updateChecklist(recipeID,position,true);
                        }
                    } else{
                        recipeChecklist.addChecklist(recipeID, recipeInstructionDTO.size());
                        instructionpoint.setTextColor(Color.parseColor("#696969"));
                        instructionpoint.setBackgroundResource(R.drawable.rounded_corner);
                        headlineElement.setTextColor(Color.parseColor("#B7B7B7"));
                        for (int i = 0; i < views.size();i++) {
                            views.get(i).findViewById(R.id.instructionPoint2).setBackgroundResource(R.drawable.rounded_corner);
                            ((TextView) views.get(i).findViewById(R.id.underlineElement)).setTextColor(Color.parseColor("#B7B7B7"));
                        }
                        recipeChecklist.updateChecklist(recipeID, position,true);
                    }
                    saveChecklists();
                }
            });
        }

        public void bindView(int position){
            String state = recipeChecklist.getChecklistState(recipeID, position);
            headlineElement.setText(recipeInstructionDTO.get(position).getTitle());
            instructionpoint.setText(position+1+"");
            underlineCycleview.setAdapter(new InstructionUnderAdapter(recipeInstructionDTO.get(position).getInstructions(), state));
            underlineCycleview.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
            underlineCycleview.setLayoutFrozen(true);
            if(state != null){
                if (state.equals("true")) {
                    instructionpoint.setText("\u2713");
                    instructionpoint.setTextColor(Color.parseColor("#ffffff"));
                    instructionpoint.setBackgroundResource(R.drawable.rounded_corner_green);
                    headlineElement.setTextColor(Color.parseColor("#B7B7B7"));
                }
            }
        }

    }


}
