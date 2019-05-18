package jdbc.repository.impl;

import jdbc.repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import model.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class CustomerRepositryImpl implements CustomerRepository<Customer> {

    private final Connection connection;

    public CustomerRepositryImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Customer> getCustomers() {
        final List<Customer> result = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(SQLCustomer.GET_CUSTOMERS.QUERY)) {
            final ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                final Customer resultCustomer = new Customer();

                int customerId = rs.getInt("id");
                resultCustomer.setId(customerId);

                resultCustomer.setName(rs.getString("name"));

                int contactId = rs.getInt("contact_id");
                resultCustomer.setContacts(new ContactRepositoryImpl(connection).getContactsFromId(contactId));

                resultCustomer.setRole(new RoleRepositoryImpl(connection).getRoleFromCustomerId(customerId));

                resultCustomer.setOrders(new OrderRepositoryImpl(connection).getOrdersFromCustomerId(customerId));

                result.add(resultCustomer);
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }

        return result;
    }

    enum SQLCustomer {

        GET_CUSTOMERS("SELECT * FROM CUSTOMERS");

        String QUERY;

        SQLCustomer(String QUERY) {
            this.QUERY = QUERY;
        }
    }
}
