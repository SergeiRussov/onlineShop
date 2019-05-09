package views.impl;

import jdbc.JDBCUtils;
import jdbc.impl.CategoryRepositoryImpl;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import model.Category;
import views.Executable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Data
public class CatalogMenuItem implements Executable {

    private JDBCUtils driver = new JDBCUtils();
    private Connection connection;
    private List<Category> list;
    private Map<Integer, Executable> categoryMenuItems = new HashMap<>();

    @Override
    public void run() {
        connection = driver.createConnection();
        list = new CategoryRepositoryImpl(connection).getCategories();

        for (Category category : list) {
            System.out.println(category.getId() + ". " + category.getName());
            CategoryMenuItem categoryMenuItem = new CategoryMenuItem();
            categoryMenuItem.setId(category.getId());
            categoryMenuItems.put(category.getId(), categoryMenuItem);
        }

        driver.closeConnection(connection);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.print("\nВведите номер раздела: ");
            int id = Integer.parseInt(reader.readLine());
            if (categoryMenuItems.containsKey(id)) {
                categoryMenuItems.get(id).run();
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
