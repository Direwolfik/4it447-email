package javaee.mail;

import java.io.IOException;
import java.util.List;

import javax.mail.MessagingException;
import javax.ejb.EJB;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Jakub Kolář, Josef Novotný
 * @since 1.0
 * 
 */
@MultipartConfig
public class MailServlet extends HttpServlet {

	private static final String OWNER2 = "owner";

	private static final String EMAIL2 = "email";

	private static final String MAIL_FORM_JSP = "mailForm.jsp";

	private static final long serialVersionUID = 1L;

	@EJB
	private Sender sender;
	@EJB
	private ContactsDAO contactsDAO;
	@EJB
	private EmailDAO emailDAO;

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.GenericServlet#init(javax.servlet.ServletConfig)
	 */
	@Override
	public void init(ServletConfig servletConfig) throws ServletException {
		servletConfig.getServletContext().setAttribute("contactsDAO",
				contactsDAO);
		servletConfig.getServletContext().setAttribute("emailDAO", emailDAO);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest
	 * , javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		// Při HTTP metodě POST se očekává, že je v session
		// k dispozici e-mail bean.
		Email email = (Email) request.getSession()
				.getAttribute(EMAIL2);
		if (email == null) {
			createEmailBean(request, response);
		}

		try {
			// Přečteme se akci
			String action = request.getParameter("action");
			if ("send".equals(action)) {
				doSend(email, request, response);
			} else if ("new".equals(action)) {
				createEmailBean(request, response);
			} else if ("store".equals(action)) {
				doStore(request);
			} else if ("restore".equals(action)) {
				createEmailBean(request, response);
			} else if ("pridat".equals(action)) {
				doAddContact(request, response);
			} else if ("logout".equals(action)) {
				doLogout(request, response);
			} else if ("deleteContact".equals(action)) {
				doRemoveContact(request, response);
			} else if ("editContact".equals(action)) {
				doEditContact(request, response);
			}
		} catch (MessagingException e) {
			throw new ServletException(e);
		}
	}

	/**
	 * Akce, která provádí editaci kontaktu
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	private void doEditContact(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String name = request.getParameter("name");
		String email = request.getParameter(EMAIL2);
		String owner = request.getParameter(OWNER2);
		int id = Integer.parseInt(request.getParameter("id"));
		contactsDAO.updateContact(name, email, owner, id);
		refreshContacts(request);
		response.sendRedirect(MAIL_FORM_JSP);

	}

	/**
	 * Akce, která provádí smazání kontaktu
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	private void doRemoveContact(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String selectedContact = request.getParameter("selectedContact");
		contactsDAO.removeContact(selectedContact);

		refreshContacts(request);
		response.sendRedirect(MAIL_FORM_JSP);

	}

	/**
	 * Akce, která odesílá email pomocí třídy Sender.
	 * 
	 * @param emailBean
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws MessagingException
	 * @throws ServletException
	 */
	private void doSend(Email email, HttpServletRequest request,
			HttpServletResponse response) throws IOException,
			MessagingException, ServletException {

		// Přečteme si parametry z formuláře
		String to = request.getParameter("to");
		String copy = request.getParameter("copy");
		String hiddenCopy = request.getParameter("hiddenCopy");
		String subject = request.getParameter("subject");
		String message = request.getParameter("message");
		int time = Integer.parseInt(request.getParameter("time"));
		String owner = request.getParameter(OWNER2);

		// Nastavíme vlastnosti e-mail beanu
		email.setRecipient(to);
		email.setCopy(copy);
		email.setSubject(subject);
		email.setBody(message);
		email.setOwner(owner);
		email.setHiddenCopy(hiddenCopy);

		// odešleme email
		sender.sendToJMS(to, copy, hiddenCopy, subject, message, time, owner);
		doStore(request);
		// přesměrujeme na resumé
		response.sendRedirect("sent.jsp");
	}

	/**
	 * Akce, která provádí přidání kontaktu do databáze
	 * 
	 * @param emailBean
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	private void doAddContact(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String name = request.getParameter("name");
		String email = request.getParameter(EMAIL2);
		String owner = request.getParameter(OWNER2);

		contactsDAO.addContact(name, email, owner);

		refreshContacts(request);

		response.sendRedirect(MAIL_FORM_JSP);
	}

	/**
	 * Akce, která provádí přidání emailu do databáze
	 * 
	 * @param emailBean
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws MessagingException
	 * @throws ServletException
	 */
	private void doStore(HttpServletRequest request) throws IOException,
			MessagingException, ServletException {

		// Přečteme si parametry z formuláře
		String to = request.getParameter("to");
		String subject = request.getParameter("subject");
		String body = request.getParameter("message");
		String copy = request.getParameter("copy");
		String hiddenCopy = request.getParameter("hiddenCopy");
		String owner = request.getParameter(OWNER2);

		emailDAO.addEmail(to, copy, hiddenCopy, subject, body, owner);
		refreshEmails(request);
	}

	/**
	 * Akce, která provádí odhlášení uživatele
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void doLogout(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		request.getSession().setAttribute(EMAIL2, null);
		request.getSession().invalidate();
		response.sendRedirect("");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest
	 * , javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		// Lazy inicializace email beanu
		Email email = (Email) request.getSession()
				.getAttribute(EMAIL2);

		if (email == null) {
			// email bean není v session, vytvoříme jej tedy
			createEmailBean(request, response);
		} else {
			// Pokud je email bean v session, přesměrováváme
			// na emailForm.jsp.

			response.sendRedirect(MAIL_FORM_JSP);
		}
	}

	/**
	 * Akce, která vytvoří nový e-mail bean a vloží jej do session jako atribut.
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	private void createEmailBean(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String nick = request.getUserPrincipal().getName();

		Email email = new Email();
		email.setOwner(nick);
		request.getSession().setAttribute(EMAIL2, email);

		refreshContacts(request);
		refreshEmails(request);

		// přesměrujeme na formulář
		response.sendRedirect(MAIL_FORM_JSP);
	}

	/**
	 * Metoda, která na základě přihlášeného uživatele uloží seznam jeho
	 * kontaktů do session
	 * 
	 * @param request
	 */
	private void refreshContacts(HttpServletRequest request) {
		String owner = request.getUserPrincipal().getName();
		List<Contacts> contacts = contactsDAO.getContactsByOwner(owner);
		request.getSession().setAttribute("contacts", contacts);
	};

	/**
	 * Metoda, která na základě přihlášeného uživatele uloží seznam jeho
	 * odeslaných emailů do session
	 * 
	 * @param request
	 */
	private void refreshEmails(HttpServletRequest request) {
		String owner = request.getUserPrincipal().getName();
		List<Email> emails = emailDAO.getEmailsByOwner(owner);
		request.getSession().setAttribute("emails", emails);
	};

}
