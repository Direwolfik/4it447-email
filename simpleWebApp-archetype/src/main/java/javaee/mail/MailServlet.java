package javaee.mail;

import java.io.IOException;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@MultipartConfig
public class MailServlet extends HttpServlet {

	/**
	 * 
	 */
	@EJB
    private Sender sender;
	
	private static final long serialVersionUID = 1L;
	

	  @EJB
	   private ContactsDAO contactsDAO;
	  @EJB
	   private EmailDAO emailDAO;
	  @Resource(name = "mail/myMailSession")
		private Session mailSession;

	    @Override
	    public void init(ServletConfig servletConfig) throws ServletException {
	        // Zpřístupníme albumDAO JSP stránkám přes kontext aplikace
	        servletConfig.getServletContext().setAttribute("contactsDAO", contactsDAO);
	        servletConfig.getServletContext().setAttribute("emailDAO", emailDAO);
	    }

	 @Override
	    protected void doPost(HttpServletRequest request,
	                          HttpServletResponse response)
	            throws ServletException, IOException {
		 	System.out.println("doPost");
		 	
	        // Při HTTP metodě POST se očekává, že je v session
	        // k dispozici e-mail bean.
	        EmailBean emailBean = (EmailBean)
	                request.getSession().getAttribute("emailBean");
	        if (emailBean == null) {
	        	createEmailBean(request, response);
	        }

	        try {
	            // Přečteme se akci
	            String action = request.getParameter("action");
	            if ("send".equals(action)) {
	                doSend(emailBean, request, response);
	            } else if ("new".equals(action)) {
	                createEmailBean(request, response);
	            } else if ("store".equals(action)) {
	                doStore(emailBean, request, response);
	            } else if ("restore".equals(action)) {
	                createEmailBean(request, response);
	            } else if("pridat".equals(action)) {
	            	
	            	doAddContact(emailBean,request,response);
	            } else if ("logout".equals(action)) {
	                doLogout(request, response);
	            } else if ("deleteContact".equals(action)) {
	                doRemoveContact(request, response);
	            } else if ("editContact".equals(action)){
	            	doEditContact(request, response);
	            }

	        } catch (MessagingException e) {
	            throw new ServletException(e);
	        }
	    }


	private void doEditContact(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String name = request.getParameter("name");
		String email = request.getParameter("email");
		String owner = request.getParameter("owner");
		int id = Integer.parseInt(request.getParameter("id"));
		contactsDAO.updateContact(name,email, owner,id);
		refreshContacts(request);
		response.sendRedirect("mailForm.jsp");
		
	}

	private void doRemoveContact(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String selectedContact = request.getParameter("selectedContact");
		System.out.println("attempting to remove "+selectedContact);
        contactsDAO.removeContact(selectedContact);
        
        refreshContacts(request);
        response.sendRedirect("mailForm.jsp");
		
	}

	/**
	 * Akce, která odesílá email voláním business metody na email beanu.
	 */
	private void doSend(EmailBean emailBean, HttpServletRequest request,
			HttpServletResponse response) throws IOException,
			MessagingException, ServletException {

		// Přečteme si parametry z formuláře
		String to = request.getParameter("to");
		String copy = request.getParameter("copy");
		String hiddenCopy = request.getParameter("hiddenCopy");
		String subject = request.getParameter("subject");
		String message = request.getParameter("message");
		int time = Integer.parseInt(request.getParameter("time"));
		String owner = request.getParameter("owner");

		// Nastavíme vlastnosti e-mail beanu
		emailBean.setRecipient(to);
		emailBean.setCopy(copy);
		emailBean.setSubject(subject);
		emailBean.setBody(message);
		emailBean.setOwner(owner);
		emailBean.setHiddenCopy(hiddenCopy);

		// odešleme email
		//emailBean.send(mailSession);
		sender.sendToJMS(to,copy,hiddenCopy,subject,message,time,owner);
		doStore(emailBean, request, response);
		// přesměrujeme na resumé
		response.sendRedirect("sent.jsp");
	}
	private void doAddContact(EmailBean emailBean, HttpServletRequest request,
			HttpServletResponse response) throws IOException{
		String name = request.getParameter("name");
		String email = request.getParameter("email");
		String owner = request.getParameter("owner");
		
		
		contactsDAO.addContact(name,email, owner);
		
		refreshContacts(request);
		
		
		response.sendRedirect("mailForm.jsp");
	}
	
	private void doStore(EmailBean emailBean, HttpServletRequest request,
			HttpServletResponse response) throws IOException,
			MessagingException, ServletException {

		// Přečteme si parametry z formuláře
		String to = request.getParameter("to");
		String subject = request.getParameter("subject");
		String body = request.getParameter("message");
		String copy = request.getParameter("copy");
		String owner = request.getParameter("owner");

		emailDAO.addEmail(to, copy, subject, body, owner);
		refreshEmails(request);
	}

	 private void doLogout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 request.getSession().setAttribute("emailBean", null);
		 System.out.println("bla "+request.getSession().getAttribute("emailBean"));
		 request.getSession().invalidate();
         response.sendRedirect("");
}


	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		System.out.println("doGet Servlet");
		 
		// Lazy inicializace email beanu
		EmailBean emailBean = (EmailBean) request.getSession().getAttribute(
				"emailBean");
		
		if (emailBean == null) {
			// email bean není v session, vytvoříme jej tedy
			createEmailBean(request, response);
		} else {
			// Pokud je email bean v session, přesměrováváme
			// na emailForm.jsp.
			
			response.sendRedirect("mailForm.jsp");
		}
	}

	/**
	 * Akce, která vytvoří nový e-mail bean a vloží jej do session jako atribut.
	 */
	private void createEmailBean(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		System.out.println("create Email Bean");
		String nick = request.getUserPrincipal().getName();
		
		EmailBean emailBean = new EmailBean();
		emailBean.setOwner(nick);
		request.getSession().setAttribute("emailBean", emailBean);
		
		System.out.println("createEmailBean nick: "+nick);
		refreshContacts(request);
		refreshEmails(request);
		// Atribut 'user' v dotazu nastavuje
		// autentizační filtr (FrontControllerFilter)
//		String user = (String) request.getAttribute("user");
//		emailBean.setUser(user);

		// přesměrujeme na formulář
		response.sendRedirect("mailForm.jsp");
	}
	
	private void refreshContacts(HttpServletRequest request){
		String owner = request.getUserPrincipal().getName();
		List<Contacts> contacts = contactsDAO.getContactsByOwner(owner);
		request.getSession().setAttribute("contacts", contacts);
	};
	
	private void refreshEmails(HttpServletRequest request){
		String owner = request.getUserPrincipal().getName();
		List<EmailBean> emails = emailDAO.getEmailsByOwner(owner);
		request.getSession().setAttribute("emails", emails);
	};
	

}
