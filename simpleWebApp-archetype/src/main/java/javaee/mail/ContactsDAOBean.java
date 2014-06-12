package javaee.mail;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

public class ContactsDAOBean implements ContactsDAO {
	@PersistenceContext(unitName = "mail")
    private EntityManager entityManager;
	
	@Override
	public List<Contacts> getContacts() {
		Query query = entityManager.createQuery("select c from Contacts c");
        return query.getResultList();
		
	}

	@Override
	public void addContact(String name, String email) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Contact findContact(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateContact(Contact contact) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeContacts(int[] contactIDs) {
		// TODO Auto-generated method stub
		
	}
	

}
