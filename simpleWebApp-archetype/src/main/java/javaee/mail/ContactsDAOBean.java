package javaee.mail;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Bean implementující rozhraní ContactsDAO umožnující práci s kontakty.
 * 
 * @author Jakub Kolář, Josef Novotný
 * @since 1.0
 * 
 */
@Stateless
@Local(ContactsDAO.class)
public class ContactsDAOBean implements ContactsDAO {
	@PersistenceContext(unitName = "mail")
	private EntityManager entityManager;

	@Override
	public List<Contacts> getContactsByOwner(String owner) {
		Query q = entityManager
				.createQuery("select c from Contacts c where c.owner=:owner");
		q.setParameter("owner", owner);
		return q.getResultList();
	}

	@Override
	public void addContact(String name, String email, String owner) {
		Contacts contact = new Contacts();
		contact.setName(name);
		contact.setEmail(email);
		contact.setOwner(owner);

		entityManager.persist(contact);
		entityManager.flush();
		entityManager.refresh(contact);

	}

	@Override
	public void removeContacts(int[] contactIDs, String owner) {
		String seq = createDelimitedSequence(contactIDs);
		Query query = entityManager
				.createQuery("delete from Contacts c where c.id in (" + seq
						+ ")");
		query.executeUpdate();

	}

	/**
	 * Přidá oddělovač (,) mezi jednotlivé prvky pole Integerů a převede je do
	 * formy řetězce znaků.
	 * 
	 * @param selectedIds
	 *            - pole Integerů, mezi které se přidá oddělovač
	 * @return Stringový řetězec Integerů oddělených oddělovačem
	 */
	private String createDelimitedSequence(int[] selectedIds) {
		StringBuilder sb = new StringBuilder();
		for (int j = 0; j < selectedIds.length; j++) {
			int selectedId = selectedIds[j];
			if (j > 0) {
				sb.append(',');
			}
			sb.append(selectedId);
		}
		return sb.toString();
	}

	@Override
	public List<Contacts> getContacts() {
		Query query = entityManager.createQuery("select c from Contacts c");
		System.out.println("kentus hovno mrdka" + query.toString());
		return query.getResultList();
	}

	@Override
	public void removeContact(String contactID) {
		int i = Integer.parseInt(contactID);
		Query query = entityManager
				.createQuery("delete from Contacts c where c.id=:id");
		query.setParameter("id", i);
		query.executeUpdate();

	}

	@Override
	public void updateContact(String name, String email, String owner, int id) {
		Contacts contact = new Contacts();
		contact.setId(id);
		contact.setName(name);
		contact.setEmail(email);
		contact.setOwner(owner);

		entityManager.merge(contact);
		entityManager.flush();

	}
}
