package dk.michaelwestergaard.strikkehkleapp.DAO;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import dk.michaelwestergaard.strikkehkleapp.DTO.UserDTO;

public class UserDAO implements DAO<UserDTO> {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getReference("users");
    StorageReference storageRef = FirebaseStorage.getInstance().getReference("users");

    @Override
    public String insert(UserDTO user) {
        if(get(user.getUserID()) == null){
            databaseReference.child(user.getUserID()).setValue(user);
        } else {
            return "";
        }
        return user.getUserID();
    }

    @Override
    public boolean update(UserDTO user) {
        databaseReference.child(user.getUserID()).setValue(user);
        return true;
    }

    @Override
    public boolean delete(UserDTO object) throws NotImplementedException {
        throw new NotImplementedException("Denne metode er ikke lavet");
    }

    public UserDTO get(final String userID) {
        final UserDTO[] userDTO = new UserDTO[1];
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    if(snapshot.getKey().equals(userID)){
                        System.out.println("User found! " + snapshot.getKey());
                        userDTO[0] = snapshot.getValue(UserDTO.class);
                        System.out.println(userDTO[0]);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Kunne ikke få fat i brugeren
            }
        });
        return userDTO[0];
    }

    @Override
    public DatabaseReference getReference() {
        return databaseReference;
    }
}
