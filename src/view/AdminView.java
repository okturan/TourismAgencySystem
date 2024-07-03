package view;

import java.awt.*;

import javax.swing.*;

import entity.AppUser;
import view.panels.user.UserTableHandler;

public class AdminView extends BaseLayout {
    private JPanel container;
    private JPanel panel_top;
    private JLabel label_welcome;
    private JButton button_logout;
    private JPanel panel_bottom;
    private JScrollPane scroll_users;
    private JTable table_users;
    private JPanel panel_filter_users;
    private JComboBox<String> combo_filter_role;
    private JLabel label_filter_role;

    public AdminView(AppUser loginUser) throws HeadlessException {

        this.setContentPane(container);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setTitle("admin");
        guiInitialize(900, 600);

        combo_filter_role.addItem("all");
        combo_filter_role.addItem("admin");
        combo_filter_role.addItem("staff");

        button_logout.addActionListener(e -> {
            dispose();
            new LoginView().setVisible(true);
        });

        label_welcome.setText(
                "Welcome " + loginUser.getFirstName() + " " + loginUser.getLastName());

        UserTableHandler userTableHandler = new UserTableHandler(table_users);
        combo_filter_role.addActionListener(e -> {
            String selectedRole = (String) combo_filter_role.getSelectedItem();

            if ("all".equalsIgnoreCase(selectedRole)) {
                userTableHandler.getEntities();
            } else {
                userTableHandler.getByRole(selectedRole);
            }
        });
    }
}
