package jdbc.repository.impl;

import jdbc.repository.CouponRepository;
import lombok.extern.slf4j.Slf4j;
import model.Coupon;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class CouponRepositoryImpl implements CouponRepository<Coupon> {

    private final Connection connection;

    public CouponRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Coupon> getCoupons() {
        final List<Coupon> coupons = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(SQLCoupon.GET_COUPONS.QUERY)) {
            final ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                final Coupon coupon = new Coupon();
                coupon.setId(rs.getInt("id"));
                coupon.setDiscount(rs.getInt("discount"));

                coupons.add(coupon);
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }

        return coupons;
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

    public Coupon getCouponFromId(int id) {
        Coupon coupon = new Coupon();
        coupon.setId(0);
        coupon.setDiscount(0);

        for (Coupon temp : getCoupons()) {
            if (temp.getId() == id) {
                coupon = temp;
            }
        }

        return coupon;
    }

    enum SQLCoupon {

        GET_COUPONS("SELECT * FROM coupons"),
        ADD_COUPON("INSERT INTO coupons (discount) VALUES (?)");

        String QUERY;

        SQLCoupon(String QUERY) {
            this.QUERY = QUERY;
        }
    }
}
