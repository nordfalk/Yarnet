package dk.michaelwestergaard.strikkehkleapp.DTO;

public class UserDTO {

    String userID;
    String email;
    String first_name;
    String last_name;
    String avatar;
    // Måske også role
    int status;

    public UserDTO(){}

    public UserDTO(String userID, String email, String first_name, String last_name, String avatar, int status) {
        this.userID = userID;
        this.email = email;
        this.first_name = first_name;
        this.last_name = last_name;
        this.avatar = avatar;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "userID=" + userID +
                ", email='" + email + '\'' +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", avatar='" + avatar + '\'' +
                ", status=" + status +
                '}';
    }
}
