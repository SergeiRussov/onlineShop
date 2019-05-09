package dao.impl;

import dao.DAO;
import lombok.extern.slf4j.Slf4j;
import model.Good;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class GoodDAO implements DAO<Good, String> {

    private final Connection connection;

    public GoodDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean create(Good good) {
        boolean result = false;

//        try (PreparedStatement preparedStatement = connection.prepareStatement(SQLModel))
        return false;
    }

    @Override
    public Good read(String id) {
        final Good result = new Good();
        result.setId(-1);

        try (PreparedStatement statement = connection.prepareStatement(GoodDAO.SQLGood.GET.QUERY)) {
            statement.setString(1, id);
            final ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                result.setId(Integer.parseInt(rs.getString("id")));
                result.setName(rs.getString("name"));
                result.setPrice(Integer.parseInt(rs.getString("price")));
            }

        } catch (SQLException e) {
            log.error(e.getSQLState());
        }

        return result;
    }

    public List<Good> readGoodsFromCategory(int categoryId) {
        final List<Good> result = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(GoodDAO.SQLGood.GET.QUERY)) {
            statement.setInt(1, categoryId);
            final ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                final Good resultGood = new Good();
                resultGood.setId(Integer.parseInt(rs.getString("id")));
                resultGood.setName(rs.getString("name"));
                resultGood.setPrice(Integer.parseInt(rs.getString("price")));
                result.add(resultGood);
            }

        } catch (SQLException e) {
            log.error(e.getSQLState());
        }

        return result;
    }

    @Override
    public boolean update(Good model) {
        return false;
    }

    @Override
    public boolean delete(String s) {
        return false;
    }

    enum SQLGood {

        GET("SELECT * FROM goods WHERE category_id = ?"),
        INSERT(""),
        DELETE(""),
        UPDATE("");

        String QUERY;

        SQLGood(String QUERY) {
            this.QUERY = QUERY;
        }
    }
}
