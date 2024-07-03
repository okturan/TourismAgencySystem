package view.panels.user;

import javax.swing.*;

import business.AppUserManager;
import core.Helper;
import entity.AppUser;
import view.panels.BaseUpdateView;

public class UserUpdateView extends BaseUpdateView<AppUser> {

    private JPanel container;
    private JTextField field_username;
    private JTextField field_password;
    private JTextField field_firstname;
    private JTextField field_lastname;
    private JTextField field_email;
    private JRadioButton adminRadioButton;
    private JRadioButton staffRadioButton;
    private JLabel label_username;
    private JLabel label_password;
    private JLabel label_firstname;
    private JLabel label_lastname;
    private JLabel label_email;
    private JLabel label_role;
    private JPanel panel_action;
    private JPanel panel_info;
    private JButton button_cancel;
    private JButton button_save;
    private final ButtonGroup roleGroup;

    protected UserUpdateView() {
        super(new AppUserManager());
        this.add(container);
        setButton_Save(button_save);
        setButton_Cancel(button_cancel);

        roleGroup = new ButtonGroup();
        roleGroup.add(adminRadioButton);
        roleGroup.add(staffRadioButton);

        initializeEventListeners();
    }

    @Override
    public void initializeUIComponents(AppUser user) {
        this.guiInitialize(400, 500);
        this.currentEntity = user;

        if (user != null) {
            field_username.setText(user.getUsername());
            field_password.setText(user.getPassword());
            field_firstname.setText(user.getFirstName());
            field_lastname.setText(user.getLastName());
            field_email.setText(user.getEmail());

            if (user.getRole().equals("admin")) {
                adminRadioButton.setSelected(true);
            } else if (user.getRole().equals("staff")) {
                staffRadioButton.setSelected(true);
            }
        }
    }

    @Override
    protected boolean validateFields() {
        return !Helper.isFieldListEmpty(field_username.getText(),
                                        field_password.getText(),
                                        field_firstname.getText(),
                                        field_lastname.getText(),
                                        field_email.getText());
    }

    @Override
    protected AppUser setFields(AppUser user) {
        if (user == null) {
            user = new AppUser();
        }

        user.setUsername(field_username.getText());
        user.setPassword(field_password.getText());
        user.setFirstName(field_firstname.getText());
        user.setLastName(field_lastname.getText());
        user.setEmail(field_email.getText());

        if (adminRadioButton.isSelected()) {
            user.setRole("admin");
        } else if (staffRadioButton.isSelected()) {
            user.setRole("staff");
        }

        return user;
    }
}

