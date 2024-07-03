package view.panels.reservation;

import javax.swing.*;

import business.HotelManager;
import business.ReservationManager;
import entity.BaseEntity;
import entity.Reservation;
import view.panels.BaseTableHandler;
import view.panels.BaseUpdateView;
import view.panels.ReservationUpdateView;

public class ReservationTableHandler extends BaseTableHandler<Reservation, ReservationManager, ReservationUpdateView> {


    private static final String[] HEADERS = {
            "id",
            "Hotel Room",
            "start_date",
            "end_date",
            "calculated_cost_usd",
            "full_name",
            "email"
    };

    public ReservationTableHandler(JTable table) {
        super(HEADERS, table, new ReservationManager());
        initializeTable();
    }

    protected void populateRightClickMenu() {
        addMenuItem("Edit", e -> onEdit());
        addMenuItem("Delete", e -> onDelete());
    }

    @Override
    protected ReservationUpdateView createViewInstance() {
        return new ReservationUpdateView(null);
    }


}
