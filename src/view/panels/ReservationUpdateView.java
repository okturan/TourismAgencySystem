package view.panels;

import com.github.lgooddatepicker.components.DatePicker;

import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import business.ReservationManager;
import core.Helper;
import dao.RoomDao;
import entity.Reservation;
import entity.Room;
import entity.RoomPriceSummary;
import entity.Season;

public class ReservationUpdateView extends BaseUpdateView<Reservation> {
    private Room room;
    private JPanel container;
    private JPanel panel_hotel_info;
    private JPanel panel_room_info;
    private JTextField field_hotel_country;
    private JTextField field_hotel_city;
    private JTextField field_hotel_district;
    private JTextField field_hotel_address;
    private JTextField field_hotel_phone;
    private JTextField field_hotel_email;
    private JTextField field_hotel_stars;
    private JList list_hotel_amenities;
    private JTextField field_room_type;
    private JTextField field_checkin;
    private JTextField field_checkout;
    private JList list_room_amenities;
    private JComboBox combo_boardtype;
    private JLabel label_hotel_phone;
    private JLabel label_hotel_email;
    private JLabel label_hotel_stars;
    private JLabel label_hotel_country;
    private JLabel label_hotel_city;
    private JLabel label_hotel_district;
    private JLabel label_hotel_address;
    private JLabel label_hotel_amenities;
    private JLabel label_checkin;
    private JLabel label_checkout;
    private JLabel label_room_type;
    private JLabel label_room_amenities;
    private JLabel label_totalcost;
    private JLabel label_adults;
    private JLabel label_children;
    private JButton button_cancel;
    private JButton button_save;
    private JPanel panel_contact_phone;
    private JPanel panel_email;
    private JPanel panel_stars;
    private JPanel panel_country;
    private JPanel panel_district;
    private JPanel panel_city;
    private JPanel panel_countrcitydistrict;
    private JPanel panel_phoneemailstars;
    private JPanel panel_address;
    private JPanel panel_hotel_amenities;
    private JPanel panel_room_amenities;
    private JPanel panel_room_type;
    private JPanel panel_dates;
    private JPanel panel_cost;
    private JPanel panel_board_type;
    private JLabel label_board_type;
    private JPanel panel_buttons;
    private JPanel panel_guest_info;
    private JTextField field_guest_name;
    private JTextField field_guest_phone;
    private JTextField field_guest_email;
    private JTextField field_guest_id;
    private JLabel label_guest_name;
    private JLabel label_guest_phone;
    private JLabel label_guest_email;
    private JLabel label_guest_id;
    private JTextField field_num_adults;
    private JTextField field_num_children;
    private JTextField field_total_cost;
    private DatePicker startDatePicker;
    private DatePicker endDatePicker;
    private int numAdults;
    private int numChildren;

    public ReservationUpdateView(Room room) {
        super(new ReservationManager());
        this.room = room;
        this.add(container);
        setButton_Save(button_save);
        setButton_Cancel(button_cancel);
        initializeEventListeners();
    }

    public DatePicker getStartDatePicker() {
        return startDatePicker;
    }

    public void setStartDatePicker(DatePicker startDatePicker) {
        this.startDatePicker = startDatePicker;
    }

    public DatePicker getEndDatePicker() {
        return endDatePicker;
    }

    public void setEndDatePicker(DatePicker endDatePicker) {
        this.endDatePicker = endDatePicker;
    }

    public void setNumAdults(int numAdults) {
        this.numAdults = numAdults;
    }

    public void setNumChildren(int numChildren) {
        this.numChildren = numChildren;
    }

    @Override
    public void initializeUIComponents(Reservation reservation) {
        this.guiInitialize(600, 800);
        this.currentEntity = reservation;
        Font titleFont = new Font("Arial", Font.BOLD, 16);

        if (room == null) {
            room = reservation.getRoom();
        }

        TitledBorder roomTitle = BorderFactory.createTitledBorder(room.getName());
        roomTitle.setTitleFont(titleFont);
        panel_room_info.setBorder(roomTitle);

        field_room_type.setText(room.getRoomType().toString());
        field_hotel_country.setText(room.getHotel().getCountry());
        field_hotel_city.setText(room.getHotel().getCity());
        field_hotel_district.setText(room.getHotel().getDistrict());
        field_hotel_address.setText(room.getHotel().getAddressLine());
        field_hotel_phone.setText(room.getHotel().getPhone());
        field_hotel_email.setText(room.getHotel().getEmail());
        field_hotel_stars.setText(Integer.toString(room.getHotel().getStars()));
        list_hotel_amenities.setListData(room.getHotel().getAmenities().toArray());
        list_room_amenities.setListData(room.getAmenities().toArray());

        TitledBorder hotelTitle = BorderFactory.createTitledBorder(room.getHotel().getName());
        hotelTitle.setTitleFont(titleFont);
        panel_hotel_info.setBorder(hotelTitle);


        field_num_adults.setText(String.valueOf(numAdults));
        field_num_adults.setEnabled(false);
        field_num_children.setText(String.valueOf(numChildren));
        field_num_children.setEnabled(false);

        if (reservation != null) {
            field_guest_name.setText(reservation.getFullName());
            field_guest_phone.setText(reservation.getPhone());
            field_guest_email.setText(reservation.getEmail());
            field_guest_id.setText(reservation.getIdentification());
            field_num_adults.setText(Integer.toString(reservation.getNumAdults()));
            field_num_children.setText(Integer.toString(reservation.getNumChildren()));
            field_total_cost.setText(reservation.getCalculatedCostUsd().toString());
            field_checkin.setText(reservation.getStartDate().toString());
            field_checkout.setText(reservation.getEndDate().toString());
            combo_boardtype.setEnabled(false);
        } else {
            field_checkin.setText(startDatePicker.getText());
            field_checkout.setText(endDatePicker.getText());
            startDatePicker.addDateChangeListener(e -> updateTotalCost());
            endDatePicker.addDateChangeListener(e -> updateTotalCost());

            List<String> boardTypes = room.getHotel().getBoardTypes();
            for (String boardType : boardTypes) {
                combo_boardtype.addItem(boardType);
            }
            combo_boardtype.addActionListener(e -> updateTotalCost());
            updateTotalCost();
        }


        disableFields();
    }
    private void updateTotalCost() {
        LocalDate startDate = startDatePicker.getDate();
        LocalDate endDate = endDatePicker.getDate();
        long daysBetween = ChronoUnit.DAYS.between(startDate, endDate);

        TitledBorder dateTitle = BorderFactory.createTitledBorder(daysBetween + " nights");
        Font titleFont = new Font("Arial", Font.BOLD, 16);
        dateTitle.setTitleFont(titleFont);
        panel_dates.setBorder(dateTitle);

        field_checkin.setText(startDate.toString());
        field_checkout.setText(endDate.toString());

        BigDecimal totalCost = calculateTotalCost(numAdults, numChildren, startDate, endDate);
        field_total_cost.setText(totalCost.toString());
    }

    private BigDecimal calculateTotalCost(int numAdults, int numChildren, LocalDate startDate, LocalDate endDate) {
        BigDecimal totalCost = BigDecimal.ZERO;

        String selectedBoardType = (String) combo_boardtype.getSelectedItem();

        // Retrieve room prices and seasons
        List<RoomPriceSummary> priceSummaries = new RoomDao().getRoomPrices(room.getId());
        List<Season> seasons = room.getHotel().getSeasons();

        // Iterate over each day to calculate the daily cost according to the season and board type
        for (LocalDate date = startDate; date.isBefore(endDate); date = date.plusDays(1)) {
            RoomPriceSummary priceSummary = getPriceSummaryForDateAndBoardType(date, selectedBoardType, priceSummaries, seasons);
            if (priceSummary != null) {
                BigDecimal dailyCost = priceSummary.getAdultPriceUsd().multiply(BigDecimal.valueOf(numAdults))
                        .add(priceSummary.getChildPriceUsd().multiply(BigDecimal.valueOf(numChildren)));
                totalCost = totalCost.add(dailyCost);
            }
        }

        return totalCost;
    }

    private RoomPriceSummary getPriceSummaryForDateAndBoardType(LocalDate date, String boardType, List<RoomPriceSummary> priceSummaries, List<Season> seasons) {
        Season currentSeason = getCurrentSeason(date, seasons);

        if (currentSeason == null) {
            return null;
        }

        for (RoomPriceSummary priceSummary : priceSummaries) {
            if (priceSummary.getBoardTypeName().equals(boardType) &&
                    isWithinSeason(currentSeason, priceSummary)) {
                return priceSummary;
            }
        }
        return null;
    }

    private Season getCurrentSeason(LocalDate date, List<Season> seasons) {
        for (Season season : seasons) {
            if ((date.isEqual(season.getStartDate()) || date.isAfter(season.getStartDate())) &&
                    (date.isEqual(season.getEndDate()) || date.isBefore(season.getEndDate()))) {
                return season;
            }
        }
        return null;
    }

    private boolean isWithinSeason(Season season, RoomPriceSummary priceSummary) {
        // Assuming the price summary has a season name that matches the current season.
        // This might need to be adjusted based on your actual implementation.
        if (season != null && season.getName() != null && priceSummary.getSeasonName() != null) {
            return season.getName().equals(priceSummary.getSeasonName());
        }
        return false;
    }


    @Override
    protected boolean validateFields() {
        return !Helper.isFieldListEmpty(field_guest_name.getText(), field_guest_phone.getText(), field_guest_email.getText(), field_guest_id.getText(), field_checkin.getText(), field_checkout.getText(), field_num_adults.getText(), field_num_children.getText());
    }

    @Override
    protected Reservation setFields(Reservation reservation) {
        if (reservation == null) {
            reservation = new Reservation();
        }
        reservation.setRoom(room);
        reservation.setFullName(field_guest_name.getText());
        reservation.setPhone(field_guest_phone.getText());
        reservation.setEmail(field_guest_email.getText());
        reservation.setIdentification(field_guest_id.getText());
        reservation.setStartDate(LocalDate.parse(field_checkin.getText()));
        reservation.setEndDate(LocalDate.parse(field_checkout.getText()));
        reservation.setNumAdults(Integer.parseInt(field_num_adults.getText()));
        reservation.setNumChildren(Integer.parseInt(field_num_children.getText()));
        reservation.setCalculatedCostUsd(new BigDecimal(field_total_cost.getText()));

        return reservation;
    }

    private void disableFields() {
        field_room_type.setEnabled(false);
        field_hotel_country.setEnabled(false);
        field_hotel_city.setEnabled(false);
        field_hotel_district.setEnabled(false);
        field_hotel_address.setEnabled(false);
        field_hotel_phone.setEnabled(false);
        field_hotel_email.setEnabled(false);
        field_hotel_stars.setEnabled(false);
        list_hotel_amenities.setEnabled(false);
        list_room_amenities.setEnabled(false);
        field_checkin.setEnabled(false);
        field_checkout.setEnabled(false);

        // Disable guest information fields
        field_total_cost.setEnabled(false);
        field_num_adults.setEnabled(false);
        field_num_children.setEnabled(false);
    }
}
