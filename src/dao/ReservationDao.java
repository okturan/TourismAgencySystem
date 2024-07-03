package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import entity.Reservation;

public class ReservationDao extends BaseDao<Reservation> {
    private final RoomDao roomDao;

    public ReservationDao() {
        super("reservations");
        this.roomDao = new RoomDao();
    }

    public boolean save(Reservation reservation) {
        String insertSql = "INSERT INTO reservations "
                + "(room_id, full_name, phone, email, identification, start_date, end_date, calculated_cost_usd, num_adults, num_children) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        // Connections, statements, and result sets should be managed properly
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertSql)) {

            setParameters(preparedStatement, reservation);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(Reservation reservation) {
        String updateSql = "UPDATE reservations SET "
                + "room_id = ?, full_name = ?, phone = ?, email = ?, identification = ?, "
                + "start_date = ?, end_date = ?, calculated_cost_usd = ?, num_adults = ?, num_children = ? "
                + "WHERE id = ?";

        // Connections, statements, and result sets should be managed properly
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(updateSql)) {

            setParameters(preparedStatement, reservation);
            // Assuming the id is part of your Reservation object and the last parameter is the ID
            preparedStatement.setInt(11, reservation.getId());
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected Reservation mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        Reservation reservation = new Reservation();
        reservation.setId(resultSet.getInt("id"));
        reservation.setRoom(roomDao.findById(resultSet.getInt("room_id")));
        reservation.setFullName(resultSet.getString("full_name"));
        reservation.setPhone(resultSet.getString("phone"));
        reservation.setEmail(resultSet.getString("email"));
        reservation.setIdentification(resultSet.getString("identification"));
        reservation.setStartDate(resultSet.getDate("start_date").toLocalDate());
        reservation.setEndDate(resultSet.getDate("end_date").toLocalDate());
        reservation.setCalculatedCostUsd(resultSet.getBigDecimal("calculated_cost_usd"));
        reservation.setNumAdults(resultSet.getInt("num_adults"));
        reservation.setNumChildren(resultSet.getInt("num_children"));
        return reservation;
    }

    @Override
    protected void setParameters(PreparedStatement preparedStatement, Reservation reservation) throws SQLException {
        preparedStatement.setInt(1, reservation.getRoom().getId());
        preparedStatement.setString(2, reservation.getFullName());
        preparedStatement.setString(3, reservation.getPhone());
        preparedStatement.setString(4, reservation.getEmail());
        preparedStatement.setString(5, reservation.getIdentification());
        preparedStatement.setDate(6, java.sql.Date.valueOf(reservation.getStartDate()));
        preparedStatement.setDate(7, java.sql.Date.valueOf(reservation.getEndDate()));
        preparedStatement.setBigDecimal(8, reservation.getCalculatedCostUsd());
        preparedStatement.setInt(9, reservation.getNumAdults());
        preparedStatement.setInt(10, reservation.getNumChildren());
    }
}
