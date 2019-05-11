package jdbc.repository;

import model.Contact;

import java.util.List;

public interface ContactRepository<Entity extends Contact> {

    List<Entity> getContactsFromId(int id);
}
