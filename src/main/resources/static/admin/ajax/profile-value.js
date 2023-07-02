var code = "";
var name = "";
var status = "";
var api_url = "/api/admin/profile/values/";
$(document).ready(function() {
    var profile_id = $('#profile-id').val();
    var table = loadData(name, code, status);
    $("#tbl-profile-values_length").append('<button class="float-right btn btn-success" style="margin-bottom:5px" data-toggle="modal" data-target="#modal" id="btn-modal-add">Thêm mới</button>');

    function loadData(name, code, status) {
        return $('#tbl-profile-values').DataTable({
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
                    "profile_id": profile_id
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
            }, {
                "className": "text-center",
                "targets": 4,
                "bSortable": false,
            }, ],
            "columns": [{
                "data": "name"
            }, {
                "data": "code"
            }, {
                "data": "status",
                render: function(data, type, row) {
                    if (!row.status)
                        return ' <a class="btn-status switch"  href="javascript:;" id="' + row.id + '"><p ></p><span class="slider round"></span></label> ';
                    else return ' <a class="btn-status switch"  href="javascript:;" id="' + row.id + '"><p class="checked" ></p><span class="slider round"></span></label> ';
                }
            },
            {
                "data": "value"
            }, {
                "data": "action",
                render: function(data, type, row) {
                    return ' <a  class="btn btn-primary  btn-edit button_action" href="javascript:;" id="' + row.id + '"  title="Sửa"><i class="fa fa-pencil-square-o" aria-hidden="true"></i></a> <a id="' + row.id + '"  class="btn btn-danger btn-delete button_action" href="javascript:;" title="Xóa" ><i id="deleteUnit" class="fa fa-trash" aria-hidden="true"></i></a>';
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
        $("#tbl-profile-values_length").append('<button class="float-right btn btn-success" style="margin-bottom:5px" data-toggle="modal" data-target="#modal" id="btn-modal-add">Thêm mới</button>');

    });
    $('#btn-reset').on("click", function() {
        $('#input-name').val("");
        $('#input-code').val("");
        $("#status").val("");
        table.destroy();
        table = loadData("", "", "");
        $("#tbl-profile-values_length").append('<button class="float-right btn btn-success" style="margin-bottom:5px" data-toggle="modal" data-target="#modal" id="btn-modal-add">Thêm mới</button>');

    });
    $('#tbl-profile-values').on("click", '.btn-delete', function() {
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
    $('#tbl-profile-values').on("click", '.btn-status', function() {
        var id = $(this).attr('id');
        $.confirm({
            title: 'Xác nhận!',
            content: 'Bạn muốn thay đổi trạng thái chủ đề này?',
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
        setValue("", "", "", true, "", "", "");
    });

    $('#btn-add').click(function() {
        var name = $("#profile-value-name").val();
        var code = $("#profile-value-code").val();
        var status = $("input[name='profile-value-status']:checked").val() < 1 ? false : true;
        var value = $("#profile-value-value").val();
        var nameDisplayStudentSide = $("#name_display_student_side").val();
        var nameDisplayTeacherSide = $("#name_display_teacher_side").val();
        var data = {'name':name, 'code': code, 'status': status, 'value': value, 'profileId': profile_id, 'nameDisplayStudentSide': nameDisplayStudentSide, 'nameDisplayTeacherSide': nameDisplayTeacherSide  };
        $.confirm({
            title: 'Xác nhận!',
            content: 'Bạn muốn thêm mới giá trị này?',
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
                                	$('#modal').modal('toggle');
                                    var code = res.element.code;
                                    var status = res.element.status ? 1 : 0;
                                    var name = res.element.name;
                                    $('#input-name').val(name);
                                    $('#input-code').val(code);
                                    $('#status').val(status);
                                    table.destroy();
                                    table = loadData(name, code, status);
                                    $("#tbl-profile-values_length").append('<button class="float-right btn btn-success" style="margin-bottom:5px" data-toggle="modal" data-target="#modal" id="btn-modal-add">Thêm mới</button>');
                                    setValue("", "", "", true, "");
                                    $.alert({
                                        title: "Thông báo!",
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
        $('#modal-title').text('Thêm mới giá trị');
        setValue("", "", "", true, "");
        $("#button-add").show();
        $("#button-edit").hide();
    });
    $('#tbl-profile-values').on("click", '.btn-edit', function() {
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
                $('#modal-title').text('Thay đổi chủ đề');
                $('#profile-value-name').val(res.element.name);
                $('#profile-value-code').val(res.element.code);
                $('#profile-value-value').val(res.element.value);
                $('#profile-value-id').val(res.element.id);
                res.element.status ? $("#status_active").prop("checked", true) : $("#status_no_active").prop("checked", true);
                $("#name_display_student_side").val(res.element.nameDisplayStudentSide);
                $("#name_display_teacher_side").val(res.element.nameDisplayTeacherSide);
                $('#btn-reset-modal-edit').click(function() {
                    setValue(res.element.id, res.element.name, res.element.code, true, res.element.value, res.element.nameDisplayStudentSide, res.element.nameDisplayTeacherSide);
                });
            },
            error: function() {
                $.alert({
                    title: "Thông báo!",
                    content: "Server error."
                });
            }
        })

    });

    $('#btn-edit').click(function() {
        var name = $("#profile-value-name").val();
        var code = $("#profile-value-code").val();
        var status = $("input[name='profile-value-status']:checked").val() < 1 ? false : true;
        var value = $("#profile-value-value").val();
        var id = $("#profile-value-id").val();
        var nameDisplayStudentSide = $("#name_display_student_side").val();
        var nameDisplayTeacherSide = $("#name_display_teacher_side").val();
        var data = {'id': id, 'name':name, 'code': code, 'status': status, 'value': value, 'profileId': profile_id, 'nameDisplayStudentSide': nameDisplayStudentSide, 'nameDisplayTeacherSide': nameDisplayTeacherSide  };
        $.confirm({
            title: 'Xác nhận!',
            content: 'Bạn muốn thay đổi chủ đề này?',
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
                                    $('#modal').modal('toggle');
                                    var code = res.element.code;
                                    var status = res.element.status ? 1 : 0;
                                    var name = res.element.name;
                                    $('#input-name').val(name);
                                    $('#input-code').val(code);
                                    $('#status').val(status);
                                    table.destroy();
                                    table = loadData(name, code, status);
                                    $("#tbl-profile-values_length").append('<button class="float-right btn btn-success" style="margin-bottom:5px" data-toggle="modal" data-target="#modal" id="btn-modal-add">Thêm mới</button>');
                                    $.alert({
                                        title: "Thông báo!",
                                        content: res.message
                                    });
                                }

                            },
                            error: function() {
                                $.alert({
                                    title: "Thông báo!",
                                    content: "Lỗi không thể thay đổi."
                                });
                            }
                        })

                    }
                }
            }
        });
    });


    function setValue(id, name, code, status, value, nameDisplayStudentSide, nameDisplayTeacherSide) {
        $('#profile-value-id').val(id);
        $('#profile-value-name').val(name);
        $('#profile-value-code').val(code);
        $('#profile-value-value').val(value);
        status ? $("#status_active").prop("checked", true) : $("#status_no_active").prop("checked", true);
        $("#name_display_student_side").val(nameDisplayStudentSide);
        $("#name_display_teacher_side").val(nameDisplayTeacherSide);
    }
})