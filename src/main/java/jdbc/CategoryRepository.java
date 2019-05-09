package jdbc;

import model.Category;

import java.util.List;

public interface CategoryRepository<Entity extends Category> {

    List<Entity> getCategories();
}
