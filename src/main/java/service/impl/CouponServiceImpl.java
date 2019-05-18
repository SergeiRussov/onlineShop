package service.impl;

import lombok.extern.slf4j.Slf4j;
import service.CouponService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Slf4j
public class CouponServiceImpl implements CouponService {

    private final Connection connection;

    public CouponServiceImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean addCoupon(int discount) {
        boolean result = false;

        try (PreparedStatement statement = connection.prepareStatement(SQLCoupon.ADD_COUPON.QUERY)) {
            statement.setInt(1, discount);

            statement.executeUpdate();

            result = true;
        } catch (SQLException e) {
            log.error(e.getMessage());
        }

        return result;
    }

    enum SQLCoupon {

        ADD_COUPON("INSERT INTO coupons (discount) VALUES (?)");

        String QUERY;

        SQLCoupon(String QUERY) {
            this.QUERY = QUERY;
        }
    }
}
