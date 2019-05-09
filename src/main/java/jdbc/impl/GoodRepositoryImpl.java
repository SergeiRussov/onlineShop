package jdbc.impl;

import jdbc.GoodRepository;
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
    public List getGoodsFromCategory(int id) {
        final List<Good> result = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(SQLGood.GET_GOODS_FROM_CAT.QUERY)) {
            statement.setInt(1, id);
            final ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                final Good resultGood = new Good();
                resultGood.setId(Integer.parseInt(resultSet.getString("id")));
                resultGood.setName(resultSet.getString("name"));
                resultGood.setPrice(Integer.parseInt(resultSet.getString("price")));
                result.add(resultGood);
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }

        return result;
    }

    enum SQLGood {

        GET_GOODS_FROM_CAT("SELECT * FROM goods WHERE category_id = ?");

        String QUERY;

        SQLGood(String QUERY) {
            this.QUERY = QUERY;
        }
    }
}
