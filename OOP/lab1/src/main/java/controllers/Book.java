package controllers;

import domain.AppricationProperties;
import factory.BeanFactory;
import lombok.SneakyThrows;
import service.CarService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/book")
public class Book extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        if (session.getAttribute("sessionId") == null || session.getAttribute("sessionId") == AppricationProperties.ERROR) {
            response.getWriter().println("Unauthorized");
            return;
        }
        if (request.getParameter("carId") == null) {
            response.getWriter().println("Need carId query param");
            return;
        }
        if (request.getParameter("duration") == null) {
            response.getWriter().println("Need duration query param");
            return;
        }

        CarService service = (CarService) BeanFactory.getBean(CarService.class);

        try {
            service.bookCar(session, Integer.parseInt(request.getParameter("carId")), Integer.parseInt(request.getParameter("duration")));
        } catch (Exception e) {
            response.getWriter().println(e.getMessage());
            return;
        }
        response.getWriter().println("Done");
    }
}
