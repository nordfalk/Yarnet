package dk.michaelwestergaard.strikkehkleapp.DAO;

import java.util.List;

public interface DAO<T> {
    boolean insert(T object) throws NotImplementedException;
    boolean update(T object) throws NotImplementedException;
    boolean delete(T object) throws NotImplementedException;
    T get(String id) throws NotImplementedException;
    List<T> getAll() throws NotImplementedException;

    class NotImplementedException extends Exception {

        public NotImplementedException(String msg) {
            super(msg);
        }
    }
}
