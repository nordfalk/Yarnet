package dk.michaelwestergaard.strikkehkleapp.DTO;

import java.util.ArrayList;
import java.util.List;

public class UserDTO {

    private String userID;
    private String email;
    private String first_name;
    private String last_name;
    private String avatar;
    private String type;
    private List<String> savedRecipes = new ArrayList<>();
    private List<String> boughtRecipes = new ArrayList<>();
    // Måske også role
    int status;

    public UserDTO(){}

    public UserDTO(String userID, String email, String first_name, String last_name, String avatar, String type, int status) {
        this.userID = userID;
        this.email = email;
        this.first_name = first_name;
        this.last_name = last_name;
        this.avatar = avatar;
        this.type = type;
        this.status = status;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<String> getSavedRecipes() {
        return savedRecipes;
    }

    public void setSavedRecipes(List<String> savedRecipes) {
        this.savedRecipes = savedRecipes;
    }

    public List<String> getBoughtRecipes() {
        return boughtRecipes;
    }

    public void setBoughtRecipes(List<String> boughtRecipes) {
        this.boughtRecipes = boughtRecipes;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "userID=" + userID +
                ", email='" + email + '\'' +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", avatar='" + avatar + '\'' +
                ", type='" + type + '\'' +
                ", status=" + status +
                '}';
    }
}