package business;

import java.util.ArrayList;
import dao.GuestDao;
import entity.Guest;

public class GuestManager extends BaseManager<Guest> {

    public GuestManager() {
        super(new GuestDao());
    }

    public ArrayList<Object[]> formatDataForTable(ArrayList<Guest> guests) {
        ArrayList<Object[]> guestRows = new ArrayList<>();
        for (Guest guest : guests) {
            Object[] rowObject = new Object[] {
                    guest.getId(),
                    guest.getName(),
                    guest.getPhone(),
                    guest.getEmail(),
                    guest.getIdentification()
            };
            guestRows.add(rowObject);
        }
        return guestRows;
    }
}
