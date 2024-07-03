package view.panels.hotel;

import javax.swing.*;


public class HotelPanelView extends JPanel {
    private JPanel panel_hotels;
    private JScrollPane scroll_hotels;
    private JTable table_hotels;
    private JButton button_add_new_hotel;
    private JPanel panel_header;

    public HotelPanelView() {
        this.add(panel_hotels);

        HotelTableHandler hotelTableHandler = new HotelTableHandler(table_hotels);

        button_add_new_hotel.addActionListener(e -> hotelTableHandler.onAdd());
    }

}
