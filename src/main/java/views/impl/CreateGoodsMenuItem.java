package views.impl;

import jdbc.JDBCUtils;
import jdbc.repository.impl.GoodRepositoryImpl;
import lombok.extern.slf4j.Slf4j;
import views.Executable;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;

@Slf4j
public class CreateGoodsMenuItem implements Executable {

    private JDBCUtils driver = new JDBCUtils();
    private Connection connection;
    private BufferedReader reader;

    public CreateGoodsMenuItem(BufferedReader reader) {
        this.reader = reader;
    }

    @Override
    public void run() {
        connection = driver.createConnection();

        System.out.println(downloadGoods(reader));

        driver.closeConnection(connection);
    }

    private String downloadGoods(BufferedReader reader) {
        boolean flag = false;

        try {
            System.out.print("\nВведите путь до файла: ");
            String path = reader.readLine();

            new GoodRepositoryImpl(connection).addGoods(new File(path));

            flag = true;
        } catch (IOException e) {
            log.error(e.getMessage());
        }

        String result = (flag ? "Успешно" : "Ошибка");

        return result;
    }
}
