package views.impl;

import model.Order;
import views.Executable;
import views.Menu;

import java.util.List;

public class OrderHistoryView implements Executable {

    @Override
    public void run() {
        List<Order> orders = Menu.getCustomer().getOrders();

        for (Order order : orders) {
            System.out.println("ID заказа: " + order.getId() + ", дата заказа: " + order.getDate() +
                    ", общая сумма заказа: " + order.getTotalPrice() + ", статус заказа: " + order.getStatus().getName());
        }
    }
}
