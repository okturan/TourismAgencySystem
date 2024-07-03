package business;

import java.util.ArrayList;

import dao.AppUserDao;
import entity.AppUser;

public class AppUserManager extends BaseManager<AppUser> {

    public AppUserManager() {
        super(new AppUserDao());
    }

    public AppUser findByLogin(String username, String password) {
        return ((AppUserDao) getDao()).findByLogin(username, password);
    }

    public ArrayList<Object[]> formatDataForTable(ArrayList<AppUser> users) {
        ArrayList<Object[]> userRows = new ArrayList<>();
        for (AppUser user : users) {
            Object[] rowObject = new Object[]{
                    user.getId(),
                    user.getUsername(),
                    user.getPassword(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getEmail(),
                    user.getRole(),
            };
            userRows.add(rowObject);
        }
        return userRows;
    }

    public ArrayList<AppUser> getByRole(String role) {
        return ((AppUserDao) getDao()).getByRole(role);
    }
}
