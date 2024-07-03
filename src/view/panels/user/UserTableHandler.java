package view.panels.user;

import javax.swing.*;

import business.AppUserManager;
import entity.AppUser;
import view.panels.BaseTableHandler;

public class UserTableHandler extends BaseTableHandler<AppUser, AppUserManager, UserUpdateView> {

    private static final String[] HEADERS = {
            "id",
            "username",
            "password",
            "firstName",
            "lastName",
            "email",
            "role"
    };

    public UserTableHandler(JTable table) {
        super(HEADERS, table, new AppUserManager());
        initializeTable();
    }

    @Override
    protected UserUpdateView createViewInstance() {
        return new UserUpdateView();
    }

    public void getByRole(String role) {
        loadTable(getManager().getByRole(role));
    }
}
