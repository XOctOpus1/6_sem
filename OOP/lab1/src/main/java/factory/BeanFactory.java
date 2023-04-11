package factory;

import dao.BookDao;
import dao.CarDao;
import dao.ClientDao;
import dao.ReportDao;
import domain.DataBaseRepository;
import service.CarService;
import service.LoginService;

import java.util.HashMap;
import java.util.Map;

public class BeanFactory {

    private static Map<Class<?>, Object> beans = new HashMap<>();

    static {

        DataBaseRepository repository = DataBaseRepository.init();
        beans.put(DataBaseRepository.class, repository);

        ClientDao clientDao = new ClientDao(repository);
        beans.put(ClientDao.class, clientDao);

        LoginService loginService = new LoginService(clientDao);
        beans.put(LoginService.class, loginService);

        CarDao carDao = new CarDao(repository);
        beans.put(CarDao.class, carDao);

        BookDao bookDao = new BookDao(repository);
        beans.put(BookDao.class, bookDao);

        ReportDao reportDao = new ReportDao(repository);
        beans.put(ReportDao.class, reportDao);

        CarService carService = new CarService(carDao, bookDao, reportDao);
        beans.put(CarService.class, carService);
    }

    public static Object getBean(Class<?> beanClass) {
        return beans.get(beanClass);
    }

}
