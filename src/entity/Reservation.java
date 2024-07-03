package entity;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Reservation extends BaseEntity {
    private Room room;
    private String fullName;
    private String phone;
    private String email;
    private String identification;
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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
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
                ", fullName='" + fullName + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", identification='" + identification + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", calculatedCostUsd=" + calculatedCostUsd +
                ", numAdults=" + numAdults +
                ", numChildren=" + numChildren +
                '}';
    }
}