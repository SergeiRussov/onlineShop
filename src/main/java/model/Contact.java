package model;

import lombok.Data;

@Data
public class Contact {

    private int id;
    private String address;
    private long phone;
    private String email;
}
