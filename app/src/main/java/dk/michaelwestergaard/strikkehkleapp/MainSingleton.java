package dk.michaelwestergaard.strikkehkleapp;

public class MainSingleton {

    private static MainSingleton instance = null;
    private String searchValue = null;

    public static MainSingleton getInstance() {
        // To ensure only one instance is created
        if (instance == null)
        {
            instance = new MainSingleton();
        }
        return instance;
    }

    public String getSearchValue() {
        return searchValue;
    }

    public void setSearchValue(String searchValue) {
        this.searchValue = searchValue;
    }
}
