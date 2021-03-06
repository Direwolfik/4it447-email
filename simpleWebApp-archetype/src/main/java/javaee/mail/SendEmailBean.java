package javaee.mail;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Timer;

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
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


/**
 * Message-Driven Bean implementation class for: SendEmailBean 
 * Třída naslouchá
 * zprávám v JMS frontě a provádí další akce na základě těchto zpráv
 * 
 * @author Jakub Kolář, Josef Novotný
 * @since 1.0
 */
@MessageDriven(mappedName = "jms/sendQueue", activationConfig = {
		@ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"),
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue") })
public class SendEmailBean implements MessageListener {

	@Resource(name = "mail/myMailSession")
	private Session mailSession;
	@Resource(mappedName = "jms/sendQueueFactory")
	private ConnectionFactory connectionFactory;
	private Connection connection;
	

	/**
	 * Metoda vytváří spojení s JMS frontou
	 */
	@PostConstruct
	private void init() {
		try {
			connection = connectionFactory.createConnection();
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
	 * Metoda, která při nové zprávě v JMS frontě vytvoří objekt emailu, nastaví
	 * mu dané atributy a následně email pomocí Timeru odešle buď rovnou, nebo
	 * se spožděním
	 * 
	 * @see MessageListener#onMessage(Message)
	 */
	@Override
	public void onMessage(Message message) {
		try {
			String to = message.getStringProperty("to");
			String copy = message.getStringProperty("copy");
			String hiddenCopy = message.getStringProperty("hiddenCopy");
			String subject = message.getStringProperty("subject");
			String body = message.getStringProperty("body");
			String owner = message.getStringProperty("owner");
			int time = message.getIntProperty("time");

			// Vytvoříme objekt zprávy

			javax.mail.Message mail = new MimeMessage(mailSession);
			// Zatím nenastavujeme From, použije se default
			// z konfigurace serveru
			// message.setFrom();
			mail.setRecipients(javax.mail.Message.RecipientType.TO,
					InternetAddress.parse(to, false));
			mail.setRecipients(javax.mail.Message.RecipientType.CC,
					InternetAddress.parse(copy, false));
			mail.setRecipients(javax.mail.Message.RecipientType.BCC,
					InternetAddress.parse(hiddenCopy, false));

			// Nastavíme předmět
			mail.setSubject(subject);

			// Vložíme text zprávy
			mail.setText(body);

			try {
				mail.setFrom(new InternetAddress(owner + "@4it447.java", owner));
			} catch (UnsupportedEncodingException e) {

			}

			// Nastavíme hlavičku indikující mailového klienta
			mail.setHeader("X-Mailer", "My Mailer");

			// Nastavíme datum odeslání
			Date timeStamp = new Date();
			mail.setSentDate(timeStamp);

			// Odešleme zprávu
			// Transport.send(mail);
			Timer scheduler;
			scheduler = new Timer();
			SendTimer timeMe = new SendTimer(mail);
			scheduler.schedule(timeMe, time * 60 * 1000);

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
