package views.impl;

import jdbc.JDBCUtils;
import jdbc.repository.impl.CouponRepositoryImpl;
import lombok.extern.slf4j.Slf4j;
import model.Coupon;
import model.Good;
import model.Order;
import service.impl.OrderServiceImpl;
import views.Executable;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;

@Slf4j
public class OrderItem implements Executable {

    private Order order;
    private BufferedReader reader;
    private JDBCUtils driver = new JDBCUtils();

    public OrderItem(BufferedReader reader) {
        this.reader = reader;
    }

    @Override
    public void run() {
        showOrder();
    }

    private void showOrder() {
        if (CategoryMenuItem.getOrder() == null) {
            System.out.println("Корзина пуста. Товары можно добавлять из раздела Каталог.");
        } else {
            order = CategoryMenuItem.getOrder();
            int totalPrice = 0;

            for (Good good : order.getGoods()) {
                totalPrice += good.getPrice();
            }

            order.setTotalPrice(totalPrice);

            String inf = "Дата заказа: " + order.getDate() + ", сумма заказа: " +
                    (totalPrice + ".\nСписок товаров: ");
            System.out.println(inf);
            System.out.println(getGoodsList());

            validePurchase(reader);
        }
    }

    private String getGoodsList() {
        StringBuilder temp = new StringBuilder();

        for (Good good : order.getGoods()) {
            temp.append("ID: " + good.getId() + ", наименование товара: " + good.getName() + ", цена товара: " +
                    good.getPrice() +"\n");
        }

        return temp.toString();
    }

    private void validePurchase(BufferedReader reader) {
        System.out.println("Провести заказ?\n1. Да\n2. Нет");
        try {
            int answer = Integer.parseInt(reader.readLine());
            if (answer == 1) {
                addCoupon(reader);
                Connection connection = driver.createConnection();
                new OrderServiceImpl(connection).addOrder(order);
                driver.closeConnection(connection);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void addCoupon(BufferedReader reader) {
        System.out.println("Хотите добавить купон на скидку?\n1. Да\n2. Нет");
        try {
            int answer = Integer.parseInt(reader.readLine());
            if (answer == 1) {
                Connection connection = driver.createConnection();
                System.out.println("Введите ID купона: ");
                int id = Integer.parseInt(reader.readLine());
                order.setCoupon(new CouponRepositoryImpl(connection).getCouponFromId(id));
                driver.closeConnection(connection);

                int newTotalPrice = order.getTotalPrice() - (order.getTotalPrice() / 100 * order.getCoupon().getDiscount());
                order.setTotalPrice(newTotalPrice);
            } else {
                Coupon coupon = new Coupon();
                coupon.setId(404);
                coupon.setDiscount(0);

                order.setCoupon(coupon);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }

        System.out.println("Текущая сумма заказа: " + order.getTotalPrice());
    }
}
