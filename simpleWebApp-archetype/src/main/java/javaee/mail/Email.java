package javaee.mail;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Třída reprezentující entitu emailu.
 * 
 * @author Jakub Kolář, Josef Novotný
 * @since 1.0
 */
@Entity
public class Email implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	private int id;
	@Column(length = 500)
	private String recipient;
	@Column(length = 500)
	private String copy;
	@Column(length = 500)
	private String hiddenCopy;
	private String subject;
	@Column(length = 5000)
	private String body;
	private String owner;

	/**
	 * Vrátí ID kontaktu z tabulky kontaktů
	 * 
	 * @return ID emailu
	 */
	
	public int getId() {
		return id;
	}

	/**
	 * Nastaví ID emailu
	 * 
	 * @param id
	 *            ID emailu
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Vrátí vlastníka emailu
	 * 
	 * @return vlastník emailu
	 */
	public String getOwner() {
		return owner;
	}

	/**
	 * Nastaví vlastníka emailu
	 * 
	 * @param owner
	 *            vlastník emailu
	 */
	public void setOwner(String owner) {
		this.owner = owner;
	}

	/**
	 * Vrátí předmět emailu
	 * 
	 * @return předmět emailu
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * Nastaví předmět emailu
	 * 
	 * @param subject
	 *            předmět emailu
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * Vrátí zprávu emailu
	 * 
	 * @return zpráva emailu
	 */
	public String getBody() {
		return body;
	}

	/**
	 * Nastaví zprávu emailu
	 * 
	 * @param message
	 *            zpráva emailu
	 */
	public void setBody(String message) {
		this.body = message;
	}

	/**
	 * Vrátí příjemce kopie emailu
	 * 
	 * @return příjemce kopie emailu
	 */
	public String getCopy() {
		return copy;
	}

	/**
	 * Nastaví příjemce kopie emailu
	 * 
	 * @param copy
	 *            příjemce kopie emailu
	 */
	public void setCopy(String copy) {
		this.copy = copy;
	}

	/**
	 * Vrátí příjemce emailu
	 * 
	 * @return příjemce emailu
	 */
	public String getRecipient() {
		return recipient;
	}

	/**
	 * Nastaví příjemce emailu
	 * 
	 * @param recipient
	 *            příjemce emailu
	 */
	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}

	/**
	 * Vrátí příjemce skryté kopie emailu
	 * 
	 * @return příjemce skryté kopie emailu
	 */
	public String getHiddenCopy() {
		return hiddenCopy;
	}

	/**
	 * Nastaví příjemce kopie emailu
	 * 
	 * @param hiddenCopy
	 *            příjemce kopie emailu
	 */
	public void setHiddenCopy(String hiddenCopy) {
		this.hiddenCopy = hiddenCopy;
	}
}
