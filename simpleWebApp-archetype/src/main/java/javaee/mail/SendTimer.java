package javaee.mail;

import java.util.Timer;
import java.util.TimerTask;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;

public class SendTimer extends TimerTask{
	
	private Message mail;
	
	public SendTimer(Message mail, Timer scheduler){
		this.mail = mail;
	}
	@Override
	public void run() {
		try {
			System.out.print("Odesílám mail...");
			Transport.send(mail);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
