package controllers;

import domain.AppricationProperties;
import factory.BeanFactory;
import lombok.SneakyThrows;
import service.CarService;
import service.LoginService;
import validation.Validation;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/cars")
public class Car extends HttpServlet {

    @SneakyThrows
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        if (session.getAttribute("sessionId") == null || session.getAttribute("sessionId") == AppricationProperties.ERROR) {
            response.getWriter().println("Unauthorized");
            return;
        }

        CarService service = (CarService) BeanFactory.getBean(CarService.class);

        final var cars = service.getCars(session);

        response.getWriter().println(cars);
    }
}
