<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head><title>E-mail formulář</title></head>
<body>
<jsp:useBean id="emailBean"
             class="javaee.mail.EmailBean"
             scope="session"/>
<jsp:useBean id="emailDAO"
             class="javaee.mail.EmailDAO"
             scope="application"/>    
<jsp:useBean id="contactsDAO"
             class="javaee.mail.ContactsDAO"
             scope="application"/>         

<h1>Formulář pro email</h1>
<h2>Uživatel: ${emailBean.owner}</h2>
<h2>Maily: ${emailDAO.emails}</h2>
<form action="logout" method="post">
    <input type="hidden" name="action" value="doLogout"/>
    <input type="submit" value="Logout"/>
</form>
<form action="sendMail" method="post">
    <input type="hidden" name="action" value="store"/>
    <input type="submit" value="Store"/>
</form>
<form action="sendMail" method="post">
    <input type="hidden" name="action" value="restore"/>
    <input type="submit" value="Restore"/>
</form>
<form action="sendMail" method="post">
    <label for="to">Komu:</label><br/>
    <input id="to" name="to" type="select"/><br/>
    <label for="to">Kopie:</label><br/>
    <input id="copy" name="copy" type="select"/><br/>
    <label for="to">Odeslat za # minut:</label><br/>
    <select id="time" name="time" type="select" size="1">
    <c:forEach begin="0" end="600" step="5" var="i">
    <option value="${i}">${i}</option>
	</c:forEach>
	</select> <br/>
    <label for="subject">Předmět:</label><br/>
    <input id="subject" name="subject" type="text"/><br/>
    <label for="message">Zpráva:</label><br/>
    <textarea id="message" name="message" rows="20" cols="60">
    </textarea><br/>
    <input type="submit" value="Odeslat"/>
	<input type="hidden" name="action" value="send">
</form>

<form id="selectContactForm" action="" method="post">
    <input type="hidden" name="action" value="selectContact"/>
    <label for="selectedContact">Select contact:</label><br>
    	
    <select id="selectedContact" name="selectedContact" size="1" onchange="selectAlbumForm.submit()">
<c:forEach items="${contactsDAO.contacts}" var="contact">
           
                
                    <option value="${contact.email}">${contact.name} - ${contact.email}</option>
               
           
        </c:forEach>
    </select><br>
</form>

<form action="kontakty" method="post">
    <input type="hidden" name="action" value="pridat"/><br>
    <h3>Vložit nový kontakt</h3>
    <label for="name">Jméno:</label><br>
    <input id="name" type="text" name="name"/><br>
    <label for="email">Email:</label><br>
    <input id="email" type="text" name="email"/><br>
    <input type="hidden" name="owner" value="${emailBean.owner}"/>
    <input type="submit" value="Přidat"/><br>
</form>


</body>
</html>
