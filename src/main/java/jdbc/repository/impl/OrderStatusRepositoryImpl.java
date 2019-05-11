package jdbc.repository.impl;

import jdbc.repository.OrderStatusRepository;
import lombok.extern.slf4j.Slf4j;
import model.OrderStatus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class OrderStatusRepositoryImpl implements OrderStatusRepository<OrderStatus> {

    private final Connection connection;

    public OrderStatusRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<OrderStatus> getOrderStatusList() {
        final List<OrderStatus> orderStatuses = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(SQLOrderStatus.GET_ORDER_STATUSES.QUERY)) {
            final ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                final OrderStatus orderStatus = new OrderStatus();
                orderStatus.setId(rs.getInt("id"));
                orderStatus.setName(rs.getString("name"));

                orderStatuses.add(orderStatus);
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }

        return orderStatuses;
    }

    public OrderStatus getOrderStatusFromOrderId(int id) {
        OrderStatus orderStatus = new OrderStatus();

        for (OrderStatus temp : getOrderStatusList()) {
            if (temp.getId() == id) {
                orderStatus = temp;
            }
        }

        return orderStatus;
    }

    enum SQLOrderStatus {

        GET_ORDER_STATUSES("SELECT * FROM order_status");

        String QUERY;

        SQLOrderStatus(String QUERY) {
            this.QUERY = QUERY;
        }
    }
}
