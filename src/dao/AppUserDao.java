package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import entity.AppUser;

public class AppUserDao extends BaseDao<AppUser> {

    public AppUserDao() {
        super("users");
    }

    public AppUser findByLogin(String username, String password) {
        Map<String, Object> columnValues = new HashMap<>();
        columnValues.put("username", username);
        columnValues.put("password", password);

        return findByColumns(columnValues);
    }

    public ArrayList<AppUser> getByRole(String role) {
        String query = "SELECT * FROM users WHERE role = '" + role + "'";
        return selectByQuery(query);
    }

    @Override
    protected void setParameters(PreparedStatement preparedStatement, AppUser user) throws SQLException {
        preparedStatement.setString(1, user.getUsername());
        preparedStatement.setString(2, user.getPassword());
        preparedStatement.setString(3, user.getFirstName());
        preparedStatement.setString(4, user.getLastName());
        preparedStatement.setString(5, user.getEmail());
        preparedStatement.setString(6, user.getRole());
        System.out.println(preparedStatement);
    }

    @Override
    public AppUser mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        AppUser appUser = new AppUser();
        appUser.setId(resultSet.getInt("id"));  // Use "id" instead of "user_id"
        appUser.setUsername(resultSet.getString("username"));
        appUser.setPassword(resultSet.getString("password"));
        appUser.setFirstName(resultSet.getString("first_name"));  // Add mapping for first name
        appUser.setLastName(resultSet.getString("last_name"));    // Add mapping for last name
        appUser.setEmail(resultSet.getString("email"));           // Add mapping for email
        appUser.setRole(resultSet.getString("role"));
        return appUser;
    }
}
