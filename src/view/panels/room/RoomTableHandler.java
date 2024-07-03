package view.panels.room;

import javax.swing.*;

import business.RoomManager;
import entity.Hotel;
import entity.Room;
import view.panels.BaseTableHandler;

public class RoomTableHandler extends BaseTableHandler<Room, RoomManager, RoomUpdateView> {

    private static final String[] HEADERS = {
            "id",
            "Hotel Room",
            "roomType",
            "size",
            "capacity",
            "stock",
    };

    private final Hotel hotel;

    public RoomTableHandler(JTable table, Hotel hotel) {
        super(HEADERS, table, new RoomManager());
        this.hotel = hotel;
        initializeTable();
    }

    @Override
    public void getEntities() {
        loadTable(getManager().getHotelRooms(hotel));
    }

    @Override
    protected RoomUpdateView createViewInstance() {
        return new RoomUpdateView(hotel);
    }

}
