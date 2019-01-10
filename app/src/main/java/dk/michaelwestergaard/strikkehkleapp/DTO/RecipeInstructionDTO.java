package dk.michaelwestergaard.strikkehkleapp.DTO;


import java.util.List;

public class RecipeInstructionDTO {

    String title;
    List<String> instructions;

    public RecipeInstructionDTO(String title, List<String> instructions) {
        this.title = title;
        this.instructions = instructions;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getInstructions() {
        return instructions;
    }

    public void setInstructions(List<String> instructions) {
        this.instructions = instructions;
    }

    @Override
    public String toString() {
        return "RecipeInstructionDTO{" +
                "title='" + title + '\'' +
                ", instructions=" + instructions +
                '}';
    }
}