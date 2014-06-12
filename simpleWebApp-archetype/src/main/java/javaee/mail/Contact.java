package javaee.mail;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;



public class Contact implements Serializable {
	private int id;
	private String email;
	private String name;
	private Contacts contacts;
	
	 @Id
	 @GeneratedValue
	 public int getId() {
	        return id;
	 }

	    public void setId(int id) {
	        this.id = id;
	    }

		public String getEmail() {
			return email;
		}

		public String getName() {
			return name;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public void setName(String name) {
			this.name = name;
		}
		
		 @ManyToOne
		    public Contacts getContacts() {
		        return contacts;
		    }

		    public void setAlbum(Contacts contacts) {
		        this.contacts = contacts;
		    }
	    
}
