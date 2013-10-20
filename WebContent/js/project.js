$.ajaxSetup({
	// Disable caching of AJAX responses */
	// to make it works in IE
	cache : false
});



function validateLogin() {
	$.getJSON("ValidateLogin", {
		"email" : $.cookie("email"),
		"token" : $.cookie("Wachamarei")
	}).done(function(data) {
		$.each(data, function(i, item) {
			if (item.typeOfMessage == "error") {
				$.removeCookie('email');
				$.removeCookie('Wachamarei');
				return;
			} else if (item.typeOfMessage == "correct"){
				window.location = "dashboard.html";
				return;
			} 
		});
	}).fail(function(jqxhr, textStatus, error) {
		var err = textStatus + ', ' + error;
		console.log("Request Failed: " + err);
	});
}

function login() {
	if ($("#emaillogin").val() == ""
			|| $("#emaillogin").val().trim() == "") {
		return;
	}
	$.getJSON("Login", {
		"email" : $("#emaillogin").val(),
		"password" : $("#password").val()
	}).done(function(data) {
		$.each(data, function(i, item) {
			if (item.typeOfMessage == "redirect") {
				window.location = item.message;
				return;
			} 
			showMessages("#popupError", "#popmessage", item.typeOfMessage, item.message);
		});
	}).fail(function(jqxhr, textStatus, error) {
		var err = textStatus + ', ' + error;
		console.log("Request Failed: " + err);
	});
}

function register(email) {
	if ($("#emailregister").val() == ""
			|| $("#emailregister").val().trim() == "") {
		return;
	}
	$.getJSON("Register", {
		"email" : $("#emailregister").val()
	}).done(function(data) {
		$.each(data, function(i, item) {
//			showMessages("#lblResultRegister", item.typeOfMessage, item.message);
			showMessages("#popupError1", "#popmessage1", item.typeOfMessage, item.message);
		});
	}).fail(function(jqxhr, textStatus, error) {
		var err = textStatus + ', ' + error;
		console.log("Request Failed: " + err);
	});
}

function sendPassword() {
	if ($("#emailregister").val() == ""
			|| $("#emailregister").val().trim() == "") {
		return;
	}
	$.getJSON("Reminder", {
		"email" : $("#emailregister").val()
	}).done(function(data) {
		$.each(data, function(i, item) {
			alert('sent password');
			showMessages("#divResultPassword", item.typeOfMessage, item.message);
		});
	}).fail(function(jqxhr, textStatus, error) {
		var err = textStatus + ', ' + error;
		console.log("Request Failed: " + err);
	});
}

function sendReminder(from) {
	var email = $("#emailregister").val();
	
	if (from == 'registration') {
		if ($("#emailregister").val() == ""
				|| $("#emailregister").val().trim() == "") {
			return;
		}
		email = $("#emailregister").val();
	} else {
		if ($("#emailreminder").val() == ""
			|| $("#emailreminder").val().trim() == "") {
			return;
		}
	}
	
	$.getJSON("Reminder", {
		"email" : email
	}).done(function(data) {
		$.each(data, function(i, item) {
			if (from == 'registration') {
				showMessages("#lblResultRegister", item.typeOfMessage, item.message);
			} else {
				showMessages("#divResultReminder", item.typeOfMessage, item.message);
			}
		});
	}).fail(function(jqxhr, textStatus, error) {
		var err = textStatus + ', ' + error;
		console.log("Request Failed: " + err);
	});
}


$(document).ready(function() {
//	if ($.cookie('Wachamarei') != 'null' || $.cookie('Wachamarei') != null || $.cookie('Wachamarei') != ''){
		validateLogin();
//		return;
//	}
//	$("#lblResultRegister").hide();
//	$("#divResultReminder").hide();
//	$("#divResultLogin").hide();
//	$("#divResultPassword").hide();
//		$( "#btn1" ).click(function() {
//			alert('1');
//			});	
	$( "#btnClose" ).click(function() {
		closeMessage("#popupError", "#popmessage");
		});		
	$( "#btnClose1" ).click(function() {
		closeMessage("#popupError1", "#popmessage1");
		});		
	$( "#btnRegister" ).click(function() {
		register();
		});	
	$( "#btnPassword" ).click(function() {
		sendPassword();
		});	
});

function showMessages(div, lblmessage, typeOfMessage, message){
	$(div).popup("open");
	$(lblmessage).append(message);
//	$(div).empty();
//	$(div).show();
//	$(div).removeClass("error correct info");
//	$(div).addClass(typeOfMessage);
//	$(div).append(message);
//	if (typeOfMessage == "error" || typeOfMessage == "correct") {
//		$(div).delay(4000).fadeOut();
//	}
}

function closeMessage(pop, message){
	$(pop).popup("close");
	$(message).empty();
}