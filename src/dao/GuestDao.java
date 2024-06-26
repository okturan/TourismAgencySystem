package dao;

import entity.Guest;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GuestDao extends BaseDao<Guest> {
    public GuestDao() {
        super("guests");
    }

    @Override
    protected Guest mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        Guest guest = new Guest();
        guest.setId(resultSet.getInt("id"));
        guest.setName(resultSet.getString("name"));
        guest.setPhone(resultSet.getString("phone"));
        guest.setEmail(resultSet.getString("email"));
        guest.setIdentification(resultSet.getString("identification"));
        return guest;
    }

    @Override
    protected void setParameters(PreparedStatement preparedStatement, Guest guest) throws SQLException {
        preparedStatement.setString(1, guest.getName());
        preparedStatement.setString(2, guest.getPhone());
        preparedStatement.setString(3, guest.getEmail());
        preparedStatement.setString(4, guest.getIdentification());
    }
}
