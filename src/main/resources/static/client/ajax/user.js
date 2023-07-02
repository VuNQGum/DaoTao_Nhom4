$(document).ready(function() {
	var api_url = "/api/users";
	var imageDefault = "/images/no-image.jpg";
	function preview(input) {
	    if (input.files && input.files[0]) {
	        var reader = new FileReader();

	        reader.onload = function(e) {
	            $('#image-preview').attr('src', e.target.result);
	        }

	        reader.readAsDataURL(input.files[0]);
	    }
	}

	$("#image").change(function() {
	    preview(this);
	});
	$('input[name="role_id"]').change(function() {
		var role_id = $('[name="role_id"]:radio:checked').val();
		if (role_id == role_student) {
			$('#profile').show();
		}
		if (role_id == role_teacher) {
			$('#profile').hide();
		}
	});
	$('#form-register').on("submit", function(e) {
		e.preventDefault();
		var form = $("#form-register").serializeArray();
		var formData = new FormData();
        $.each(form, function() {
        	if(formData.has(this.name)) {
        		var data = formData.get(this.name);
        		data = data + "," +this.value;
        		formData.set(this.name, data);
        	}
        	else {
            	formData.append(this.name, this.value)
        	}
        });
        formData.append("image", document.getElementById("image").files[0]);
        $(document).ajaxStart(function(){
		    $("#wait").css("display", "block");
		  });
		  $(document).ajaxComplete(function(){
		    $("#wait").css("display", "none");
		 });
		$.ajax({
			type : "POST",
		    encoding:"UTF-8",
			processData : false,
			contentType : false,
			url : api_url,
			data : formData,
			success : function(res) {
				if (res.code == 403) {
                    $.alert({
                        title: 'Thông báo!',
                        content: res.message
                    });
                } else {
                	$.confirm({
                    	title: "Thông báo!",
                                content: res.message,
                                buttons: {
   
                                    'Next': {
                                text: 'Đăng nhập ngay', // With spaces and symbols
                                action: function () {
                                	window.location.href = "/login";
                                }
                            }
                        }
                    });
                	setTimeout(function(){ window.location.href = "/login" }, 2000);
                }
			},
			error : function() {
				$.alert({
					title : 'Thông báo!',
					content : 'Error server'
				});
			}
		})
	});
	
	$('#btn-edit-password').click(function(e) {
		var formData = new FormData();
		formData.append('current_password', $('#current_password').val());
		formData.append('password', $('#password').val());
		formData.append('repassword', $('#repassword').val());
		$(document).ajaxStart(function(){
		    $("#wait").css("display", "block");
		  });
		  $(document).ajaxComplete(function(){
		    $("#wait").css("display", "none");
		 });
		$.ajax({
			type : "PUT",
			processData : false,
			contentType : false,
			url : api_url+'/change-password',
			data : formData,
			success : function(res) {
				if (res.code == 403) {
                    $.alert({
                        title: 'Thông báo!',
                        content: res.message
                    });
                } else {
                	$.confirm({
                    	title: "Thông báo!",
                                content: res.message,
                                buttons: {
   
                                    'Next': {
                                text: 'Đăng nhập lại', // With spaces and symbols
                                action: function () {
                                	window.location.href = "/logout";
                                }
                            }
                        }
                    });
                	setTimeout(function(){ window.location.href = "/logout" }, 2000);
                }
			},
			error : function() {
				$.alert({
					title : 'Thông báo!',
					content : 'Error server'
				});
			}
		})

	});
	
	$('#form-edit').on("submit", function(e) {
		e.preventDefault();
        e.preventDefault();
		var form = $("#form-edit").serializeArray();
		var formData = new FormData();
        $.each(form, function() {
        	if(formData.has(this.name)) {
        		var data = formData.get(this.name);
        		data = data + "," +this.value;
        		formData.set(this.name, data);
        	}
        	else {
            	formData.append(this.name, this.value)
        	}
        });
        formData.append("image", document.getElementById("image").files[0]);
        $(document).ajaxStart(function(){
		    $("#wait").css("display", "block");
		  });
		  $(document).ajaxComplete(function(){
		    $("#wait").css("display", "none");
		 });
		$.ajax({
			type : "PUT",
			processData : false,
			contentType : false,
			url : api_url,
			data : formData,
			success : function(res) {
				if (res.code == 403) {
	                $.alert({
	                    title: 'Thông báo!',
	                    content: res.message
	                });
	            } else {
	            	$.confirm({
	                	title: "Thông báo!",
	                    content: res.message,
	                    buttons: {
	                    	'OK': {
	                            text: 'OK', // With spaces and symbols
	                            action: function () {
	                            	window.location.href = "/users";
	                            }
	                        },
	                        'Next': {
	                            text: 'Về trang chủ.', // With spaces and symbols
	                            action: function () {
	                            	window.location.href = "/";
	                            }
	                        }
	                    }
	                });
	            }
			},
			error : function() {
				$.alert({
					title : 'Thông báo!',
					content : 'Error server'
				});
			}
		})
	});
	$('#btn-reset-password').click(function(e) {
		var formData = new FormData();
		formData.append('email', $('#email_send_new_password').val());
		$(document).ajaxStart(function(){
			$('#forgot_password').modal('toggle');
		    $("#wait").css("display", "block");
		  });
		  $(document).ajaxComplete(function(){
		    $("#wait").css("display", "none");
		 });
		$.ajax({
			type : "PUT",
			processData : false,
			contentType : false,
			url : api_url+'/forgot-password',
			data : formData,
			success : function(res) {
				if (res.code == 403) {
	                $.alert({
	                    title: 'Thông báo!',
	                    content: res.message
	                });
	            } else {
	            	$.confirm({
	                	title: "Thông báo!",
	                                content: res.message,
	                                buttons: {
	   
	                                    'Next': {
	                            text: 'Đăng nhập lại', 
	                            action: function () {
	                            	window.location.href = "/login";
	                            }
	                        }
	                    }
	                });
	            	setTimeout(function(){ window.location.href = "/login" }, 2000);
	            }
			},
			error : function() {
				$.alert({
					title : 'Thông báo!',
					content : 'Error server'
				});
			}
		})
	});
	$('.select').select2({
		placeholder: 'Lựa chọn giá trị'
	});
	$('#btn-reset').click(function() {
		window.location.href = "/users" ;
	})
});