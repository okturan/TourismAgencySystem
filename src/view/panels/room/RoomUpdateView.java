package view.panels.room;

import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import business.RoomManager;
import core.Helper;
import entity.Hotel;
import entity.Room;
import view.panels.BaseUpdateView;

public class RoomUpdateView extends BaseUpdateView<Room> {

    private JPanel container;
    private JPanel panel_action;
    private JPanel panel_info;
    private JLabel label_room_type;
    private JLabel label_room_name;
    private JTextField field_room_name;
    private JLabel label_capacity;
    private JLabel label_size;
    private JPanel panel_room_amenities;
    private JCheckBox minibarCheckBox;
    private JCheckBox gameConsoleCheckBox;
    private JCheckBox safeCheckBox;
    private JCheckBox projectorCheckBox;
    private JCheckBox televisionCheckBox;
    private JLabel label_room_amenities;
    private JButton button_cancel;
    private JButton button_save;
    private JComboBox<Room.RoomType> combo_room_type;
    private JSpinner spinner_capacity;
    private JSpinner spinner_size;
    private JSpinner spinner_stock;
    private JPanel panel_spinners;
    private JLabel label_stock;
    private JPanel panel_room_name;
    private JPanel panel_room_type;
    private JPanel panel_price;
    private JTextField field_adult_price;
    private JTextField field_child_price;
    private JLabel label_child_price;
    private JLabel label_price_adult;
    private final Hotel hotel;

    protected RoomUpdateView(Hotel hotel) {
        super(new RoomManager());
        this.add(container);
        this.hotel = hotel;
        setBtn_save(button_save);
        setButton_cancel(button_cancel);
        initializeEventListeners();
    }

    @Override
    public void initializeUIComponents(Room room) {
        this.guiInitialize(500, 600);
        this.currentEntity = room;

        combo_room_type.setModel((new DefaultComboBoxModel<>(Room.RoomType.values())));
        // Initialize UI components with room data
        if (room != null) {
            field_room_name.setText(room.getName());
            spinner_capacity.setValue(room.getCapacity());
            spinner_size.setValue(room.getSizeSqm());
            spinner_stock.setValue(room.getStock());
            combo_room_type.setSelectedItem(room.getRoomType());
            field_adult_price.setText(String.valueOf(room.getAdultPriceUsd()));
            field_child_price.setText(String.valueOf(room.getChildPriceUsd()));

            // Set room amenities
            setCheckBoxes(room.getAmenities(),
                          minibarCheckBox, gameConsoleCheckBox, safeCheckBox,
                          projectorCheckBox, televisionCheckBox);
        }
    }

    private void setCheckBoxes(List<String> items, JCheckBox... checkBoxes) {
        for (JCheckBox checkBox : checkBoxes) {
            checkBox.setSelected(items.contains(checkBox.getText()));
        }
    }

    @Override
    protected boolean validateFields() {
        return !Helper.isFieldListEmpty(field_adult_price.getText(),
                                        field_child_price.getText(),
                                        field_room_name.getText(),
                                        spinner_size.getValue().toString(),
                                        spinner_stock.getValue().toString(),
                                        spinner_capacity.getValue().toString());
    }


    @Override
    protected Room setFields(Room room) {
        if (room == null) {
            room = new Room();
        }

        room.setName(field_room_name.getText());
        room.setCapacity((Integer) spinner_capacity.getValue());
        room.setSizeSqm((Integer) spinner_size.getValue());
        room.setStock((Integer) spinner_stock.getValue());
        room.setRoomType((Room.RoomType) combo_room_type.getSelectedItem());
        room.setAdultPriceUsd(Integer.parseInt(field_adult_price.getText()));
        room.setChildPriceUsd(Integer.parseInt(field_child_price.getText()));
        room.setHotel(hotel);

        // Set room amenities
        room.setAmenities(getSelectedItems(
                minibarCheckBox, gameConsoleCheckBox, safeCheckBox,
                projectorCheckBox, televisionCheckBox));

        return room;
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
