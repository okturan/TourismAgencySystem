package business;

import java.util.ArrayList;

import dao.AppUserDao;
import entity.AppUser;
import security.PasswordHasher;
import security.PasswordPolicy;

public class AppUserManager extends BaseManager<AppUser> {

    public AppUserManager() {
        super(new AppUserDao());
    }

    public AppUser findByLogin(String username, char[] password) {
        if (isBlank(username) || password == null || password.length == 0 || password.length > 4_096) {
            return null;
        }

        AppUserDao userDao = getUserDao();
        AppUser user = userDao.findByUsername(username);
        if (user == null) {
            return null;
        }

        String storedPassword = user.getPassword();
        if (!PasswordHasher.verify(password, storedPassword)) {
            user.setPassword(null);
            return null;
        }

        if (!PasswordHasher.isEncoded(storedPassword) || PasswordHasher.needsRehash(storedPassword)) {
            String migratedHash = PasswordHasher.hashLegacy(password);
            if (!userDao.updatePasswordIfCurrent(user.getId(), storedPassword, migratedHash)) {
                user.setPassword(null);
                return null;
            }
        }

        user.setPassword(null);
        return user;
    }

    @Override
    public boolean save(AppUser user) {
        throw new IllegalStateException("Use createUser so plaintext passwords cannot reach the generic save path.");
    }

    @Override
    public boolean update(AppUser user) {
        throw new IllegalStateException("Use updateUser so plaintext passwords cannot reach the generic update path.");
    }

    public boolean createUser(AppUser user, char[] password) {
        if (user == null || user.getId() != 0 || !hasValidProfile(user)) {
            return false;
        }
        PasswordPolicy.requireStrong(password);

        user.setPassword(PasswordHasher.hash(password));
        try {
            return getUserDao().save(user);
        } finally {
            user.setPassword(null);
        }
    }

    public boolean updateUser(AppUser user, char[] password) {
        if (user == null || user.getId() == 0) {
            return false;
        }

        if (!hasValidProfile(user)) {
            return false;
        }

        try {
            if (password == null || password.length == 0) {
                return getUserDao().updateWithoutPassword(user);
            }

            PasswordPolicy.requireStrong(password);
            user.setPassword(PasswordHasher.hash(password));
            return getUserDao().update(user);
        } finally {
            user.setPassword(null);
        }
    }

    public boolean createFirstAdmin(
            String username,
            String firstName,
            String lastName,
            String email,
            char[] password
    ) {
        if (isBlank(username) || isBlank(firstName) || isBlank(lastName) || isBlank(email)) {
            throw new IllegalArgumentException("Admin profile fields must not be blank.");
        }
        PasswordPolicy.requireStrong(password);

        AppUser admin = new AppUser();
        admin.setUsername(username.trim());
        admin.setPassword(PasswordHasher.hash(password));
        admin.setFirstName(firstName.trim());
        admin.setLastName(lastName.trim());
        admin.setEmail(email.trim());
        admin.setRole("admin");

        try {
            return getUserDao().insertFirstAdmin(admin);
        } finally {
            admin.setPassword(null);
        }
    }

    public ArrayList<Object[]> formatDataForTable(ArrayList<AppUser> users) {
        ArrayList<Object[]> userRows = new ArrayList<>();
        for (AppUser user : users) {
            userRows.add(formatUserRow(user));
        }
        return userRows;
    }

    public ArrayList<AppUser> getByRole(String role) {
        return scrubPasswords(getUserDao().getByRole(role));
    }

    @Override
    public ArrayList<AppUser> findAll() {
        return scrubPasswords(getUserDao().findAll());
    }

    @Override
    public AppUser getById(int id) {
        return scrubPassword(getUserDao().findById(id));
    }

    public boolean hasAdmin() {
        return getUserDao().hasAdmin();
    }

    static Object[] formatUserRow(AppUser user) {
        return new Object[]{
                user.getId(),
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRole(),
        };
    }

    private AppUserDao getUserDao() {
        return (AppUserDao) getDao();
    }

    private static ArrayList<AppUser> scrubPasswords(ArrayList<AppUser> users) {
        for (AppUser user : users) {
            scrubPassword(user);
        }
        return users;
    }

    private static AppUser scrubPassword(AppUser user) {
        if (user != null) {
            user.setPassword(null);
        }
        return user;
    }

    private static boolean hasValidProfile(AppUser user) {
        return !isBlank(user.getUsername())
                && !isBlank(user.getFirstName())
                && !isBlank(user.getLastName())
                && !isBlank(user.getEmail())
                && ("admin".equals(user.getRole()) || "staff".equals(user.getRole()));
    }

    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
