package entity;

import java.util.List;

public class Room extends BaseEntity {
    private Hotel hotel;
    private String name;
    private int capacity;
    private int sizeSqm;
    private int stock;
    private RoomType roomType;
    private List<String> amenities;
    private int availableStock;
    private List<RoomPriceSummary> roomPrices;

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    public int getAvailableStock() {
        return availableStock;
    }

    public void setAvailableStock(int availableStock) {
        this.availableStock = availableStock;
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

    @Override
    public String toString() {
        return "Room{" +
                "hotel=" + hotel +
                ", name='" + name + '\'' +
                ", capacity=" + capacity +
                ", sizeSqm=" + sizeSqm +
                ", stock=" + stock +
                ", roomType=" + roomType +
                ", amenities=" + amenities +
                ", availableStock=" + availableStock +
                ", roomPrices=" + roomPrices + // Add this line
                "} " + super.toString();
    }

    public List<String> getAmenities() {
        return amenities;
    }

    public void setAmenities(List<String> amenities) {
        this.amenities = amenities;
    }

    public List<RoomPriceSummary> getRoomPrices() {
        return roomPrices; // Add this getter
    }

    public void setRoomPrices(List<RoomPriceSummary> roomPrices) {
        this.roomPrices = roomPrices; // Add this setter
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
