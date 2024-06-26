package view;

import java.awt.*;

import javax.swing.*;

import view.panels.hotel.HotelPanelView;

public class StaffView extends BaseLayout {
    private JPanel container;
    private JPanel panel_top;
    private JButton button1;
    private JPanel panel_bottom;
    private JTabbedPane tabbedPane1;
    private HotelPanelView hotels_tab;

    public StaffView() throws HeadlessException {

        this.setContentPane(container);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setTitle("admin");
        guiInitialize(900, 600);


//        lbl_welcome.setText("Welcome!");
    }

}
