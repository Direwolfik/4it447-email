package javaee.mail;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;
import javax.naming.NamingException;

import junit.framework.TestCase;


public class ContactsTest extends TestCase {
	@EJB
	private ContactsDAOBean contactsDAO;
	
	MailServlet servlet = new MailServlet();
    
	public ContactsTest(String name) throws NamingException {
		super(name);
		
		
		
	}

	protected void setUp() throws Exception {
		super.setUp();
		
		
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		
     
		
	}
	
	public void testApp() throws NamingException {

        
        assertNotNull(contactsDAO);
        contactsDAO.addContact("pepa", "jn91@seznam.cz", "Pepa");
        List<Contacts> contact = contactsDAO.getContactsByOwner("Pepa");
        assertNotNull(contact);
        
    }

}
