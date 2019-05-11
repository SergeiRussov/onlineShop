package jdbc.repository;

import model.Role;

import java.util.List;

public interface RoleRepository<Entity extends Role> {

    List<Entity> getRoles();
}
