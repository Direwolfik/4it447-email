<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<title>E-mail formulář</title>
<script src="//code.jquery.com/jquery-1.11.0.min.js"></script>
<link rel="stylesheet"
	href="//netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap.min.css">
</head>
<body>
	<jsp:useBean id="email" class="javaee.mail.Email" scope="session" />
	<jsp:useBean id="emailDAO" class="javaee.mail.EmailDAO"
		scope="application" />
	<jsp:useBean id="contactsDAO" class="javaee.mail.ContactsDAO"
		scope="application" />
	<div class="row" style="margin-left:0;margin-right:0">
		<div class="col-xs-12"><div class="col-xs-12">
		<h1>Formulář pro email</h1>
		</div></div>
		<div class="col-xs-12">
			<div class="col-xs-6">
				<h2>Uživatel: ${email.owner}</h2>
				<form action="sendMail" method="post">
					<label for="to">Komu:</label><br /> <input class="form-control"
						id="to" name="to" type="select" /><br /> <label for="to">Kopie:</label><br />
					<input class="form-control" id="copy" name="copy" type="select" /><br />
					<label for="to">Skrytá kopie:</label><br /> <input
						class="form-control" id="hiddenCopy" name="hiddenCopy"
						type="select" /><br /> <label for="to">Odeslat za:</label><br /> <select class="form-control" id="time" name="time"
						type="select" size="1">
							<option value="0">Hned</option>
							<option value="1">1.0 minutu</option>
							<option value="2">2.0 minuty</option>
							<option value="3">3.0 minuty</option>
							<option value="4">4.0 minuty</option>
						<c:forEach begin="5" end="55" step="5" var="i">
							<option value="${i}">${i/1} minut</option>
						</c:forEach>
							<option value="60">1.0 hodinu</option>
							<option value="120">2.0 hodiny</option>
							<option value="180">3.0 hodiny</option>
							<option value="240">4.0 hodiny</option>
						<c:forEach begin="300" end="1440" step="60" var="i">
							<option value="${i}">${i/60} hodin</option>
						</c:forEach>
					</select> <br /> <label for="subject">Předmět:</label><br /> <input
						class="form-control" id="subject" name="subject" type="text" /><br />
					<label for="message">Zpráva:</label><br />
					<textarea class="form-control" id="message" name="message"
						rows="20" cols="20">
    </textarea>
					<br /> <input type="hidden" name="owner" value="${email.owner}" />
					<input class="btn btn-info form-control" type="submit"
						value="Odeslat" /> <input type="hidden" name="action"
						value="send">
				</form>
			</div>
			<div class="col-xs-6">
				<form id="selectContactForm" action="kontakty" method="post">
					<h3>Kontakty:</h3>
					<br> <select
						class="form-control" id="selectedContact" name="selectedContact"
						size="1" onchange="selectAlbumForm.submit()">
						<c:forEach items="${contacts}" var="contact">
							<option value="${contact.id}">${contact.name} -
								${contact.email}</option>
						</c:forEach>
					</select> <input type="hidden" name="action" value="deleteContact" /> <br>
					<div class="col-xs-6">
						<button class="btn btn-default form-control" type="button"
							onclick="addContact(); return false;">Přidat "komu"</button>
						<button class="btn btn-default form-control" type="button"
							onclick="addCopy(); return false;">Přidat "kopie"</button>
						<button class="btn btn-default form-control" type="button"
							onclick="addHiddenCopy(); return false;">Přidat "skrytá
							kopie"</button>
					</div>
					<div class="col-xs-6">
						<button class="btn btn-default form-control" type="button"
							onclick="editContact(); return false;">Editovat</button>
						<button class="btn btn-default form-control" type="button"
							onclick="editContactClose(); return false;">Zavřit editaci</button>
						<input class="btn btn-default form-control" type="submit"
							value="Smazat" />
					</div>
				</form>
				<div id="edit_div" style="display: none">
					<form action="kontakty" method="post">
						<input type="hidden" name="action" value="editContact" /><br>
						<br> <br> <br><br>
						<label>Editovat stávající kontakt:</label><br>
						<label for="name">Jméno:</label><br> <input
							class="form-control" id="name_edit" type="text" name="name" /><br>
						<label for="email">Email:</label><br> <input
							class="form-control" id="email_edit" type="text" name="email" /><br>
						<input type="hidden" name="owner" value="${email.owner}" /> <input
							id="id_edit" type="hidden" name="id" value="" /><input
							class="btn btn-success form-control" type="submit"
							value="Upravit" />
					</form>
				</div>
				<form action="kontakty" method="post">
					<input type="hidden" name="action" value="pridat" /><br> <br>
					<br> <br><br>
					<label>Vložit nový kontakt:</label><br>
					<label for="name">Jméno:</label><br> <input
						class="form-control" id="name" type="text" name="name" /><br>
					<label for="email">Email:</label><br> <input
						class="form-control" id="email" type="text" name="email" /><br>
					<input type="hidden" name="owner" value="${email.owner}" /> <input
						class="btn btn-success form-control" type="submit" value="Přidat" /><br>
				</form>
				<form action="sendMail" method="post">
					<input type="hidden" name="action" value="logout" /> <input
						class="btn btn-danger form-control" type="submit" value="Logout" />
				</form>
			</div>
		</div>
		<div class="col-xs-12">
		<div class="col-xs-12">
			<h2>Maily:</h2>
			<table id="maily" class="table table-condensed">
				<th>Komu:</th>
				<th>Kopie:</th>
				<th>Skrytá kopie:</th>
				<th>Předmět:</th>
				<th>Zpráva:</th>

				<tr>
					<td><input class="form-control" type="email" id="filterTo"
						placeholder="Filtr komu..." /></td>
					<td><input class="form-control" type="text" id="filterCopy"
						placeholder="Filtr kopie..." /></td>
					<td><input class="form-control" type="text"
						id="filterHiddenCopy" placeholder="Filtr skyryté kopie..." /></td>
					<td><input class="form-control" type="text" id="filterSubject"
						placeholder="Filtr předmět..." /></td>
					<td><input class="form-control" type="text" id="filterBody"
						placeholder="Filtr zpráva..." /></td>
				</tr>
				<c:forEach items="${emails}" var="email">
					<tr>
						<td class="to">${email.recipient}</td>
						<td class="copy">${email.copy}</td>
						<td class="hiddenCopy">${email.hiddenCopy}</td>
						<td class="subject">${email.subject}</td>
						<td class="body">${email.body}</td>
					</tr>
				</c:forEach>
			</table>
		</div></div>
	</div>
	<script>
		function addContact() {
			var e = document.getElementById("selectedContact");
			var strContact = e.options[e.selectedIndex].text;
			var res = strContact.split(" - ");
			var elem = document.getElementById("to");
			elem.value = elem.value + res[1] + ", ";
		}
		function addCopy() {
			var e = document.getElementById("selectedContact");
			var strContact = e.options[e.selectedIndex].text;
			var res = strContact.split(" - ");
			var elem = document.getElementById("copy");
			elem.value = elem.value + res[1] + ", ";
		}
		function addHiddenCopy() {
			var e = document.getElementById("selectedContact");
			var strContact = e.options[e.selectedIndex].text;
			var res = strContact.split(" - ");
			var elem = document.getElementById("hiddenCopy");
			elem.value = elem.value + res[1] + ", ";
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
		function editContactClose() {
			document.getElementById("edit_div").style.display = 'none';
			}
	</script>
	<script>
		$(function() {
			$('#filterTo').keyup(
					function() {
						$("#maily td.to:contains('" + $(this).val() + "')")
								.parent().show();
						$(
								"#maily td.to:not(:contains('" + $(this).val()
										+ "'))").parent().hide();
					});
		});
		$(function() {
			$('#filterCopy').keyup(
					function() {
						$("#maily td.copy:contains('" + $(this).val() + "')")
								.parent().show();
						$(
								"#maily td.copy:not(:contains('"
										+ $(this).val() + "'))").parent()
								.hide();
					});
		});
		$(function() {
			$('#filterHiddenCopy')
					.keyup(
							function() {
								$(
										"#maily td.hiddenCopy:contains('"
												+ $(this).val() + "')")
										.parent().show();
								$(
										"#maily td.hiddenCopy:not(:contains('"
												+ $(this).val() + "'))")
										.parent().hide();
							});
		});
		$(function() {
			$('#filterSubject')
					.keyup(
							function() {
								$(
										"#maily td.subject:contains('"
												+ $(this).val() + "')")
										.parent().show();
								$(
										"#maily td.subject:not(:contains('"
												+ $(this).val() + "'))")
										.parent().hide();
							});
		});
		$(function() {
			$('#filterBody').keyup(
					function() {
						$("#maily td.body:contains('" + $(this).val() + "')")
								.parent().show();
						$(
								"#maily td.body:not(:contains('"
										+ $(this).val() + "'))").parent()
								.hide();
					});
		});
	</script>
</body>

</html>
