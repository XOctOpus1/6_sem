import dao.BookDao;
import dao.CarDao;
import dao.ReportDao;
import domain.AppricationProperties;
import model.Car;
import org.junit.jupiter.api.Test;
import service.CarService;

import javax.servlet.http.HttpSession;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CarServiceTest {

    private final CarDao carDao = mock(CarDao.class);
    private final ReportDao reportDao = mock(ReportDao.class);
    private final BookDao bookDao = mock(BookDao.class);

    private final HttpSession session = mock(HttpSession.class);
    private final CarService carService = new CarService(carDao, bookDao, reportDao);

    private final Car car = new Car(1, "car", true, 100);

    @Test
    void book() throws Exception {
        when(session.getAttribute("sessionId")).thenReturn(AppricationProperties.ID_USER);
        when(carDao.getCarFromDB(1)).thenReturn(car);
        when(session.getAttribute("id")).thenReturn(1);

        carService.bookCar(session, 1, 20);

        verify(bookDao).insertBook(1, 1, 20, 2000);
    }

    @Test
    void pay() throws Exception {
        when(session.getAttribute("sessionId")).thenReturn(AppricationProperties.ID_USER);
        when(carDao.getCarFromDB(1)).thenReturn(car);
        when(session.getAttribute("id")).thenReturn(1);
        when(bookDao.existBook(1, 1)).thenReturn(true);

        carService.pay(session, 1);

        verify(carDao).updateCar(1, false);
        verify(bookDao).updateBook(1, 1);
    }

    @Test
    void report() throws Exception {
        when(session.getAttribute("sessionId")).thenReturn(AppricationProperties.ID_ADMIN);
        when(bookDao.existActiveBook(1, 1)).thenReturn(true);

        carService.report(session, 1, 1, true, "message", 20);

        verify(reportDao).insertReport(1, 1, true, "message", 20);
        verify(bookDao).deleteBook(1, 1);
        verify(carDao).updateCar(1, true);
    }

}