package dk.michaelwestergaard.strikkehkleapp;

import com.crashlytics.android.Crashlytics;

import dk.michaelwestergaard.strikkehkleapp.DTO.UserDTO;

public class MainSingleton {

    private static MainSingleton instance = null;
    private UserDTO user = null;
    private String searchValue = null;

    public static MainSingleton getInstance() {
        // To ensure only one instance is created
        if (instance == null)
        {
            instance = new MainSingleton();
        }
        return instance;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
        Crashlytics.setUserIdentifier(user.getUserID());
        Crashlytics.setUserEmail(user.getEmail());
        Crashlytics.setUserName(user.getFirst_name() + " " + user.getLast_name());
    }

    public String getSearchValue() {
        return searchValue;
    }

    public void setSearchValue(String searchValue) {
        this.searchValue = searchValue;
    }
}
