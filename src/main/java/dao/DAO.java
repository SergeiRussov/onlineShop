package dao;

import java.sql.Connection;

public interface DAO<Entity, Key> {

    boolean create(Entity model);
    Entity read(Key key);
    boolean update(Entity model);
    boolean delete(Key key);
}
