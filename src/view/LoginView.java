package view;

import javax.swing.*;

import business.AppUserManager;
import core.Helper;
import entity.AppUser;

public class LoginView extends BaseLayout {
    private final AppUserManager appUserManager;
    private JPanel container;
    private JLabel lbl_welcome;
    private JLabel lbl_welcome2;
    private JPanel w_top;
    private JPanel w_bottom;
    private JLabel lbl_userName;
    private JTextField fld_userName;
    private JLabel lbl_password;
    private JPasswordField fld_password;
    private JButton btn_login;

    public LoginView() {
        this.appUserManager = new AppUserManager();

        this.add(container);
        this.setTitle("Login");
        this.guiInitialize(350, 500);

        btn_login.addActionListener(e -> {
            String[] checkFieldList = {this.fld_userName.getText(), this.fld_password.getText()};
            if (Helper.isFieldListEmpty(checkFieldList)) {
                Helper.showMessage("fill");
            } else {
                AppUser loginUser = this.appUserManager.findByLogin(this.fld_userName.getText(),
                                                                    new String(this.fld_password.getPassword()));
                if (loginUser == null) {
                    Helper.showMessage("notFound");
                } else {
                    switch (loginUser.getRole()) {
                        case "admin":
                            new AdminView(loginUser);
                            dispose();
                            break;
                        case "staff":
                            new StaffView(loginUser);
                            dispose();
                            break;
                        default:
                            Helper.showMessage("unauthorized");
                            break;
                    }
                }
            }
        });
    }
}
