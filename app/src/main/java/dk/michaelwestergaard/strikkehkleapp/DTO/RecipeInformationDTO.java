package dk.michaelwestergaard.strikkehkleapp.DTO;

public class RecipeInformationDTO {

    private String description;
    private String materials;
    private String tools;

    public RecipeInformationDTO() {}

    public RecipeInformationDTO(String description, String materials, String tools) {
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

    public String getMaterials() {
        return materials;
    }

    public void setMaterials(String materials) {
        this.materials = materials;
    }

    public String getTools() {
        return tools;
    }

    public void setTools(String tools) {
        this.tools = tools;
    }

    @Override
    public String toString() {
        return "RecipeInformationDTO{" +
                "description='" + description + '\'' +
                ", materials='" + materials + '\'' +
                ", tools='" + tools + '\'' +
                '}';
    }
}
