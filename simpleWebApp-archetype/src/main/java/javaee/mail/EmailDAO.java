package javaee.mail;

import java.util.List;

public interface EmailDAO {
		List<EmailBean> getEmailsByOwner(String owner);
		void addEmail(String to, String copy, String subject, String body, String owner);
		List<EmailBean> getEmails();
}