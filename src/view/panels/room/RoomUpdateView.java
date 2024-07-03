package view.panels.room;

import business.RoomManager;
import core.Helper;
import entity.Hotel;
import entity.Room;
import entity.RoomPriceSummary;
import entity.Season;
import view.panels.BaseUpdateView;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoomUpdateView extends BaseUpdateView<Room> {

    private Hotel hotel;
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
    private JTextField field_hotel_name;
    private JLabel label_hotel_name;
    private JScrollPane scroll_price;

    // A map to store the text fields for prices for easy data retrieval
    private Map<String, JTextField> priceFields;

    protected RoomUpdateView(Hotel hotel) {
        super(new RoomManager());
        this.add(container);
        this.hotel = hotel;
        setButton_Save(button_save);
        setButton_Cancel(button_cancel);
        initializeEventListeners();
        priceFields = new HashMap<>();
    }


    @Override
    public void initializeUIComponents(Room room) {
        this.guiInitialize(500, 700);
        this.currentEntity = room;

        combo_room_type.setModel((new DefaultComboBoxModel<>(Room.RoomType.values())));
        // Initialize UI components with room data
        if (room != null) {
            field_room_name.setText(room.getName());
            spinner_capacity.setValue(room.getCapacity());
            spinner_size.setValue(room.getSizeSqm());
            spinner_stock.setValue(room.getStock());
            combo_room_type.setSelectedItem(room.getRoomType());

            field_hotel_name.setText(room.getHotel().getName());
            field_hotel_name.setEnabled(false);
            // Set room amenities
            setCheckBoxes(room.getAmenities(),
                          minibarCheckBox, gameConsoleCheckBox, safeCheckBox,
                          projectorCheckBox, televisionCheckBox);

            // Populate room prices if updating an existing room
            populateRoomPrices(room.getId());
        } else {
            // Fill in with default values for a new room entry
            field_hotel_name.setText(hotel.getName());
            field_hotel_name.setEnabled(false);
            populateRoomPrices(null); // No roomId, so it is considered as a new room
        }
    }

    private void setCheckBoxes(List<String> items, JCheckBox... checkBoxes) {
        for (JCheckBox checkBox : checkBoxes) {
            checkBox.setSelected(items.contains(checkBox.getText()));
        }
    }

    @Override
    protected boolean validateFields() {
        return !Helper.isFieldListEmpty(field_room_name.getText(),
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

        if (hotel == null) {
            room.setHotel(currentEntity.getHotel());
        } else {
            room.setHotel(hotel);
        }

        // Set room amenities
        room.setAmenities(getSelectedItems(
                minibarCheckBox, gameConsoleCheckBox, safeCheckBox,
                projectorCheckBox, televisionCheckBox));

        // Set room price summaries
        room.setRoomPrices(getRoomPrices());

        return room;
    }

    private void saveRoom(Room room) {
        RoomManager roomManager = new RoomManager();
        if (room.getId() == 0) { // If the room is new (id == 0), call save
            if (roomManager.save(room)) {
                JOptionPane.showMessageDialog(this, "Room saved successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Error saving room.");
            }
        } else { // Otherwise call update
            if (roomManager.update(room)) {
                JOptionPane.showMessageDialog(this, "Room updated successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Error updating room.");
            }
        }
    }

    private List<RoomPriceSummary> getRoomPrices() {
        ArrayList<RoomPriceSummary> roomPrices = new ArrayList<>();
        for (String key : priceFields.keySet()) {
            String[] parts = key.split(":");
            String seasonName = parts[0];
            String boardTypeName = parts[1];
            String priceType = parts[2];

            String textValue = priceFields.get(key).getText();
            if (textValue == null || textValue.trim().isEmpty()) {
                continue;  // Skip empty values to avoid NumberFormatException
            }

            BigDecimal value;
            try {
                value = new BigDecimal(textValue);
            } catch (NumberFormatException e) {
                // Handle the invalid number format
                JOptionPane.showMessageDialog(this, "Invalid number format for room price: " + textValue);
                continue;
            }

            RoomPriceSummary priceSummary = findOrCreateRoomPriceSummary(roomPrices, seasonName, boardTypeName);
            if ("Adult".equals(priceType)) {
                priceSummary.setAdultPriceUsd(value);
            } else if ("Child".equals(priceType)) {
                priceSummary.setChildPriceUsd(value);
            }
        }
        return roomPrices;
    }

    private RoomPriceSummary findOrCreateRoomPriceSummary(List<RoomPriceSummary> roomPrices, String seasonName, String boardTypeName) {
        for (RoomPriceSummary priceSummary : roomPrices) {
            if (priceSummary.getSeasonName().equals(seasonName) && priceSummary.getBoardTypeName().equals(boardTypeName)) {
                return priceSummary;
            }
        }
        RoomPriceSummary newSummary = new RoomPriceSummary(seasonName, boardTypeName, BigDecimal.ZERO, BigDecimal.ZERO);
        roomPrices.add(newSummary);
        return newSummary;
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

    private void populateRoomPrices(Integer roomId) {
        ArrayList<RoomPriceSummary> roomPrices = roomId != null ? new RoomManager().getRoomPrices(roomId) : new ArrayList<>();
        List<Season> seasons;
        List<String> boardTypes;
        // Clear any existing fields
        priceFields.clear();
        panel_price.removeAll();

        if (hotel == null) {
            seasons = currentEntity.getHotel().getSeasons();
            boardTypes = currentEntity.getHotel().getBoardTypes();
        } else {
            seasons = hotel.getSeasons();
            boardTypes = hotel.getBoardTypes();
        }


        // Set layout for the panel that holds the price fields
        panel_price.setLayout(new BoxLayout(panel_price, BoxLayout.Y_AXIS));

        // Iterate over each season and board type to create the necessary fields
        for (Season season : seasons) {
            JLabel seasonLabel = new JLabel(season.getName());
            panel_price.add(seasonLabel);

            for (String boardType : boardTypes) {
                JPanel boardPanel = new JPanel(new GridLayout(2, 2));
                boardPanel.setBorder(BorderFactory.createTitledBorder(boardType));

                JTextField adultField = new JTextField();
                JTextField childField = new JTextField();

                boardPanel.add(new JLabel("Adult"));
                boardPanel.add(adultField);
                boardPanel.add(new JLabel("Child"));
                boardPanel.add(childField);

                panel_price.add(boardPanel);

                // Save these fields in the map for later retrieval
                priceFields.put(season.getName() + ":" + boardType + ":Adult", adultField);
                priceFields.put(season.getName() + ":" + boardType + ":Child", childField);

                // Only populate fields with existing data if roomId is not null
                if (roomId != null){
                    roomPrices.stream()
                            .filter(price -> price.getSeasonName().equals(season.getName()) && price.getBoardTypeName().equals(boardType))
                            .findFirst()
                            .ifPresent(price -> {
                                adultField.setText(price.getAdultPriceUsd().toString());
                                childField.setText(price.getChildPriceUsd().toString());
                            });
                }
            }
        }
        panel_price.revalidate();
        panel_price.repaint();
    }

    private void createUIComponents() {
        // Custom component creation because of intellij idea gui designer
        panel_price = new JPanel();
    }
}
