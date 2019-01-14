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

import dk.michaelwestergaard.strikkehkleapp.R;

public class CreateRecipeStepThree extends Fragment implements Step, View.OnClickListener {

    LayoutInflater inflater;
    LinearLayout instructionLinearLayout;
    Button newInstructionBtn;

    public CreateRecipeStepThree() {}

    public static CreateRecipeStepThree newInstance(String param1, String param2) {
        CreateRecipeStepThree fragment = new CreateRecipeStepThree();
        return fragment;
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
        return null;
    }

    @Override
    public void onSelected() {}

    @Override
    public void onError(@NonNull VerificationError error) {}

    @Override
    public void onClick(View view) {
        TextView instructionNumber;
        EditText inputField, subInputField;
        Button removeInstructionBtn;

        if(view.equals(newInstructionBtn)){
            View listElement = inflater.inflate(R.layout.create_recipe_instruction_element, null);

            instructionNumber = listElement.findViewById(R.id.create_recipe_instruction_number);
            inputField = listElement.findViewById(R.id.create_recipe_instruction_title);

            removeInstructionBtn = listElement.findViewById(R.id.create_recipe_instruction_remove_btn);

            instructionNumber.setText(""+(instructionLinearLayout.getChildCount()+1));

            inputField.setHint("Nyt trin");

            removeInstructionBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    instructionLinearLayout.removeView((View) view.getParent());
                }
            });

            instructionLinearLayout.addView(listElement, instructionLinearLayout.getChildCount());

        }
    }
}
