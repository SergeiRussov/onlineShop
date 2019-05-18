package service;

import model.Order;

public interface OrderService<Entity extends Order> {

    boolean addOrder(Entity order);
}
