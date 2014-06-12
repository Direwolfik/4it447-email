package javaee.mail;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class MailServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Resource(name = "mail/myMailSession")
	private Session mailSession;

	@Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {
		
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        PrintWriter output = response.getWriter();

        // sdělíme kontejneru, v jakém kódování
        // očekáváme data od klienta
        request.setCharacterEncoding("UTF-8");

        // metoda getParameter(paramName) vrací
        // hodnotu parametru
        String to = request.getParameter("to");
        String subject = request.getParameter("subject");
        String message = request.getParameter("message");

        // zavoláme pomocnou metodu - zatím nedělá nic
        sendMail(to, subject, message);

        // Odpověď bude obsahovat resumé
        
        HttpSession session = request.getSession();
        session.setAttribute("resume.to", to);
        session.setAttribute("resume.subject", subject);
        session.setAttribute("resume.message", message);
        

        response.sendRedirect(request.getRequestURI());

        output.flush();


//        // Odpověď bude obsahovat resumé
//        output.println("Email odeslán");
//        output.println("Komu: " + to);
//        output.println("Předmět: " + subject);
//        output.println("Zpráva: " + message);
//        output.println("MailSession: " + mailSession);
//        
//        output.flush();
    }

	 private void sendMail(String to, String subject, String msg)
	            throws ServletException {
	        try {
	            // Vytvoříme objekt zprávy
	            Message message = new MimeMessage(mailSession);

	            // Zatím nenastavujeme From, použije se default
	            // z konfigurace serveru
	            //message.setFrom();
	            message.setRecipients(Message.RecipientType.TO,
	                    InternetAddress.parse(to, false));

	            // Nastavíme předmět
	            message.setSubject(subject);

	            // Vložíme text zprávy
	            message.setText(msg);

	            // Nastavíme hlavičku indikující mailového klienta
	            message.setHeader("X-Mailer", "My Mailer");

	            // Nastavíme datum odeslání
	            Date timeStamp = new Date();
	            message.setSentDate(timeStamp);

	            // Odešleme zprávu
	            Transport.send(message);

	            getServletContext().log("Mail sent to " + to + ".");
	        } catch (MessagingException e) {
	            throw new ServletException(e);
	        }
	    }




	 @Override
	    protected void doGet(HttpServletRequest request,
	                         HttpServletResponse response)
	            throws ServletException, IOException {

	        response.setContentType("text/plain");
	        response.setCharacterEncoding("UTF-8");
	        PrintWriter output = response.getWriter();

	        // Odpověď bude obsahovat resumé
	        HttpSession session = request.getSession();
	        RequestDispatcher rd = request.getRequestDispatcher("sent.jsp");
	        String timeStamp = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(Calendar.getInstance().getTime());
	        request.setAttribute("to", session.getAttribute("resume.to"));
	        request.setAttribute("subject", session.getAttribute("resume.subject"));
	        request.setAttribute("message", session.getAttribute("resume.message"));
	        request.setAttribute("now", timeStamp);
	        rd.forward(request, response);
	        response.sendRedirect("sent.jsp");
//	        output.println("Email odeslán");
//	        output.println("Komu:" + session.getAttribute("resume.to"));
//	        output.println("Předmět:" + session.getAttribute("resume.subject"));
//	        output.println("Zpráva:" + session.getAttribute("resume.message"));

	        output.flush();
	    }

	
}
