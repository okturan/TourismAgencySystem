package view.panels.hotel;

import javax.swing.*;

import business.HotelManager;
import entity.Hotel;
import view.panels.room.RoomPanelView;
import view.panels.BaseTableHandler;

public class HotelTableHandler extends BaseTableHandler<Hotel, HotelManager, HotelUpdateView> {

    private static final String[] HEADERS = {
            "id",
            "name",
            "stars",
            "country",
            "city",
            "district",
            "phone",
            "email"
    };

    public HotelTableHandler(JTable table) {
        super(HEADERS, table, new HotelManager());
        initializeTable();
    }

    @Override
    protected void populateRightClickMenu() {
        addMenuItem("Edit", e -> onEdit());
        addMenuItem("Delete", e -> onDelete());
        addMenuItem("Show Rooms", e -> onShowRooms());
    }

    private void onShowRooms() {
        int selectedId = Integer.parseInt(this.getTable().getValueAt(getSelectedRow(), 0).toString());
        Hotel hotel = this.getManager().getById(selectedId);
        new RoomPanelView(hotel);
    }

    @Override
    protected HotelUpdateView createViewInstance() {
        return new HotelUpdateView();
    }
}
