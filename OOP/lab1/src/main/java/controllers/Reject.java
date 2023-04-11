package controllers;

import domain.AppricationProperties;
import factory.BeanFactory;
import model.Book;
import service.CarService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/book/reject")
public class Reject extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        if (session.getAttribute("sessionId") == null || session.getAttribute("sessionId") == AppricationProperties.ERROR) {
            response.getWriter().println("Unauthorized");
            return;
        }

        if (request.getParameter("userId") == null) {
            response.getWriter().println("Need userId query param");
            return;
        }

        if (request.getParameter("carId") == null) {
            response.getWriter().println("Need carId query param");
            return;
        }
        if (request.getParameter("message") == null) {
            response.getWriter().println("Need message query param");
            return;
        }

        CarService service = (CarService) BeanFactory.getBean(CarService.class);

        try {
            service.reject(session, Integer.parseInt(request.getParameter("userId")), Integer.parseInt(request.getParameter("carId")), request.getParameter("message"));
        } catch (Exception e) {
            response.getWriter().println(e.getMessage());
            return;
        }
        response.getWriter().println("Done");

    }
}
