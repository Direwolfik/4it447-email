package javaee.mail;

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
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Message-Driven Bean implementation class for: SendEmailBean
 */
//@MessageDriven(
//		activationConfig = { @ActivationConfigProperty(
//				propertyName = "destinationType", propertyValue = "javax.jms.Queue")
//		})
public class SendEmailBean implements MessageListener {

    /**
     * Default constructor. 
     */
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
    public void onMessage(Message message) {
        System.out.print("Něco se stalo");
        
    }

}
