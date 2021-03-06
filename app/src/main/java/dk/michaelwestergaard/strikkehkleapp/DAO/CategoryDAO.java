package dk.michaelwestergaard.strikkehkleapp.DAO;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import dk.michaelwestergaard.strikkehkleapp.DTO.CategoryDTO;

public class CategoryDAO implements DAO<CategoryDTO> {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference("categories");

    @Override
    public String insert(CategoryDTO category) {
        String categoryID = databaseReference.push().getKey();
        category.setId(categoryID);
        databaseReference.child(categoryID).setValue(category);
        return categoryID;
    }

    @Override
    public boolean update(CategoryDTO category){
        databaseReference.child(category.getId()).setValue(category);
        return true;
    }

    @Override
    public boolean delete(CategoryDTO object) throws NotImplementedException {
        throw new NotImplementedException("Denne metode er ikke lavet");
    }

    @Override
    public DatabaseReference getReference() {
        return databaseReference;
    }

}