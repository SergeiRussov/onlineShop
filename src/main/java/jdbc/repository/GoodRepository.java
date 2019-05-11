package jdbc.repository;

import model.Good;

import java.util.List;

public interface GoodRepository<Entity extends Good> {

    List<Entity> getGoodsFromCategory(int id);
}
