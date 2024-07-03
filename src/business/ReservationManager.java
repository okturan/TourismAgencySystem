package business;

import java.util.ArrayList;

import dao.ReservationDao;
import entity.Reservation;

public class ReservationManager extends BaseManager<Reservation> {

    public ReservationManager() {
        super(new ReservationDao());
    }

    public ArrayList<Object[]> formatDataForTable(ArrayList<Reservation> reservations) {
        ArrayList<Object[]> reservationRows = new ArrayList<>();
        for (Reservation reservation : reservations) {
            Object[] rowObject = new Object[]{
                    reservation.getId(),
                    reservation.getRoom().getName() + " - " + reservation.getRoom().getHotel().getName(),
                    reservation.getStartDate(),
                    reservation.getEndDate(),
                    reservation.getCalculatedCostUsd(),
                    reservation.getFullName(),
                    reservation.getEmail()
            };
            reservationRows.add(rowObject);
        }
        return reservationRows;
    }
}
