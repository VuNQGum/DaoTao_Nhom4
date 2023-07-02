var code = "";
var name = "";
var status = "";
var api_url = "/api/admin/lessons/";

$(document).ready(function() {
	var course_id = $('#course_id').val();
	var user_role = $('#role_id').val();
	var role_name = '';
	if(user_role == role_admin) role_name = 'administrator';
	else role_name = 'teacher';
    var table = loadData(name, code, status);
    $("#tbl-lessons_length").append('<a class="float-right btn btn-success" style="margin-bottom:5px" href="/'+role_name+'/courses/'+course_id+'/lessons/create">Thêm mới</a>');
    $(document).ajaxStart(function(){
	    $("#wait").css("display", "block");
	  });
	  $(document).ajaxComplete(function(){
	    $("#wait").css("display", "none");
	 });
    function loadData(name, code, status) {
        return $('#tbl-lessons').DataTable({
            "searching": false,
            "language": {
                "lengthMenu": "Hiện thị _MENU_",
                "zeroRecords": "Nothing found - sorry",
                "info": "Hiện thị trang _PAGE_ trong _PAGES_",
                "infoEmpty": "Không có bản ghi nào",
                "infoFiltered": "(Lọc trong _MAX_ bản ghi)",
                "search": "Tìm kiếm:",
                "loadingRecords": "Tải dữ liệu...",
                "processing": "Đang xử lý...",
                "zeroRecords": "Không tìm thấy kết quả",
                "paginate": {
                    "first": "Trang đầu",
                    "last": "Trang cuối",
                    "next": "Trang sau",
                    "previous": "Trang trước"
                },

            },
            "processing": true,
            "serverSide": true,
            "bSortable": true,
            "sort": "position",
            // Default: Page display length
            "iDisplayLength": 10,
            "iDisplayStart": 0,
            "ajax": {
                "url": api_url,
                "type": "GET",

                "data": {
                    "name": name,
                    "code": code,
                    "status": status,
                    "course_id": course_id
                }
            },
            "columnDefs": [{
                "className": "text-left",
                "targets": 0,
                "orderable": false,
            }, {
                "className": "text-left",
                "targets": 1,
                "orderable": false,
            },{
                "className": "text-left",
                "targets": 2,
                "orderable": false,
            },  {
                "className": "text-center",
                "targets": 3,
                "bSortable": false,
            }, {
                "className": "text-center",
                "targets": 4,
                "bSortable": false,
            }, ],
            "columns": [{
                "data": "name"
            }, {
                "data": "code"
            },{
                "data": "description"
            }, {
                "data": "status",
                render: function(data, type, row) {
                    if (!row.status)
                        return ' <a class="btn-status switch"  href="javascript:;" id="' + row.id + '"><p ></p><span class="slider round"></span></label> ';
                    else return ' <a class="btn-status switch"  href="javascript:;" id="' + row.id + '"><p class="checked" ></p><span class="slider round"></span></label> ';
                }
            }, {
                "data": "action",
                render: function(data, type, row) {
                	return '<a  class="btn btn-info  btn-content button_action" href="javascript:;" id="'
							+ row.id
							+ '"  title="Xem bài giảng"><i class="fa fa-eye"></i></a>  <a  class="btn btn-primary  btn-edit button_action" href="javascript:;" id="'
							+ row.id
							+ '"  title="Sửa"><i class="fa fa-pencil-square-o" aria-hidden="true"></i></a> <a id="'
							+ row.id
							+ '"  class="btn btn-danger btn-delete button_action" href="javascript:;" title="Xóa" ><i id="deleteUnit" class="fa fa-trash" aria-hidden="true"></i></a>';
                }
            }, ]

        });
    }

    $('#btn-search').on("click", function() {
        name = $('#input-name').val();
        code = $('#input-code').val();
        status = $("#status").val();
        table.destroy();
        table = loadData(name, code, status);
        $("#tbl-lessons_length").append('<a class="float-right btn btn-success" style="margin-bottom:5px" href="/administrator/courses/'+course_id+'/lessons/create">Thêm mới</a>');

    });
    $('#btn-reset').on("click", function() {
        $('#input-name').val("");
        $('#input-code').val("");
        $("#status").val("");
        table.destroy();
        table = loadData("", "", "");
        $("#tbl-lessons_length").append('<button class="float-right btn btn-success" style="margin-bottom:5px" data-toggle="modal" data-target="#modal" id="btn-modal-add">Thêm mới</button>');

    });
    $('#tbl-lessons').on("click", '.btn-delete', function() {
        var id = $(this).attr('id');
        $.confirm({
            title: 'Xác nhận!',
            content: 'Bạn muốn xóa chủ đề này?',
            buttons: {
                cancel: function() {
                    text: 'Hủy'
                },
                confirm: {
                    text: 'Xác nhận',
                    btnClass: 'btn-red',
                    keys: ['enter', 'shift'],
                    action: function() {
                        $.ajax({
                            type: "DELETE",
                            processData: false,
                            contentType: false,
                            url: api_url + id,
                            success: function(res) {
                                table.ajax.reload();
                                $.alert({
                                    title: 'Thông báo!',
                                    content: res.message
                                });
                            },
                            error: function() {
                                $.alert({
                                    title: 'Thông báo!',
                                    content: 'Error server'
                                });
                            }
                        })

                    }
                }
            }
        });
    });
    $('#tbl-lessons').on("click", '.btn-status', function() {
        var id = $(this).attr('id');
        $.confirm({
            title: 'Xác nhận!',
            content: 'Bạn muốn thay đổi trạng thái thuộc tính này?',
            buttons: {

                cancel: function() {
                    text: 'Hủy'
                },
                confirm: {
                    text: 'Xác nhận',
                    btnClass: 'btn-green',
                    keys: ['enter', 'shift'],
                    action: function() {
                        $.ajax({
                            type: "PUT",
                            processData: false,
                            contentType: false,
                            url: api_url + id + "/change-status",
                            success: function(res) {
                                table.ajax.reload();
                                $.alert({
                                    title: 'Thông báo!',
                                    content: res.message
                                });
                            },
                            error: function() {
                                $.alert({
                                    title: "Thông báo!",
                                    content: "Error server."
                                });
                            }
                        })

                    }
                }
            }
        });
    });

    $('#btn-add').click(function() {
        var status = $("input[name='status']:checked").val() < 1 ? false : true;
        var formData = new FormData();
        formData.append("name", $("#lesson-name").val());
        formData.append("code", $("#lesson-code").val());
        formData.append("status", status);
        formData.append("description", CKEDITOR.instances['description'].getData());
        formData.append("course_id", $("#course_id").val());

        formData.append("file", document.getElementById("url").files[0]);
        var link_other = $("#link_other").val();
        link_other = link_other.replace('watch?v=','embed/');
        formData.append("link_other", link_other);
        $(document).ajaxStart(function(){
		    $("#wait").css("display", "block");
		  });
		  $(document).ajaxComplete(function(){
		    $("#wait").css("display", "none");
		 });
        $.confirm({
            title: 'Xác nhận!',
            content: 'Bạn muốn thêm mới bài giảng này?',
            buttons: {

                cancel: function() {
                    text: 'Hủy'
                },
                confirm: {
                    text: 'Xác nhận',
                    btnClass: 'btn-green',
                    keys: ['enter', 'shift'],
                    action: function() {
                        $.ajax({
	                    	type: "POST",
	                        processData: false,
	                        contentType: false,
	                        url: api_url,
	                        data: formData,
                            success: function(res) {
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
                                                text: 'Quản lý bài giảng', // With spaces and symbols
                                                action: function () {
                                                	window.location.href = "/administrator/courses/"+res.element.courseOfLessonId+"/lessons"
                                                }
                                            }
                                        }
                                    });
                                	setTimeout(function(){window.location.href = "/administrator/courses/"+res.element.courseOfLessonId+"/lessons" }, 2000);

                                }

                            },
                            error: function() {
                                $.alert({
                                    title: "Thông báo!",
                                    content: "Lỗi không thể thêm mới."
                                });
                            }
                        })

                    }
                }
            }
        });
    });

    $('#tbl-lessons').on("click", '.btn-edit', function() {
        var id = $(this).attr('id');
        window.location.href = "/"+role_name+"/courses/"+course_id+"/lessons/"+id+"/edit";
    });

    $('#btn-edit').click(function() {
    	 var status = $("input[name='status']:checked").val() < 1 ? false : true;
         var formData = new FormData();
         formData.append("name", $("#lesson-name").val());
         formData.append("code", $("#lesson-code").val());
         formData.append("status", status);
         formData.append("description", CKEDITOR.instances['description'].getData());
         formData.append("course_id", $("#course_id").val());
         formData.append("file", document.getElementById("url").files[0]);
         formData.append("lesson_id", $("#lesson_id").val());
         formData.append("current_url", $("#current_url").val());
         var link_other = $("#link_other").val();
         link_other = link_other.replace('watch?v=','embed/');
         formData.append("link_other", link_other);
        $.confirm({
            title: 'Xác nhận!',
            content: 'Bạn muốn thay đổi khóa học này?',
            buttons: {
                cancel: function() {
                    text: 'Hủy'
                },
                confirm: {
                    text: 'Xác nhận',
                    btnClass: 'btn-green',
                    keys: ['enter', 'shift'],
                    action: function() {
                        $.ajax({
                        	type: "PUT",
	                        processData: false,
	                        contentType: false,
	                        url: api_url,
	                        data: formData,
	                        success: function(res) {
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
                                                text: 'Quản lý bài giảng', // With spaces and symbols
                                                action: function () {
                                                	window.location.href = "/administrator/courses/"+res.element.courseOfLessonId+"/lessons"
                                                }
                                            }
                                        }
                                    });
                                }

                            },
                            error: function() {
                                $.alert({
                                    title: "Thông báo!",
                                    content: "Server error."
                                });
                            }
                        })

                    }
                }
            }
        });
    });


    function setValue(id, name, code, status, description) {
        $('#profile-id').val(id);
        $('#profile-name').val(name);
        $('#profile-code').val(code);
        $('#profile-description').val(description);
        status ? $("#status_active").prop("checked", true) : $("#status_no_active").prop("checked", true);
    }
    $(".custom-file-input").on("change", function() {
    	  var fileName = $(this).val().split("\\").pop();
    	  $(this).siblings(".custom-file-label").addClass("selected").html(fileName);
    });
    $('#tbl-lessons').on("click", '.btn-content', function() {
        $('#modal').modal('show');

        var id = $(this).attr('id');
        $.ajax({
            type: "GET",
            processData: false,
            contentType: false,
            url: api_url + id,
            success: function(res) {
            	$('#modal').modal('show');
    			$('#view_content').attr('src', res.element.url)
            },
            error: function() {
                $.alert({
                    title: "Thông báo!",
                    content: "Lỗi không thể thêm mới."
                });
            }
        })

    });
    
    $(document).on("change", ".file_video", function(evt) {
  	  	  $("#label-video").text("Xem thử");
    	  var source = $('#video');
    	  source[0].src = URL.createObjectURL(this.files[0]);
    	  source.parent()[0].load();
    });

})