package jdbc.repository.impl;

import jdbc.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import model.Good;
import model.Order;
import views.Menu;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class OrderRepositoryImpl implements OrderRepository<Order> {

    private final Connection connection;

    public OrderRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Order> getOrdersFromCustomerId(int customerId) {
        final List<Order> result = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(SQLOrder.GET_ORDERS_FROM_CUSTOMER_ID.QUERY)) {
            statement.setInt(1, customerId);
            final ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                final Order resultOrder = new Order();

                int orderId = rs.getInt("id");
                resultOrder.setId(orderId);
                resultOrder.setDate(LocalDate.parse(rs.getString("order_date")));
                resultOrder.setTotalPrice(rs.getInt("total_price"));

                int statusId = rs.getInt("status_id");
                resultOrder.setStatus(new OrderStatusRepositoryImpl(connection).getOrderStatusFromOrderId(statusId));

                int couponId = rs.getInt("coupon_id");
                resultOrder.setCoupon(new CouponRepositoryImpl(connection).getCouponFromId(couponId));

                resultOrder.setGoods(new GoodRepositoryImpl(connection).getGoodsFromOrderId(orderId));

                result.add(resultOrder);
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }

        return result;
    }

    public List<Order> getOrders() {
        final List<Order> result = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(SQLOrder.GET_ORDERS.QUERY)) {
            final ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                final Order resultOrder = new Order();

                int orderId = rs.getInt("id");
                resultOrder.setId(orderId);
                resultOrder.setDate(LocalDate.parse(rs.getString("order_date")));
                resultOrder.setTotalPrice(rs.getInt("total_price"));

                int statusId = rs.getInt("status_id");
                resultOrder.setStatus(new OrderStatusRepositoryImpl(connection).getOrderStatusFromOrderId(statusId));

                int couponId = rs.getInt("coupon_id");
                resultOrder.setCoupon(new CouponRepositoryImpl(connection).getCouponFromId(couponId));

                resultOrder.setGoods(new GoodRepositoryImpl(connection).getGoodsFromOrderId(orderId));

                result.add(resultOrder);
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }

        return result;
    }

    enum SQLOrder {

        GET_ORDERS("SELECT * FROM orders"),
        GET_ORDERS_FROM_CUSTOMER_ID("SELECT * FROM orders WHERE id IN (SELECT order_id FROM customers_orders " +
                "WHERE customer_id = ?)");

        String QUERY;

        SQLOrder(String QUERY) {
            this.QUERY = QUERY;
        }
    }
}
