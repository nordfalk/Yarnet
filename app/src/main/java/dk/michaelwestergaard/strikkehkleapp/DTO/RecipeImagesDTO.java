package dk.michaelwestergaard.strikkehkleapp.DTO;

import java.util.List;

public class RecipeImagesDTO {

    private List<String> imageID;

    public RecipeImagesDTO() {}

    public RecipeImagesDTO(List<String> imageID) {
        this.imageID = imageID;
    }

    public List<String> getImageID() {
        return imageID;
    }

    public void setImageID(List<String> imageID) {
        this.imageID = imageID;
    }

    @Override
    public String toString() {
        return "RecipeImagesDTO{" +
                ", imageID=" + imageID +
                '}';
    }
}
