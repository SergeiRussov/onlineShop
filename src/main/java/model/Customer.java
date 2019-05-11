package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Customer {

    private int id;
    private String name;
    private List<Contact> contacts;
    private Role role;
    private List<Order> orders;
}
