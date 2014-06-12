package javaee.mail;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.EJBException;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.jms.*;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Tento bezstavový bean má na starosti export fotografií do JMS fronty. Při inicializaci vytvoří spojení
 * k JMS frontě, kterou drží otevřenou až do okamžiku jeho likvidace (metoda destroy).
 */
@Stateless
@LocalBean
public class Sender {

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
        	System.out.print("Connecting...");
            connection = connectionFactory.createConnection();
            System.out.print("Connected!");
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

	public void sendToJMS(String to, String copy, String subject, String body) throws AddressException, MessagingException {		
		try {
			System.out.print("Vytvářím session...");
			Session queueSession = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
			System.out.print("Session vytvořena!");
			MessageProducer producer = queueSession.createProducer(queue);
		                BytesMessage mailMessage = queueSession.createBytesMessage();
		                mailMessage.setStringProperty("body", body);
		                mailMessage.setStringProperty("subject", subject);
		                mailMessage.setStringProperty("to", to);
		                mailMessage.setStringProperty("copy", copy);
		                
		                producer.send(mailMessage);
        } catch (JMSException e) {
        	destroy();
        	throw new EJBException(e);

            
        }
		
	}

}

