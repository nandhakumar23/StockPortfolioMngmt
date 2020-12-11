<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="styleSI.css">
<link href="https://fonts.googleapis.com/css2?family=Teko:wght@200;400;700;900&display=swap" rel="stylesheet">
<title>Register for Portfolio Management</title>




</head>
<body id="start">
	<%
		String login = (String) session.getAttribute("login");
		boolean loginB = false;
		if (login != null) {
			loginB = Boolean.parseBoolean(login);
		}
		
		if(loginB){
			String site = new String("homepage.jsp");
	         response.setStatus(response.SC_MOVED_TEMPORARILY);
	         response.setHeader("Location", site);
		}
	%>
	<div class="navbar">
		<div class="wrap">
			<h1>USC CS 310: Stock Portfolio Management</h1>
		</div>
	</div>
	<div class="signup">

		<form id="register">
			<h3>Sign Up</h3>
			<div>
				<label for="username"> Username </label> <input type="text"
					id="username" name="username">
			</div>

			<div>
				<label for="password"> Password (8 characters minimum
					required) </label> <input type="password" id="password" name="password">
			</div>

			<div>
				<label> Confirm Password </label> <input type="password"
					id="confirmpassword" name="confirmpassword">
			</div>

			<div id="subButton">
				<a id = "c"  class="sign-in-button">
					<i class="fas fa-sign-out-alt"></i>&nbsp&nbspCancel
				</a>
				
	
				
				<button type="submit" id = "b" class="sign-in-button">
					<i class="fas fa-sign-out-alt"></i>&nbsp&nbspCreate User
				</button>
			</div>
			 </br>
			<div id="eMess"></div>
		</form>

	</div>

	<script>
		let url = "api/reg";
		
		document.querySelector("#c").onclick = function(){
			window.location.href = "signIn.jsp";
		}
		
		function parseResponse(response) {
			let data = JSON.parse(response);
			console.log(data);
			let num = data; // to be gotten from data;
			if (num == 0) {
				document.body.id = "errorScreen";
				document.querySelector("#username").style.borderColor = "#ff0033";
				document.querySelector("#password").style.borderColor = "#ff0033";
				document.querySelector("#confirmpassword").style.borderColor = "#ff0033";
				document.querySelector("#eMess").innerHTML = "<p id = \"Merror\">Username is already been chosen</p>";
				document.querySelector('#b').className = "sign-in-button-error";
				document.querySelector('#c').className = "sign-in-button-error";

			} else if (num == 1) {
				window.location.href = "signIn.jsp"
			}

		}

		document.querySelector("#register").onsubmit = function(event) {
			event.preventDefault();

			let username = document.getElementById("username").value;
			let password = document.getElementById("password").value;
			let password_confirmation = document.getElementById("confirmpassword").value;

			if (username.length < 1) {
				document.body.id = "errorScreen";
				document.querySelector("#username").style.borderColor = "#ff0033";
				document.querySelector("#password").style.borderColor = "#ff0033";
				document.querySelector("#confirmpassword").style.borderColor = "#ff0033";
				document.querySelector('#b').className = "sign-in-button-error";
				document.querySelector('#c').className = "sign-in-button-error";

				if (password.length < 8) {
					document.querySelector("#eMess").innerHTML = "<p id = \"Merror\">Please input a username of atleast 1 character as well as a password of atleast 8 characters</p>";

				} else {
					document.querySelector("#eMess").innerHTML = "<p id = \"Merror\">Please input a username of atleast 1 character</p>";
				}
			} else if (password.length < 8) {
				document.body.id = "errorScreen";
				document.querySelector("#username").style.borderColor = "#ff0033";
				document.querySelector("#password").style.borderColor = "#ff0033";
				document.querySelector("#confirmpassword").style.borderColor = "#ff0033";
				document.querySelector('#b').className = "sign-in-button-error";
				document.querySelector('#c').className = "sign-in-button-error";
				document.querySelector("#eMess").innerHTML = "<p id = \"Merror\">Please input a password of atleast 8 characters</p>";

			} else if (password != password_confirmation) {
				document.body.id = "errorScreen";
				document.querySelector("#username").style.borderColor = "#ff0033";
				document.querySelector("#password").style.borderColor = "#ff0033";
				document.querySelector("#confirmpassword").style.borderColor = "#ff0033";
				document.querySelector('#b').className = "sign-in-button-error";
				document.querySelector('#c').className = "sign-in-button-error";
				document.querySelector("#eMess").innerHTML = "<p id = \"Merror\">Please make sure the two passwords inputted match each other</p>";

			} else {
				let httpRequest = new XMLHttpRequest();
				httpRequest.open("POST", url, true);

				// We will get alerted when backend gives back some kind of response
				httpRequest.onreadystatechange = function() {
					// This function runs when we get some kind of response back from iTunes
					console.log(httpRequest);
					// When we get back a DONE state (readyState == 4, let's do something with it)
					if (httpRequest.readyState == httpRequest.DONE) {
						// Check for errors - status code 200 means success
						if (httpRequest.status == 200) {
							console.log(httpRequest.responseText);

							// Display the results on the browser - a separate function is created for this purpose
							parseResponse(httpRequest.responseText);

						} else {
							console.log("AJAX Error!!");
							console.log(httpRequest.status);
							console.log(httpRequest.statusText);
						}

					}
				}

				httpRequest.send(JSON.stringify({
					"username" : username,
					"password" : password
				}));
			}
		}
	</script>
</body>
</html>
