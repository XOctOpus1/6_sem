package dao;

import domain.DataBaseRepository;
import lombok.AllArgsConstructor;
import model.Report;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class ReportDao {

    private DataBaseRepository repository;

    public void insertReport(Integer userId, Integer carId, Boolean hasInjuries, String message, Integer cost) throws SQLException {
        final String sqlQuery =
                String.format(
                        "INSERT INTO report(client_id, car_id, has_injuries, message, cost) " +
                                "VALUES (%d, %d, %s, '%s', %d)",
                        userId, carId, hasInjuries, message, cost
                );

        try (Connection connection = repository.createConnection();
             PreparedStatement pstmt = connection.prepareStatement(sqlQuery)) {
            pstmt.execute();
        }
    }

    public List<Report> getReportsForClient(Integer userId) throws SQLException {
        final String sqlQuery =
                String.format(
                        "SELECT * FROM report WHERE client_id = %d",
                        userId
                );

        return getReports(sqlQuery);
    }

    public List<Report> getReportsForAmin() throws SQLException {
        final String sqlQuery =
                String.format(
                        "SELECT * FROM report"
                );

        return getReports(sqlQuery);
    }

    private List<Report> getReports(String sqlQuery) throws SQLException {
        final var reports = new ArrayList<Report>();
        try (Connection connection = repository.createConnection();
             PreparedStatement pstmt = connection.prepareStatement(sqlQuery)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Report Report = new Report(rs.getInt("client_id"), rs.getInt("car_id"), rs.getBoolean("has_injuries"), rs.getString("message"), rs.getInt("cost"));

                reports.add(Report);
            }
        }
        return reports;
    }
}
