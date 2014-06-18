<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
	<title>E-mail formulář</title>
	<script src="//code.jquery.com/jquery-1.11.0.min.js"></script>
</head>
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
<h2>Maily:</h2>
<table id="maily">
	<th>Komu:</th>
	<th>Kopie:</th>
	<th>Předmět:</th>
	<th>Zpráva:</th>
	
	<tr>
		<td>
			<input type="text" id="filterTo" placeholder="Filtr komu..."/>
		</td>
		<td>
			<input type="text" id="filterCopy" placeholder="Filtr kopie..."/>
		</td>
		<td>
			<input type="text" id="filterSubject" placeholder="Filtr předmět..."/>
		</td>
		<td>
			<input type="text" id="filterBody" placeholder="Filtr zpráva..."/>
		</td>
	</tr>
	<c:forEach items="${emails}" var="email">
		<tr>
			<td class="to">${email.recipient}</td>
			<td class="copy">${email.copy}</td>
			<td class="subject">${email.subject}</td>
			<td class="body">${email.body}</td>
		</tr>
	</c:forEach>
</table>


<form action="sendMail" method="post">
    <label for="to">Komu:</label><br/>
    <input id="to" name="to" type="select"/><br/>
    <label for="to">Kopie:</label><br/>
    <input id="copy" name="copy" type="select"/><br/>
    <label for="to">Skrytá kopie:</label><br/> 
    <input id="hiddenCopy" name="hiddenCopy" type="select"/><br/>
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
    <input type="hidden" name="owner" value="${emailBean.owner}"/>
    <input type="submit" value="Odeslat"/>
	<input type="hidden" name="action" value="send">
</form>

<form id="selectContactForm" action="kontakty" method="post">
  
    <label for="selectedContact">Select contact:</label><br>
    	
    <select id="selectedContact" name="selectedContact" size="1" onchange="selectAlbumForm.submit()">
<c:forEach items="${contacts}" var="contact">
                    <option value="${contact.id}">${contact.name} - ${contact.email}</option>
        </c:forEach>
    </select><br>
     <input type="hidden" name="action" value="deleteContact"/><br>
      <input type="submit" value="Smazat"/>
      <button type="button" onclick="addContact(); return false;">Přidat "komu"</button>
      <button type="button" onclick="addCopy(); return false;">Přidat "kopie"</button>
      <button type="button" onclick="editContact(); return false;">Editovat</button>
</form>
<div id="edit_div" style="display: none">
<form action="kontakty" method="post">
	<input type="hidden" name="action" value="editContact"/><br>
    <h3>Editovat stávající kontakt</h3>
    <label for="name">Jméno:</label><br>
    <input id="name_edit" type="text" name="name"/><br>
    <label for="email">Email:</label><br>
    <input id="email_edit" type="text" name="email"/><br>
    <input type="hidden" name="owner" value="${emailBean.owner}"/>
    <input id="id_edit" type="hidden" name="id" value=""/><br>
    <input type="submit" value="Upravit"/><br>
</form>
</div>
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
<form action="sendMail" method="post">
     <input type="hidden" name="action" value="logout"/>
     <input type="submit" value="Logout"/>
</form>
<script>
function addContact() {
	var e = document.getElementById("selectedContact");
	var strContact = e.options[e.selectedIndex].text;
	var res = strContact.split(" - "); 
	var elem = document.getElementById("to");
	elem.value = elem.value+res[1]+", ";
	}
function addCopy() {
	var e = document.getElementById("selectedContact");
	var strContact = e.options[e.selectedIndex].text;
	var res = strContact.split(" - "); 
	var elem = document.getElementById("copy");
	elem.value = elem.value+res[1]+", ";
	}
function editContact() {
	var e = document.getElementById("selectedContact");
	var id = e.options[e.selectedIndex].value;
	
	var elem = document.getElementById("selectedContact");
	var strContact = elem.options[e.selectedIndex].text;
	var res = strContact.split(" - "); 
	var name = res[0];
	var email = res[1];

	var elemName = document.getElementById("name_edit");
	var elemEmail = document.getElementById("email_edit");
	var elemId = document.getElementById("id_edit");

	elemName.value = name;
	elemEmail.value = email;
	elemId.value = id;

	document.getElementById("edit_div").style.display = 'block';

	
	}
</script>
<script>
$(function() {    
    $('#filterTo').keyup(function() { 
        $("#maily td.to:contains('" + $(this).val() + "')").parent().show();
        $("#maily td.to:not(:contains('" + $(this).val() + "'))").parent().hide();
    });    
});
$(function() {    
    $('#filterCopy').keyup(function() { 
        $("#maily td.copy:contains('" + $(this).val() + "')").parent().show();
        $("#maily td.copy:not(:contains('" + $(this).val() + "'))").parent().hide();
    });    
});
$(function() {    
    $('#filterSubject').keyup(function() { 
        $("#maily td.subject:contains('" + $(this).val() + "')").parent().show();
        $("#maily td.subject:not(:contains('" + $(this).val() + "'))").parent().hide();
    });    
});
$(function() {    
    $('#filterBody').keyup(function() { 
        $("#maily td.body:contains('" + $(this).val() + "')").parent().show();
        $("#maily td.body:not(:contains('" + $(this).val() + "'))").parent().hide();
    });    
});
</script>

</body>

</html>
