package dao;

import entity.Reservation;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;

public class ReservationDao extends BaseDao<Reservation> {
    private final RoomDao roomDao;
    private final GuestDao guestDao;

    public ReservationDao() {
        super("reservations");
        this.roomDao = new RoomDao();
        this.guestDao = new GuestDao();
    }

    @Override
    protected Reservation mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        Reservation reservation = new Reservation();
        reservation.setId(resultSet.getInt("id"));
        reservation.setRoom(roomDao.findById(resultSet.getInt("room_id")));
        reservation.setPrimaryGuest(guestDao.findById(resultSet.getInt("primary_guest_id")));
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
        preparedStatement.setInt(2, reservation.getPrimaryGuest().getId());
        preparedStatement.setDate(3, Date.valueOf(reservation.getStartDate()));
        preparedStatement.setDate(4, Date.valueOf(reservation.getEndDate()));
        preparedStatement.setBigDecimal(5, reservation.getCalculatedCostUsd());
        preparedStatement.setInt(6, reservation.getNumAdults());
        preparedStatement.setInt(7, reservation.getNumChildren());
    }
}
