package jdbc.repository;

import model.OrderStatus;

import java.util.List;

public interface OrderStatusRepository<Entity extends OrderStatus> {

    List<Entity> getOrderStatusList();
}
