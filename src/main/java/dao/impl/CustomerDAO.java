package dao.impl;

import dao.DAO;
import jdbc.repository.impl.ContactRepositoryImpl;
import jdbc.repository.impl.OrderRepositoryImpl;
import jdbc.repository.impl.RoleRepositoryImpl;
import lombok.extern.slf4j.Slf4j;
import model.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
public class CustomerDAO implements DAO<Customer, String> {

    private final Connection connection;

    public CustomerDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean create(Customer model) {
        return false;
    }

    @Override
    public Customer read(String name) {
        final Customer result = new Customer();
        result.setId(-1);

        try (PreparedStatement statement = connection.prepareStatement(SQLCustomer.GET.QUERY)) {
            statement.setString(1, name);
            final ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                int customerId = rs.getInt("id");
                result.setId(customerId);
                result.setName(rs.getString("name"));

                int roleId = rs.getInt("role_id");
                result.setRole(new RoleRepositoryImpl(connection).getRoleFromCustomerId(roleId));

                int contactsId = rs.getInt("contact_id");
                result.setContacts(new ContactRepositoryImpl(connection).getContactsFromId(contactsId));

                result.setOrders(new OrderRepositoryImpl(connection).getOrdersFromCustomerId(customerId));
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }

        return result;
    }

    @Override
    public boolean update(Customer model) {
        return false;
    }

    @Override
    public boolean delete(String s) {
        return false;
    }

    enum SQLCustomer {

        INSERT(""),
        GET("SELECT * FROM customers WHERE name = ?"),
        UPDATE(""),
        DELETE("");

        String QUERY;

        SQLCustomer(String QUERY) {
            this.QUERY = QUERY;
        }
    }
}
