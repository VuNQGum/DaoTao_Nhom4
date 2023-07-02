var code = "";
var name = "";
var status = "";
var imageDefault = "/images/no-image.jpg";
var api_url = "/api/admin/categories/";
function preview(input) {
    if (input.files && input.files[0]) {
        var reader = new FileReader();

        reader.onload = function(e) {
            $('#image-preview').attr('src', e.target.result);
        }

        reader.readAsDataURL(input.files[0]);
    }
}

$("#upload-image").change(function() {
    preview(this);
});

$(document).ready(function() {
	 $(document).ajaxStart(function(){
	    $("#wait").css("display", "block");
	});
	 $(document).ajaxComplete(function(){
		    $("#wait").css("display", "none");
	});
    var table = loadData(name, code, status);
    $("#tbl-categories_length").append('<button class="float-right btn btn-success" style="margin-bottom:5px" data-toggle="modal" data-target="#modal" id="btn-modal-add">Thêm mới</button>');

    function loadData(name, code, status) {
        return $('#tbl-categories').DataTable({
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
                "className": "text-center",
                "targets": 2,
                "bSortable": false,
            }, {
                "className": "text-center",
                "targets": 3,
                "bSortable": false,
            }, {
                "className": "text-left",
                "targets": 4,
                "bSortable": false,
            }, {
                "className": "text-center",
                "targets": 5,
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
            }, {
                "data": "image",
                render: function(data, type, row) {
                    return '<img src="' + row.image + '"/>';
                }
            }, {
                "data": "description"
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
        $("#tbl-categories_length").append('<button class="float-right btn btn-success" style="margin-bottom:5px" data-toggle="modal" data-target="#modal" id="btn-modal-add">Thêm mới</button>');

    });
    $('#btn-reset').on("click", function() {
        $('#input-name').val("");
        $('#input-code').val("");
        $("#status").val("");
        table.destroy();
        table = loadData("", "", "");
        $("#tbl-categories_length").append('<button class="float-right btn btn-success" style="margin-bottom:5px" data-toggle="modal" data-target="#modal" id="btn-modal-add">Thêm mới</button>');

    });
    $('#tbl-categories').on("click", '.btn-delete', function() {
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
    $('#tbl-categories').on("click", '.btn-status', function() {
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
        setValue("", "", "", true, "", "");
    });

    $('#btn-add').click(function() {
        var name = $("#category-name").val();
        var code = $("#category-code").val();
        var status = $("input[name='category-status']:checked").val() < 1 ? false : true;
        var description = $("#category-description").val();
        var formData = new FormData();
        formData.append("name", name);
        formData.append("code", code);
        formData.append("status", status);
        formData.append("description", description);
        formData.append("image", document.getElementById("upload-image").files[0]);
        $.confirm({
            title: 'Xác nhận!',
            content: 'Bạn muốn thêm mới chủ đề này?',
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
                                    $('#modal').modal('toggle');
                                    var code = res.element.code;
                                    var status = res.element.status ? 1 : 0;
                                    var name = res.element.name;
                                    $('#input-name').val(name);
                                    $('#input-code').val(code);
                                    $('#status').val(status);
                                    table.destroy();
                                    table = loadData(name, code, status);
                                    $("#tbl-categories_length").append('<button class="float-right btn btn-success" style="margin-bottom:5px" data-toggle="modal" data-target="#modal" id="btn-modal-add">Thêm mới</button>');
                                    setValue("", "", "", true, "", "");
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
        $('#modal-title').text('Thêm mới chủ đề');
        setValue("", "", "", true, "", "");
        $("#button-add").show();
        $("#button-edit").hide();
    });
    $('#tbl-categories').on("click", '.btn-edit', function() {
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
                    content: "Server error!!!."
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
        formData.append("image", document.getElementById("upload-image").files[0]);
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
                                    $("#tbl-categories_length").append('<button class="float-right btn btn-success" style="margin-bottom:5px" data-toggle="modal" data-target="#modal" id="btn-modal-add">Thêm mới</button>');
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


    function setValue(id, name, code, status, description, image) {
        $('#category-id').val(id);
        $('#category-name').val(name);
        $('#category-code').val(code);
        $('#category-description').val(description);
        status ? $("#status_active").prop("checked", true) : $("#status_no_active").prop("checked", true);
        image != "" ? $('#image-preview').attr("src", image) : $('#image-preview').attr("src", imageDefault);

    }
})