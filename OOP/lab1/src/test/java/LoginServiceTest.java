import dao.ClientDao;
import domain.AppricationProperties;
import model.Client;
import org.junit.jupiter.api.Test;
import service.LoginService;

import javax.servlet.http.HttpSession;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class LoginServiceTest {

    private final ClientDao clientDao = mock(ClientDao.class);

    private final HttpSession session = mock(HttpSession.class);
    private final LoginService loginService = new LoginService(clientDao);

    private final Client client = new Client(1, "name", "nam", "login", "password", 0);

    @Test
    void loginAccount() throws Exception {

        when(clientDao.getClientFromDB("login", "password", -1)).thenReturn(List.of(client));

        loginService.loginAccount(session, "password", "login");

        verify(session).setAttribute("sessionId", AppricationProperties.ID_USER);
        verify(session).setAttribute("type", "Client");
    }


}