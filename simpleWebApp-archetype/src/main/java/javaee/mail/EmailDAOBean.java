package javaee.mail;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * @author Jakub Kolář
 *
 */
@Stateless
@Local(EmailDAO.class)
public class EmailDAOBean implements EmailDAO {
	@PersistenceContext(unitName = "mail")
	private EntityManager entityManager;

	/* (non-Javadoc)
	 * @see javaee.mail.EmailDAO#getEmailsByOwner(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Email> getEmailsByOwner(String owner) {
		Query query = entityManager
				.createQuery("select e from EmailBean e where e.owner=:owner");
		query.setParameter("owner", owner);
		return query.getResultList();
	}

	/* (non-Javadoc)
	 * @see javaee.mail.EmailDAO#addEmail(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void addEmail(String to, String copy, String hiddenCopy,
			String subject, String body, String owner) {
		Email mail = new Email();
		mail.setRecipient(to);
		mail.setCopy(copy);
		mail.setHiddenCopy(hiddenCopy);
		mail.setSubject(subject);
		mail.setBody(body);
		mail.setOwner(owner);
		System.out.println("Ukládám email");
		entityManager.persist(mail);
		entityManager.flush();
	}

	/* (non-Javadoc)
	 * @see javaee.mail.EmailDAO#getEmails()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Email> getEmails() {
		Query query = entityManager.createQuery("select e from EmailBean e");

		return query.getResultList();
	}
}
