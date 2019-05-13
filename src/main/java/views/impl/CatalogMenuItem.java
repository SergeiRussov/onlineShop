package views.impl;

import jdbc.JDBCUtils;
import jdbc.repository.impl.CategoryRepositoryImpl;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import model.Category;
import views.Executable;

import java.io.BufferedReader;
import java.io.IOException;
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
    private BufferedReader reader;

    public CatalogMenuItem(BufferedReader reader) {
        this.reader = reader;
    }

    @Override
    public void run() {
        connection = driver.createConnection();

        fillCategoryMenuItem();

        driver.closeConnection(connection);

        selectCategory(reader);
    }

    private void fillCategoryMenuItem() {
        list = new CategoryRepositoryImpl(connection).getCategories();

        for (Category category : list) {
            System.out.println(category.getId() + ". " + category.getName());
            CategoryMenuItem categoryMenuItem = new CategoryMenuItem(reader);
            categoryMenuItem.setId(category.getId());
            categoryMenuItems.put(category.getId(), categoryMenuItem);
        }
    }

    private void selectCategory(BufferedReader reader) {
        try {
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
