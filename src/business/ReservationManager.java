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
            Object[] rowObject = new Object[] {
                    reservation.getId(),
                    reservation.getRoom(),
                    reservation.getPrimaryGuest(),
                    reservation.getStartDate(),
                    reservation.getEndDate(),
                    reservation.getCalculatedCostUsd(),
                    reservation.getNumAdults(),
                    reservation.getNumChildren()
            };
            reservationRows.add(rowObject);
        }
        return reservationRows;
    }
}
