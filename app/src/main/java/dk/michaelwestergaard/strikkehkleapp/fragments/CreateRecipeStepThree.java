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
import android.widget.TextView;

import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import java.util.ArrayList;
import java.util.List;

import dk.michaelwestergaard.strikkehkleapp.DTO.RecipeDTO;
import dk.michaelwestergaard.strikkehkleapp.DTO.RecipeInstructionDTO;
import dk.michaelwestergaard.strikkehkleapp.R;

public class CreateRecipeStepThree extends Fragment implements Step, View.OnClickListener {

    private LayoutInflater inflater;
    private LinearLayout instructionLinearLayout;
    private Button newInstructionBtn;

    public CreateRecipeStepThree() {}

    public static CreateRecipeStepThree newInstance(String param1, String param2) {
        CreateRecipeStepThree fragment = new CreateRecipeStepThree();
        return fragment;
    }

    public RecipeDTO getData(RecipeDTO recipeDTO){

        List<RecipeInstructionDTO> instructionList = new ArrayList<RecipeInstructionDTO>();

        for(int i = 0; i < instructionLinearLayout.getChildCount(); i++){
            View view = instructionLinearLayout.getChildAt(i);
            EditText title = view.findViewById(R.id.create_recipe_instruction_title);
            LinearLayout underLinearLayout = view.findViewById(R.id.create_recipe_sub_instruction);

            List<String> underInstructionList = new ArrayList<String>();

            for(int n = 0; n < underLinearLayout.getChildCount(); n++){
                View viewUnder = instructionLinearLayout.getChildAt(i);
                EditText underInstructionTxt = viewUnder.findViewById(R.id.create_recipe_sub_instruction_text);
                underInstructionList.add(underInstructionTxt.getText().toString());
            }
            RecipeInstructionDTO recipeInstructionDTO = new RecipeInstructionDTO(title.getText().toString(), underInstructionList);
            instructionList.add(recipeInstructionDTO);
        }

        recipeDTO.setRecipeInstructionDTO(instructionList);

        return recipeDTO;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.inflater = inflater;

        View view = inflater.inflate(R.layout.fragment_create_recipe_step_three, container, false);

        instructionLinearLayout = view.findViewById(R.id.create_recipe_instruction_list);
        newInstructionBtn = view.findViewById(R.id.create_recipe_add_new_instruction);

        newInstructionBtn.setOnClickListener(this);

        return view;
    }

    @Nullable
    @Override
    public VerificationError verifyStep() {
        if(instructionLinearLayout.getChildCount() == 0) {
            return new VerificationError("Tilføj venligst et instruktionstrin!");
        } else if(instructionLinearLayout.getChildCount() != 0) {
            for(int i = 0; i < instructionLinearLayout.getChildCount(); i++){
                View view = instructionLinearLayout.getChildAt(i);
                EditText title = view.findViewById(R.id.create_recipe_instruction_title);

                if(title.getText().toString().equals("")) {
                    return new VerificationError("Tilføj venligst en tekst til alle trin!");
                }

                LinearLayout underLinearLayout = view.findViewById(R.id.create_recipe_sub_instruction);

                if(underLinearLayout.getChildCount() != 0) {
                    for (int n = 0; n < underLinearLayout.getChildCount(); n++) {
                        View viewUnder = instructionLinearLayout.getChildAt(i);
                        EditText underInstructionTxt = viewUnder.findViewById(R.id.create_recipe_sub_instruction_text);

                        if (underInstructionTxt.getText().toString().equals("")) {
                            return new VerificationError("Tilføj venligst en tekst til alle underlinjer!");
                        }
                    }
                }
            }
        }

        return null;
    }

    @Override
    public void onSelected() {}

    @Override
    public void onError(@NonNull VerificationError error) {}

    @Override
    public void onClick(View view) {
        final TextView instructionNumber;
        EditText inputField;
        Button removeInstructionBtn;
        Button addSubInstructionBtn;

        final EditText[] subInputField = new EditText[1];
        final Button[] removeSubInstructionBtn = new Button[1];
        final LinearLayout linearLayout;

        if(view.equals(newInstructionBtn)){
            View listElement = inflater.inflate(R.layout.create_recipe_instruction_element, null);

            instructionNumber = listElement.findViewById(R.id.create_recipe_instruction_number);
            inputField = listElement.findViewById(R.id.create_recipe_instruction_title);

            linearLayout = listElement.findViewById(R.id.create_recipe_sub_instruction);

            removeInstructionBtn = listElement.findViewById(R.id.create_recipe_instruction_remove_btn);
            addSubInstructionBtn = listElement.findViewById(R.id.create_recipe_add_sub_instruction);

            instructionNumber.setText(""+(instructionLinearLayout.getChildCount()+1));

            inputField.setHint("Nyt trin");

            removeInstructionBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    instructionLinearLayout.removeViewAt(0);

                    final int childCount = instructionLinearLayout.getChildCount();

                    for (int i = 0; i < childCount; i++) {
                        View v = instructionLinearLayout.getChildAt(i);
                        TextView instructionNumber = v.findViewById(R.id.create_recipe_instruction_number);
                        instructionNumber.setText(""+(i+1));
                    }
                }
            });

            addSubInstructionBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    View subElement = inflater.inflate(R.layout.create_recipe_instruction_sub_element, null);

                    subInputField[0] = subElement.findViewById(R.id.create_recipe_sub_instruction_text);
                    removeSubInstructionBtn[0] = subElement.findViewById(R.id.create_recipe_sub_instruction_remove_btn);

                    removeSubInstructionBtn[0].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            linearLayout.removeView((View) view.getParent());
                        }
                    });

                    linearLayout.addView(subElement, linearLayout.getChildCount());
                }
            });



            instructionLinearLayout.addView(listElement, instructionLinearLayout.getChildCount());

        }
    }
}
