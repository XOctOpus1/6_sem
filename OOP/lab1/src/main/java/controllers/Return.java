package controllers;

import domain.AppricationProperties;
import factory.BeanFactory;
import service.CarService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/car/return")
public class СarReturn extends HttpServlet {
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
        if (request.getParameter("hasInjuries") == null) {
            response.getWriter().println("Need hasInjuries query param");
            return;
        }
        if (Boolean.getBoolean(request.getParameter("hasInjuries"))) {
            if (request.getParameter("message") == null) {
                response.getWriter().println("Need message query param");
                return;
            }
            if (request.getParameter("cost") == null) {
                response.getWriter().println("Need cost query param");
                return;
            }
        }

        CarService service = (CarService) BeanFactory.getBean(CarService.class);

        try {
            service.report(session, Integer.parseInt(request.getParameter("userId")), Integer.parseInt(request.getParameter("carId")),
                    Boolean.getBoolean(request.getParameter("hasInjuries")), request.getParameter("message"), Integer.parseInt(request.getParameter("cost")));
        } catch (Exception e) {
            response.getWriter().println(e.getMessage());
            return;
        }
        response.getWriter().println("Done");
    }
}
