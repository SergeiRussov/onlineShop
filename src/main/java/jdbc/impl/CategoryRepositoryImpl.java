package jdbc.impl;

import jdbc.CategoryRepository;
import lombok.extern.slf4j.Slf4j;
import model.Category;
import model.Good;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class CategoryRepositoryImpl implements CategoryRepository<Category> {

    private final Connection connection;

    public CategoryRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Category> getCategories() {
        final List<Category> result = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(SQLCategory.GET_CATEGORIES.QUERY)) {
            final ResultSet resultSet = statement.executeQuery();
            int id = 0;
            while (resultSet.next()) {
                id++;
                final Category category = new Category();
                category.setId(Integer.parseInt(resultSet.getString("id")));
                category.setName(resultSet.getString("name"));
                category.setGoods(new GoodRepositoryImpl(connection).getGoodsFromCategory(id));
                result.add(category);
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }

        return result;
    }

    enum SQLCategory {

        GET_CATEGORIES("SELECT * FROM categories");

        String QUERY;

        SQLCategory(String QUERY) {
            this.QUERY = QUERY;
        }
    }
}
