package view;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;

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
            String username = this.fld_userName.getText();
            char[] password = this.fld_password.getPassword();
            if (Helper.isFieldListEmpty(username) || password.length == 0) {
                Arrays.fill(password, '\0');
                this.fld_password.setText("");
                Helper.showMessage("fill");
                return;
            }

            this.fld_password.setText("");
            this.btn_login.setEnabled(false);
            new SwingWorker<AppUser, Void>() {
                @Override
                protected AppUser doInBackground() {
                    try {
                        return appUserManager.findByLogin(username, password);
                    } finally {
                        Arrays.fill(password, '\0');
                    }
                }

                @Override
                protected void done() {
                    btn_login.setEnabled(true);
                    try {
                        completeLogin(get());
                    } catch (InterruptedException exception) {
                        Thread.currentThread().interrupt();
                        Helper.showMessage("Login interrupted");
                    } catch (ExecutionException exception) {
                        Helper.showMessage("Login failed");
                    }
                }
            }.execute();
        });
    }

    private void completeLogin(AppUser loginUser) {
        if (loginUser == null) {
            Helper.showMessage("notFound");
            return;
        }

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
