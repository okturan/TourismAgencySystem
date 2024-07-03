package view.panels.room;

import java.awt.*;

import javax.swing.*;

import entity.Hotel;
import view.BaseLayout;

public class RoomPanelView extends BaseLayout {
    private final Hotel hotel;
    private JPanel container;
    private JScrollPane scroll_rooms;
    private JTable table_rooms;
    private JButton button_add_new_room;
    private JPanel panel_header;
    private JLabel label_hotel;
    private JPanel panel_rooms_table;
    private JLabel label_rooms;

    public RoomPanelView(Hotel hotel) throws HeadlessException {
        this.add(container);
        this.guiInitialize(700, 600);
        this.hotel = hotel;

        RoomTableHandler roomTableHandler = new RoomTableHandler(table_rooms, hotel);

        button_add_new_room.addActionListener(e -> roomTableHandler.onAdd());

        label_hotel.setText(hotel.getName());
    }


}
