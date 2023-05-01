package domain;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.concurrent.locks.ReentrantLock;

public class DataBaseRepository {
    private static final ReentrantLock lock = new ReentrantLock();
    private static String JDBC_DRIVER = "org.postgresql.Driver";
    private static String DB_URL = "jdbc:postgresql://localhost:5432/d";
    private static String USERNAME = "user";
    private static String PASSWORD = "password";
    private static DataBaseRepository repository;

    public static DataBaseRepository init() {
        if (repository != null) {
            return repository;
        }
        lock.lock();
        if (repository != null) {
            lock.unlock();
            return repository;
        }
        repository = new DataBaseRepository();
        lock.unlock();
        return repository;
    }

    public Connection createConnection() {
        Connection connection = null;
        try {
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
        } catch (Exception ignore) {
        }
        return connection;
    }
}

