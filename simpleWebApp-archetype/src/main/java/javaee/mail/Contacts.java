package javaee.mail;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 * Třída reprezentující entitu kontaktu.
 * 
 * @author Josef Novotný
 * @since 1.0
 * 
 */
@Entity
public class Contacts implements Serializable {
	private int id;
	private String name;
	private String email;
	private String owner;

	/**
	 * Vrátí ID kontaktu z tabulky kontaktů
	 * 
	 * @return ID kontaktu
	 */
	@Id
	@GeneratedValue
	public int getId() {
		return id;
	}

	/**
	 * Nastaví ID kontaktu v tabulce kontaktů
	 * 
	 * @param id
	 *            - ID k nastavení
	 * 
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Vrátí jméno kontaktu z tabulky kontaktů
	 * 
	 * @return jméno kontaktu
	 */
	public String getName() {
		return name;
	}

	/**
	 * Nastaví jméno kontaktu v tabulce kontaktů
	 * 
	 * @param name
	 *            - jméno k nastavení
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Vrátí email kontaktu z tabulky kontaktů
	 * 
	 * @return email kontaktu
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Vrátí vlastníka kontaktu z tabulky kontaktů
	 * 
	 * @return vlastník kontaktu
	 */
	public String getOwner() {
		return owner;
	}

	/**
	 * Nastaví email kontaktu v tabulce kontaktů
	 * 
	 * @param email
	 *            - email k nastavení
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Nastaví vlastníka kontaktu v tabulce kontaktů
	 * 
	 * @param owner
	 *            - vlastník k nastavení
	 * 
	 */
	public void setOwner(String owner) {
		this.owner = owner;
	}

}
