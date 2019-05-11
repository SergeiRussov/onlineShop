package jdbc.repository;

import model.Category;

import java.util.List;

public interface CategoryRepository<Entity extends Category> {

    List<Entity> getCategories();
}
