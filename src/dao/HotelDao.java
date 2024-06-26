package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import entity.Hotel;

public class HotelDao extends BaseDao<Hotel> {

    public HotelDao() {
        super("hotels");
    }

    @Override
    protected Hotel mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        Hotel hotel = new Hotel();
        hotel.setId(resultSet.getInt("id"));
        hotel.setName(resultSet.getString("name"));
        hotel.setPhone(resultSet.getString("phone"));
        hotel.setEmail(resultSet.getString("email"));
        hotel.setStars(resultSet.getInt("stars"));
        hotel.setAddressLine(resultSet.getString("address_line"));
        hotel.setCountry(resultSet.getString("country"));
        hotel.setCity(resultSet.getString("city"));
        hotel.setDistrict(resultSet.getString("district"));

        // Fetch amenities and board types
        hotel.setAmenities(getAmenitiesForHotel(hotel.getId()));
        hotel.setBoardTypes(getBoardTypesForHotel(hotel.getId()));

        return hotel;
    }

    @Override
    protected void setParameters(PreparedStatement preparedStatement, Hotel hotel) throws SQLException {
        preparedStatement.setString(1, hotel.getName());
        preparedStatement.setString(2, hotel.getPhone());
        preparedStatement.setString(3, hotel.getEmail());
        preparedStatement.setInt(4, hotel.getStars());
        preparedStatement.setString(5, hotel.getAddressLine());
        preparedStatement.setString(6, hotel.getCountry());
        preparedStatement.setString(7, hotel.getCity());
        preparedStatement.setString(8, hotel.getDistrict());
    }

    private List<String> getAmenitiesForHotel(int hotelId) {
        List<String> amenities = new ArrayList<>();
        String query = "SELECT ha.name FROM hotel_amenities ha " +
                "JOIN hotels_hotel_amenities hha ON ha.id = hha.hotel_amenity_id " +
                "WHERE hha.hotel_id = ?";

        try (PreparedStatement stmt = getConnection().prepareStatement(query)) {
            stmt.setInt(1, hotelId);
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

    private List<String> getBoardTypesForHotel(int hotelId) {
        List<String> boardTypes = new ArrayList<>();
        String query = "SELECT bt.name FROM board_types bt " +
                "JOIN hotels_board_types hbt ON bt.id = hbt.board_type_id " +
                "WHERE hbt.hotel_id = ?";

        try (PreparedStatement stmt = getConnection().prepareStatement(query)) {
            stmt.setInt(1, hotelId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    boardTypes.add(rs.getString("name"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return boardTypes;
    }

    @Override
    public boolean save(Hotel hotel) {
        boolean result = false;
        String query = "INSERT INTO " +
                getTableName() +
                " (" +
                String.join(", ", getColumnNames()) +
                ") VALUES (" +
                getPlaceholders() +
                ")";

        try (PreparedStatement preparedStatement = getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            setParameters(preparedStatement, hotel);
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating hotel failed, no rows affected.");
            }

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    hotel.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating hotel failed, no ID obtained.");
                }
            }

            // Now we can update amenities and board types after we have an ID
            result = updateAmenities(hotel.getId(), hotel.getAmenities());
            if (result) {
                result = updateBoardTypes(hotel.getId(), hotel.getBoardTypes());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public boolean update(Hotel hotel) {
        boolean result = false;
        String query = "UPDATE " +
                getTableName() +
                " SET " +
                getUpdateColumns() +
                " WHERE " +
                getIdColumnName() +
                " = ?";

        try (PreparedStatement preparedStatement = getConnection().prepareStatement(query)) {
            setParameters(preparedStatement, hotel);
            preparedStatement.setInt(getColumnNames().size() + 1, hotel.getId());
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating hotel failed, no rows affected.");
            }

            result = updateAmenities(hotel.getId(), hotel.getAmenities());
            if (result) {
                result = updateBoardTypes(hotel.getId(), hotel.getBoardTypes());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public boolean delete(int id) {
        try {
            // Delete related board types first
            deleteAllBoardTypesFromHotel(id);

            // Delete related amenities
            deleteAllAmenitiesFromHotel(id);

            // Proceed to delete the hotel
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

    public boolean updateAmenities(int hotelId, List<String> newAmenities) {
        List<String> currentAmenities = getAmenitiesForHotel(hotelId);
        // Find amenities to delete
        for (String amenity : currentAmenities) {
            if (!newAmenities.contains(amenity)) {
                if (!deleteAmenityFromHotel(hotelId, amenity)) {
                    return false; // Deletion failed
                }
            }
        }
        // Find amenities to add
        for (String amenity : newAmenities) {
            if (!currentAmenities.contains(amenity)) {
                if (!addAmenityToHotel(hotelId, amenity)) {
                    return false; // Addition failed
                }
            }
        }
        return true;
    }

    public boolean updateBoardTypes(int hotelId, List<String> newBoardTypes) {
        List<String> currentBoardTypes = getBoardTypesForHotel(hotelId);
        // Find board types to delete
        for (String boardType : currentBoardTypes) {
            if (!newBoardTypes.contains(boardType)) {
                if (!deleteBoardTypeFromHotel(hotelId, boardType)) {
                    return false; // Deletion failed
                }
            }
        }
        // Find board types to add
        for (String boardType : newBoardTypes) {
            if (!currentBoardTypes.contains(boardType)) {
                if (!addBoardTypeToHotel(hotelId, boardType)) {
                    return false; // Addition failed
                }
            }
        }
        return true;
    }

    public boolean addAmenityToHotel(int hotelId, String amenityName) {
        String query = "INSERT INTO hotels_hotel_amenities (hotel_id, hotel_amenity_id) " +
                "SELECT ?, id FROM hotel_amenities WHERE name = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(query)) {
            stmt.setInt(1, hotelId);
            stmt.setString(2, amenityName);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean addBoardTypeToHotel(int hotelId, String boardTypeName) {
        String query = "INSERT INTO hotels_board_types (hotel_id, board_type_id) " +
                "SELECT ?, id FROM board_types WHERE name = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(query)) {
            stmt.setInt(1, hotelId);
            stmt.setString(2, boardTypeName);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteAmenityFromHotel(int hotelId, String amenityName) {
        String deleteQuery = "DELETE FROM hotels_hotel_amenities " +
                "WHERE hotel_id = ? " +
                "AND hotel_amenity_id = (SELECT id FROM hotel_amenities " +
                "WHERE name = ?)";
        try (PreparedStatement deleteStmt = getConnection().prepareStatement(deleteQuery)) {
            deleteStmt.setInt(1, hotelId);
            deleteStmt.setString(2, amenityName);
            return deleteStmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteBoardTypeFromHotel(int hotelId, String boardTypeName) {
        String deleteQuery = "DELETE FROM hotels_board_types " +
                "WHERE hotel_id = ? " +
                "AND board_type_id = (SELECT id FROM board_types " +
                "WHERE name = ?)";
        try (PreparedStatement deleteStmt = getConnection().prepareStatement(deleteQuery)) {
            deleteStmt.setInt(1, hotelId);
            deleteStmt.setString(2, boardTypeName);
            return deleteStmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean deleteAllAmenitiesFromHotel(int hotelId) {
        String deleteQuery = "DELETE FROM hotels_hotel_amenities WHERE hotel_id = ?";
        try (PreparedStatement deleteStmt = getConnection().prepareStatement(deleteQuery)) {
            deleteStmt.setInt(1, hotelId);
            return deleteStmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean deleteAllBoardTypesFromHotel(int hotelId) {
        String deleteQuery = "DELETE FROM hotels_board_types WHERE hotel_id = ?";
        try (PreparedStatement deleteStmt = getConnection().prepareStatement(deleteQuery)) {
            deleteStmt.setInt(1, hotelId);
            return deleteStmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
