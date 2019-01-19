package dk.michaelwestergaard.strikkehkleapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import dk.michaelwestergaard.strikkehkleapp.DTO.RecipeInstructionDTO;

public class fragment_recipe_instruction extends Fragment {
    RecyclerView instructionRecycleView;
    TextView headlineElement,underlineElement,instructionPoint;

    List<RecipeInstructionDTO> recipeInstructionDTO;
    String recipeID;

    public fragment_recipe_instruction() {
        // Required empty public constructor
    }

    public static fragment_recipe_instruction newInstance(String recipeID,List<RecipeInstructionDTO> recipeInstructionDTO) {
        fragment_recipe_instruction fragment = new fragment_recipe_instruction();
        fragment.recipeInstructionDTO = recipeInstructionDTO;
        fragment.recipeID = recipeID;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Opskrift opskrift = new Opskrift();
        View view = inflater.inflate(R.layout.fragment_recipe_instruction, container, false);

        if(opskrift.bought = true){
            instructionRecycleView = view.findViewById(R.id.recipeInstructionList);

            InstructionAdapter instructionAdapter = new InstructionAdapter(recipeID, recipeInstructionDTO);
            instructionRecycleView.setAdapter(instructionAdapter);
            instructionRecycleView.setLayoutManager(new LinearLayoutManager(container.getContext(), LinearLayoutManager.VERTICAL, false));
        }

        return view;
    }


}

