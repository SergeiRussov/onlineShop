package jdbc.repository;

import model.Coupon;

import java.util.List;

public interface CouponRepository<Entity extends Coupon> {

    List<Entity> getCoupons();
    boolean addCoupon(int discount);
}
