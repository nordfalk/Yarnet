package dk.michaelwestergaard.strikkehkleapp.DTO;

import java.util.List;

public class CategoryDTO {

    private String id;
    private String name;
    private int position;
    private boolean showInTopMenu;
    private boolean showSubcategories;
    private boolean showOnDiscover;
    private List<SubcategoryDTO> subcategoryList;

    public CategoryDTO(){}

    public CategoryDTO(String id, String name, int position, boolean showInTopMenu, boolean showSubcategories, boolean showOnDiscover, List<SubcategoryDTO> subcategoryList) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.showInTopMenu = showInTopMenu;
        this.showSubcategories = showSubcategories;
        this.showOnDiscover = showOnDiscover;
        this.subcategoryList = subcategoryList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean isShowInTopMenu() {
        return showInTopMenu;
    }

    public void setShowInTopMenu(boolean showInTopMenu) {
        this.showInTopMenu = showInTopMenu;
    }

    public boolean isShowSubcategories() {
        return showSubcategories;
    }

    public void setShowSubcategories(boolean showSubcategories) {
        this.showSubcategories = showSubcategories;
    }

    public boolean isShowOnDiscover() {
        return showOnDiscover;
    }

    public void setShowOnDiscover(boolean showOnDiscover) {
        this.showOnDiscover = showOnDiscover;
    }

    public List<SubcategoryDTO> getSubcategoryList() {
        return subcategoryList;
    }

    public void setSubcategoryList(List<SubcategoryDTO> subcategoryList) {
        this.subcategoryList = subcategoryList;
    }

}
