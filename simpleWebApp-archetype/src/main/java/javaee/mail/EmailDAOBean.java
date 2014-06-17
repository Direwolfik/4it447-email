package javaee.mail;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;





@Stateless
@Local(EmailDAO.class)
public class EmailDAOBean implements EmailDAO {
	@PersistenceContext(unitName = "mail")
    private EntityManager entityManager;

	@Override
	public List<EmailBean> getEmailsByOwner(String owner) {
		Query query = entityManager.createQuery("select e from EmailBean e");
        return query.getResultList();
	}

	@Override
	public void addEmail(String to, String copy, String subject, String body, String owner) {
		EmailBean mail = new EmailBean();
		mail.setRecipient(to);
		mail.setCopy(copy);
		mail.setSubject(subject);
		mail.setBody(body);
		mail.setOwner(owner);
		System.out.println("Ukládám email");
        entityManager.persist(mail);
        entityManager.flush();
	}
	
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
	public List<EmailBean> getEmails() {
		Query query = entityManager.createQuery("select e from EmailBean e where owner like "+);
		System.out.println("kentus hovno mrdka "+query.toString());
        return query.getResultList();
	}
}






	


