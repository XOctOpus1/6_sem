package controllers;

import domain.AppricationProperties;
import factory.BeanFactory;
import service.LoginService;
import validation.Validation;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/login")
public class Login extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        if (session.getAttribute("sessionId") == null)
            session.setAttribute("sessionId", AppricationProperties.LOGIN_ACCOUNT);
        String pass;
        String login;
        try {
            pass = request.getParameter("pass").equals("") ? "" : request.getParameter("pass");
            login = request.getParameter("login");
        } catch (Exception e) {
            pass = session.getAttribute("pass").toString();
            login = session.getAttribute("login").toString();
        }
        pass = Validation.checkValidation(Validation.checkXSS(pass));
        login = Validation.checkValidation(Validation.checkXSS(login));
        session.setAttribute("pass", pass);
        session.setAttribute("login", login);
        try {
            ((LoginService) BeanFactory.getBean(LoginService.class)).loginAccount(session, pass, login);
        } catch (SQLException ignored) {
        }
        if (session.getAttribute("sessionId") != AppricationProperties.ERROR) {
            response.getWriter().println(session.getAttribute("name"));
            return;
        }
        response.getWriter().println("Error");
    }
}
