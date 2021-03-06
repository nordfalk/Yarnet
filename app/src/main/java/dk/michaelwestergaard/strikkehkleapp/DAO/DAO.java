package dk.michaelwestergaard.strikkehkleapp.DAO;

import com.google.firebase.database.DatabaseReference;

public interface DAO<T> {
    String insert(T object) throws NotImplementedException;
    boolean update(T object) throws NotImplementedException;
    boolean delete(T object) throws NotImplementedException;
    DatabaseReference getReference() throws NotImplementedException;

    class NotImplementedException extends Exception {

        public NotImplementedException(String msg) {
            super(msg);
        }
    }
}