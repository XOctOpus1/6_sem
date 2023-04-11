package service;

import dao.BookDao;
import dao.CarDao;
import dao.ReportDao;
import domain.AppricationProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import model.Book;
import model.Car;
import model.Report;

import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.List;

@Data
@AllArgsConstructor
public class CarService {

    private CarDao carDao;
    private BookDao bookDao;
    private ReportDao reportDao;

    public List<Car> getCars(HttpSession session) throws SQLException {
        if (session.getAttribute("sessionId") == AppricationProperties.ID_ADMIN)
            return carDao.getCarsFromDB();
        return carDao.getCarsFromDB(true);
    }

    public List<Car> getCarsByUser(HttpSession session, Boolean paid) throws Exception {
        if (session.getAttribute("sessionId") == AppricationProperties.ID_ADMIN)
            throw new Exception("You are administration");

        Integer userId = (Integer) session.getAttribute("id");

        return carDao.getCarsFromDB(userId, paid);
    }

    public void bookCar(HttpSession session, Integer carId, Integer durationInDays) throws Exception {
        if (session.getAttribute("sessionId") == AppricationProperties.ID_ADMIN)
            throw new Exception("You are administration");

        Car car = carDao.getCarFromDB(carId);

        if (car == null)
            throw new Exception("Car not found");

        if (!car.getAvailabel())
            throw new Exception("Car Unavailable");

        Integer cost = durationInDays * car.getCostPerDay();
        Integer userId = (Integer) session.getAttribute("id");

        bookDao.insertBook(userId, carId, durationInDays, cost);
    }

    public List<Book> books(HttpSession session) throws Exception {
        if (session.getAttribute("sessionId") == AppricationProperties.ID_ADMIN)
            return bookDao.getBooksForAmin();
        else return bookDao.getBooksForClient((Integer) session.getAttribute("id"));
    }

    public void report(HttpSession session, Integer userId, Integer carId, Boolean hasInjuries, String message, Integer cost) throws Exception {
        if (session.getAttribute("sessionId") == AppricationProperties.ID_USER)
            throw new Exception("Has not permissions");

        if (!bookDao.existActiveBook(userId, carId))
            throw new Exception("Book not found or already reported");

        reportDao.insertReport(userId, carId, hasInjuries, message, cost);

        bookDao.deleteBook(userId, carId);

        carDao.updateCar(carId, true);
    }


    public List<Report> reports(HttpSession session) throws Exception {
        if (session.getAttribute("sessionId") == AppricationProperties.ID_ADMIN)
            return reportDao.getReportsForAmin();
        else return reportDao.getReportsForClient((Integer) session.getAttribute("id"));
    }

    public void reject(HttpSession session, Integer userId, Integer carId, String message) throws Exception {
        if (session.getAttribute("sessionId") == AppricationProperties.ID_USER)
            throw new Exception("Has not permissions");

        if (!bookDao.existBook(userId, carId))
            throw new Exception("Book not found or already payed or blocked");

        bookDao.updateBook(userId, carId, message);
    }


    public void pay(HttpSession session, Integer carId) throws Exception {
        if (session.getAttribute("sessionId") == AppricationProperties.ID_ADMIN)
            throw new Exception("You are administration");

        Car car = carDao.getCarFromDB(carId);

        if (car == null)
            throw new Exception("Car not found");

        if (!car.getAvailabel())
            throw new Exception("Car Unavailable");

        Integer userId = (Integer) session.getAttribute("id");

        if (!bookDao.existBook(userId, carId))
            throw new Exception("Book not found or already payed or blocked by admin");


        carDao.updateCar(carId, false);
        bookDao.updateBook(userId, carId);
    }

}
