package dao;

import domain.AppricationProperties;
import domain.DataBaseRepository;
import lombok.AllArgsConstructor;
import model.Client;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class ClientDao {

    private DataBaseRepository repository;

    public List<Client> getClientFromDB(String login, String password, int isAdmin) throws SQLException {
        List<Client> getClients = new ArrayList<>();
        final String sqlQuery =
                String.format(
                        "SELECT * FROM client WHERE login %s AND password %s %s",
                        "= '" + login + "'",
                        "= '" + password + "'",
                        isAdmin == AppricationProperties.SELECT_ALL_INT ? "" : "AND isAdmin = " + isAdmin
                );

        try (Connection connection = repository.createConnection();
             PreparedStatement pstmt = connection.prepareStatement(sqlQuery)) {

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Client client = new Client(rs.getInt("id"), rs.getString("name"), rs.getString("surname"),
                        rs.getString("login"), rs.getString("password"), rs.getInt("isAdmin"));
                getClients.add(client);
            }
        }

        return getClients;
    }
}
