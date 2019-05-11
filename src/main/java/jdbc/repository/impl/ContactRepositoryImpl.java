package jdbc.repository.impl;

import jdbc.repository.ContactRepository;
import lombok.extern.slf4j.Slf4j;
import model.Contact;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ContactRepositoryImpl implements ContactRepository<Contact> {

    private final Connection connection;

    public ContactRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Contact> getContactsFromId(int id) {
        final List<Contact> result = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(SQLContact.GET_CONTACTS_FROM_ID.QUERY)) {
            statement.setInt(1, id);
            final ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                final Contact resultContact = new Contact();
                resultContact.setId(rs.getInt("id"));
                resultContact.setAddress(rs.getString("address"));
                resultContact.setPhone(rs.getLong("phone"));
                resultContact.setEmail(rs.getString("email"));

                result.add(resultContact);
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }

        return result;
    }

    enum SQLContact {

        GET_CONTACTS_FROM_ID("SELECT * FROM contacts WHERE id = ?");

        String QUERY;

        SQLContact(String QUERY) {
            this.QUERY = QUERY;
        }
    }
}
