package views;

import dao.impl.CustomerDAO;
import jdbc.JDBCUtils;
import lombok.extern.slf4j.Slf4j;
import model.Customer;
import views.impl.CatalogMenuItem;
import views.impl.CloseShop;
import views.impl.OrderHistoryView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class Menu {

    private Customer customer;
    private Connection connection;
    private JDBCUtils driver = new JDBCUtils();

    private Map<Integer, Executable> menuItems = new HashMap<>();

    {
        menuItems.put(0, new CloseShop());
    }

    private void showMenu() {
        System.out.println("1. Каталог");
        System.out.println("2. История покупок");
        System.out.println("3. Корзина");
        System.out.println("0. Выход");
    }

    public void getCommand() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            customerLogin(reader);
            menuItems.put(2, new OrderHistoryView(customer));
            menuItems.put(1, new CatalogMenuItem(reader));

            showMenu();

            while (CloseShop.isIsWork()) {
                System.out.print("\nВведите номер команды: ");
                int itemNumber = Integer.parseInt(reader.readLine());
                if (menuItems.containsKey(itemNumber)) {
                    menuItems.get(itemNumber).run();
                }
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private Customer customerLogin(BufferedReader reader) {
        System.out.println("Введите полное имя пользователя: ");
        connection = driver.createConnection();

        try {
            String customerName = reader.readLine();
            customer = new CustomerDAO(connection).read(customerName);
            System.out.println("Успешный вход\n");
        } catch (IOException e) {
            System.out.println("Пользователь не найден");
            log.error(e.getMessage());
        }

        return customer;
    }
}
