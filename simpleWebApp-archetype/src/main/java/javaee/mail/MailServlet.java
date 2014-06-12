package javaee.mail;

import java.io.IOException;

import javax.mail.MessagingException;
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
	  

	    @Override
	    public void init(ServletConfig servletConfig) throws ServletException {
	        // Zpřístupníme albumDAO JSP stránkám přes kontext aplikace
	        servletConfig.getServletContext().setAttribute("contactsDAO", contactsDAO);
	    }

	 @Override
	    protected void doPost(HttpServletRequest request,
	                          HttpServletResponse response)
	            throws ServletException, IOException {
		 
	        // Při HTTP metodě POST se očekává, že je v session
	        // k dispozici e-mail bean.
	        EmailBean emailBean = (EmailBean)
	                request.getSession().getAttribute("emailBean");
	        if (emailBean == null) {
	            throw new ServletException("No email session");
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
	            }else {
	                throw new ServletException("No action specified");
	            }

	        } catch (MessagingException e) {
	            throw new ServletException(e);
	        }
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
		String subject = request.getParameter("subject");
		String message = request.getParameter("message");

		// Nastavíme vlastnosti e-mail beanu
		emailBean.setTo(to);
		emailBean.setCopy(copy);
		emailBean.setSubject(subject);
		emailBean.setBody(message);

		// odešleme email
		//emailBean.send(mailSession);
		sender.sendToJMS(to,copy,subject,message);

		// přesměrujeme na resumé
		response.sendRedirect("sent.jsp");
	}
	private void doAddContact(EmailBean emailBean, HttpServletRequest request,
			HttpServletResponse response) throws IOException{
		String name = request.getParameter("name");
		String email = request.getParameter("email");
		String owner = request.getParameter("owner");
		
		contactsDAO.addContact(name,email, owner);
		response.sendRedirect("mailForm.jsp");
	}
	
	private void doStore(EmailBean emailBean, HttpServletRequest request,
			HttpServletResponse response) throws IOException,
			MessagingException, ServletException {

		// Přečteme si parametry z formuláře
		String to = request.getParameter("to");
		String subject = request.getParameter("subject");
		String message = request.getParameter("message");

		// Nastavíme vlastnosti e-mail beanu
		emailBean.setTo(to);
		emailBean.setSubject(subject);
		emailBean.setBody(message);

		// odešleme email
		//emailBean.store(mailSession);

		// přesměrujeme na resumé
		//response.sendRedirect("sent.jsp");
	}



	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

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

		EmailBean emailBean = new EmailBean();
		request.getSession().setAttribute("emailBean", emailBean);

		// Atribut 'user' v dotazu nastavuje
		// autentizační filtr (FrontControllerFilter)
//		String user = (String) request.getAttribute("user");
//		emailBean.setUser(user);

		// přesměrujeme na formulář
		response.sendRedirect("mailForm.jsp");
	}
	
	

}
