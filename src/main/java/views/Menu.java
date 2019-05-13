package views;

import dao.impl.CustomerDAO;
import jdbc.JDBCUtils;
import lombok.extern.slf4j.Slf4j;
import model.Customer;
import views.impl.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class Menu {

    private static Customer customer;
    private Connection connection;
    private JDBCUtils driver = new JDBCUtils();

    private Map<Integer, Executable> menuItems = new HashMap<>();

    {
        menuItems.put(0, new CloseShop());
    }

    public static Customer getCustomer() {
        return customer;
    }

    public void getCommand() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            customerLogin(reader);

            menuItems.put(1, new Ref());
            menuItems.put(2, new CatalogMenuItem(reader));
            menuItems.put(3, new OrderHistoryView());
            menuItems.put(4, new OrderItem(reader));
            menuItems.put(5, new CouponMenuItem(reader));
            menuItems.put(6, new CreateGoodsMenuItem(reader));

            while (CloseShop.isIsWork()) {
                menuItems.get(1).run();
                System.out.print("\nВведите номер команды: ");
                int itemNumber = Integer.parseInt(reader.readLine());
                if (menuItems.containsKey(itemNumber)) {
                    menuItems.get(itemNumber).run();
                } else {
                    System.out.println("Такой команды не существует. Повторите попытку.");
                }
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private Customer customerLogin(BufferedReader reader) {
        connection = driver.createConnection();

        while (true) {
            try {
                System.out.print("Введите полное имя пользователя: ");
                String customerName = reader.readLine();
                customer = new CustomerDAO(connection).read(customerName);

                if (customer.getId() == -1) {
                    throw new IOException();
                }

                System.out.println("\nУспешный вход");
                break;
            } catch (IOException e) {
                System.out.println("\nПользователь не найден");
                log.error(e.getMessage());
            }
        }

        return customer;
    }
}
