var currentGroup = 0;
var currentGroupName = '';
var currentUser = '';
var currentInvitation = 0;
var servlet = "GroupServlet";

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
	$("#formLogout").validate({
		  submitHandler: function(form) {
		    logout();
		  }
		});
	$("#formGroup").validate({
		  submitHandler: function(form) {
		    saveGroup();
		  }
		});	
	$("#btnBack")
    .click(function() {
          history.back();
          return false;
    });

	$("#btnBack1")
    .click(function() {
          history.back();
          return false;
    });	

	$("#btnBackChat")
    .click(function() {
          history.back();
          return false;
    });		
	
	$("#btnBackInvitation")
    .click(function() {
          history.back();
          return false;
    });		

	$("#formDelete").validate({
		  submitHandler: function(form) {
				deleteGroup();
		  }
		});
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
//			showMessages("#divResultPassword", item.typeOfMessage, item.message);
			showMessages("#popupError", "#popmessage", item.typeOfMessage, item.message);
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
//				showMessages("#divResultCancel", item.typeOfMessage, item.message);
				showMessages("#popupError", "#popmessage", item.typeOfMessage, item.message);
			});
		}).fail(function(jqxhr, textStatus, error) {
			var err = textStatus + ', ' + error;
			console.log("Request Failed: " + err);
		});
	}
}

function showMessages(div, lblmessage, typeOfMessage, message){
	$(lblmessage).empty();
	$(lblmessage).append(message);
	setTimeout(function () {
		$(div).popup("open");
    }, 100);
}

function setCurrentGroup(groupId, groupName){
	currentGroup = groupId;
	currentGroupName = groupName;
	isOwner();
	$("#gnumber").text(currentGroup);
}

function setCurrentUser(userId){
	currentUser = userId;
	$("#gnumber").text(currentUser);
}

function setCurrentInvitation(invitationId){
	currentInvitation = invitationId;
	$("#gnumber").text(currentInvitation);
}

$(document).on("pageinit", "#dashboard-page", function () {
	loadGroups();
	loadInvitations("#listInvitations");
});

$(document).on("pagebeforeshow", "#invitation-page", function () {
	loadUsers("#listUsers");
	inviteFriends();
});

$(document).on("pagebeforeshow", "#groupusers-page", function () {
	loadGroupMembers("#listGroupMembers");
});

function loadGroups() {
	$.getJSON("GroupServlet", {
		"action" : "loadGroups"
	}).done(function(data) {
		var output = '';
		$.each(data, function(i, item) {
			output += '<li><a href="#chat-page" onclick="setCurrentGroup(\'' + item.id + '\', \'' + item.name + '\'); chat();">' +item.name+ '</a>';
			//output += '<span class="ui-li-count" title="Number of Members">' +item.numberMembers+ '</span>';
			output += '<a href="#popupOptions" data-rel="popup" data-transition="pop" onclick="setCurrentGroup(\'' + item.id + '\', \'' + item.name + '\')"></a>';
			output += '</li>';
		});
		$('#listGroups').html(output);
		$('#listGroups').listview('refresh');
	}).fail(function(jqxhr, textStatus, error) {
		var err = textStatus + ', ' + error;
		console.log("Request Failed: " + err);
	});
}

function inviteFriends(){
	loadGroupsSelect("#selectlistGroup");
}


function loadGroupsSelect(control) {
	$.getJSON("GroupServlet", {
		"action" : "loadGroups"
	}).done(function(data) {
		$(control).empty();
		var select = '';
		if (currentGroup == 0){
			select = 'selected="selected"';
		}
		var output = '<option value="0" ' + select + '>Select a group</option>';
		$.each(data, function(i, item) {
			if (item.id == currentGroup){
				select = 'selected="selected"';
			} else {
				select = '';
			}
			output += '<option value="' + item.id + '" ' + select + '>' +item.name+ '</option>';
		});
		$(control).append(output);
		$(control).selectmenu('refresh');
	}).fail(function(jqxhr, textStatus, error) {
		var err = textStatus + ', ' + error;
		console.log("Request Failed: " + err);
	});
}

function loadUsers(control) {
	$.getJSON("GroupServlet", {
		"action" : "loadUsers"
	}).done(function(data) {
		var output = '';
		$.each(data, function(i, item) {
			output += '<li>';
			output += '<a href="#popConfirmInvitation" data-rel="popup" data-transition="pop" onclick="setCurrentUser(' +item.email+ ');">' +item.email+ '</a>';
			output += '</li>';
		});
		$(control).html(output);
		$(control).listview('refresh');
	}).fail(function(jqxhr, textStatus, error) {
		var err = textStatus + ', ' + error;
		console.log("Request Failed: " + err);
	});
}

function loadInvitations(control) {
	$.getJSON("GroupServlet", {
		"action" : "loadInvitations"
	}).done(function(data) {
		var output = '';
		$.each(data, function(i, item) {
			output += '<li>';
			output += ' <a href="#popupOptionsInvitations" data-rel="popup" data-transition="pop" data-position-to="#divMainContent" id="invit' + item.id + '" onclick="setCurrentInvitation(' + item.id + ')">' +item.name+ '</a>';
			output += '</li>';
		});
		$(control).html(output);
		$(control).listview('refresh');
	}).fail(function(jqxhr, textStatus, error) {
		var err = textStatus + ', ' + error;
		console.log("Request Failed: " + err);
	});
}

function acceptInvitations(){
	$.getJSON(servlet, {
		"action" : "acceptInvitation",
		"groupId" : currentInvitation
	}).done(function(data) {
		alert('invitation accepted');
	}).fail(function(jqxhr, textStatus, error) {
		var err = textStatus + ', ' + error;
		console.log("Request Failed: " + err);
	});
}

function loadGroupMembers(control) {
	$("#titleMembers").empty();
	$("#titleMembers").append("Members of " + currentGroupName);
	$.getJSON(servlet, {
		"action" : "loadGroupMembers",
		"currentGroup" : currentGroup
	}).done(function(data) {
		
		var output = '';
		for (var i = 0; i< data.length; i++) {
			output += '<li>';
			if (isOwnerVar == true) {
				output += '<a href="#popConfirmDeletion" data-rel="popup" data-transition="pop" onclick="setCurrentUser(\'' +data[i]+ '\');">' +data[i]+ '</a>';
			} else {
				output += '<a href="#" data-rel="popup" data-transition="pop" onclick="setCurrentUser(\'' +data[i]+ '\');">' +data[i]+ '</a>';
			}
			output += '</li>';
		}
		$(control).html(output);
		$(control).listview('refresh');
		 // $.mobile.activePage.trigger("refresh");
	}).fail(function(jqxhr, textStatus, error) {
		var err = textStatus + ', ' + error;
		console.log("Request Failed: " + err);
	});
}
var isOwnerVar = false;
function isOwner(){
	$.getJSON(servlet, {
		"action" : "checkOwner",
		"currentGroup" : currentGroup
	}).done(function(data) {
		$.each(data, function(i, item) {
			if (item.typeOfMessage == "correct" && item.message == "OK"){
				isOwnerVar = true;
				return true;
			} else {
				isOwnerVar = false;
				return false;
			}
		});
	}).fail(function(jqxhr, textStatus, error) {
		var err = textStatus + ', ' + error;
		console.log("Request Failed: " + err);
	});
}

function saveGroup() {
	$("#popmessagegroup").append("");
	if ($("#newgroup").val().trim() == "") {
		return;
	}
	$.getJSON(servlet, {
		"action" : "createGroup",
		"newGroup" : $("#newgroup").val()
	}).done(function(data) {
		$.each(data, function(i, item) {
			if (item.typeOfMessage == "correct") {
				$("#popupDialog").popup( "close" );
				$("#newgroup").val("");
				loadGroups();
			} else {
				//$("#popupDialog").popup( "close" );
				$("#popmessagegroup").append(item.message);
				//showMessages("#popupError", "#popmessage", item.typeOfMessage, item.message);
			}
		});
	}).fail(function(jqxhr, textStatus, error) {
		var err = textStatus + ', ' + error;
		console.log("Request Failed: " + err);
	});
}


function deleteGroup() {
	if (isOwnerVar == false){
		$("#popDelete").popup("close");
		showMessages("#popupError21", "#popmessage21", "error", "You are not the owner of the group. You cannot delete it.");
	} else {
		$.getJSON(servlet, {
			"action" : "deleteGroup",
			"currentGroup" : currentGroup
		}).done(function(data) {
			$.each(data, function(i, item) {
				if (item.typeOfMessage == "correct") {
					//$("#popupDialog").popup( "close" );
					$("#popDelete").popup("close");
					loadGroups();
				} else {
					//$("#popmessagegroup").append(item.message);
					$("#popDelete").popup("close");
					showMessages("#popupError21", "#popmessage21", item.typeOfMessage, item.message);
				}
			});
		}).fail(function(jqxhr, textStatus, error) {
			var err = textStatus + ', ' + error;
			console.log("Request Failed: " + err);
		});
	}
}

function validateOwnerGroup(message) {
	if (isOwnerVar == false){
		$("#popupOptions").popup("close");
		$("#popDelete").popup("close");
		showMessages("#popupError21", "#popmessage21", "error", message);
		return false;
	} else {
		return true;
	}
}

function validateOwnerGroupInvitation(message) {
	if (isOwnerVar == false){
		//history.back();
		setTimeout(function () {
			$("#popupOptions").popup("close");
		}, 100);
		showMessages("#popupError21", "#popmessage21", "error", message);
		return false;
	} else {
		//alert(isOwnerVar);
//		setTimeout(function () {
//			$("#popupOptions").popup("close");
//	    }, 100);
		//setTimeout(function () {
			$("#invitation-page").panel("open");
	    //}, 100);
		return true;
	}
}

////////////////////////////////////
//chat functions
///////////////////////////////////

function changeGroupChat(object){
	currentGroupName = $(object).find(":selected").text();
	currentGroup = $(object).find(":selected").val();
	$("#content").html("");
//	alert(currentGroupName + currentGroup);
}

function chat(){
	loadGroupsSelect("#selectlistGroupChat");
	loadChat();
}

function loadChat() {
	$.getJSON("Chat", {
		"currentGroup" : currentGroup,
		"user" : $("#username").val(),
		"message" : $("#message").val(),
		"lastmessage" : $("#lastmessage").val()
	}).done(
			function(data) {
				$.each(data, function(i, item) {
					var msgp = "<p class=\"triangle-border\">" + item.message
							+ "</p> ";
					var msgs = "<span class=\"date\">" + item.timestamp
							+ " -- " + item.username + "</span>";
					$("#content").html($("#content").html() + msgp + msgs);
					$("#lastmessage").val(item.id);
				});
			}).fail(function(jqxhr, textStatus, error) {
		var err = textStatus + ', ' + error;
		console.log("Request Failed: " + err);
	});
}