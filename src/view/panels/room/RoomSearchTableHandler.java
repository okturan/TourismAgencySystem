package view.panels.room;

import com.github.lgooddatepicker.components.DatePicker;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;

import business.RoomManager;
import entity.Room;
import view.panels.BaseTableHandler;
import view.panels.ReservationUpdateView;

public class RoomSearchTableHandler extends BaseTableHandler<Room, RoomManager, RoomUpdateView> {

    private static final String[] HEADERS = {
            "id",
            "name",
            "roomType",
            "size",
            "capacity",
            "stock",
    };

    private final DatePicker startDatePicker;
    private final DatePicker endDatePicker;
    private final RoomSearchPanelView panelView;

    public RoomSearchTableHandler(JTable table, DatePicker startDatePicker, DatePicker endDatePicker, RoomSearchPanelView panelView) {
        super(HEADERS, table, new RoomManager());
        this.startDatePicker = startDatePicker;
        this.endDatePicker = endDatePicker;
        this.panelView = panelView;
        initializeTable();
    }

    @Override
    protected void populateRightClickMenu() {
        addMenuItem("Edit", e -> onEdit());
        addMenuItem("Delete", e -> onDelete());
        addMenuItem("Reserve", e -> onReserve());
    }

    @Override
    protected void onEdit() {
        int selectedId = Integer.parseInt(this.getTable().getValueAt(getSelectedRow(), 0).toString());
        Room room = this.getManager().getById(selectedId);
        RoomUpdateView roomUpdateView = createViewInstance();
        roomUpdateView.initializeUIComponents(room);
        roomUpdateView.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                getEntities();
            }
        });
    }

    private void onReserve() {
        if (startDatePicker.getDate() == null || endDatePicker.getDate() == null) {
            JOptionPane.showMessageDialog(null, "Please filter rooms by dates first", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!panelView.isSearchUpToDate()) {
            JOptionPane.showMessageDialog(null, "Please hit the 'Search' button to refresh room availability before making a reservation.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int selectedId = Integer.parseInt(this.getTable().getValueAt(getSelectedRow(), 0).toString());
        Room selectedRoom = getManager().getById(selectedId);

        ReservationUpdateView view = new ReservationUpdateView(selectedRoom);
        view.setStartDatePicker(startDatePicker);
        view.setEndDatePicker(endDatePicker);
        view.setNumAdults((int) panelView.spinner_adult.getValue());
        view.setNumChildren((int) panelView.spinner_children.getValue());
        view.initializeUIComponents(null);
    }

    @Override
    protected RoomUpdateView createViewInstance() {
        return new RoomUpdateView(null);
    }
}
