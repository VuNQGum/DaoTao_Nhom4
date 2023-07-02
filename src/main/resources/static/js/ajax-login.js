$(document).ready(function() {
	$.ajax({
		type : "GET",
		processData : false,
		contentType : false,
		url : "/api/users/user-login",
		success : function(res) {
			var full_name = res.fullName.split(" ");
			var dem = full_name[full_name.length - 2] != null ? full_name[full_name.length - 2] : "";
			var name = dem + " "
					+ full_name[full_name.length - 1];
			$('#name_loginer').text(res.fullName);
			$('#avatar').attr("src", res.image);
			$('#role_loginer').text(res.roleName);
		},
		error : function() {
		}
	})
});