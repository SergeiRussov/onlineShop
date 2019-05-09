package views;

import lombok.extern.slf4j.Slf4j;
import views.impl.CatalogMenuItem;
import views.impl.CloseShop;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class Menu {

    private Map<Integer, Executable> menuItems = new HashMap<>();

    {
        menuItems.put(0, new CloseShop());
        menuItems.put(1, new CatalogMenuItem());
    }

    private void showMenu() {
        System.out.println("1. Каталог");
        System.out.println("0. Выход");
    }

    public void getCommand() {
        showMenu();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
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


}
