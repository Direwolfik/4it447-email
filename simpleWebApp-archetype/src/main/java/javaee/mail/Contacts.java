package javaee.mail;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;



public class Contacts implements Serializable {
	private int id;
	private String name;
	 @OneToMany(mappedBy = "contacts", fetch = FetchType.LAZY)
	private List<Contact> contacts = new ArrayList<Contact>();
	
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
	    
}
