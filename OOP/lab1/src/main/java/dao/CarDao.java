package dao;

import domain.DataBaseRepository;
import lombok.AllArgsConstructor;
import model.Car;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class CarDao {

    private DataBaseRepository repository;

    public List<Car> getCarsFromDB() throws SQLException {
        final String sqlQuery =
                String.format(
                        "SELECT * FROM car"
                );

        List<Car> getCars = runningQuery(sqlQuery);

        return getCars;
    }

    public List<Car> getCarsFromDB(Boolean available) throws SQLException {
        final String sqlQuery =
                String.format(
                        "SELECT * FROM car WHERE availabel = %s",
                        available
                );

        List<Car> getCars = runningQuery(sqlQuery);

        return getCars;
    }

    public List<Car> getCarsFromDB(Integer userId, Boolean paid) throws SQLException {
        final String sqlQuery =
                String.format(
                        "SELECT * FROM " +
                                "car INNER JOIN book ON car.id = book.car_id " +
                                "WHERE book.client_id = %d AND book.allow = true AND book.paid = %s",
                        userId, paid
                );

        List<Car> getCars = runningQuery(sqlQuery);

        return getCars;
    }

    public Car getCarFromDB(Integer id) throws SQLException {
        final String sqlQuery =
                String.format(
                        "SELECT * FROM car WHERE id = %s",
                        id
                );

        List<Car> getCars = runningQuery(sqlQuery);


        return getCars.size() != 1 ? null : getCars.get(0);
    }

    public void updateCar(Integer id, Boolean available) throws SQLException {
        final String sqlQuery =
                String.format(
                        "UPDATE car SET availabel = %s WHERE id = %d",
                        available, id
                );


        try (Connection connection = repository.createConnection();
             PreparedStatement pstmt = connection.prepareStatement(sqlQuery)) {

            pstmt.execute();
        }
    }

    private List<Car> runningQuery(String sqlQuery) throws SQLException {
        List<Car> getCars = new ArrayList<>();
        try (Connection connection = repository.createConnection();
             PreparedStatement pstmt = connection.prepareStatement(sqlQuery)) {

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Car car = new Car(rs.getInt("id"), rs.getString("name"), rs.getBoolean("availabel"), rs.getInt("cost_per_day"));

                getCars.add(car);
            }
        }
        return getCars;
    }

}
