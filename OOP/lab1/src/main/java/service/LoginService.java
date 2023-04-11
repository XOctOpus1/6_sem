package service;

import dao.ClientDao;
import domain.AppricationProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import model.Client;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.List;

@Data
@AllArgsConstructor
public class LoginService {

    private ClientDao clientDao;

    public void loginAccount(HttpSession session, String pass, String login) throws SQLException {
        int selectAllInt = AppricationProperties.SELECT_ALL_INT;

        List<Client> clients = clientDao.getClientFromDB(login, pass, selectAllInt);
        System.out.println(clients);
        if (clients.size() == 1) {
            Client client = clients.get(0);
            session.setAttribute("name", client.getName());
            session.setAttribute("surname", client.getSurname());
            session.setAttribute("id", client.getId());
            if (client.getIsAdmin() == 0) {
                session.setAttribute("sessionId", AppricationProperties.ID_USER);
                session.setAttribute("type", "Client");
            } else {
                session.setAttribute("sessionId", AppricationProperties.ID_ADMIN);
                session.setAttribute("type", "Admin");
            }
        } else {
            session.setAttribute("sessionId", AppricationProperties.ERROR);
            session.setAttribute("type", "Wrong");
        }
    }

}
