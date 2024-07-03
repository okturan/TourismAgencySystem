package business;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import core.Db;
import dao.RoomDao;
import entity.Hotel;
import entity.Room;
import entity.RoomPriceSummary;

public class RoomManager extends BaseManager<Room> {

    public RoomManager() {
        super(new RoomDao());
    }

    public ArrayList<Object[]> formatDataForTable(ArrayList<Room> rooms) {
        ArrayList<Object[]> roomRows = new ArrayList<>();
        for (Room room : rooms) {
            Object[] rowObject = new Object[]{
                    room.getId(),
                    room.getHotel().getName() + " - " + room.getName(),
                    room.getRoomType(),
                    room.getSizeSqm() + " m²",
                    room.getCapacity(),
                    room.getStock(),
            };
            roomRows.add(rowObject);
        }
        return roomRows;
    }

    public ArrayList<RoomPriceSummary> getRoomPrices(Integer roomId) {
        return ((RoomDao) getDao()).getRoomPrices(roomId);
    }

    public boolean saveRoomPrices(int roomId, ArrayList<RoomPriceSummary> roomPrices) throws SQLException {
        return ((RoomDao) getDao()).saveRoomPrices(roomId, roomPrices, Db.getInstance());
    }

    public ArrayList<Room> getHotelRooms(Hotel hotel) {
        return ((RoomDao) getDao()).getHotelRooms(hotel);
    }

    public ArrayList<Room> getFilteredRooms(String city, String country, String hotelName, int capacity, LocalDate startDate, LocalDate endDate) {
        return ((RoomDao) getDao()).getRoomsByFilters(city, country, hotelName, capacity, startDate, endDate);
    }

}
