package jdbc.repository.impl;

import jdbc.repository.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import model.Role;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class RoleRepositoryImpl implements RoleRepository<Role> {

    private final Connection connection;

    public RoleRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Role> getRoles() {
        final List<Role> roles = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(SQLRole.GET_ROLES.QUERY)) {
            final ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                final Role role = new Role();
                role.setId(rs.getInt("id"));
                role.setName(rs.getString("name"));

                roles.add(role);
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }

        return roles;
    }

    public Role getRoleFromCustomerId(int id) {
        Role role = new Role();

        for (Role temp : getRoles()) {
            if (temp.getId() == id) {
                role = temp;
            }
        }

        return role;
    }

    enum SQLRole {

        GET_ROLES("SELECT * FROM roles");

        String QUERY;

        SQLRole(String QUERY) {
            this.QUERY = QUERY;
        }
    }
}
