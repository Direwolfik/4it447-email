package javaee.mail;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author Jakub Kolář
 *
 */
@Entity
public class Email implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String recipient;
	private String copy;
	private String hiddenCopy;
    private String subject;
    private String body;
    private String owner;

    @Id
	 @GeneratedValue
	 public int getId() {
	        return id;
	 }

	    public void setId(int id) {
	        this.id = id;
	    }
	    
    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
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

	public String getCopy() {
		return copy;
	}

	public void setCopy(String copy) {
		this.copy = copy;
	}

	public String getRecipient() {
		return recipient;
	}

	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}

	public String getHiddenCopy() {
		return hiddenCopy;
	}

	public void setHiddenCopy(String hiddenCopy) {
		this.hiddenCopy = hiddenCopy;
	}
}


