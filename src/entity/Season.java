package entity;

import java.time.LocalDate;

public class Season extends BaseEntity {
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private int rateMultiplier;
    private Hotel hotel;

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public int getRateMultiplier() {
        return rateMultiplier;
    }

    public void setRateMultiplier(int rateMultiplier) {
        this.rateMultiplier = rateMultiplier;
    }

    @Override
    public String toString() {
        return "Season{" +
                "id=" + getId() +
                ", name='" + name + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", rateMultiplier=" + rateMultiplier +
                ", hotelId=" + hotel.getName() +
                '}';
    }
}