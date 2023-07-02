var email = "";
var name = "";
var status = "";
var course_id = $('#course_id').val();
var api_url = "/api/admin/class/";
$(document).ready(function() {
    var table = loadData(name, email, status);
    $("#tbl-class_length").append('<button class="float-right btn btn-success" style="margin-bottom:5px" data-toggle="modal" data-target="#modal" id="btn-modal-add">Thêm sinh viên</button>');

    function loadData(name, email, status) {
        return $('#tbl-class').DataTable({
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
                    "email": email,
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
            }, {
                "className": "text-center",
                "targets": 2,
                "bSortable": false,
            }, 
            {
                "className": "text-center",
                "targets": 3,
                "bSortable": false,
            }],
            "columns": [{
                "data": "studentDto.fullName"
            }, {
                "data": "studentDto.email"
            }, {
                "data": "status",
                render: function(data, type, row) {
                    if (row.status)
                        return ' <span style="padding:0.5em" class="badge badge-success"  href="javascript:;" id="' + row.id + '">Xác nhận</span> ';
                    else return ' <span style="padding:0.5em" 	class="badge badge-warning"  href="javascript:;" id="' + row.id + '">Chờ xác nhận</span> ';
                }
            }, {
                "data": "action",
                render: function(data, type, row) {
                	var btn_confirm = '';
                	if(!row.status)
                		btn_confirm = '<a  class="btn btn-primary  btn-confirm button_action" href="javascript:;" id="'
							+ row.id
							+ '"  title="Xác nhận tham gia"><i class="fa fa-check-circle"></i></a>';
                	return btn_confirm+'  <a id="'
							+ row.id
							+ '"  class="btn btn-danger btn-delete button_action" href="javascript:;" title="Xóa" ><i id="deleteUnit" class="fa fa-trash" aria-hidden="true"></i></a>';
                }
            }, ]

        });
    }

    $('#btn-search').on("click", function() {
        name = $('#input-name').val();
        email = $('#input-email').val();
        status = $("#status").val();
        table.destroy();
        table = loadData(name, email, status);
        $("#tbl-class_length").append('<button class="float-right btn btn-success" style="margin-bottom:5px" data-toggle="modal" data-target="#modal" id="btn-modal-add">Thêm sinh viên</button>');

    });
    $('#btn-reset').on("click", function() {
        $('#input-name').val("");
        $('#input-email').val("");
        $("#status").val("");
        table.destroy();
        table = loadData("", "", "");
        $("#tbl-class_length").append('<button class="float-right btn btn-success" style="margin-bottom:5px" data-toggle="modal" data-target="#modal" id="btn-modal-add">Thêm sinh viên</button>');

    });
    $('#tbl-class').on("click", '.btn-delete', function() {
        var id = $(this).attr('id');
        $.confirm({
            title: 'Xác nhận!',
            content: 'Bạn muốn xóa sinh viên này khỏi khóa học?',
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
    $('#tbl-class').on("click", '.btn-confirm', function() {
        var id = $(this).attr('id');
        $(document).ajaxStart(function(){
		    $("#wait").css("display", "block");
		  });
		  $(document).ajaxComplete(function(){
		    $("#wait").css("display", "none");
		 });
        $.confirm({
            title: 'Xác nhận!',
            content: 'Bạn muốn thêm sinh viên này vào khóa học?',
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
 
    $('#btn-reset-modal-add').click(function() {
        setValue("", "", "", true, "");
    });

    $('#btn-add').click(function() {
        var name = $("#profile-name").val();
        var email = $("#profile-email").val();
        var status = $("input[name='profile-status']:checked").val() < 1 ? false : true;
        var description = $("#profile-description").val();
        var data = {'name':name, 'email': email, 'status': status, 'description': description};
        $.confirm({
            title: 'Xác nhận!',
            content: 'Bạn muốn Thêm sinh viên thuộc tính này?',
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
                            contentType: 'application/json; charset=utf-8',
                            url: api_url,
                            data:  JSON.stringify(data),
                            success: function(res) {
                                if (res.email == 403) {
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
                                                text: 'Thêm giá trị', // With spaces and symbols
                                                action: function () {
                                                	window.location.href = "/admin/profiles/"+res.element.id+"/values"
                                                }
                                            }
                                        }
                                    });
                                	setTimeout(function(){ window.location.href = "/admin/profiles/"+res.element.id+"/values" }, 2000);

                                }

                            },
                            error: function() {
                                $.alert({
                                    title: "Thông báo!",
                                    content: "Lỗi không thể Thêm sinh viên."
                                });
                            }
                        })

                    }
                }
            }
        });
    });

    $('#btn-modal-add').click(function() {
        $('#modal-title').text('Thêm sinh viên vào khóa học ');
        setValue("", "", "", true, "");
        $("#button-add").show();
        $("#button-edit").hide();
    });
   
    $('#btn-add-student').click(function() {
        var list_student_id = $('#email_student').val();
        var formData = new FormData();
        formData.append("list_student_id", list_student_id);
        formData.append("course_id", course_id);
        $(document).ajaxStart(function(){
		    $("#wait").css("display", "block");
		  });
		  $(document).ajaxComplete(function(){
		    $("#wait").css("display", "none");
		 });
		$.confirm({
            title: 'Xác nhận!',
            content: 'Bạn muốn thêm sinh viên vào khóa học này?',
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
                            data:  formData,
                            success: function(res) {
                                if (res.code == 400) {
                                    $.alert({
                                        title: 'Thông báo!',
                                        content: res.message
                                    });
                                } else {
                                    $('#modal').modal('toggle');
                                    $('#email_student').val('');
                                    table.destroy();
                                    table = loadData(name, email, status);
                                    $("#tbl-class_length").append('<button class="float-right btn btn-success" style="margin-bottom:5px" data-toggle="modal" data-target="#modal" id="btn-modal-add">Thêm sinh viên</button>');

                                    $.alert({
                                        title: "Thông báo!",
                                        content: res.message,
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


    function setValue(id, name, email, status) {
        $('#profile-id').val(id);
        $('#profile-name').val(name);
        $('#profile-email').val(email);
        status ? $("#status_active").prop("checked", true) : $("#status_no_active").prop("checked", true);
    }

    $('.select').select2({
		placeholder: 'Thêm email sinh viên'
	});

})