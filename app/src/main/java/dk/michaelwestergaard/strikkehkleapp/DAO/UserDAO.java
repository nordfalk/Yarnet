package dk.michaelwestergaard.strikkehkleapp.DAO;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import dk.michaelwestergaard.strikkehkleapp.DTO.UserDTO;

public class UserDAO implements DAO<UserDTO> {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getReference("users");

    @Override
    public boolean insert(UserDTO user) {
        if(get(user.getUserID()) == null){
            databaseReference.child(user.getUserID()).setValue(user);
        } else {
            return false;
        }
        return true;
    }

    @Override
    public boolean update(UserDTO object) throws NotImplementedException {
        throw new NotImplementedException("Denne metode er ikke lavet");
    }

    @Override
    public boolean delete(UserDTO object) throws NotImplementedException {
        throw new NotImplementedException("Denne metode er ikke lavet");
    }

    @Override
    public UserDTO get(final String userID) {
        final UserDTO[] userDTO = new UserDTO[1];
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    if(snapshot.getKey().equals(userID)){
                        userDTO[0] = snapshot.getValue(UserDTO.class);
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
    public List<UserDTO> getAll() throws NotImplementedException {
        throw new NotImplementedException("Denne metode er ikke lavet");
    }
}
