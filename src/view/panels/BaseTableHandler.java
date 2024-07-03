package view.panels;

import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import business.BaseManager;
import core.Helper;
import entity.BaseEntity;
import view.BaseLayout;


public abstract class BaseTableHandler<
        E extends BaseEntity,
        M extends BaseManager<E>,
        V extends BaseUpdateView<? extends BaseEntity>
        > extends BaseLayout {

    private final String[] HEADERS;
    private final JTable table;
    private final DefaultTableModel defaultTableModel;
    private final JPopupMenu rightClickMenu;
    private final M manager;
    private V view;
    private int selectedRow = -1;

    public BaseTableHandler(String[] headers, JTable table, M manager) {
        this.HEADERS = headers;
        this.table = table;
        this.manager = manager;
        this.defaultTableModel = new DefaultTableModel();
        this.table.setModel(defaultTableModel);
        this.rightClickMenu = new JPopupMenu();
        setupTableMouseListener();
    }

    public void initializeTable() {
        populateRightClickMenu();
        getEntities();
    }

    protected void setupWindowClosedListener() {
        view.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                getEntities();
            }
        });
    }

    public void getEntities() {
        loadTable(getManager().findAll());
    }

    protected void setupTableMouseListener() {
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    selectedRow = table.rowAtPoint(e.getPoint());
                    rightClickMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
    }

    protected void populateRightClickMenu() {
        addMenuItem("Edit", e -> onEdit());
        addMenuItem("Add", e -> onAdd());
        addMenuItem("Delete", e -> onDelete());
    }

    protected void addMenuItem(String title, ActionListener actionListener) {
        JMenuItem menuItem = new JMenuItem(title);
        menuItem.addActionListener(actionListener);
        rightClickMenu.add(menuItem);
    }

    public void onAdd() {
        view = createViewInstance();
        view.initializeUIComponents(null);
        setupWindowClosedListener();
    }

    protected void onEdit() {
        int selectedId = Integer.parseInt(this.getTable().getValueAt(getSelectedRow(), 0).toString());
        E entity = this.getManager().getById(selectedId);
        view = createViewInstance();
        ((BaseUpdateView<E>) getView()).initializeUIComponents(entity);
        setupWindowClosedListener();
    }

    protected void onDelete() {
        if (Helper.confirm("sure")) {
            int selectedId = Integer.parseInt(table.getValueAt(selectedRow, 0).toString());
            if (manager.delete(selectedId)) {
                Helper.showMessage("done");
                loadTable(manager.findAll());
            } else {
                core.Helper.showMessage("Delete Error: Record Not found");
            }
        }
    }

    public void loadTable(ArrayList<E> entities) {
        defaultTableModel.setRowCount(0);
        defaultTableModel.setColumnIdentifiers(HEADERS);
        ArrayList<Object[]> entityRows = getManager().formatDataForTable(entities);
        fillRows(entityRows);
    }

    private void fillRows(ArrayList<Object[]> rows) {
        if (rows == null) {
            rows = new ArrayList<>();
        }
        for (Object[] row : rows) {
            defaultTableModel.addRow(row);
        }
        configureTableAppearance();
    }

    private void configureTableAppearance() {
        Helper.resizeColumnWidth(table);
        table.getTableHeader().setReorderingAllowed(true);
        table.setEnabled(false);
    }

    protected abstract V createViewInstance();

    public int getSelectedRow() {
        return selectedRow;
    }

    public JTable getTable() {
        return table;
    }

    public M getManager() {
        return manager;
    }

    public V getView() {
        return view;
    }

    public void setView(V view) {
        this.view = view;
    }

}
