package dao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import entity.Hotel;
import entity.Season;

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
        hotel.setAmenities(getAmenitiesForHotel(hotel));
        hotel.setBoardTypes(getBoardTypesForHotel(hotel));
        hotel.setSeasons(getSeasonsForHotel(hotel));

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

    private List<Season> getSeasonsForHotel(Hotel hotel) {
        List<Season> seasons = new ArrayList<>();
        String query = "SELECT * FROM seasons WHERE hotel_id = ?";

        try (PreparedStatement preparedStatement = getConnection().prepareStatement(query)) {
            preparedStatement.setInt(1, hotel.getId());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Season season = new Season();
                    season.setId(resultSet.getInt("id"));
                    season.setHotel(hotel);
                    season.setName(resultSet.getString("name"));
                    season.setStartDate(resultSet.getDate("start_date").toLocalDate());
                    season.setEndDate(resultSet.getDate("end_date").toLocalDate());
                    season.setRateMultiplier(resultSet.getInt("rate_multiplier"));
                    seasons.add(season);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return seasons;
    }

    public boolean deleteSeason(int id) {
        try {
            String query = "DELETE FROM seasons WHERE id = ?";
            try (PreparedStatement preparedStatement = getConnection().prepareStatement(query)) {
                preparedStatement.setInt(1, id);
                return preparedStatement.executeUpdate() != -1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean saveSeason(Season season) {
        String query = "INSERT INTO seasons (name, start_date, end_date, rate_multiplier, hotel_id) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(query)) {
            preparedStatement.setString(1, season.getName());
            preparedStatement.setDate(2, Date.valueOf(season.getStartDate()));
            preparedStatement.setDate(3, Date.valueOf(season.getEndDate()));
            preparedStatement.setFloat(4, season.getRateMultiplier());
            preparedStatement.setInt(5, season.getHotel().getId());
            return preparedStatement.executeUpdate() != -1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateSeason(Season season) {
        String query = "UPDATE seasons SET name = ?, start_date = ?, end_date = ?, rate_multiplier = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(query)) {
            preparedStatement.setString(1, season.getName());
            preparedStatement.setDate(2, Date.valueOf(season.getStartDate()));
            preparedStatement.setDate(3, Date.valueOf(season.getEndDate()));
            preparedStatement.setFloat(4, season.getRateMultiplier());
            preparedStatement.setInt(5, season.getId());
            return preparedStatement.executeUpdate() != -1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private List<String> getAmenitiesForHotel(Hotel hotel) {
        List<String> amenities = new ArrayList<>();
        String query = "SELECT ha.name FROM hotel_amenities ha " +
                "JOIN hotels_hotel_amenities hha ON ha.id = hha.hotel_amenity_id " +
                "WHERE hha.hotel_id = ?";

        try (PreparedStatement preparedStatement = getConnection().prepareStatement(query)) {
            preparedStatement.setInt(1, hotel.getId());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    amenities.add(resultSet.getString("name"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return amenities;
    }

    private List<String> getBoardTypesForHotel(Hotel hotel) {
        List<String> boardTypes = new ArrayList<>();
        String query = "SELECT bt.name FROM board_types bt " +
                "JOIN hotels_board_types hbt ON bt.id = hbt.board_type_id " +
                "WHERE hbt.hotel_id = ?";

        try (PreparedStatement stmt = getConnection().prepareStatement(query)) {
            stmt.setInt(1, hotel.getId());
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
            result = updateAmenities(hotel, hotel.getAmenities());
            if (result) {
                result = updateBoardTypes(hotel, hotel.getBoardTypes());
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

            result = updateAmenities(hotel, hotel.getAmenities());
            if (result) {
                result = updateBoardTypes(hotel, hotel.getBoardTypes());
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
            deleteAllBoardTypesFromHotel(findById(id));

            // Delete related amenities
            deleteAllAmenitiesFromHotel(findById(id));

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

    public boolean updateAmenities(Hotel hotel, List<String> newAmenities) {
        List<String> currentAmenities = getAmenitiesForHotel(hotel);
        // Find amenities to delete
        for (String amenity : currentAmenities) {
            if (!newAmenities.contains(amenity)) {
                if (!deleteAmenityFromHotel(hotel, amenity)) {
                    return false; // Deletion failed
                }
            }
        }
        // Find amenities to add
        for (String amenity : newAmenities) {
            if (!currentAmenities.contains(amenity)) {
                if (!addAmenityToHotel(hotel, amenity)) {
                    return false; // Addition failed
                }
            }
        }
        return true;
    }

    public boolean updateBoardTypes(Hotel hotel, List<String> newBoardTypes) {
        List<String> currentBoardTypes = getBoardTypesForHotel(hotel);
        // Find board types to delete
        for (String boardType : currentBoardTypes) {
            if (!newBoardTypes.contains(boardType)) {
                if (!deleteBoardTypeFromHotel(hotel, boardType)) {
                    return false; // Deletion failed
                }
            }
        }
        // Find board types to add
        for (String boardType : newBoardTypes) {
            if (!currentBoardTypes.contains(boardType)) {
                if (!addBoardTypeToHotel(hotel, boardType)) {
                    return false; // Addition failed
                }
            }
        }
        return true;
    }

    public boolean addAmenityToHotel(Hotel hotel, String amenityName) {
        String query = "INSERT INTO hotels_hotel_amenities (hotel_id, hotel_amenity_id) " +
                "SELECT ?, id FROM hotel_amenities WHERE name = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(query)) {
            stmt.setInt(1, hotel.getId());
            stmt.setString(2, amenityName);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean addBoardTypeToHotel(Hotel hotel, String boardTypeName) {
        String query = "INSERT INTO hotels_board_types (hotel_id, board_type_id) " +
                "SELECT ?, id FROM board_types WHERE name = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(query)) {
            stmt.setInt(1, hotel.getId());
            stmt.setString(2, boardTypeName);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteAmenityFromHotel(Hotel hotel, String amenityName) {
        String deleteQuery = "DELETE FROM hotels_hotel_amenities " +
                "WHERE hotel_id = ? " +
                "AND hotel_amenity_id = (SELECT id FROM hotel_amenities " +
                "WHERE name = ?)";
        try (PreparedStatement deleteStmt = getConnection().prepareStatement(deleteQuery)) {
            deleteStmt.setInt(1, hotel.getId());
            deleteStmt.setString(2, amenityName);
            return deleteStmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteBoardTypeFromHotel(Hotel hotel, String boardTypeName) {
        String deleteQuery = "DELETE FROM hotels_board_types " +
                "WHERE hotel_id = ? " +
                "AND board_type_id = (SELECT id FROM board_types " +
                "WHERE name = ?)";
        try (PreparedStatement deleteStmt = getConnection().prepareStatement(deleteQuery)) {
            deleteStmt.setInt(1, hotel.getId());
            deleteStmt.setString(2, boardTypeName);
            return deleteStmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean deleteAllAmenitiesFromHotel(Hotel hotel) {
        String deleteQuery = "DELETE FROM hotels_hotel_amenities WHERE hotel_id = ?";
        try (PreparedStatement deleteStmt = getConnection().prepareStatement(deleteQuery)) {
            deleteStmt.setInt(1, hotel.getId());
            return deleteStmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean deleteAllBoardTypesFromHotel(Hotel hotel) {
        String deleteQuery = "DELETE FROM hotels_board_types WHERE hotel_id = ?";
        try (PreparedStatement deleteStmt = getConnection().prepareStatement(deleteQuery)) {
            deleteStmt.setInt(1, hotel.getId());
            return deleteStmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
