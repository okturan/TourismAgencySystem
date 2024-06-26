package view.panels.hotel;

import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import business.HotelManager;
import entity.Hotel;
import view.panels.BaseUpdateView;

public class HotelUpdateView extends BaseUpdateView<Hotel> {
    private JPanel container;
    private JPanel panel_action;
    private JPanel panel_info;
    private JTextField field_hotel_name;
    private JTextField field_country;
    private JTextField field_city;
    private JTextField field_district;
    private JTextField field_address;
    private JSlider slider_stars;
    private JLabel label_hotel_name;
    private JLabel label_country;
    private JLabel label_stars;
    private JLabel label_city;
    private JLabel label_district;
    private JLabel label_address;
    private JCheckBox fullBoardCheckBox;
    private JCheckBox halfBoardCheckBox;
    private JCheckBox bedOnlyCheckBox;
    private JCheckBox fullBoardWithoutAlcoholCheckBox;
    private JCheckBox bedAndBreakfastCheckBox;
    private JCheckBox allInclusiveCheckBox;
    private JCheckBox ultraAllInclusiveCheckBox;
    private JCheckBox freeParkingCheckBox;
    private JCheckBox freeWiFiCheckBox;
    private JCheckBox swimmingPoolCheckBox;
    private JCheckBox fitnessCenterCheckBox;
    private JCheckBox hotelConciergeCheckBox;
    private JCheckBox spaCheckBox;
    private JPanel panel_board_types;
    private JPanel panel_hotel_amenities;
    private JCheckBox a247RoomServiceCheckBox;
    private JLabel label_hotel_amenities;
    private JLabel label_board_types;
    private JLabel label_phone;
    private JTextField field_phone;
    private JLabel label_email;
    private JTextField field_email;
    private JButton button_cancel;
    private JButton button_save;
    private JPanel panel_rooms;
    private JButton button_rooms;

    protected HotelUpdateView() {
        super(new HotelManager());
        this.add(container);
        setBtn_save(button_save);
        setButton_cancel(button_cancel);
        initializeEventListeners();
    }

    @Override
    public void initializeUIComponents(Hotel hotel) {
        this.guiInitialize(500, 600);
        this.currentEntity = hotel;

        // Initialize UI components with hotel data
        if (hotel != null) {
            field_hotel_name.setText(hotel.getName());
            field_country.setText(hotel.getCountry());
            field_city.setText(hotel.getCity());
            field_district.setText(hotel.getDistrict());
            field_address.setText(hotel.getAddressLine());
            slider_stars.setValue(hotel.getStars());
            field_phone.setText(hotel.getPhone());
            field_email.setText(hotel.getEmail());

            // Set board types
            setCheckBoxes(hotel.getBoardTypes(),
                          fullBoardCheckBox, halfBoardCheckBox, bedOnlyCheckBox,
                          fullBoardWithoutAlcoholCheckBox, bedAndBreakfastCheckBox,
                          allInclusiveCheckBox, ultraAllInclusiveCheckBox);

            // Set amenities
            setCheckBoxes(hotel.getAmenities(),
                          freeParkingCheckBox, freeWiFiCheckBox, swimmingPoolCheckBox,
                          fitnessCenterCheckBox, hotelConciergeCheckBox, spaCheckBox,
                          a247RoomServiceCheckBox);
        }
    }

    private void setCheckBoxes(List<String> items, JCheckBox... checkBoxes) {
        for (JCheckBox checkBox : checkBoxes) {
            checkBox.setSelected(items.contains(checkBox.getText()));
        }
    }

    @Override
    protected boolean validateFields() {
        // Basic validation
        if (field_hotel_name.getText().trim().isEmpty() ||
                field_country.getText().trim().isEmpty() ||
                field_city.getText().trim().isEmpty() ||
                field_district.getText().trim().isEmpty() ||
                field_address.getText().trim().isEmpty() ||
                field_phone.getText().trim().isEmpty() ||
                field_email.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.");
            return false;
        }
        return true;
    }

    @Override
    protected Hotel setFields(Hotel entity) {
        if (entity == null) {
            entity = new Hotel();
        }

        entity.setName(field_hotel_name.getText());
        entity.setCountry(field_country.getText());
        entity.setCity(field_city.getText());
        entity.setDistrict(field_district.getText());
        entity.setAddressLine(field_address.getText());
        entity.setStars(slider_stars.getValue());
        entity.setPhone(field_phone.getText());
        entity.setEmail(field_email.getText());

        // Set board types
        entity.setBoardTypes(getSelectedItems(
                fullBoardCheckBox, halfBoardCheckBox, bedOnlyCheckBox,
                fullBoardWithoutAlcoholCheckBox, bedAndBreakfastCheckBox,
                allInclusiveCheckBox, ultraAllInclusiveCheckBox));

        // Set amenities
        entity.setAmenities(getSelectedItems(
                freeParkingCheckBox, freeWiFiCheckBox, swimmingPoolCheckBox,
                fitnessCenterCheckBox, hotelConciergeCheckBox, spaCheckBox,
                a247RoomServiceCheckBox));

        return entity;
    }

    private List<String> getSelectedItems(JCheckBox... checkBoxes) {
        List<String> selectedItems = new ArrayList<>();
        for (JCheckBox checkBox : checkBoxes) {
            if (checkBox.isSelected()) {
                selectedItems.add(checkBox.getText());
            }
        }
        return selectedItems;
    }
}