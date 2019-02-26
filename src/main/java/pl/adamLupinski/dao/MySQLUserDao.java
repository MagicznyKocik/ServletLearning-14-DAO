package pl.adamLupinski.dao;

import pl.adamLupinski.model.User;
import pl.adamLupinski.util.ConnectionProvider;
import pl.adamLupinski.util.DbOperationException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySQLUserDao implements UserDao {


    private final static String CREATE = "insert into user(pesel, firstname, lastname) values (?,?,?);";
    private final static String READ = "select pesel, firstname, lastname from user where pesel = ?;";
    private final static String UPDATE = "update user set firstname=?, lastname=? where pesel = ?;";
    private final static String DELETE = "delete from user where pesel = ?;";


    @Override
    public void create(User user) {
        try(Connection connection = ConnectionProvider.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(CREATE)){
            preparedStatement.setString(1,user.getPesel());
            preparedStatement.setString(2,user.getFirstName());
            preparedStatement.setString(3,user.getLastName());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DbOperationException(e);
        }
    }

    @Override
    public User read(String pesel) {
        User resultUser =null;
        try(Connection connection = ConnectionProvider.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(READ)){
            preparedStatement.setString(1, pesel);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                resultUser =new User();
                resultUser.setPesel(resultSet.getString("pesel"));
                resultUser.setFirstName(resultSet.getString("firstname"));
                resultUser.setLastName(resultSet.getString("lastname"));
            }
        } catch (SQLException e) {
            throw new DbOperationException(e);
        }
        return resultUser;
    }

    @Override
    public void update(User user) {
        try (Connection conn = ConnectionProvider.getConnection();
             PreparedStatement prepStmt = conn.prepareStatement(UPDATE);) {
            prepStmt.setString(1, user.getFirstName());
            prepStmt.setString(2, user.getLastName());
            prepStmt.setString(3, user.getPesel());
            prepStmt.executeUpdate();
        } catch(SQLException e) {
            throw new DbOperationException(e);
        }
    }

    @Override
    public void delete(User user) {
        try (Connection conn = ConnectionProvider.getConnection();
             PreparedStatement prepStmt = conn.prepareStatement(DELETE);) {
            prepStmt.setString(1, user.getPesel());
            prepStmt.executeUpdate();
        } catch(SQLException e) {
            throw new DbOperationException(e);
        }
    }
}
