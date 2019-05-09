import dao.impl.CategoryDAO;
import jdbc.impl.CategoryRepositoryImpl;
import model.Category;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class App {

    static final String DB_URL = "jdbc:postgresql://127.0.0.1:5432/shop_base_v2";
    static final String USER = "postgres";
    static final String PASSWORD = "qwerty74123ib";

    public static void main(String[] args) {

        Connection connection = null;

        try {
            connection = DriverManager
                    .getConnection(DB_URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.out.println("connection failed");
            return;
        }

//        Category hardwareCategory = new CategoryDAO(connection).read("1");
//        Category perephirialsCategory = new CategoryDAO(connection).read("2");
//
//        System.out.println(perephirialsCategory.toString());
//        System.out.println(hardwareCategory.toString());

        List<Category> categories = new CategoryRepositoryImpl(connection).getCategories();

        for (Category category : categories) {
            System.out.println(category);
        }
    }
}
