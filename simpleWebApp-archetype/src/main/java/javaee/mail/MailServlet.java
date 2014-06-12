package javaee.mail;

import java.io.IOException;
//import java.io.PrintWriter;
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//import java.util.Date;

//import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
//import javax.mail.Transport;
//import javax.mail.internet.InternetAddress;
//import javax.mail.internet.MimeMessage;
import javax.annotation.Resource;
//import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;

public class MailServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Resource(name = "mail/myMailSession")
	private Session mailSession;

	// @Override
	// protected void doPost(HttpServletRequest request,
	// HttpServletResponse response) throws ServletException, IOException {
	//
	// response.setContentType("text/plain");
	// response.setCharacterEncoding("UTF-8");
	// PrintWriter output = response.getWriter();
	//
	// // sdělíme kontejneru, v jakém kódování
	// // očekáváme data od klienta
	// request.setCharacterEncoding("UTF-8");
	//
	// // metoda getParameter(paramName) vrací
	// // hodnotu parametru
	// String to = request.getParameter("to");
	// String subject = request.getParameter("subject");
	// String message = request.getParameter("message");
	//
	// // zavoláme pomocnou metodu - zatím nedělá nic
	// sendMail(to, subject, message);
	//
	// // Odpověď bude obsahovat resumé
	//
	// HttpSession session = request.getSession();
	// session.setAttribute("resume.to", to);
	// session.setAttribute("resume.subject", subject);
	// session.setAttribute("resume.message", message);
	//
	// response.sendRedirect(request.getRequestURI());
	//
	// output.flush();
	//
	// // // Odpověď bude obsahovat resumé
	// // output.println("Email odeslán");
	// // output.println("Komu: " + to);
	// // output.println("Předmět: " + subject);
	// // output.println("Zpráva: " + message);
	// // output.println("MailSession: " + mailSession);
	// //
	// // output.flush();
	// }

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
	            } else {
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
		String subject = request.getParameter("subject");
		String message = request.getParameter("message");

		// Nastavíme vlastnosti e-mail beanu
		emailBean.setTo(to);
		emailBean.setSubject(subject);
		emailBean.setBody(message);

		// odešleme email
		emailBean.send(mailSession);

		// přesměrujeme na resumé
		response.sendRedirect("sent.jsp");
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
		emailBean.store(mailSession);

		// přesměrujeme na resumé
		//response.sendRedirect("sent.jsp");
	}

//	private void sendMail(String to, String subject, String msg)
//			throws ServletException {
//		try {
//			// Vytvoříme objekt zprávy
//			Message message = new MimeMessage(mailSession);
//
//			// Zatím nenastavujeme From, použije se default
//			// z konfigurace serveru
//			// message.setFrom();
//			message.setRecipients(Message.RecipientType.TO,
//					InternetAddress.parse(to, false));
//
//			// Nastavíme předmět
//			message.setSubject(subject);
//
//			// Vložíme text zprávy
//			message.setText(msg);
//
//			// Nastavíme hlavičku indikující mailového klienta
//			message.setHeader("X-Mailer", "My Mailer");
//
//			// Nastavíme datum odeslání
//			Date timeStamp = new Date();
//			message.setSentDate(timeStamp);
//
//			// Odešleme zprávu
//			Transport.send(message);
//
//			getServletContext().log("Mail sent to " + to + ".");
//		} catch (MessagingException e) {
//			throw new ServletException(e);
//		}
//	}

	// @Override
	// protected void doGet(HttpServletRequest request,
	// HttpServletResponse response)
	// throws ServletException, IOException {
	//
	// response.setContentType("text/plain");
	// response.setCharacterEncoding("UTF-8");
	// PrintWriter output = response.getWriter();
	//
	// // Odpověď bude obsahovat resumé
	// HttpSession session = request.getSession();
	// RequestDispatcher rd = request.getRequestDispatcher("sent.jsp");
	// String timeStamp = new
	// SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(Calendar.getInstance().getTime());
	// request.setAttribute("to", session.getAttribute("resume.to"));
	// request.setAttribute("subject", session.getAttribute("resume.subject"));
	// request.setAttribute("message", session.getAttribute("resume.message"));
	// request.setAttribute("now", timeStamp);
	// rd.forward(request, response);
	// response.sendRedirect("sent.jsp");
	// output.println("Email odeslán");
	// output.println("Komu:" + session.getAttribute("resume.to"));
	// output.println("Předmět:" + session.getAttribute("resume.subject"));
	// output.println("Zpráva:" + session.getAttribute("resume.message"));
	//
	// output.flush();
	// }

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
