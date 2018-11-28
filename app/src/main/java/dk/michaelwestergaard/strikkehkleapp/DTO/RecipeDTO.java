package dk.michaelwestergaard.strikkehkleapp.DTO;

import java.util.Date;

public class RecipeDTO {

    String recipeID;
    String categoryID;
    String userID;
    String title;
    double price;
    Date createdTimestamp;
    Date updatedTimestamp;

    public RecipeDTO(){}

    public RecipeDTO(String recipeID, String categoryID, String userID, String title, double price, Date createdTimestamp, Date updatedTimestamp) {
        this.recipeID = recipeID;
        this.categoryID = categoryID;
        this.userID = userID;
        this.title = title;
        this.price = price;
        this.createdTimestamp = createdTimestamp;
        this.updatedTimestamp = updatedTimestamp;
    }

    public RecipeDTO(String recipeID, String categoryID, String userID, String title, double price) {
        this.recipeID = recipeID;
        this.categoryID = categoryID;
        this.userID = userID;
        this.title = title;
        this.price = price;
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
}
