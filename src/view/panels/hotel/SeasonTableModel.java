package view.panels.hotel;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import entity.Season;

public class SeasonTableModel extends AbstractTableModel {

    private final String[] columnNames = {"ID", "Name", "Start Date", "End Date", "Rate (%)"};
    private final List<Season> seasons;

    public SeasonTableModel(List<Season> seasons) {
        this.seasons = seasons;
    }

    @Override
    public int getRowCount() {
        return seasons.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Season season = seasons.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return season.getId();
            case 1:
                return season.getName();
            case 2:
                return season.getStartDate();
            case 3:
                return season.getEndDate();
            case 4:
                return season.getRateMultiplier();
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return Integer.class;
            case 1:
                return String.class;
            case 2:
                return LocalDate.class;
            case 3:
                return LocalDate.class;
            case 4:
                return BigDecimal.class;
            default:
                return Object.class;
        }
    }

}
