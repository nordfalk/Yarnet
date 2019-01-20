package dk.michaelwestergaard.strikkehkleapp.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import java.util.ArrayList;
import java.util.List;

import dk.michaelwestergaard.strikkehkleapp.DTO.RecipeDTO;
import dk.michaelwestergaard.strikkehkleapp.R;

public class EditRecipeStepTwo extends Fragment implements Step, View.OnClickListener {

    private LayoutInflater inflater;
    private LinearLayout materialLinearLayout, toolLinearLayout;
    private Button newMaterialBtn, newToolBtn;

    public EditRecipeStepTwo() {}

    public RecipeDTO getData(RecipeDTO recipeDTO){

        List<String> materials = new ArrayList<String>();
        List<String> tools = new ArrayList<String>();

        for(int i = 0; i < materialLinearLayout.getChildCount(); i++){
            View view = materialLinearLayout.getChildAt(i);
            EditText materialTxt = view.findViewById(R.id.create_recipe_instruction_title);
            materials.add(materialTxt.getText().toString());
        }

        for(int i = 0; i < toolLinearLayout.getChildCount(); i++){
            View view = toolLinearLayout.getChildAt(i);
            EditText material = view.findViewById(R.id.create_recipe_instruction_title);
            tools.add(material.getText().toString());
        }

        recipeDTO.getRecipeInformationDTO().setMaterials(materials);
        recipeDTO.getRecipeInformationDTO().setTools(tools);

        return recipeDTO;
    }

    public static EditRecipeStepTwo newInstance(String param1, String param2) {
        EditRecipeStepTwo fragment = new EditRecipeStepTwo();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.inflater = inflater;

        View view = inflater.inflate(R.layout.fragment_edit_recipe_step_two, container, false);

        materialLinearLayout = view.findViewById(R.id.edit_recipe_material_list);
        toolLinearLayout = view.findViewById(R.id.edit_recipe_tool_list);

        newMaterialBtn = view.findViewById(R.id.edit_recipe_add_new_material);
        newToolBtn = view.findViewById(R.id.edit_recipe_add_new_tool);

        newMaterialBtn.setOnClickListener(this);
        newToolBtn.setOnClickListener(this);

        return view;
    }

    public void removeMaterialField(View view){
        materialLinearLayout.removeView((View) view.getParent());
    }

    public void removeToolField(View view){
        toolLinearLayout.removeView((View) view.getParent());
    }


    @Nullable
    @Override
    public VerificationError verifyStep() {
        if(materialLinearLayout.getChildCount() == 0) {
            return new VerificationError("Tilføj venligst et materiale!");
        } else if(materialLinearLayout.getChildCount() != 0) {
            for(int i = 0; i < materialLinearLayout.getChildCount(); i++){
                View view = materialLinearLayout.getChildAt(i);
                EditText materialTxt = view.findViewById(R.id.create_recipe_instruction_title);
                if(materialTxt.getText().toString().equals("")) {
                    return new VerificationError("Tilføj venligst en titel til alle materialer!");
                }
            }
        }

        if(toolLinearLayout.getChildCount() == 0) {
            return new VerificationError("Tilføj venligst et redskab!");
        } else if(toolLinearLayout.getChildCount() != 0) {
            for(int i = 0; i < toolLinearLayout.getChildCount(); i++){
                View view = toolLinearLayout.getChildAt(i);
                EditText editTxt = view.findViewById(R.id.create_recipe_instruction_title);
                if(editTxt.getText().toString().equals("")) {
                    return new VerificationError("Tilføj venligst en titel til alle redskaber!");
                }
            }
        }

        return null;
    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }

    @Override
    public void onClick(View view) {
        View listElement;
        EditText inputField;
        Button removeBtn;

        if(view.equals(newMaterialBtn)){
            listElement = inflater.inflate(R.layout.recipe_new_field, null);

            inputField = listElement.findViewById(R.id.create_recipe_instruction_title);
            removeBtn = listElement.findViewById(R.id.create_recipe_instruction_remove_btn);

            inputField.setHint("Nyt Materiale");
            removeBtn.setOnClickListener(this);

            materialLinearLayout.addView(listElement, materialLinearLayout.getChildCount());
        } else if(view.equals(newToolBtn)){
            listElement = inflater.inflate(R.layout.recipe_new_field, null);

            inputField = listElement.findViewById(R.id.create_recipe_instruction_title);
            removeBtn = listElement.findViewById(R.id.create_recipe_instruction_remove_btn);

            inputField.setHint("Nyt Redskab");
            removeBtn.setOnClickListener(this);

            toolLinearLayout.addView(listElement, toolLinearLayout.getChildCount());
        } else if(view.getId() == R.id.create_recipe_instruction_remove_btn){
            materialLinearLayout.removeView((View) view.getParent());
            toolLinearLayout.removeView((View) view.getParent());
            //TODO: Find ud fra hvordan man kan finde hvilket id den er inde i.
        }
    }
}
