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

    @Override
    public boolean addOrder(Order order) {
        boolean result = false;

        try (PreparedStatement statement = connection.prepareStatement(SQLOrder.ADD_ORDER.QUERY)) {
            statement.setDate(1, Date.valueOf(order.getDate()));
            statement.setInt(2, order.getTotalPrice());
            statement.setInt(3, 1);
            statement.setInt(4, order.getCoupon().getId());

            statement.executeUpdate();

            List<Order> lastOrders = getOrders();
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

    private List<Order> getOrders() {
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
                "WHERE customer_id = ?)"),
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
