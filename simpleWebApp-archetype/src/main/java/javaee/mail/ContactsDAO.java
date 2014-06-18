package javaee.mail;

import java.util.List;

public interface ContactsDAO {
		List<Contacts> getContactsByOwner(String owner);
		void addContact(String name, String email, String owner);
		void removeContacts(int[] contactIDs, String owner);
		List<Contacts> getContacts();
		void removeContact(String contactID);
		void updateContact(String name, String email, String owner, int id);
	
}
