package javaee.mail;

import java.util.Date;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJBException;
import javax.ejb.MessageDriven;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Message-Driven Bean implementation class for: SendEmailBean
 */
@MessageDriven(mappedName="jms/sendQueue", activationConfig =  {
        @ActivationConfigProperty(propertyName = "acknowledgeMode",
                                  propertyValue = "Auto-acknowledge"),
        @ActivationConfigProperty(propertyName = "destinationType",
                                  propertyValue = "javax.jms.Queue")
    })
public class SendEmailBean implements MessageListener {

    /**
     * Default constructor. 
     */
	@Resource(name = "mail/myMailSession")
	private Session mailSession;
	
    public SendEmailBean() {
        // TODO Auto-generated constructor stub
    }
    
    @PersistenceContext
    private EntityManager entityManager;
    @Resource(mappedName = "jms/sendQueueFactory")
    private ConnectionFactory connectionFactory;
    @Resource(mappedName = "jms/sendQueue")
    private Queue queue;

    private Connection connection;

    @PostConstruct
    private void init() {
        try {
            connection = connectionFactory.createConnection();
        } catch (JMSException e) {
            throw new EJBException(e);
        }
    }

    @PreDestroy
    private void destroy() {
        try {
            connection.close();
        } catch (JMSException e) {
            throw new EJBException();
        }
    }
	
	/**
     * @see MessageListener#onMessage(Message)
     */
    public void onMessage(Message message){
        try {
        	String to = message.getStringProperty("to");
        	String copy = message.getStringProperty("copy");
        	String subject = message.getStringProperty("subject");
        	String body = message.getStringProperty("body");
			System.out.print("Session: "+mailSession);
			// Vytvoříme objekt zprávy
			javax.mail.Message mail = new MimeMessage(mailSession);

	        // Zatím nenastavujeme From, použije se default
	        // z konfigurace serveru
	        //message.setFrom();
	        mail.setRecipients(javax.mail.Message.RecipientType.TO,
	                InternetAddress.parse(to, false));
	        mail.setRecipients(javax.mail.Message.RecipientType.CC,
	                InternetAddress.parse(copy, false));

	        // Nastavíme předmět
	        mail.setSubject(subject);

	        // Vložíme text zprávy
	        mail.setText(body);
	        
	       // Nastavíme hlavičku indikující mailového klienta
	        mail.setHeader("X-Mailer", "My Mailer");

	        // Nastavíme datum odeslání
	        Date timeStamp = new Date();
	        mail.setSentDate(timeStamp);


	        // Odešleme zprávu
	        Transport.send(mail);
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AddressException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }

}
