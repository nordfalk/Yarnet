package dk.michaelwestergaard.strikkehkleapp;

import java.util.ArrayList;
import java.util.List;
import dk.michaelwestergaard.strikkehkleapp.DTO.RecipeChecklistDTO;

public class RecipeChecklist {
    List<RecipeChecklistDTO> checklist;
    private static RecipeChecklist instance = null;

    public RecipeChecklist() {
        checklist = new ArrayList<RecipeChecklistDTO>();
    }

    public static RecipeChecklist getInstance() {
        if (instance == null) {
            return new RecipeChecklist();
        }
        return instance;
    }

    public List<RecipeChecklistDTO> getChecklist() {
        return checklist;
    }

    public String getChecklistState(String recipeID, int stepPosition){
        List<RecipeChecklistDTO> checklist = getChecklist();
        for (int i = 0; i<checklist.size();i++){
            if(checklist.get(i).getRecipeID().equals(recipeID)){
                return String.valueOf(checklist.get(i).getSteps()[stepPosition]);
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
        getChecklist().add(recipeChecklistDTO);
    }

    public boolean updateChecklist(String recipeID, int stepPosition, boolean state){
        for (int i = 0; i<getChecklist().size();i++) {
            if(getChecklist().get(i).getRecipeID().contains(recipeID)) {
                getChecklist().get(i).updateStep(stepPosition, state);
                return true;
            }
        }
        return false;
    }


}
