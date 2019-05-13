package jdbc.repository.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jdbc.repository.GoodRepository;
import lombok.extern.slf4j.Slf4j;
import model.Good;

import java.io.*;
import java.lang.reflect.Type;
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

    @Override
    public boolean addGoods(File file) {
        Type itemsListType = new TypeToken<List<Good>>() {}.getType();
        boolean result = false;

        try (Reader reader = new FileReader(file)) {
            List<Good> newGoods = new Gson().fromJson(reader, itemsListType);

            for (Good good : newGoods) {
                result = addGoodFromBase(good);
            }
        } catch (FileNotFoundException e) {
            log.error(e.getMessage());
        } catch (IOException e) {
            log.error(e.getMessage());
        }

        return result;
    }

    private boolean addGoodFromBase(Good newGood) {
        boolean result = false;

        try (PreparedStatement statement = connection.prepareStatement(SQLGood.ADD_GOOD.QUERY)) {
            statement.setString(1, newGood.getName());
            statement.setInt(2, newGood.getPrice());
            statement.setInt(3, newGood.getCategoryId());

            statement.executeUpdate();

            result = true;
        } catch (SQLException e) {
            log.error(e.getMessage());
        }

        return result;
    }

    enum SQLGood {

        GET_GOODS_FROM_CAT("SELECT * FROM goods WHERE category_id = ?"),
        GET_GOODS_FROM_ORDER_ID("SELECT * FROM goods WHERE id IN (SELECT good_id FROM goods_orders WHERE order_id = ?)"),
        ADD_GOOD("INSERT INTO goods (name, price, category_id) VALUES (?, ?, ?)");

        String QUERY;

        SQLGood(String QUERY) {
            this.QUERY = QUERY;
        }
    }
}
