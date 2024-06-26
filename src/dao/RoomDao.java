package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import entity.Hotel;
import entity.Room;

public class RoomDao extends BaseDao<Room> {
    private final HotelDao hotelDao;

    public RoomDao() {
        super("rooms");
        this.hotelDao = new HotelDao();
    }

    @Override
    protected Room mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        Room room = new Room();
        room.setId(resultSet.getInt("id"));
        room.setHotel(hotelDao.findById(resultSet.getInt("hotel_id")));
        room.setName(resultSet.getString("name"));
        room.setCapacity(resultSet.getInt("capacity"));
        room.setSizeSqm(resultSet.getInt("size_sqm"));
        room.setStock(resultSet.getInt("stock"));
        room.setAmenities(getAmenitiesForRoom(room.getId()));
        room.setAdultPriceUsd(resultSet.getInt("adult_price_usd"));
        room.setChildPriceUsd(resultSet.getInt("child_price_usd"));

        String roomTypeString = resultSet.getString("room_type");
        Room.RoomType roomType = Room.RoomType.fromString(roomTypeString);
        room.setRoomType(roomType);

        return room;
    }

    @Override
    protected void setParameters(PreparedStatement preparedStatement, Room room) throws SQLException {
        preparedStatement.setInt(1, room.getHotel().getId());
        preparedStatement.setString(2, room.getName());
        preparedStatement.setInt(3, room.getCapacity());
        preparedStatement.setInt(4, room.getSizeSqm());
        preparedStatement.setInt(5, room.getStock());
        preparedStatement.setString(6, room.getRoomType().toString());
        preparedStatement.setInt(7, room.getAdultPriceUsd());
        preparedStatement.setInt(8, room.getChildPriceUsd());
    }

    public ArrayList<Room> getHotelRooms(Hotel hotel) {
        ArrayList<Room> rooms = new ArrayList<>();
        String query = "SELECT * FROM rooms WHERE hotel_id = ?";

        try (PreparedStatement preparedStatement = getConnection().prepareStatement(query)) {
            preparedStatement.setInt(1, hotel.getId());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Room room = mapResultSetToEntity(resultSet);
                    rooms.add(room);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rooms;
    }

    private List<String> getAmenitiesForRoom(int roomId) {
        List<String> amenities = new ArrayList<>();
        String query = "SELECT ra.name FROM room_amenities ra " +
                "JOIN rooms_room_amenities rra ON ra.id = rra.room_amenity_id " +
                "WHERE rra.room_id = ?";

        try (PreparedStatement stmt = getConnection().prepareStatement(query)) {
            stmt.setInt(1, roomId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    amenities.add(rs.getString("name"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return amenities;
    }

    @Override
    public boolean save(Room room) {
        boolean result = false;
        String query = "INSERT INTO " +
                getTableName() +
                " (" +
                String.join(", ", getColumnNames()) +
                ") VALUES (" +
                getPlaceholders() +
                ")";

        try (PreparedStatement preparedStatement = getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            setParameters(preparedStatement, room);
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating room failed, no rows affected.");
            }

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    room.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating room failed, no ID obtained.");
                }
            }

            // Now we can update amenities after we have an ID
            result = updateAmenities(room.getId(), room.getAmenities());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public boolean update(Room room) {
        boolean result = false;
        String query = "UPDATE " +
                getTableName() +
                " SET " +
                getUpdateColumns() +
                " WHERE " +
                getIdColumnName() +
                " = ?";

        try (PreparedStatement preparedStatement = getConnection().prepareStatement(query)) {
            setParameters(preparedStatement, room);
            preparedStatement.setInt(getColumnNames().size() + 1, room.getId());
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating room failed, no rows affected.");
            }

            result = updateAmenities(room.getId(), room.getAmenities());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public boolean delete(int id) {
        try {
            // Delete related amenities first
            deleteAllAmenitiesFromRoom(id);

            // Proceed to delete the room
            String query = "DELETE FROM " + getTableName() + " WHERE " + getIdColumnName() + " = ?";
            try (PreparedStatement preparedStatement = getConnection().prepareStatement(query)) {
                preparedStatement.setInt(1, id);
                return preparedStatement.executeUpdate() != -1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateAmenities(int roomId, List<String> newAmenities) {
        List<String> currentAmenities = getAmenitiesForRoom(roomId);
        // Find amenities to delete
        for (String amenity : currentAmenities) {
            if (!newAmenities.contains(amenity)) {
                if (!deleteAmenityFromRoom(roomId, amenity)) {
                    return false; // Deletion failed
                }
            }
        }
        // Find amenities to add
        for (String amenity : newAmenities) {
            if (!currentAmenities.contains(amenity)) {
                if (!addAmenityToRoom(roomId, amenity)) {
                    return false; // Addition failed
                }
            }
        }
        return true;
    }

    public boolean addAmenityToRoom(int roomId, String amenityName) {
        String query = "INSERT INTO rooms_room_amenities (room_id, room_amenity_id) " +
                "SELECT ?, id FROM room_amenities WHERE name = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(query)) {
            stmt.setInt(1, roomId);
            stmt.setString(2, amenityName);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteAmenityFromRoom(int roomId, String amenityName) {
        String deleteQuery = "DELETE FROM rooms_room_amenities " +
                "WHERE room_id = ? " +
                "AND room_amenity_id = (SELECT id FROM room_amenities " +
                "WHERE name = ?)";
        try (PreparedStatement deleteStmt = getConnection().prepareStatement(deleteQuery)) {
            deleteStmt.setInt(1, roomId);
            deleteStmt.setString(2, amenityName);
            return deleteStmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean deleteAllAmenitiesFromRoom(int roomId) {
        String deleteQuery = "DELETE FROM rooms_room_amenities WHERE room_id = ?";
        try (PreparedStatement deleteStmt = getConnection().prepareStatement(deleteQuery)) {
            deleteStmt.setInt(1, roomId);
            return deleteStmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
