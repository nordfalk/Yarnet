package dk.michaelwestergaard.strikkehkleapp.DTO;

import java.util.Arrays;

public class RecipeChecklistDTO {
    private String recipeID;
    boolean[] steps;

    public RecipeChecklistDTO(String recipeID, boolean[] steps) {
        this.recipeID = recipeID;
        this.steps = steps;
    }

    public String getRecipeID() {
        return recipeID;
    }

    public void setRecipeID(String recipeID) {
        this.recipeID = recipeID;
    }

    public boolean[] getSteps() {
        return steps;
    }

    public void updateStep(int position, boolean state) {
        steps[position] = state;
    }

    @Override
    public String toString() {
        return "RecipeChecklistDTO{" +
                "recipeID='" + recipeID + '\'' +
                ", steps=" + Arrays.toString(steps) +
                '}';
    }
}
