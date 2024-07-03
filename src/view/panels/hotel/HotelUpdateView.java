package view.panels.hotel;

import com.github.lgooddatepicker.components.DatePicker;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import business.HotelManager;
import core.Helper;
import entity.Hotel;
import entity.Season;
import view.panels.BaseUpdateView;

public class HotelUpdateView extends BaseUpdateView<Hotel> {
    private final JPopupMenu rightClickMenu;
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
    private JTextField field_email;
    private JButton button_cancel;
    private JButton button_save;
    private JLabel label_email;
    private JTable table_seasons;
    private JPanel panel_seasons;
    private JLabel label_seasons;
    private JPanel panel_add_seasons;
    private JPanel panel_table_seasons;
    private JTextField field_season_name;
    private JLabel label_season_name;
    private JLabel label_season_start;
    private JLabel label_season_end;
    private JLabel label_season_rate;
    private JPanel panel_startDate;
    private JPanel panel_endDate;
    private JPanel panel_rate;
    private JButton button_save_season;
    private JScrollPane scroll_seasons;
    private JPanel panel_left;
    private JLabel label_season_id;
    private JTextField field_season_id;
    private JPanel panel_season_id;
    private JButton button_clear_season;
    private JPanel panel_rooms;
    private JButton button_rooms;
    private DatePicker startDatePicker;
    private DatePicker endDatePicker;
    private JSpinner spinner_rate;
    private int selectedRow = -1;

    protected HotelUpdateView() {
        super(new HotelManager());
        this.add(container);
        this.rightClickMenu = new JPopupMenu();
        setButton_Save(button_save);
        setButton_Cancel(button_cancel);
        initializeEventListeners();
    }

    @Override
    public void initializeUIComponents(Hotel hotel) {
        this.guiInitialize(1000, 600);
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

            // Populate seasons table
            loadSeasonTable();

            button_save_season.addActionListener(e -> onSeasonSave());
            button_clear_season.addActionListener(e -> clearSeasonFields());
        }

        setupTableMouseListener();
        populateRightClickMenu();
    }

    private void loadSeasonTable() {
        SeasonTableModel seasonTableModel = new SeasonTableModel(currentEntity.getSeasons());
        table_seasons.setModel(seasonTableModel);
        Helper.resizeColumnWidth(table_seasons);
        table_seasons.setEnabled(false);
    }

    protected void setupTableMouseListener() {
        table_seasons.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    selectedRow = table_seasons.rowAtPoint(e.getPoint());
                    rightClickMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
    }

    protected void populateRightClickMenu() {
        addMenuItem("Edit", e -> onEdit());
        addMenuItem("Delete", e -> onDelete());
    }

    protected void onEdit() {
        int selectedId = Integer.parseInt(table_seasons.getValueAt(selectedRow, 0).toString());
        Season season = currentEntity.matchSeasonById(selectedId);
        startDatePicker.setDate(season.getStartDate());
        endDatePicker.setDate(season.getEndDate());
        spinner_rate.setValue(season.getRateMultiplier());
        field_season_name.setText(season.getName());
        field_season_id.setText(String.valueOf(season.getId()));
        panel_season_id.setVisible(true);
    }

    protected void onDelete() {
        if (Helper.confirm("sure")) {
            int selectedId = Integer.parseInt(table_seasons.getValueAt(selectedRow, 0).toString());
            if (((HotelManager) manager).deleteSeason(selectedId)) {
                Helper.showMessage("done");
                currentEntity.removeSeasonById(selectedId);
                loadSeasonTable();
            } else {
                core.Helper.showMessage("Delete Error: Record Not found");
            }
        }
    }

    protected void onSeasonSave() {
        if (validateSeasonFields() && validateFields()) {
            Season season = getSeasonFromFields();

            HotelManager hotelManager = (HotelManager) this.manager;
            boolean isSuccessful;

            if (season.getId() == 0) {
                isSuccessful = hotelManager.saveSeason(season);
                if (isSuccessful) {
                    currentEntity.addSeason(season);
                    Helper.showMessage("Save Successful");
                } else {
                    Helper.showMessage("Save Error: Erroneous entry");
                }
            } else {
                isSuccessful = hotelManager.updateSeason(season);
                if (isSuccessful) {
                    currentEntity.updateSeason(season);
                    Helper.showMessage("Update Successful");
                } else {
                    Helper.showMessage("Update Error: Erroneous entry");
                }
            }

            if (isSuccessful) {
                loadSeasonTable();
            }
        } else {
            Helper.showMessage("fill");
        }
    }

    private Season getSeasonFromFields() {
        Season season = new Season();
        // Handle empty field_season_id
        String seasonIdText = field_season_id.getText().trim();
        int seasonId = seasonIdText.isEmpty() ? 0 : Integer.parseInt(seasonIdText);
        season.setId(seasonId);

        season.setName(field_season_name.getText());
        season.setStartDate(startDatePicker.getDate());
        season.setEndDate(endDatePicker.getDate());
        season.setRateMultiplier((Integer) spinner_rate.getValue());
        season.setHotel(currentEntity);
        return season;
    }

    protected void addMenuItem(String title, ActionListener actionListener) {
        JMenuItem menuItem = new JMenuItem(title);
        menuItem.addActionListener(actionListener);
        rightClickMenu.add(menuItem);
    }

    private void setCheckBoxes(List<String> items, JCheckBox... checkBoxes) {
        for (JCheckBox checkBox : checkBoxes) {
            checkBox.setSelected(items.contains(checkBox.getText()));
        }
    }

    @Override
    protected boolean validateFields() {
        return !Helper.isFieldListEmpty(field_hotel_name.getText(),
                                        field_address.getText(),
                                        field_country.getText(),
                                        field_city.getText(),
                                        field_email.getText(),
                                        field_phone.getText(),
                                        field_district.getText(),
                                        String.valueOf(slider_stars.getValue())
        );
    }

    protected boolean validateSeasonFields() {
        return !Helper.isFieldListEmpty(field_season_name.getText(),
                                        startDatePicker.getText(),
                                        endDatePicker.getText(),
                                        String.valueOf(spinner_rate)
        );
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



    private void clearSeasonFields() {
        field_season_name.setText("");
        startDatePicker.setDate(null);
        endDatePicker.setDate(null);
        spinner_rate.setValue(100); // Reset to default value
        field_season_id.setText("");
        panel_season_id.setVisible(false);
    }

    private void createUIComponents() {
        startDatePicker = new DatePicker();
        endDatePicker = new DatePicker();

        SpinnerNumberModel model = new SpinnerNumberModel(100, -100, 200, 5);
        spinner_rate = new JSpinner(model);

        panel_startDate = new JPanel();
        panel_endDate = new JPanel();
        panel_rate = new JPanel();

        panel_startDate.add(startDatePicker, BorderLayout.CENTER);
        panel_endDate.add(endDatePicker, BorderLayout.CENTER);
        panel_rate.add(spinner_rate, BorderLayout.CENTER);

        Helper.addDateChangeListeners(startDatePicker, endDatePicker, container);
    }
}