package javaee.mail;

import java.util.List;

/**
 * DAO pro fotografie v albumu (bezstavová fasáda).
 * 
 * @author Jakub Kolář
 * 
 */
public interface EmailDAO {

	/**
	 * Metoda, která vrací seznam Emailů, které odeslal daný uživatel
	 * 
	 * @param owner
	 *            odesílatel a vlastník emailu
	 * @return
	 */
	List<Email> getEmailsByOwner(String owner);

	/**
	 * Metoda, která přidá odeslaný email do databáze
	 * 
	 * @param to
	 *            příjemce emailu
	 * @param copy
	 *            příjemce kopie emailu
	 * @param hiddenCopy
	 *            příjemce skryté kopie emailu
	 * @param subject
	 *            předmět emailu
	 * @param body
	 *            zpráva emailu
	 * @param owner
	 *            odesílatel a vlastník emailu
	 */
	void addEmail(String to, String copy, String hiddenCopy, String subject,
			String body, String owner);

	/**
	 * Metoda, která vrací seznam všech odeslaných emailů
	 * 
	 * @return
	 */
	List<Email> getEmails();
}