package model;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class Order {

    private int id;
    private LocalDate date;
    private int totalPrice;
    private OrderStatus status;
    private Coupon coupon;
    private List<Good> goods;
}
