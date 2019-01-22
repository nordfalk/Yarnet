package dk.michaelwestergaard.strikkehkleapp.DTO;

public class RecipeFeedbackDTO {

    String userID;
    String userName;
    String userAvatar;
    String title;
    String comment;

    public RecipeFeedbackDTO(){}

    public RecipeFeedbackDTO(String userID, String userName, String userAvatar, String title, String comment) {
        this.userID = userID;
        this.userName = userName;
        this.userAvatar = userAvatar;
        this.title = title;
        this.comment = comment;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
