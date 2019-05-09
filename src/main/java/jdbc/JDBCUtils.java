package jdbc;

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Slf4j
public class JDBCUtils {

    static final String DB_URL = "jdbc:postgresql://127.0.0.1:5432/shop_base_v2";
    static final String USER = "postgres";
    static final String PASSWORD = "qwerty74123ib";

    public Connection createConnection() {
        Connection connection = null;
        try {
            connection = DriverManager
                    .getConnection(DB_URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.out.println("connection failed");
            return null;
        }

        return connection;
    }

    public void closeConnection(Connection connection) {
        try {
            connection.close();
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
    }
}
