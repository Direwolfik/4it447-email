package javaee.mail;

import java.util.TimerTask;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;

/**
 * Třída zajišťující odeslání emailu po tom, co vyprší časovač.
 * 
 * @author Jakub Kolář, Josef Novotný
 * @since 1.0
 * 
 */
public class SendTimer extends TimerTask {

	private final Message mail;

	public SendTimer(Message mail) {
		this.mail = mail;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.TimerTask#run()
	 */
	@Override
	public void run() {
		try {
			Transport.send(mail);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
