$(document).ready(function() {
	function getCaptcha() {
		var iNumber = Math.floor(Math.random() * 10000);
		return iNumber;
	}
	$("#btnGetCaptcha").prop("disabled", true);
	var iNumber = getCaptcha();
	$("#txtNewInput").val(iNumber);
	$("#txtNewInput").prop('disabled', true);
	$("#login").click(function() {
		if ($("#textInput").val() != iNumber) {
			iNumber = getCaptcha();
			$("#txtNewInput").val(iNumber);
			$('#captcha').show();
			return false;
		}

	});

});