package service.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import model.Good;
import service.GoodService;

import java.io.*;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Slf4j
public class GoodServiceImpl implements GoodService {

    private final Connection connection;

    public GoodServiceImpl(Connection connection) {
        this.connection = connection;
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

        ADD_GOOD("INSERT INTO goods (name, price, category_id) VALUES (?, ?, ?)");

        String QUERY;

        SQLGood(String QUERY) {
            this.QUERY = QUERY;
        }
    }
}
