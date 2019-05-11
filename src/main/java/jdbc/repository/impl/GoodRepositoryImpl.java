package jdbc.repository.impl;

import jdbc.repository.GoodRepository;
import lombok.extern.slf4j.Slf4j;
import model.Good;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class GoodRepositoryImpl implements GoodRepository<Good> {

    private final Connection connection;

    public GoodRepositoryImpl (Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Good> getGoodsFromCategory(int categoryId) {
        final List<Good> result = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(SQLGood.GET_GOODS_FROM_CAT.QUERY)) {
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
            log.error(e.getMessage());
        }

        return result;
    }

    public List<Good> getGoodsFromOrderId(int orderId) {
        final List<Good> result = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(SQLGood.GET_GOODS_FROM_ORDER_ID.QUERY)) {
            statement.setInt(1, orderId);
            final ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                final Good resultGood = new Good();
                resultGood.setId(Integer.parseInt(rs.getString("id")));
                resultGood.setName(rs.getString("name"));
                resultGood.setPrice(Integer.parseInt(rs.getString("price")));

                result.add(resultGood);
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }

        return result;
    }

    enum SQLGood {

        GET_GOODS_FROM_CAT("SELECT * FROM goods WHERE category_id = ?"),
        GET_GOODS_FROM_ORDER_ID("SELECT * FROM goods WHERE id IN (SELECT good_id FROM goods_orders WHERE order_id = ?)");

        String QUERY;

        SQLGood(String QUERY) {
            this.QUERY = QUERY;
        }
    }
}
