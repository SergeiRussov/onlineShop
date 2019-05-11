package jdbc.repository.impl;

import jdbc.repository.CategoryRepository;
import lombok.extern.slf4j.Slf4j;
import model.Category;

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
            final ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                final Category category = new Category();
                int categoryId = rs.getInt("id");
                category.setId(categoryId);
                category.setName(rs.getString("name"));
                category.setGoods(new GoodRepositoryImpl(connection).getGoodsFromCategory(categoryId));

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
