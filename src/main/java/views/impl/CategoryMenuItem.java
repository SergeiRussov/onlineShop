package views.impl;

import jdbc.JDBCUtils;
import jdbc.impl.GoodRepositoryImpl;
import lombok.Data;
import model.Good;
import views.Executable;

import java.sql.Connection;
import java.util.List;

@Data
public class CategoryMenuItem implements Executable {

    private JDBCUtils driver = new JDBCUtils();
    private List<Good> goods;
    private Connection connection;
    private int id;

    @Override
    public void run() {
        getGoodsFromCategory(id);

        for (Good good : goods) {
            System.out.println(good.getId() + ". " + good.getName() + " " + good.getPrice());
        }
    }

    private void getGoodsFromCategory(int id) {
        connection = driver.createConnection();
        goods = new GoodRepositoryImpl(connection).getGoodsFromCategory(id);
        driver.closeConnection(connection);
    }
}
