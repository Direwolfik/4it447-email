package javaee.mail;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.EJBException;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.jms.*;
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

	public void sendToJMS(javax.mail.Session mailSession) {
		
		try {
			Session queueSession = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
			MessageProducer producer = queueSession.createProducer(queue);
            
		                BytesMessage mailMessage = queueSession.createBytesMessage();
		                mailMessage.setStringProperty("to", mailSession.getProperty("to"));
		                mailMessage.setStringProperty("subject", mailSession.getProperty("subject"));
		                mailMessage.setStringProperty("copy", mailSession.getProperty("copy"));
		                mailMessage.setStringProperty("body", mailSession.getProperty("body"));
		                mailMessage.setStringProperty("user", mailSession.getProperty("user"));

		                producer.send(mailMessage);
        } catch (Exception e) {
            destroy();
            throw new EJBException(e);
        }
		
	}

}

