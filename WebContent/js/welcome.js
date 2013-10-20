$.ajaxSetup({
	// Disable caching of AJAX responses */
	// to make it works in IE
	cache : false
});

$(document).ready(function() {
	if ($.cookie('Wachamarei') == 'null' || $.cookie('Wachamarei') == null || $.cookie('Wachamarei') == ''){
		validateLogin();
		return;
	}
	$("#divResultLogout").hide();
	$("#divResultPassword").hide();
	$("#divResultCancel").hide();
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
				window.location = "index.html";
				return;
			} 
		});
	}).fail(function(jqxhr, textStatus, error) {
		var err = textStatus + ', ' + error;
		console.log("Request Failed: " + err);
	});
}

function logout() {
	$.getJSON("Logout", {
		"email" : $.cookie("email"),
		"token" : $.cookie("Wachamarei")
	}).done(function(data) {
		$.each(data, function(i, item) {
			if (item.typeOfMessage == "redirect") {
				$.removeCookie('email');
				$.removeCookie('Wachamarei');
				window.location = item.message;
				return;
			} 
			showMessages("#divResultLogout", item.typeOfMessage, item.message);
		});
	}).fail(function(jqxhr, textStatus, error) {
		var err = textStatus + ', ' + error;
		console.log("Request Failed: " + err);
	});
}

function resetPassword() {
	$.getJSON("Reset", {
		"email" : $.cookie("email"),
		"token" : $.cookie("Wachamarei")
	}).done(function(data) {
		$.each(data, function(i, item) {
			showMessages("#divResultPassword", item.typeOfMessage, item.message);
		});
	}).fail(function(jqxhr, textStatus, error) {
		var err = textStatus + ', ' + error;
		console.log("Request Failed: " + err);
	});
}

function deleteRegistration() {
	if (confirm('You will not able to enter this site again. \nAre you sure you want to cancel your registration?')) {
		$.getJSON("Delete", {
			"email" : $.cookie("email")
		}).done(function(data) {
			$.each(data, function(i, item) {
				if (item.typeOfMessage == "redirect") {
					$.removeCookie('email');
					$.removeCookie('Wachamarei');
					window.location = item.message;
					return;
				}
				showMessages("#divResultCancel", item.typeOfMessage, item.message);
			});
		}).fail(function(jqxhr, textStatus, error) {
			var err = textStatus + ', ' + error;
			console.log("Request Failed: " + err);
		});
	}
}

function showMessages(div, typeOfMessage, message){
	$(div).empty();
	$(div).show();
	$(div).removeClass("error correct info");
	$(div).addClass(typeOfMessage);
	$(div).append(message);
	if (typeOfMessage == "error" || typeOfMessage == "correct") {
		$(div).delay(4000).fadeOut();
	}
}