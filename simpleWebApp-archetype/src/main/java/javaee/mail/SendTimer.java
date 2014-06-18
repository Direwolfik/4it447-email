package javaee.mail;

import java.util.Timer;
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

	private Message mail;

	public SendTimer(Message mail, Timer scheduler) {
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
