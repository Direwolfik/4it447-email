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

/**
 * Tento bezstavový bean má na starosti odesílání zpráv do JMS fronty. Při
 * inicializaci vytvoří spojení k JMS frontě, kterou drží otevřenou až do
 * okamžiku jeho likvidace (metoda destroy).
 * 
 * @author Jakub Kolář, Josef Novotný
 * @since 1.0
 */
@Stateless
@LocalBean
public class Sender {

	@Resource(mappedName = "jms/sendQueueFactory")
	private ConnectionFactory connectionFactory;
	@Resource(mappedName = "jms/sendQueue")
	private Queue queue;
	Session queueSession;

	private Connection connection;

	/**
	 * Metoda vytvoří spojení k JMS frontě a také session
	 */
	@PostConstruct
	private void init() {
		try {
			connection = connectionFactory.createConnection();
			queueSession = connection.createSession(true,
					Session.AUTO_ACKNOWLEDGE);
		} catch (JMSException e) {
			throw new EJBException(e);
		}
	}

	/**
	 * Metoda uzavírá spojení s JMS frontou
	 */
	@PreDestroy
	private void destroy() {
		try {
			connection.close();
		} catch (JMSException e) {
			throw new EJBException();
		}
	}

	/**
	 * Metoda odesílá zprávy obsahující informace o odesílaném emailu do JMS
	 * fronty
	 * 
	 * @param to
	 *            příjemce emailu
	 * @param copy
	 *            příjemce kopie emailu
	 * @param hiddenCopy
	 *            příjemce skryté kopie emailu
	 * @param subject
	 *            prědmět emailu
	 * @param body
	 *            zpráva emailu
	 * @param time
	 *            za kolik minut má být email odeslán
	 * @param owner
	 *            odesílatel a součastně i vlastník emailu
	 * @throws AddressException
	 * @throws MessagingException
	 */
	public void sendToJMS(String to, String copy, String hiddenCopy,
			String subject, String body, int time, String owner)
			throws AddressException, MessagingException {
		try {
			MessageProducer producer = queueSession.createProducer(queue);
			BytesMessage mailMessage = queueSession.createBytesMessage();
			mailMessage.setStringProperty("body", body);
			mailMessage.setStringProperty("subject", subject);
			mailMessage.setStringProperty("to", to);
			mailMessage.setStringProperty("copy", copy);
			mailMessage.setStringProperty("hiddenCopy", hiddenCopy);
			mailMessage.setStringProperty("owner", owner);
			mailMessage.setIntProperty("time", time);

			producer.send(mailMessage);
		} catch (JMSException e) {
			destroy();
			throw new EJBException(e);

		}

	}

}
