package javaee.mail;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.Date;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailBean implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String to;
	private String copy;
    private String subject;
    private String body;
    private String user;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String message) {
        this.body = message;
    }

    /**
     * Schválně předáváme mail session jako parametr. Kdybychom měli
     * session uloženu jako hodnotu atributu, musel by kvůli
     * serializovatelnosti být tento atribut transientní. Při aktivaci
     * během migrace by tak bylo komplikované získat session zpět.
     * @param mailSession
     * @throws MessagingException
     * @throws IOException 
     */
    public void store(Session mailSession) throws MessagingException, IOException {
    	String subject = getSubject();
    	String message = getBody();
    	String to = getTo();
    	PrintStream fileStream = new PrintStream(new File("D:/Applications/eclipse EE/file.txt"));
    	fileStream.println(to);
    	fileStream.println(subject);
    	fileStream.println(message);
    	fileStream.close();
    	//String user = getUser();
//    	File file = new File("koncept.txt");
//		file.createNewFile();
//		FileWriter out = new FileWriter(new File("file.txt"), false);
//    	out.write(to);
//    	out.write(subject);
//    	out.write(message);
//    	out.close();    	
    }
    
    public void restore(Session mailSession) throws MessagingException {
    	
    }

    public void send(Session mailSession) throws MessagingException {
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
        message.setText(body);
        
       // Nastavíme hlavičku indikující mailového klienta
        message.setHeader("X-Mailer", "My Mailer");

        // Nastavíme datum odeslání
        Date timeStamp = new Date();
        message.setSentDate(timeStamp);


        // Odešleme zprávu
        Transport.send(message);
    }

	public String getCopy() {
		return copy;
	}

	public void setCopy(String copy) {
		this.copy = copy;
	}
}


