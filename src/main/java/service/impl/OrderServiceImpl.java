package service.impl;

import jdbc.repository.impl.OrderRepositoryImpl;
import lombok.extern.slf4j.Slf4j;
import model.Good;
import model.Order;
import service.OrderService;
import views.Menu;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Slf4j
public class OrderServiceImpl implements OrderService<Order> {

    private final Connection connection;

    public OrderServiceImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean addOrder(Order order) {
        boolean result = false;

        try (PreparedStatement statement = connection.prepareStatement(SQLOrder.ADD_ORDER.QUERY)) {
            statement.setDate(1, Date.valueOf(order.getDate()));
            statement.setInt(2, order.getTotalPrice());
            statement.setInt(3, 1);
            statement.setInt(4, order.getCoupon().getId());

            statement.executeUpdate();

            List<Order> lastOrders = new OrderRepositoryImpl(connection).getOrders();
            Order lastOrder = lastOrders.get(lastOrders.size() - 1);

            for (Good good : order.getGoods()) {
                addGoodsOrderDep(lastOrder.getId(), good.getId());
            }

            addCustOrderDep(Menu.getCustomer().getId(), lastOrder.getId());

            result = true;
        } catch (SQLException e) {
            log.error(e.getMessage());
        }

        return result;
    }

    private boolean addCustOrderDep(int customerId, int orderId) {
        boolean result = false;

        try (PreparedStatement statement = connection.prepareStatement(SQLOrder.ADD_CUSTOMER_ORDERS_DEP.QUERY)) {
            statement.setInt(1, customerId);
            statement.setInt(2, orderId);

            statement.executeUpdate();

            result = true;
        } catch (SQLException e) {
            log.error(e.getMessage());
        }

        return result;
    }

    private boolean addGoodsOrderDep(int orderId, int goodId) {
        boolean result = false;

        try (PreparedStatement statement = connection.prepareStatement(SQLOrder.ADD_ORDER_GOOD_DEP.QUERY)) {
            statement.setInt(1, orderId);
            statement.setInt(2, goodId);

            statement.executeUpdate();

            result = true;
        } catch (SQLException e) {
            log.error(e.getMessage());
        }

        return result;
    }

    enum SQLOrder {

        ADD_ORDER("INSERT INTO orders (order_date, total_price, status_id, coupon_id) " +
                "VALUES (?, ?, ?, ?)"),
        ADD_CUSTOMER_ORDERS_DEP("INSERT INTO customers_orders VALUES (?, ?)"),
        ADD_ORDER_GOOD_DEP("INSERT INTO goods_orders VALUES (?,?)");

        String QUERY;

        SQLOrder(String QUERY) {
            this.QUERY = QUERY;
        }
    }
}
