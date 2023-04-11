package controllers;

import domain.AppricationProperties;
import factory.BeanFactory;
import model.Report;
import service.CarService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/reports")
public class Reports extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        if (session.getAttribute("sessionId") == null || session.getAttribute("sessionId") == AppricationProperties.ERROR) {
            response.getWriter().println("Unauthorized");
            return;
        }

        CarService service = (CarService) BeanFactory.getBean(CarService.class);

        try {
            List<Report> reports = service.reports(session);

            response.getWriter().println(reports);
        } catch (Exception e) {
            response.getWriter().println(e.getMessage());
        }
    }
}
