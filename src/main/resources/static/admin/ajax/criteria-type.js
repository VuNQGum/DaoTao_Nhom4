var code = "";
var name = "";
var status = "";
var api_url = "/api/admin/criteria-type/";
$(document).ready(function() {
    var table = loadData(name, code, status);
    $("#tbl-criteria-type_length").append('<button class="float-right btn btn-success" style="margin-bottom:5px" data-toggle="modal" data-target="#modal" id="btn-modal-add">Thêm mới</button>');

    function loadData(name, code, status) {
        return $('#tbl-criteria-type').DataTable({
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
            }, 
            {
                "className": "text-left",
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
                "data": "description"
            }, {
                "data": "action",
                render: function(data, type, row) {
                	return '<a  class="btn btn-info  btn-values button_action" href="javascript:;" id="'
							+ row.code
							+ '"  title="Xem danh sách các tiêu chí"><i class="fa fa-info-circle"></i></a>  <a  class="btn btn-primary  btn-edit button_action" href="javascript:;" id="'
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
        $("#tbl-criteria-type_length").append('<button class="float-right btn btn-success" style="margin-bottom:5px" data-toggle="modal" data-target="#modal" id="btn-modal-add">Thêm mới</button>');

    });
    $('#btn-reset').on("click", function() {
        $('#input-name').val("");
        $('#input-code').val("");
        $("#status").val("");
        table.destroy();
        table = loadData("", "", "");
        $("#tbl-criteria-type_length").append('<button class="float-right btn btn-success" style="margin-bottom:5px" data-toggle="modal" data-target="#modal" id="btn-modal-add">Thêm mới</button>');

    });
    $('#tbl-criteria-type').on("click", '.btn-delete', function() {
        var id = $(this).attr('id');
        $.confirm({
            title: 'Xác nhận!',
            content: 'Bạn muốn xóa tiêu chí này?',
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
    $('#tbl-criteria-type').on("click", '.btn-status', function() {
        var id = $(this).attr('id');
        $.confirm({
            title: 'Xác nhận!',
            content: 'Bạn muốn thay đổi trạng thái tiêu chí này?',
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
    $('#tbl-criteria-type').on("click", '.btn-values', function() {
        var code = $(this).attr('id');
        window.location.href = "/administrator/criterias/"+code;
    });

    $('#btn-reset-modal-add').click(function() {
        setValue("", "", "", true, "");
    });

    $('#btn-add').click(function() {
        var name = $("#name").val();
        var code = $("#code").val();
        var status = $("input[name='status']:checked").val() < 1 ? false : true;
        var description = $("#profile-description").val();
        var data = {'name':name, 'code': code, 'status': status, 'description': description};
        $.confirm({
            title: 'Xác nhận!',
            content: 'Bạn muốn thêm mới tiêu chí này?',
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
                                	
                                	$.confirm({
                                    	title: "Thông báo!",
                                        content: res.message,
                                        buttons: {
           
                                            'Next': {
                                                text: 'Thêm giá trị', // With spaces and symbols
                                                action: function () {
                                                	window.location.href = "/administrator/criterias/"+res.element.code
                                                }
                                            }
                                        }
                                    });
                                	setTimeout(function(){ window.location.href = "/administrator/criterias/"+res.element.code }, 2000);

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
        $('#modal-title').text('Thêm mới bộ tiêu chí');
        setValue("", "", "", true, "");
        $("#button-add").show();
        $("#button-edit").hide();
    });
    $('#tbl-criteria-type').on("click", '.btn-edit', function() {
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
                $('#modal-title').text('Thay đổi bộ tiêu chí');
                $('#name').val(res.element.name);
                $('#code').val(res.element.code);
                $('#description').val(res.element.description);
                $('#id').val(res.element.id);
                res.element.status ? $("#status_active").prop("checked", true) : $("#status_no_active").prop("checked", true);
                $('#btn-reset-modal-edit').click(function() {
                    setValue(res.element.id, res.element.name, res.element.code, true, res.element.description);
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
        var name = $("#name").val();
        var code = $("#code").val();
        var status = $("input[name='status']:checked").val() < 1 ? false : true;
        var description = $("#description").val();
        var id = $("#id").val();
        var data = {'id': id, 'name':name, 'code': code, 'status': status, 'description': description};
        $.confirm({
            title: 'Xác nhận!',
            content: 'Bạn muốn thay đổi bộ tiêu chí này?',
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
                            type: "put",
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
                                    $("#tbl-criteria-type_length").append('<button class="float-right btn btn-success" style="margin-bottom:5px" data-toggle="modal" data-target="#modal" id="btn-modal-add">Thêm mới</button>');

                                    $.confirm({
                                    	title: "Thông báo!",
                                        content: res.message,
                                        buttons: {
                                            'OK': {
                                            	text: 'OK'                                      
                                            }
                                            ,
                                            'Next': {
                                                text: 'Sửa giá trị', // With spaces and symbols
                                                action: function () {
                                                	window.location.href = "/administrator/criterias/"+res.element.code
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
})