package dao;

import domain.DataBaseRepository;
import lombok.AllArgsConstructor;
import model.Book;
import model.Car;
import model.Client;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class BookDao {

    private DataBaseRepository repository;

    public void insertBook(Integer userId, Integer carId, Integer duration, Integer cost) throws SQLException {
        final String sqlQuery =
                String.format(
                        "INSERT INTO book(client_id, car_id, rental_period_in_day, cost) " +
                                "VALUES (%d, %d, %d, %d)",
                        userId, carId, duration, cost
                );

        try (Connection connection = repository.createConnection();
             PreparedStatement pstmt = connection.prepareStatement(sqlQuery)) {
            pstmt.execute();
        }
    }

    public List<Book> getBooksForClient(Integer userId) throws SQLException {
        final String sqlQuery =
                String.format(
                        "SELECT * FROM book WHERE client_id = %d AND paid = false",
                        userId
                );

        return getBooks(sqlQuery);
    }

    public List<Book> getBooksForAmin() throws SQLException {
        final String sqlQuery =
                String.format(
                        "SELECT * FROM book WHERE paid = false"
                );

        return getBooks(sqlQuery);
    }

    private List<Book> getBooks(String sqlQuery) throws SQLException {
        final var books = new ArrayList<Book>();
        try (Connection connection = repository.createConnection();
             PreparedStatement pstmt = connection.prepareStatement(sqlQuery)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Book book = new Book(rs.getInt("client_id"), rs.getInt("car_id"), rs.getInt("rental_period_in_day"), rs.getInt("cost"), rs.getBoolean("allow"), rs.getBoolean("paid"), rs.getString("cause"));

                books.add(book);
            }
        }
        return books;
    }

    public boolean existBook(Integer userId, Integer carId) throws SQLException {
        final String sqlQuery =
                String.format(
                        "SELECT * FROM book WHERE client_id = %d AND car_id = %d AND allow = true AND paid = false",
                        userId, carId
                );

        try (Connection connection = repository.createConnection();
             PreparedStatement pstmt = connection.prepareStatement(sqlQuery)) {
            ResultSet resultSet = pstmt.executeQuery();
            return resultSet.next();
        }
    }

    public boolean existActiveBook(Integer userId, Integer carId) throws SQLException {
        final String sqlQuery =
                String.format(
                        "SELECT * FROM book WHERE client_id = %d AND car_id = %d AND allow = true AND paid = true",
                        userId, carId
                );

        try (Connection connection = repository.createConnection();
             PreparedStatement pstmt = connection.prepareStatement(sqlQuery)) {
            ResultSet resultSet = pstmt.executeQuery();
            return resultSet.next();
        }
    }

    public void deleteBook(Integer userId, Integer carId) throws SQLException {
        final String sqlQuery =
                String.format(
                        "DELETE FROM book WHERE client_id = %d AND car_id = %d AND allow = true AND paid = true",
                        userId, carId
                );

        try (Connection connection = repository.createConnection();
             PreparedStatement pstmt = connection.prepareStatement(sqlQuery)) {
            pstmt.execute();
        }
    }

    public void updateBook(Integer userId, Integer carId) throws SQLException {
        final String sqlQuery =
                String.format(
                        "UPDATE book SET paid = true WHERE client_id = %d AND car_id = %d AND allow = true AND paid = false",
                        userId, carId
                );

        try (Connection connection = repository.createConnection();
             PreparedStatement pstmt = connection.prepareStatement(sqlQuery)) {
            pstmt.execute();
        }
    }

    public void updateBook(Integer userId, Integer carId, String message) throws SQLException {
        final String sqlQuery =
                String.format(
                        "UPDATE book SET allow = false, cause = '%s' WHERE client_id = %d AND car_id = %d AND allow = true AND paid = false",
                        message, userId, carId
                );

        try (Connection connection = repository.createConnection();
             PreparedStatement pstmt = connection.prepareStatement(sqlQuery)) {
            pstmt.execute();
        }
    }
}
