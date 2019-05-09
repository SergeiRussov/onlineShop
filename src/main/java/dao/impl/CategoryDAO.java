package dao.impl;

import dao.DAO;
import lombok.extern.slf4j.Slf4j;
import model.Category;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
public class CategoryDAO implements DAO<Category, String> {

    private final Connection connection;

    public CategoryDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean create(Category model) {
        return false;
    }

    @Override
    public Category read(String s) {
        final Category result = new Category();
        result.setId(-1);

        try (PreparedStatement statement = connection.prepareStatement(SQLCategory.GET.QUERY)) {
            statement.setInt(1, Integer.parseInt(s));
            final ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                result.setId(Integer.parseInt(rs.getString("id")));
                result.setName(rs.getString("name"));
                result.setGoods(new GoodDAO(connection).readGoodsFromCategory(Integer.parseInt(s)));
            }

        } catch (SQLException e) {
            log.error(e.getMessage());
        }

        return result;
    }

    @Override
    public boolean update(Category model) {
        return false;
    }

    @Override
    public boolean delete(String s) {
        return false;
    }

    enum SQLCategory {

        INSERT(""),
        GET("SELECT * FROM Categories WHERE id = ?"),
        UPDATE(""),
        DELETE("");

        String QUERY;

        SQLCategory(String QUERY) {
            this.QUERY = QUERY;
        }
    }
}
