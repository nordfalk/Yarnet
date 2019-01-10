package dk.michaelwestergaard.strikkehkleapp.DAO;

public interface DAO<T> {
    boolean insert(T object) throws NotImplementedException;
    boolean update(T object) throws NotImplementedException;
    boolean delete(T object) throws NotImplementedException;

    class NotImplementedException extends Exception {

        public NotImplementedException(String msg) {
            super(msg);
        }
    }
}