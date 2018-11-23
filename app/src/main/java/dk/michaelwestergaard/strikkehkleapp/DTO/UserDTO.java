package dk.michaelwestergaard.strikkehkleapp.DTO;

public class UserDTO {

    int userID;
    String email;
    String first_name;
    String first_last;
    String avatar;
    // Måske også role
    int status;

    public UserDTO(int userID, String email, String first_name, String first_last, String avatar, int status) {
        this.userID = userID;
        this.email = email;
        this.first_name = first_name;
        this.first_last = first_last;
        this.avatar = avatar;
        this.status = status;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
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

    public String getFirst_last() {
        return first_last;
    }

    public void setFirst_last(String first_last) {
        this.first_last = first_last;
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
                ", first_last='" + first_last + '\'' +
                ", avatar='" + avatar + '\'' +
                ", status=" + status +
                '}';
    }
}
