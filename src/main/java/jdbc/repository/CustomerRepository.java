package jdbc.repository;

import model.Customer;

import java.util.List;

public interface CustomerRepository<Entity extends Customer> {

    List<Entity> getCustomers();
}
