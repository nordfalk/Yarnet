package dk.michaelwestergaard.strikkehkleapp;

import java.util.ArrayList;
import java.util.List;

import dk.michaelwestergaard.strikkehkleapp.DTO.RecipeChecklistDTO;

public class RecipeChecklist {

    private List<RecipeChecklistDTO> checklist;
    private static RecipeChecklist instance = null;

    public RecipeChecklist(){}

    public static RecipeChecklist getInstance() {
        if (instance == null) {
            instance = new RecipeChecklist();
        }
        return instance;
    }


    List<RecipeChecklistDTO> getChecklist() {
        return checklist;
    }

    public boolean isChecklistCreated(String recipeID){
        for(RecipeChecklistDTO recipeChecklistDTO : getChecklist()){
            if(recipeChecklistDTO.getRecipeID().equals(recipeID)){
                return true;
            }
        }
        return false;
    }

    public String getChecklistState(String recipeID, int stepPosition){
        if(checklist != null) {
            for (RecipeChecklistDTO recipeChecklistDTO : checklist) {
                if (recipeChecklistDTO.getRecipeID().equals(recipeID)) {
                    boolean[] steps = recipeChecklistDTO.getSteps();
                    return String.valueOf(steps[stepPosition]);
                }
            }
        }
        return null;
    }

    public void setChecklist(List<RecipeChecklistDTO> checklist) {
        this.checklist = checklist;
    }

    public void addChecklist(String recipeID, int stepAmount){
        boolean[] steps = new boolean[stepAmount];
        RecipeChecklistDTO recipeChecklistDTO = new RecipeChecklistDTO(recipeID, steps);
        if(checklist == null)
            checklist = new ArrayList<RecipeChecklistDTO>();

        checklist.add(recipeChecklistDTO);
    }

    public boolean updateChecklist(String recipeID, int stepPosition, boolean state){
        for (int i = 0; i< checklist.size();i++) {
            if(checklist.get(i).getRecipeID().equals(recipeID)) {
                checklist.get(i).updateStep(stepPosition, state);
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "RecipeChecklist{" +
                "checklist=" + checklist +
                '}';
    }
}
