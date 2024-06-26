package entity;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Reservation extends BaseEntity {
    private Room room;
    private Guest primaryGuest;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal calculatedCostUsd;
    private int numAdults;
    private int numChildren;

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Guest getPrimaryGuest() {
        return primaryGuest;
    }

    public void setPrimaryGuest(Guest primaryGuest) {
        this.primaryGuest = primaryGuest;
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

    public BigDecimal getCalculatedCostUsd() {
        return calculatedCostUsd;
    }

    public void setCalculatedCostUsd(BigDecimal calculatedCostUsd) {
        this.calculatedCostUsd = calculatedCostUsd;
    }

    public int getNumAdults() {
        return numAdults;
    }

    public void setNumAdults(int numAdults) {
        this.numAdults = numAdults;
    }

    public int getNumChildren() {
        return numChildren;
    }

    public void setNumChildren(int numChildren) {
        this.numChildren = numChildren;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + getId() +
                ", room=" + room +
                ", primaryGuest=" + primaryGuest +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", calculatedCostUsd=" + calculatedCostUsd +
                ", numAdults=" + numAdults +
                ", numChildren=" + numChildren +
                '}';
    }
}
