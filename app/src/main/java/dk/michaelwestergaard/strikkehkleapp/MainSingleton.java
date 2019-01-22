package dk.michaelwestergaard.strikkehkleapp;

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
    }

    public String getSearchValue() {
        return searchValue;
    }

    public void setSearchValue(String searchValue) {
        this.searchValue = searchValue;
    }
}
