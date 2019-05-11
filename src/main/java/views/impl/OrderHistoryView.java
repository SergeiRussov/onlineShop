package views.impl;

import model.Customer;
import model.Order;
import views.Executable;

import java.util.List;

public class OrderHistoryView implements Executable {

    private Customer customer;

    public OrderHistoryView(Customer customer) {
        this.customer = customer;
    }

    @Override
    public void run() {
        List<Order> orders = customer.getOrders();

        for (Order order : orders) {
            System.out.println("ID заказа: " + order.getId() + ", дата заказа: " + order.getDate() +
                    ", общая сумма заказа: " + order.getTotalPrice() + ", статус заказа: " + order.getStatus().getName());
        }
    }
}
