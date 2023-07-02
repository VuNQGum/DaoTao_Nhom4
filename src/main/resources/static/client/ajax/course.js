$(function() {
	$.ajax({
		type : "GET",
		url : "/api/client/courses/list-name",
		success : function(res) {
			$("#courses_name").autocomplete({
				source : res
			});
		},
		error : function() {
		}
	});
	
	$('#btn_course_regist').click(function() {
		var formData = new FormData();
        formData.append("course_id", $('#course_id').val());
        formData.append("status", true);
        var link = $('#link').val();
        $(document).ajaxStart(function(){
		    $("#wait").css("display", "block");
		  });
		  $(document).ajaxComplete(function(){
		    $("#wait").css("display", "none");
		 });
		$.ajax({
			type : "POST",
			processData: false,
	        contentType: false,
	        data: formData,
			url : "/api/client/courses/register",
			success : function(res) {
				if(res.code == status_not_login) {
					$.confirm({
                    	title: "Thông báo!",
                        content: res.message,
                        buttons: {

                            'Next': {
                                text: 'Đăng nhập ngay',
                                btnClass: 'btn-green',
                                action: function () {
                                	window.location.href = "/login";
                                }
                            }
                        }
                    });
				}
				else if(res.code == 300) {
					window.location.href = "/test";
				}
				else {
					$.alert({
	                      title: "Thông báo!",
	                      content: "Gửi đăng ký thành công. Chờ giảng viên xác nhận."
	                  });
					if(link != '')  window.location.href = link;
//					else location.reload(true);
				}
			},
			error : function() {
				  $.alert({
                      title: "Thông báo!",
                      content: "Lỗi không thể đăng ký."
                  });
			}
		});
	});
	$('#lessons').on("click", '.learn-lesson', function() {
        var id = $(this).attr('id');
        $("#course_image").hide();
        $("#course_content").show();
		$('#view_content').attr('src', id);
		$("html, body").animate({ scrollTop: 140 }, 600);
		$('.learning').removeClass('learning');
		$(this).parent().parent().addClass('learning');


	});
});