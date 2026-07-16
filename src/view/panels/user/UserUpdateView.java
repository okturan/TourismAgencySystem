package view.panels.user;

import java.util.Arrays;

import javax.swing.*;

import business.AppUserManager;
import core.Helper;
import entity.AppUser;
import security.PasswordPolicy;
import view.panels.BaseUpdateView;

public class UserUpdateView extends BaseUpdateView<AppUser> {

    private JPanel container;
    private JTextField field_username;
    private JPasswordField field_password;
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
            field_firstname.setText(user.getFirstName());
            field_lastname.setText(user.getLastName());
            field_email.setText(user.getEmail());
            label_password.setText("New password (leave blank to keep current)");

            if (user.getRole().equals("admin")) {
                adminRadioButton.setSelected(true);
            } else if (user.getRole().equals("staff")) {
                staffRadioButton.setSelected(true);
            }
        } else {
            label_password.setText("Password (12+ characters)");
        }
        field_password.setText("");
    }

    @Override
    protected boolean validateFields() {
        char[] password = field_password.getPassword();
        try {
            return validateFields(password);
        } finally {
            Arrays.fill(password, '\0');
        }
    }

    private boolean validateFields(char[] password) {
        boolean profileIsComplete = !Helper.isFieldListEmpty(
                field_username.getText(),
                field_firstname.getText(),
                field_lastname.getText(),
                field_email.getText()
        ) && (adminRadioButton.isSelected() || staffRadioButton.isSelected());

        boolean passwordIsValid = currentEntity == null
                ? PasswordPolicy.isStrong(password)
                : password.length == 0 || PasswordPolicy.isStrong(password);
        return profileIsComplete && passwordIsValid;
    }

    @Override
    protected AppUser setFields(AppUser user) {
        if (user == null) {
            user = new AppUser();
        }

        user.setUsername(field_username.getText());
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

    @Override
    protected void save() {
        char[] password = field_password.getPassword();
        try {
            if (!validateFields(password)) {
                Helper.showMessage(
                        "Complete every profile field, select a role, and use a 12-256 character password. "
                                + "Leave the password blank only when keeping an existing one."
                );
                return;
            }

            boolean isNewUser = currentEntity == null;
            currentEntity = setFields(currentEntity);
            AppUserManager userManager = (AppUserManager) manager;
            boolean saved = isNewUser
                    ? userManager.createUser(currentEntity, password)
                    : userManager.updateUser(currentEntity, password);

            Helper.showMessage(saved ? "Save Successful" : "Save Error: Erroneous entry");
            if (saved) {
                dispose();
            }
        } catch (IllegalArgumentException exception) {
            Helper.showMessage(exception.getMessage());
        } finally {
            Arrays.fill(password, '\0');
            field_password.setText("");
        }
    }
}
