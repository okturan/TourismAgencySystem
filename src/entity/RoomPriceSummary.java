package entity;

import java.math.BigDecimal;

public class RoomPriceSummary {
    private String seasonName;
    private String boardTypeName;
    private BigDecimal adultPriceUsd;
    private BigDecimal childPriceUsd;

    public RoomPriceSummary(String seasonName, String boardTypeName, BigDecimal adultPriceUsd, BigDecimal childPriceUsd) {
        this.seasonName = seasonName;
        this.boardTypeName = boardTypeName;
        this.adultPriceUsd = adultPriceUsd;
        this.childPriceUsd = childPriceUsd;
    }

    public String getSeasonName() {
        return seasonName;
    }

    public void setSeasonName(String seasonName) {
        this.seasonName = seasonName;
    }

    public void setBoardTypeName(String boardTypeName) {
        this.boardTypeName = boardTypeName;
    }

    public void setAdultPriceUsd(BigDecimal adultPriceUsd) {
        this.adultPriceUsd = adultPriceUsd;
    }

    public void setChildPriceUsd(BigDecimal childPriceUsd) {
        this.childPriceUsd = childPriceUsd;
    }

    public String getBoardTypeName() {
        return boardTypeName;
    }

    public BigDecimal getAdultPriceUsd() {
        return adultPriceUsd;
    }

    public BigDecimal getChildPriceUsd() {
        return childPriceUsd;
    }
}
