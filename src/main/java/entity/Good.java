package entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Good {

    private int id;
    private String name;
    private int price;
}
