package javaee.mail;

import java.util.List;

/**
 * DAO pro kontakty v seznamu kontaktů jednotlivých uživatelů.
 * 
 * @author Jakub Kolář, Josef Novotný
 * @since 1.0
 */
public interface ContactsDAO {

	/**
	 * Vyhledá v databázi všechny kontakty spadající pod konkrétního uživatele,
	 * jehož uživatelské jméno je předáno v parametru metody a vrátí je v podobě
	 * kolekce.
	 * 
	 * @param owner
	 *            - uživatelské jméno vlastníka kontaktu
	 * @return Pole kontaktů
	 */
	List<Contacts> getContactsByOwner(String owner);

	/**
	 * Přidá kontakt do databáze na základě zadaných parametrů.
	 * 
	 * @param name
	 *            - Jméno kontaktu
	 * @param email
	 *            - Email konaktu
	 * @param owner
	 *            - Uživatelské jméno vlastníka kontaktu
	 */
	void addContact(String name, String email, String owner);

	/**
	 * Odebere kontakty vybraného uživatele na základě jejich ID. Metodě se pro
	 * kontrolu oprávnění předává i vlastník kontaktů.
	 * 
	 * @param contactIDs
	 *            - pole ID konaktů
	 * @param owner
	 *            - vlastník kontaktů
	 */
	void removeContacts(int[] contactIDs, String owner);

	/**
	 * Vrátí všechny kontakty v databázi v podobě kolekce.
	 * 
	 * @return Pole kontaktů
	 */
	List<Contacts> getContacts();

	/**
	 * Odebere kontakt na základě jeho ID.
	 * 
	 * @param contactID
	 *            - ID kontaktu, který se má odebrat.
	 */
	void removeContact(String contactID);

	/**
	 * Na základě parametrů upraví stávájící kontakt v databázi konaktů.
	 * 
	 * @param name
	 *            - jméno kontaktu
	 * @param email
	 *            - email kontaktu
	 * @param owner
	 *            - vlastník kontaktu
	 * @param id
	 *            - id kontaktu, který má být upraven
	 */
	void updateContact(String name, String email, String owner, int id);

}
