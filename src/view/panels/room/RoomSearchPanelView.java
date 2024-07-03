package view.panels.room;

import com.github.lgooddatepicker.components.DatePicker;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.ArrayList;

import javax.swing.*;

import business.RoomManager;
import core.Helper;
import entity.Room;

public class RoomSearchPanelView extends JPanel {
    private final RoomManager roomManager;
    RoomSearchTableHandler roomSearchTableHandler;
    private JPanel container;
    private JScrollPane scroll_room_search;
    private JTable table_room_search;
    private JPanel panel_room_search;
    private JPanel panel_room_filter;
    private JTextField field_city;
    private JTextField field_country;
    private JTextField field_hotelName;
    private JLabel label_city;
    private JLabel label_country;
    private JLabel label_hotelName;
    private JLabel label_checkin;
    private JPanel panel_startDate;
    private JPanel panel_endDate;
    private JPanel panel_city;
    private JPanel panel_country;
    private JPanel panel_hotelName;
    private JPanel panel_checkin;
    private JButton button_clear;
    private JButton button_search;
    private JPanel panel_guests;
    JSpinner spinner_adult;
    JSpinner spinner_children;
    private JLabel label_adult;
    private JLabel label_children;
    private JPanel panel_buttons;
    private JPanel panel_checkout;
    private JLabel label_checkout;
    private JPanel panel_dates;
    private JPanel panel_countrycityhotelname;
    private DatePicker startDatePicker;
    private DatePicker endDatePicker;

    private boolean isSearchUpToDate; // Flag to track if search is up to date

    public RoomSearchPanelView() {
        this.add(panel_room_search);
        roomManager = new RoomManager();

        ButtonHandler buttonHandler = new ButtonHandler();
        button_search.addActionListener(buttonHandler);
        button_clear.addActionListener(buttonHandler);

        roomSearchTableHandler = new RoomSearchTableHandler(table_room_search, startDatePicker, endDatePicker, this);

        isSearchUpToDate = false; // Initialize flag to false
        addDateChangeListeners();
    }

    private void createUIComponents() {
        startDatePicker = new DatePicker();
        endDatePicker = new DatePicker();

        panel_startDate = new JPanel();
        panel_endDate = new JPanel();

        panel_startDate.add(startDatePicker, BorderLayout.CENTER);
        panel_endDate.add(endDatePicker, BorderLayout.CENTER);

        Helper.addDateChangeListeners(startDatePicker, endDatePicker, container);
    }

    // Add listeners to date pickers to reset the search flag
    private void addDateChangeListeners() {
        startDatePicker.addDateChangeListener(event -> resetSearchFlag());
        endDatePicker.addDateChangeListener(event -> resetSearchFlag());
    }

    private void resetSearchFlag() {
        isSearchUpToDate = false;
    }

    private void handleSearchAction() {
        String city = field_city.getText().trim();
        String country = field_country.getText().trim();
        String hotelName = field_hotelName.getText().trim();
        int guestCount = (int) spinner_adult.getValue() + (int) spinner_children.getValue();

        LocalDate startDate = startDatePicker.getDate();
        LocalDate endDate = endDatePicker.getDate();

        ArrayList<Room> filteredRooms = roomManager.getFilteredRooms(city, country, hotelName, guestCount, startDate, endDate);

        roomSearchTableHandler.loadTable(filteredRooms);

        isSearchUpToDate = true; // Set flag to true after search
    }

    private void handleClearAction() {
        field_city.setText("");
        field_country.setText("");
        field_hotelName.setText("");
        startDatePicker.clear();
        endDatePicker.clear();

        roomSearchTableHandler.getEntities();

        isSearchUpToDate = false; // Reset flag after clearing filters
    }

    public boolean isSearchUpToDate() {
        return isSearchUpToDate;
    }

    private class ButtonHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == button_search) {
                handleSearchAction();
            } else if (e.getSource() == button_clear) {
                handleClearAction();
            }
        }
    }
}
