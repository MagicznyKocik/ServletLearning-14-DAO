package pl.adamLupinski.dao;

import pl.adamLupinski.model.Book;
import pl.adamLupinski.util.ConnectionProvider;
import pl.adamLupinski.util.DbOperationException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySQLBookDao implements BookDao {

    private final static String CREATE = "INSERT INTO book(isbn, title, description) VALUES(?, ?, ?);";
    private final static String READ = "SELECT isbn, title, description FROM book WHERE isbn = ?;";
    private final static String UPDATE = "UPDATE book SET title=?, description=? WHERE isbn = ?;";
    private final static String DELETE = "DELETE FROM book WHERE isbn=?;";

    public  void create(Book book){
        try (Connection connection = ConnectionProvider.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(CREATE)) {
            preparedStatement.setString(1, book.getIsbn());
            preparedStatement.setString(2, book.getTitle());
            preparedStatement.setString(3, book.getDescription());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DbOperationException(e);
        }
    }

    public Book read(String isbn)  {
        Book resultBook = null;
        try(Connection connection = ConnectionProvider.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(READ)){
            preparedStatement.setString(1, isbn);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                resultBook = new Book();
                resultBook.setIsbn(resultSet.getString("isbn"));
                resultBook.setTitle(resultSet.getString("title"));
                resultBook.setDescription(resultSet.getString("description"));
            }
        } catch (SQLException e) {
            throw new DbOperationException(e);
        }
        return resultBook;
    }

    public void update(Book book) {
        try (Connection conn = ConnectionProvider.getConnection();
             PreparedStatement prepStmt = conn.prepareStatement(UPDATE)) {
            prepStmt.setString(1, book.getTitle());
            prepStmt.setString(2, book.getDescription());
            prepStmt.setString(3, book.getIsbn());
            prepStmt.executeUpdate();
        } catch (SQLException e) {
            throw new DbOperationException(e);
        }
    }

    public void delete(Book book) {
        try (Connection conn = ConnectionProvider.getConnection();
             PreparedStatement prepStmt = conn.prepareStatement(DELETE)) {
            prepStmt.setString(1, book.getIsbn());
            prepStmt.executeUpdate();
        } catch (SQLException e) {
            throw new DbOperationException(e);
        }
    }




}
