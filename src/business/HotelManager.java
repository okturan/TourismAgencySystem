package business;

import java.util.ArrayList;

import dao.HotelDao;
import entity.Hotel;
import entity.Season;

public class HotelManager extends BaseManager<Hotel> {

    public HotelManager() {
        super(new HotelDao());
    }

    public ArrayList<Object[]> formatDataForTable(ArrayList<Hotel> hotels) {
        ArrayList<Object[]> hotelRows = new ArrayList<>();
        for (Hotel hotel : hotels) {
            Object[] rowObject = new Object[]{
                    hotel.getId(),
                    hotel.getName(),
                    hotel.getStars(),
                    hotel.getCountry(),
                    hotel.getCity(),
                    hotel.getDistrict(),
                    hotel.getPhone(),
                    hotel.getEmail()
            };
            hotelRows.add(rowObject);
        }
        return hotelRows;
    }

    public boolean saveSeason(Season season) {
        return ((HotelDao) dao).saveSeason(season);
    }

    public boolean deleteSeason(int seasonId) {
        return ((HotelDao) dao).deleteSeason(seasonId);
    }

    public boolean updateSeason(Season season) {
        return ((HotelDao) dao).updateSeason(season);
    }
}
