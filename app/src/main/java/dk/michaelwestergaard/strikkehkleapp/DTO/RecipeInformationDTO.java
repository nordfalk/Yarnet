package dk.michaelwestergaard.strikkehkleapp.DTO;

import java.util.List;

public class RecipeInformationDTO {

    private String description;
    private List<String> materials;
    private List<String> tools;

    public RecipeInformationDTO() {}

    public RecipeInformationDTO(String description, List<String> materials, List<String> tools) {
        this.description = description;
        this.materials = materials;
        this.tools = tools;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getMaterials() {
        return materials;
    }

    public void setMaterials(List<String> materials) {
        this.materials = materials;
    }

    public List<String> getTools() {
        return tools;
    }

    public void setTools(List<String> tools) {
        this.tools = tools;
    }

    @Override
    public String toString() {
        return "RecipeInformationDTO{" +
                "description='" + description + '\'' +
                ", materials=" + materials +
                ", tools=" + tools +
                '}';
    }
}
