package dk.michaelwestergaard.strikkehkleapp.DAO;

import java.util.List;

import dk.michaelwestergaard.strikkehkleapp.DTO.UserDTO;

public class UserDAO implements DAO<UserDTO> {
    @Override
    public boolean insert(UserDTO object) throws NotImplementedException {
        throw new NotImplementedException("Denne metode er ikke lavet");
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
    public UserDTO get(int id) throws NotImplementedException {
        throw new NotImplementedException("Denne metode er ikke lavet");
    }

    @Override
    public List<UserDTO> getAll() throws NotImplementedException {
        throw new NotImplementedException("Denne metode er ikke lavet");
    }
}
