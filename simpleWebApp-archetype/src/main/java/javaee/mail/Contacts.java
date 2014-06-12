package javaee.mail;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;


@Entity
public class Contacts implements Serializable {
	private int id;
	private String name;
	private String email;
	private String owner;
	
	@Id
	 @GeneratedValue
	 public int getId() {
	        return id;
	 }

	    public void setId(int id) {
	        this.id = id;
	    }
	    
	    public String getName() {
	        return name;
	    }

	    public void setName(String name) {
	        this.name = name;
	    }

		public String getEmail() {
			return email;
		}

		public String getOwner() {
			return owner;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public void setOwner(String owner) {
			this.owner = owner;
		}
	    
	    
}
