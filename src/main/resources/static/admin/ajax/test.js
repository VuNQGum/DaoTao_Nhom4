var code = "";
var question = "";
var status = "";
var api_url = "/api/admin/test/";
$(document).ready(function() {
	CKEDITOR.replace( 'question', {
	    filebrowserUploadUrl: 'http://localhost:8080/api/client/postupload-image'
	});
    var table = loadData(question, status);
    $("#tbl-profiles_length").append('<button class="float-right btn btn-success" style="margin-bottom:5px" data-toggle="modal" data-target="#modal" id="btn-modal-add">Thêm mới</button>');

    function loadData(question, status) {
        return $('#tbl-profiles').DataTable({
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
                    "question": question,
                    "status": status
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
                "className": "text-left",
                "targets": 2,
                "bSortable": false,
            },
            {
                "className": "text-left",
                "targets": 3,
                "bSortable": false,
            },
            {
                "className": "text-left",
                "targets": 4,
                "bSortable": false,
            },
            {
                "className": "text-center",
                "targets": 5,
                "bSortable": false,
            }, {
                "className": "text-center",
                "targets": 6,
                "bSortable": false,
            }, {
                "className": "text-center",
                "targets": 7,
                "bSortable": false,
            }  ],
            "columns": [{
                "data": "question"
            }, {
                "data": "answerA"
            },  {
                "data": "answerB"
            },
            {
                "data": "answerC"
            },
            {
                "data": "answerD"
            },{
                "data": "answerTrue"
              
            },{
                "data": "status",
                render: function(data, type, row) {
                    if (!row.status)
                        return ' <a class="btn-status switch"  href="javascript:;" id="' + row.id + '"><p ></p><span class="slider round"></span></label> ';
                    else return ' <a class="btn-status switch"  href="javascript:;" id="' + row.id + '"><p class="checked" ></p><span class="slider round"></span></label> ';
                }
            }, {
                "data": "action",
                render: function(data, type, row) {
                	return '<a  class="btn btn-info  btn-values button_action" href="javascript:;" id="'
							+ row.id
							+ '"  title="Xem danh sách giá trị"><i class="fa fa-info-circle"></i></a>  <a  class="btn btn-primary  btn-edit button_action" href="javascript:;" id="'
							+ row.id
							+ '"  title="Sửa"><i class="fa fa-pencil-square-o" aria-hidden="true"></i></a> <a id="'
							+ row.id
							+ '"  class="btn btn-danger btn-delete button_action" href="javascript:;" title="Xóa" ><i id="deleteUnit" class="fa fa-trash" aria-hidden="true"></i></a>';
                }
            }, ]

        });
    }

    $('#btn-search').on("click", function() {
    	question = $('#input-name').val();
     
        status = $("#status").val();
        table.destroy();
        table = loadData(question, status);
        $("#tbl-profiles_length").append('<button class="float-right btn btn-success" style="margin-bottom:5px" data-toggle="modal" data-target="#modal" id="btn-modal-add">Thêm mới</button>');

    });
    $('#btn-reset').on("click", function() {
        $('#input-name').val("");
        $("#status").val("");
        table.destroy();
        table = loadData("", "");
        $("#tbl-profiles_length").append('<button class="float-right btn btn-success" style="margin-bottom:5px" data-toggle="modal" data-target="#modal" id="btn-modal-add">Thêm mới</button>');

    });
    $('#tbl-profiles').on("click", '.btn-delete', function() {
        var id = $(this).attr('id');
        $.confirm({
            title: 'Xác nhận!',
            content: 'Bạn muốn xóa câu hỏi này?',
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
    $('#tbl-profiles').on("click", '.btn-status', function() {
        var id = $(this).attr('id');
        $.confirm({
            title: 'Xác nhận!',
            content: 'Bạn muốn thay đổi độ ưu tiên của câu hỏi này?',
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
        setValue("", "", "", true, "", "", "", false, "");
    });

    $('#btn-add').click(function() {
        var question = CKEDITOR.instances['question'].getData();
        var answerA = $("#answer_A").val();
        var answerB = $("#answer_B").val();
        var answerC= $("#answer_C").val();
        var answerD = $("#answer_D").val();
        var answerTrue = $("#answer_true").val();

        var data = {'question':question, 'answerA':answerA, 'answerB': answerB, 'answerC': answerC, 'answerD': answerD, 'answerTrue': answerTrue};
        $.confirm({
            title: 'Xác nhận!',
            content: 'Bạn muốn thêm mới câu hỏi này tính này?',
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
                                if (res.code == 403) {
                                    $.alert({
                                        title: 'Thông báo!',
                                        content: res.message
                                    });
                                } else {
                                	 table.ajax.reload();
                                	 $('#modal').modal('hide');
                                	  $.alert({
                                          title: 'Thông báo!',
                                          content: res.message
                                      });

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

    $('#btn-modal-add').click(function() {
        $('#modal-title').text('Thêm mới câu hỏi');
//        setValue("", "", "", true, "");
//        setValue("", "", "", true, "", "", "", false, "" )
        $("#button-add").show();
        $("#button-edit").hide();
    });
    $('#tbl-profiles').on("click", '.btn-edit', function() {
        $("#button-add").hide();
        $("#button-edit").show();
        var id = $(this).attr('id');
        $.ajax({
            type: "GET",
            processData: false,
            contentType: false,
            url: api_url + id,
            success: function(res) {
                $('#modal').modal('show');
                $('#modal-title').text('Thay đổi câu hỏi');
                CKEDITOR.instances['question'].setData(res.element.question);
                $('#test-id').val(res.element.id);
                $('#answer_A').val(res.element.answerA);
                $('#answer_B').val(res.element.answerB);
                $('#answer_C').val(res.element.answerC);
                $('#answer_D').val(res.element.answerD);
                res.element.status ? $("#status_active").prop("checked", true) : $("#status_no_active").prop("checked", true);
                $("#answer_true").val(res.element.answerTrue);
                $('#btn-reset-modal-edit').click(function() {
                    setValue(res.element.id, res.element.name, res.element.code, true, res.element.description, res.element.nameDisplayStudentSide, res.element.nameDisplayTeacherSide,  res.element.isMultiple, res.element.weight);
                });
            },
            error: function() {
                $.alert({
                    title: "Thông báo!",
                    content: "Server error !!!."
                });
            }
        })

    });

    $('#btn-edit').click(function() {
        var id = $("#test-id").val();
        var question = CKEDITOR.instances['question'].getData();
        var answerA = $("#answer_A").val();
        var answerB = $("#answer_B").val();
        var answerC= $("#answer_C").val();
        var answerD = $("#answer_D").val();
        var answerTrue = $("#answer_true").val();
        var data = {'id':id, 'question':question, 'answerA':answerA, 'answerB': answerB, 'answerC': answerC, 'answerD': answerD, 'answerTrue': answerTrue};
        $.confirm({
            title: 'Xác nhận!',
            content: 'Bạn muốn thay đổi câu hỏi này?',
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
                            contentType: 'application/json; charset=utf-8',
                            url: api_url,
                            data:  JSON.stringify(data),
                            success: function(res) {
                            	if (res.code == 403) {
                                    $.alert({
                                        title: 'Thông báo!',
                                        content: res.message
                                    });
                                } else {
                                	 table.ajax.reload();
                                	 $('#modal').modal('hide');
                                	  $.alert({
                                          title: 'Thông báo!',
                                          content: res.message
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


    function setValue(id, name, code, status, description, nameDisplayStudentSide, nameDisplayTeacherSide, isMultiple, weight ) {
        $('#profile-id').val(id);
        $('#profile-name').val(name);
        $('#profile-code').val(code);
        $('#profile-weight').val(weight);
        $('#profile-description').val(description);
        status ? $("#status_active").prop("checked", true) : $("#status_no_active").prop("checked", true);
        $("#name_display_student_side").val(nameDisplayStudentSide);
        $("#name_display_teacher_side").val(nameDisplayTeacherSide);
        isMultiple ? $("#is_multiple").prop("checked", true) : $("#not_multiple").prop("checked", true);
    }
})