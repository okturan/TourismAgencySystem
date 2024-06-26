package entity;

import java.math.BigDecimal;
import java.util.List;

public class Room extends BaseEntity {
    private Hotel hotel;
    private String name;
    private int capacity;
    private int sizeSqm;
    private int stock;
    private RoomType roomType;
    private int adultPriceUsd;
    private int childPriceUsd;
    private List<String> amenities;

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

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

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getSizeSqm() {
        return sizeSqm;
    }

    public void setSizeSqm(int sizeSqm) {
        this.sizeSqm = sizeSqm;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getAdultPriceUsd() {
        return adultPriceUsd;
    }

    public void setAdultPriceUsd(int adultPriceUsd) {
        this.adultPriceUsd = adultPriceUsd;
    }

    public int getChildPriceUsd() {
        return childPriceUsd;
    }

    public void setChildPriceUsd(int childPriceUsd) {
        this.childPriceUsd = childPriceUsd;
    }

    public List<String> getAmenities() {
        return amenities;
    }

    public void setAmenities(List<String> amenities) {
        this.amenities = amenities;
    }

    @Override
    public String toString() {
        return "Room{" +
                "hotel=" + hotel +
                ", name='" + name + '\'' +
                ", capacity=" + capacity +
                ", sizeSqm=" + sizeSqm +
                ", stock=" + stock +
                ", roomType=" + roomType +
                ", adultPriceUsd=" + adultPriceUsd +
                ", childPriceUsd=" + childPriceUsd +
                ", amenities=" + amenities +
                "} " + super.toString();
    }

    public enum RoomType {
        SINGLE_ROOM, DOUBLE_ROOM, JUNIOR_SUITE, SUITE;

        public static RoomType fromString(String str) {
            try {
                return RoomType.valueOf(str.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid transmission type: " + str);
            }
        }
    }
}
