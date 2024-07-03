package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import core.Db;
import entity.Hotel;
import entity.Room;
import entity.RoomPriceSummary;

public class RoomDao extends BaseDao<Room> {
    private final HotelDao hotelDao;

    public RoomDao() {
        super("rooms");
        this.hotelDao = new HotelDao();
    }

    public ArrayList<RoomPriceSummary> getRoomPrices(Integer roomId) {
        ArrayList<RoomPriceSummary> roomPrices = new ArrayList<>();

        String query =
                "SELECT s.name AS season_name, bt.name AS board_type_name, rp.adult_price_usd, rp.child_price_usd " +
                        "FROM room_prices rp " +
                        "JOIN seasons s ON rp.season_id = s.id " +
                        "JOIN board_types bt ON rp.board_type_id = bt.id " +
                        "WHERE rp.room_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, roomId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    RoomPriceSummary priceSummary = new RoomPriceSummary(
                            rs.getString("season_name"),
                            rs.getString("board_type_name"),
                            rs.getBigDecimal("adult_price_usd"),
                            rs.getBigDecimal("child_price_usd")
                    );
                    roomPrices.add(priceSummary);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return roomPrices;
    }

    public boolean saveRoomPrices(int roomId, List<RoomPriceSummary> roomPrices, Connection conn) throws SQLException {
        String deleteQuery = "DELETE FROM room_prices WHERE room_id = ?";
        String insertQuery = "INSERT INTO room_prices (room_id, season_id, board_type_id, adult_price_usd, child_price_usd) " +
                "VALUES (?, (SELECT id FROM seasons WHERE name = ?), (SELECT id FROM board_types WHERE name = ?), ?, ?)";

        try {
            // Use the provided connection, do not set auto commit again
            try (PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery)) {
                deleteStmt.setInt(1, roomId);
                deleteStmt.executeUpdate();
            }

            try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                for (RoomPriceSummary price : roomPrices) {
                    insertStmt.setInt(1, roomId);
                    insertStmt.setString(2, price.getSeasonName());
                    insertStmt.setString(3, price.getBoardTypeName());
                    insertStmt.setBigDecimal(4, price.getAdultPriceUsd());
                    insertStmt.setBigDecimal(5, price.getChildPriceUsd());
                    insertStmt.addBatch();  // Batch them for efficiency
                }
                insertStmt.executeBatch();
            }

            // No need to commit or rollback here because it will be done in the calling method
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;  // Rethrow the exception to let the calling method handle rollback
        }
    }


    private boolean saveRoom(Room room, Connection conn) throws SQLException {
        String query = "INSERT INTO " +
                getTableName() +
                " (" +
                String.join(", ", getColumnNames()) +
                ") VALUES (" +
                getPlaceholders() +
                ")";
        try (PreparedStatement preparedStatement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
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
            return true;
        }
    }
    @Override
    public boolean save(Room room) {
        Connection conn = null;
        try {
            conn = Db.getInstance();
            conn.setAutoCommit(false);

            boolean isRoomSaved = saveRoom(room, conn);
            boolean arePricesSaved = isRoomSaved && saveRoomPrices(room.getId(), room.getRoomPrices(), conn);

            if (isRoomSaved && arePricesSaved) {
                conn.commit();
                return true;
            } else {
                conn.rollback();
                return false;
            }
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true); // make sure to reset auto commit
                    conn.close();
                } catch (SQLException closeEx) {
                    closeEx.printStackTrace();
                }
            }
        }
    }

    @Override
    public boolean update(Room room) {
        Connection conn = null;
        boolean result = false;
        String query = "UPDATE " + getTableName() +
                " SET " + getUpdateColumns() +
                " WHERE " + getIdColumnName() + " = ?";

        try {
            conn = Db.getInstance();
            conn.setAutoCommit(false);

            try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
                setParameters(preparedStatement, room);
                preparedStatement.setInt(getColumnNames().size() + 1, room.getId());
                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows == 0) {
                    throw new SQLException("Updating room failed, no rows affected.");
                }
            }

            boolean amenitiesUpdated = updateAmenities(room.getId(), room.getAmenities());
            boolean pricesSaved = saveRoomPrices(room.getId(), room.getRoomPrices(), conn);

            if (amenitiesUpdated && pricesSaved) {
                conn.commit();
                result = true;
            } else {
                conn.rollback();
            }
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException closeEx) {
                    closeEx.printStackTrace();
                }
            }
        }

        return result;
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
    }

    public ArrayList<Room> getHotelRooms(Hotel hotel) {
        ArrayList<Room> rooms = new ArrayList<>();
        String query = "SELECT * FROM rooms WHERE hotel_id = ?";

        try (PreparedStatement preparedStatement = Db.getInstance().prepareStatement(query)) {
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

    public ArrayList<Room> getRoomsByFilters(String city, String country, String hotelName, int capacity, LocalDate startDate, LocalDate endDate) {
        ArrayList<Room> rooms = new ArrayList<>();
        StringBuilder queryBuilder = new StringBuilder(
                "SELECT r.*, " +
                        "(r.stock - COALESCE(reserved.reserved_count, 0)) as available_stock " +
                        "FROM rooms r " +
                        "JOIN hotels h ON r.hotel_id = h.id " +
                        "LEFT JOIN (" +
                        "   SELECT room_id, COUNT(id) as reserved_count " +
                        "   FROM reservations " +
                        "   WHERE 1=1");

        // Add date filtering only if dates are provided
        if (startDate != null && endDate != null) {
            queryBuilder.append(" AND (");
            queryBuilder.append("   (start_date <= ? AND end_date >= ?) OR "); // Search criteria 1: Partial/Full overlap
            queryBuilder.append("   (start_date >= ? AND start_date <= ?) OR "); // Search criteria 2: Start date within reserved range
            queryBuilder.append("   (end_date >= ? AND end_date <= ?) "); // Search criteria 3: End date within reserved range
            queryBuilder.append(")");
        }

        queryBuilder.append("   GROUP BY room_id " +
                                    ") as reserved ON r.id = reserved.room_id " +
                                    "WHERE 1=1 "); // Start WHERE clause

        // Add filters
        if (city != null && !city.isEmpty()) {
            queryBuilder.append(" AND h.city ILIKE ?");
        }
        if (country != null && !country.isEmpty()) {
            queryBuilder.append(" AND h.country ILIKE ?");
        }
        if (hotelName != null && !hotelName.isEmpty()) {
            queryBuilder.append(" AND h.name ILIKE ?");
        }
        if (capacity >= 0) {
            queryBuilder.append(" AND r.capacity >= ?");
        }

        queryBuilder.append(" AND (r.stock - COALESCE(reserved.reserved_count, 0)) > 0"); // Ensure availability

        String queryString = queryBuilder.toString();
        System.out.println("Generated SQL Query: " + queryString);

        try (PreparedStatement preparedStatement = getConnection().prepareStatement(queryString)) {
            int paramIndex = 1;

            // Set date parameters if they are provided
            if (startDate != null && endDate != null) {
                preparedStatement.setDate(paramIndex++, java.sql.Date.valueOf(startDate));
                preparedStatement.setDate(paramIndex++, java.sql.Date.valueOf(endDate));
                preparedStatement.setDate(paramIndex++, java.sql.Date.valueOf(startDate));
                preparedStatement.setDate(paramIndex++, java.sql.Date.valueOf(endDate));
                preparedStatement.setDate(paramIndex++, java.sql.Date.valueOf(startDate));
                preparedStatement.setDate(paramIndex++, java.sql.Date.valueOf(endDate));
            }

            // Set the rest of the parameters
            if (city != null && !city.isEmpty()) {
                preparedStatement.setString(paramIndex++, "%" + city + "%");
            }
            if (country != null && !country.isEmpty()) {
                preparedStatement.setString(paramIndex++, "%" + country + "%");
            }
            if (hotelName != null && !hotelName.isEmpty()) {
                preparedStatement.setString(paramIndex++, "%" + hotelName + "%");
            }
            if (capacity >= 0) {
                preparedStatement.setInt(paramIndex++, capacity);
            }

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Room room = mapResultSetToEntity(resultSet);
                    room.setAvailableStock(resultSet.getInt("available_stock"));
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
