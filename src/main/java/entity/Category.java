package entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Data
public class Category {

    private int id;
    private String name;
    private List<Good> goods;

    public Category(String name, List<Good> goods) {
        this.name = name;
        this.goods = goods;
    }

    //copy constructor
    public Category(Category other) {
        this.name = other.name;
        this.goods = new ArrayList<>(other.goods);
    }
}
