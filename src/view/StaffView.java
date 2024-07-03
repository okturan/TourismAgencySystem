package view;

import java.awt.*;

import javax.swing.*;

import entity.AppUser;
import view.panels.hotel.HotelPanelView;
import view.panels.reservation.ReservationTableHandler;

public class StaffView extends BaseLayout {
    private JPanel container;
    private JPanel panel_top;
    private JButton button_logout;
    private JPanel panel_bottom;
    private JTabbedPane tabbedPane1;
    private HotelPanelView hotels_tab;
    private JLabel label_welcome;
    private JPanel panel_reservations;
    private JScrollPane scroll_reservations;
    private JTable table_reservations;

    public StaffView(AppUser loginUser) throws HeadlessException {

        this.setContentPane(container);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setTitle("staff");
        guiInitialize(900, 600);

        button_logout.addActionListener(e -> {
            dispose();
            new LoginView().setVisible(true);
        });
        label_welcome.setText(
                "Welcome " + loginUser.getFirstName() + " " + loginUser.getLastName());


        ReservationTableHandler reservationTableHandler = new ReservationTableHandler(table_reservations);
    }

}
