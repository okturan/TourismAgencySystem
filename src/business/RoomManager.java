package business;

import java.util.ArrayList;

import dao.RoomDao;
import entity.Hotel;
import entity.Room;

public class RoomManager extends BaseManager<Room> {

    public RoomManager() {
        super(new RoomDao());
    }

    public ArrayList<Object[]> formatDataForTable(ArrayList<Room> rooms) {
        ArrayList<Object[]> roomRows = new ArrayList<>();
        for (Room room : rooms) {
            Object[] rowObject = new Object[]{
                    room.getId(),
                    room.getName(),
                    room.getRoomType(),
                    room.getSizeSqm() + " m²",
                    room.getCapacity(),
                    room.getStock(),
                    room.getAdultPriceUsd(),
                    room.getChildPriceUsd()
            };
            roomRows.add(rowObject);
        }
        return roomRows;
    }

    public ArrayList<Room> getHotelRooms(Hotel hotel) {
        return ((RoomDao) getDao()).getHotelRooms(hotel);
    }
}
