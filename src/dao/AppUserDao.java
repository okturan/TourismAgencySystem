package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import entity.AppUser;

public class AppUserDao extends BaseDao<AppUser> {

    public AppUserDao() {
        super("users");
    }

    public AppUser findByUsername(String username) {
        String query = "SELECT * FROM users WHERE username = ?";
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(query)) {
            preparedStatement.setString(1, username);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToEntity(resultSet);
                }
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public ArrayList<AppUser> getByRole(String role) {
        ArrayList<AppUser> users = new ArrayList<>();
        String query = "SELECT * FROM users WHERE role = ?";
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(query)) {
            preparedStatement.setString(1, role);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    users.add(mapResultSetToEntity(resultSet));
                }
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return users;
    }

    public boolean updatePasswordIfCurrent(int id, String expectedPassword, String passwordHash) {
        String query = "UPDATE users SET password = ? WHERE id = ? AND password = ?";
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(query)) {
            preparedStatement.setString(1, passwordHash);
            preparedStatement.setInt(2, id);
            preparedStatement.setString(3, expectedPassword);
            return preparedStatement.executeUpdate() == 1;
        } catch (SQLException exception) {
            exception.printStackTrace();
            return false;
        }
    }

    public boolean hasAdmin() {
        try {
            return hasAdmin(getConnection());
        } catch (SQLException exception) {
            throw new IllegalStateException("Unable to check for an administrator account.", exception);
        }
    }

    private boolean hasAdmin(java.sql.Connection connection) throws SQLException {
        String query = "SELECT 1 FROM users WHERE role = 'admin' LIMIT 1";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            return resultSet.next();
        }
    }

    public boolean insertFirstAdmin(AppUser user) {
        java.sql.Connection connection = getConnection();
        boolean autoCommitChanged = false;
        try {
            if (!connection.getAutoCommit()) {
                throw new IllegalStateException("First-admin provisioning cannot run inside another transaction.");
            }

            connection.setAutoCommit(false);
            autoCommitChanged = true;
            try (Statement statement = connection.createStatement()) {
                statement.execute("LOCK TABLE users IN EXCLUSIVE MODE");
            }

            if (hasAdmin(connection)) {
                connection.rollback();
                return false;
            }

            String query = "INSERT INTO users (username, password, first_name, last_name, email, role) "
                    + "VALUES (?, ?, ?, ?, ?, 'admin')";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, user.getUsername());
                preparedStatement.setString(2, user.getPassword());
                preparedStatement.setString(3, user.getFirstName());
                preparedStatement.setString(4, user.getLastName());
                preparedStatement.setString(5, user.getEmail());
                if (preparedStatement.executeUpdate() != 1) {
                    connection.rollback();
                    return false;
                }
            }

            connection.commit();
            return true;
        } catch (SQLException exception) {
            if (autoCommitChanged) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackException) {
                    exception.addSuppressed(rollbackException);
                }
            }
            throw new IllegalStateException("Unable to provision the first administrator.", exception);
        } catch (RuntimeException exception) {
            if (autoCommitChanged) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackException) {
                    exception.addSuppressed(rollbackException);
                }
            }
            throw exception;
        } finally {
            if (autoCommitChanged) {
                try {
                    connection.setAutoCommit(true);
                } catch (SQLException exception) {
                    throw new IllegalStateException("Unable to restore database auto-commit mode.", exception);
                }
            }
        }
    }

    @Override
    protected void setParameters(PreparedStatement preparedStatement, AppUser user) throws SQLException {
        preparedStatement.setString(1, user.getUsername());
        preparedStatement.setString(2, user.getPassword());
        preparedStatement.setString(3, user.getFirstName());
        preparedStatement.setString(4, user.getLastName());
        preparedStatement.setString(5, user.getEmail());
        preparedStatement.setString(6, user.getRole());
    }

    @Override
    public AppUser mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        AppUser appUser = new AppUser();
        appUser.setId(resultSet.getInt("id"));
        appUser.setUsername(resultSet.getString("username"));
        appUser.setPassword(resultSet.getString("password"));
        appUser.setFirstName(resultSet.getString("first_name"));
        appUser.setLastName(resultSet.getString("last_name"));
        appUser.setEmail(resultSet.getString("email"));
        appUser.setRole(resultSet.getString("role"));
        return appUser;
    }
}
