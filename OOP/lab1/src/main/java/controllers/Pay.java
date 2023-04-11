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

@WebServlet("/car/pay")
public class Pay extends HttpServlet {

    @SneakyThrows
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

        CarService service = (CarService) BeanFactory.getBean(CarService.class);

        try {
            service.pay(session, Integer.parseInt(request.getParameter("carId")));
        } catch (Exception e) {
            response.getWriter().println(e.getMessage());
            return;
        }
        response.getWriter().println("Payed");
    }
}
