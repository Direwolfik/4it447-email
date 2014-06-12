package javaee.mail;

import java.util.List;

public interface ContactsDAO {
		List<Contacts> getContacts();
		void addContact(String name, String email);
		Contact findContact(String name);
		void updateContact(Contact contact);
		void removeContacts(int[] contactIDs);
		List<Contact> getContactList();
}
