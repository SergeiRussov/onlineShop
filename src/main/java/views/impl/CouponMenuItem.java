package views.impl;

import jdbc.JDBCUtils;
import jdbc.repository.impl.CouponRepositoryImpl;
import lombok.extern.slf4j.Slf4j;
import views.Executable;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;

@Slf4j
public class CouponMenuItem implements Executable {

    private JDBCUtils driver = new JDBCUtils();
    private Connection connection;
    private BufferedReader reader;

    public CouponMenuItem(BufferedReader reader) {
        this.reader = reader;
    }

    @Override
    public void run() {
        connection = driver.createConnection();

        System.out.println(createCoupon(reader));

        driver.closeConnection(connection);
    }

    private String createCoupon(BufferedReader reader) {
        boolean flag = false;

        System.out.print("\nВведите величину скидки (в процентах): ");

        try {
            int discount = Integer.parseInt(reader.readLine());
            new CouponRepositoryImpl(connection).addCoupon(discount);

            flag = true;
        } catch (IOException e) {
            log.error(e.getMessage());
        }

        String result = (flag ? "Успешно" : "Ошибка");

        return result;
    }
}
