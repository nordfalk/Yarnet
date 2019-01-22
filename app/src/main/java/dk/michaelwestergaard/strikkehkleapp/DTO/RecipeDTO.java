package dk.michaelwestergaard.strikkehkleapp.DTO;

import android.net.Uri;

import java.util.Date;
import java.util.List;

public class RecipeDTO {

    public enum RecipeType {
        CROCHETING, KNITTING
    }

    public enum RecipeDifficulty {
        EASY, MEDIUM, HARD
    }

    private String recipeID;
    private String categoryID;
    private String subcategoryID;
    private String userID;
    private String title;
    private double price;
    private RecipeType recipeType;
    private RecipeDifficulty recipeDifficulty;
    private Date createdTimestamp;
    private Date updatedTimestamp;
    private int favouritedAmount;

    private RecipeInformationDTO recipeInformationDTO;
    private List<RecipeInstructionDTO> recipeInstructionDTO;
    private List<String> imageList;
    private List<Uri> imageUriList;
    private List<RecipeFeedbackDTO> feedbackList;

    public RecipeDTO(){}

    public RecipeDTO(String recipeID, String categoryID, String subcategoryID, String userID, String title, double price, RecipeType recipeType, RecipeDifficulty recipeDifficulty, Date createdTimestamp, Date updatedTimestamp, RecipeInformationDTO recipeInformationDTO, List<RecipeInstructionDTO> recipeInstructionDTO, List<String> imageList) {
        this.recipeID = recipeID;
        this.categoryID = categoryID;
        this.subcategoryID = subcategoryID;
        this.userID = userID;
        this.title = title;
        this.price = price;
        this.recipeType = recipeType;
        this.recipeDifficulty = recipeDifficulty;
        this.createdTimestamp = createdTimestamp;
        this.updatedTimestamp = updatedTimestamp;
        this.recipeInformationDTO = recipeInformationDTO;
        this.recipeInstructionDTO = recipeInstructionDTO;
        this.imageList = imageList;
        favouritedAmount = 0;
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

    public RecipeDifficulty getRecipeDifficulty() {
        return recipeDifficulty;
    }

    public void setRecipeDifficulty(RecipeDifficulty recipeDifficulty) {
        this.recipeDifficulty = recipeDifficulty;
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

    public List<RecipeInstructionDTO> getRecipeInstructionDTO() {
        return recipeInstructionDTO;
    }

    public void setRecipeInstructionDTO(List<RecipeInstructionDTO> recipeInstructionDTO) {
        this.recipeInstructionDTO = recipeInstructionDTO;
    }

    public List<String> getImageList() {
        return imageList;
    }

    public void setImageList(List<String> imageList) {
        this.imageList = imageList;
    }

    public int getFavouritedAmount() {
        return favouritedAmount;
    }

    public void increaseFavouritedAmount() {
        this.favouritedAmount++;
    }

    public void decreaseFavouritedAmount() {
        this.favouritedAmount--;
    }

    public List<Uri> getImageUriList() {
        return imageUriList;
    }

    public void setImageUriList(List<Uri> imageURIList) {
        this.imageUriList = imageURIList;
    }

    public List<RecipeFeedbackDTO> getFeedbackList() {
        return feedbackList;
    }

    public void setFeedbackList(List<RecipeFeedbackDTO> feedbackList) {
        this.feedbackList = feedbackList;
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
                ", recipeDifficulty=" + recipeDifficulty +
                ", createdTimestamp=" + createdTimestamp +
                ", updatedTimestamp=" + updatedTimestamp +
                ", favouritedAmount=" + favouritedAmount +
                ", recipeInformationDTO=" + recipeInformationDTO +
                ", recipeInstructionDTO=" + recipeInstructionDTO +
                ", imageList=" + imageList +
                '}';
    }
}
