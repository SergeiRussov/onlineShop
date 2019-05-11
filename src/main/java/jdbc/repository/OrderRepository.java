package jdbc.repository;

import model.Order;

import java.util.List;

public interface OrderRepository<Entity extends Order> {

    List<Entity> getOrdersFromCustomerId(int customerId);
}
