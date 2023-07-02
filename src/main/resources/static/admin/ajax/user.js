var email = "";
var name = "";
var status = "";
var role = $('#table_role').val();
var imageDefault = "/images/no-image.jpg";
var api_url = "/api/admin/users/";
function preview(input) {
    if (input.files && input.files[0]) {
        var reader = new FileReader();

        reader.onload = function(e) {
            $('#image-preview').attr('src', e.target.result);
        }

        reader.readAsDataURL(input.files[0]);
    }
}

$("#category-image").change(function() {
    preview(this);
});
$(document).ready(function() {
    var table = loadData(name, email, status, role);
    $("#tbl-users_length").append('<button class="float-right btn btn-success" style="margin-bottom:5px" data-toggle="modal" data-target="#modal" id="btn-modal-add">Thêm mới</button>');

    function loadData(name, email, status, role) {
        return $('#tbl-users').DataTable({
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
                    "role_id": role
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
            }, {
                "className": "text-center",
                "targets": 3,
                "bSortable": false,
            }, {
                "className": "text-center",
                "targets": 4,
                "bSortable": false,
            }, {
                "className": "text-center",
                "targets": 5,
                "bSortable": false,
            }],
            "columns": [{
                "data": "fullName"
            }, {
                "data": "email"
            },{
                "data": "birthday"
            },{
                "data": "phone"
            }, {
                "data": "status",
                render: function(data, type, row) {
                    if (!row.status)
                        return ' <a class="btn-status switch"  href="javascript:;" id="' + row.id + '"><p ></p><span class="slider round"></span></label> ';
                    else return ' <a class="btn-status switch"  href="javascript:;" id="' + row.id + '"><p class="checked" ></p><span class="slider round"></span></label> ';
                }
            },{
                "data": "action",
                render: function(data, type, row) {
                    return '<a id="' + row.id + '"  class="btn btn-danger btn-delete button_action" href="javascript:;" title="Xóa" ><i id="deleteUnit" class="fa fa-trash" aria-hidden="true"></i></a>';
                }
            }, ]

        });
    }

    $('#btn-search').on("click", function() {
        name = $('#input-name').val();
        email = $('#input-email').val();
        status = $("#status").val();
        table.destroy();
        table = loadData(name, email, status, role);
        $("#tbl-users_length").append('<button class="float-right btn btn-success" style="margin-bottom:5px" data-toggle="modal" data-target="#modal" id="btn-modal-add">Thêm mới</button>');

    });
    $('#btn-reset').on("click", function() {
        $('#input-name').val("");
        $('#input-email').val("");
        $("#status").val("");
        table.destroy();
        table = loadData("", "", "", role);
        $("#tbl-users_length").append('<button class="float-right btn btn-success" style="margin-bottom:5px" data-toggle="modal" data-target="#modal" id="btn-modal-add">Thêm mới</button>');

    });
    $('#tbl-users').on("click", '.btn-delete', function() {
        var id = $(this).attr('id');
        $.confirm({
            title: 'Xác nhận!',
            content: 'Bạn muốn xóa tài khoản này?',
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
    $('#tbl-users').on("click", '.btn-status', function() {
        var id = $(this).attr('id');
        $.confirm({
            title: 'Xác nhận!',
            content: 'Bạn muốn thay đổi trạng thái tài khoản này?',
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
        setValue("", "", "", true, 3);
    });

    $('#btn-add').click(function() {
    	 var name = $("#user-name").val();
         var email = $("#user-email").val();
         var status = $("input[name='user-status']:checked").val() < 1 ? false : true;
         var categoryId = $("#subjectId").val();
         var data = {'fullName':name, 'email': email, 'status': status, 'roleId': role, 'subjectId': categoryId};
         $.confirm({
             title: 'Xác nhận!',
             content: 'Bạn muốn thêm mới người dùng này?',
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
                                     var email = res.element.email;
                                     var status = res.element.status ? 1 : 0;
                                     var name = res.element.fullName;
                                     var role = res.element.roleId;

                                     $('#input-name').val(name);
                                     $('#input-email').val(email);
                                     $('#status').val(status);
                                     $('#role').val(role);
                                     table.destroy();
                                     table = loadData(name, email, status, role);
                                     $("#tbl-categories_length").append('<button class="float-right btn btn-success" style="margin-bottom:5px" data-toggle="modal" data-target="#modal" id="btn-modal-add">Thêm mới</button>');
                                     setValue("", "", "", true, 3);
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
        setValue("", "", "", true, 3);
        $("#button-add").show();
        $("#button-edit").hide();
    });
    $('#tbl-users').on("click", '.btn-edit', function() {
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
                $('#category-name').val(res.element.name);
                $('#category-code').val(res.element.code);
                $('#category-description').val(res.element.description);
                $('#image-preview').attr("src", res.element.image);
                $('#category-id').val(res.element.id);
                res.element.status ? $("#status_active").prop("checked", true) : $("#status_no_active").prop("checked", true);
                $('#btn-reset-modal-edit').click(function() {
                    setValue(res.element.id, res.element.name, res.element.code, true, res.element.description, res.element.image);
                });
            },
            error: function() {
                $.alert({
                    title: "Thông báo!",
                    content: "Lỗi không thể thêm mới."
                });
            }
        })

    });

    $('#btn-edit').click(function() {
        var name = $("#category-name").val();
        var code = $("#category-code").val();
        var status = $("input[name='category-status']:checked").val() < 1 ? false : true;
        var description = $("#category-description").val();
        var id = $("#category-id").val();
        var imageUrlCurrent = $('#image-preview').attr('src');
        var formData = new FormData();
        formData.append("name", name);
        formData.append("code", code);
        formData.append("status", status);
        formData.append("description", description);
        formData.append("id", id);
        formData.append("imageUrlCurrent", imageUrlCurrent);
        formData.append("image", document.getElementById("category-image").files[0]);
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
                                    $('#modal').modal('toggle');
                                    var code = res.element.code;
                                    var status = res.element.status ? 1 : 0;
                                    var name = res.element.name;
                                    $('#input-name').val(name);
                                    $('#input-code').val(code);
                                    $('#status').val(status);
                                    table.destroy();
                                    table = loadData(name, code, status);
                                    $("#tbl-users_length").append('<button class="float-right btn btn-success" style="margin-bottom:5px" data-toggle="modal" data-target="#modal" id="btn-modal-add">Thêm mới</button>');
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


    function setValue(id, name, email, status, role) {
        $('#user-id').val(id);
        $('#user-name').val(name);
        $('#user-email').val(email);
        status ? $("#status_active").prop("checked", true) : $("#status_no_active").prop("checked", true);
        role > 2 ? $("#role_student").prop("checked", true) : $("#role_teacher").prop("checked", true);


    }
})