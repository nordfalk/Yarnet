package dk.michaelwestergaard.strikkehkleapp.DTO;

import java.util.Date;
import java.util.List;

public class RecipeDTO {

    public enum RecipeType {
        CROCHETING, KNITTING
    }

    private String recipeID;
    private String categoryID;
    private String subcategoryID;
    private String userID;
    private String title;
    private double price;
    private RecipeType recipeType;
    private Date createdTimestamp;
    private Date updatedTimestamp;

    private RecipeInformationDTO recipeInformationDTO;
    private List<RecipeInstructionDTO> instructionList;

    public RecipeDTO(){}

    public RecipeDTO(String recipeID, String categoryID, String subcategoryID, String userID, String title, double price, RecipeType recipeType, Date createdTimestamp, Date updatedTimestamp, RecipeInformationDTO recipeInformationDTO, List<RecipeInstructionDTO> instructionList) {
        this.recipeID = recipeID;
        this.categoryID = categoryID;
        this.subcategoryID = subcategoryID;
        this.userID = userID;
        this.title = title;
        this.price = price;
        this.recipeType = recipeType;
        this.createdTimestamp = createdTimestamp;
        this.updatedTimestamp = updatedTimestamp;
        this.recipeInformationDTO = recipeInformationDTO;
        this.instructionList = instructionList;
    }

    public String getRecipeID() {
        return recipeID;
    }

    public void setRecipeID(String recipeID) {
        this.recipeID = recipeID;
    }

    public String getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }

    public String getSubcategoryID() {
        return subcategoryID;
    }

    public void setSubcategoryID(String subcategoryID) {
        this.subcategoryID = subcategoryID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public RecipeType getRecipeType() {
        return recipeType;
    }

    public void setRecipeType(RecipeType recipeType) {
        this.recipeType = recipeType;
    }

    public Date getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(Date createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public Date getUpdatedTimestamp() {
        return updatedTimestamp;
    }

    public void setUpdatedTimestamp(Date updatedTimestamp) {
        this.updatedTimestamp = updatedTimestamp;
    }

    public RecipeInformationDTO getRecipeInformationDTO() {
        return recipeInformationDTO;
    }

    public void setRecipeInformationDTO(RecipeInformationDTO recipeInformationDTO) {
        this.recipeInformationDTO = recipeInformationDTO;
    }

    public List<RecipeInstructionDTO> getInstructionList() {
        return instructionList;
    }

    public void setInstructionList(List<RecipeInstructionDTO> instructionList) {
        this.instructionList = instructionList;
    }

    @Override
    public String toString() {
        return "RecipeDTO{" +
                "recipeID='" + recipeID + '\'' +
                ", categoryID='" + categoryID + '\'' +
                ", subcategoryID='" + subcategoryID + '\'' +
                ", userID='" + userID + '\'' +
                ", title='" + title + '\'' +
                ", price=" + price +
                ", recipeType=" + recipeType +
                ", createdTimestamp=" + createdTimestamp +
                ", updatedTimestamp=" + updatedTimestamp +
                ", recipeInformationDTO=" + recipeInformationDTO +
                ", instructionList=" + instructionList +
                '}';
    }
}
