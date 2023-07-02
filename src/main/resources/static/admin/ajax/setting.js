var name = "";
var code = "";

var api_url = "/api/admin/settings/";
$(document).ready(function() {
    var table = loadData(name, code);
    $("#tbl-settings_length").append('<button class="float-right btn btn-success" style="margin-bottom:5px" data-toggle="modal" data-target="#modal" id="btn-modal-add">Thêm mới</button>');

    function loadData(name, code) {
        return $('#tbl-settings').DataTable({
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
                    "code": code
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
                "className": "text-center",
                "targets": 2,
                "orderable": false,
            }, {
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
            },{
                "data": "value"
            },
            {
                "data": "description"
            }, {
                "data": "action",
                render: function(data, type, row) {
                	return '<a  class="btn btn-primary  btn-edit button_action" href="javascript:;" id="'
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
        table.destroy();
        table = loadData(name, code);
        $("#tbl-settings_length").append('<button class="float-right btn btn-success" style="margin-bottom:5px" data-toggle="modal" data-target="#modal" id="btn-modal-add">Thêm mới</button>');

    });
    $('#btn-reset').on("click", function() {
        $('#input-name').val("");
        table.destroy();
        table = loadData("","");
        $("#tbl-settings_length").append('<button class="float-right btn btn-success" style="margin-bottom:5px" data-toggle="modal" data-target="#modal" id="btn-modal-add">Thêm mới</button>');

    });
    $('#tbl-settings').on("click", '.btn-delete', function() {
        var id = $(this).attr('id');

        $.confirm({
            title: 'Xác nhận!',
            content: 'Bạn muốn xóa thiết lập này?',
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

    $('#btn-reset-modal-add').click(function() {
        setValue("", "", "", true, "");
    });

    $('#btn-add').click(function() {
        var name = $("#name").val();
        var code = $("#code").val();
        var value = $("#value").val();
        var description = $("#profile-description").val();
        var data = {'name':name, 'code': code, 'value': value, 'description': description};
        $.confirm({
            title: 'Xác nhận!',
            content: 'Bạn muốn thêm mới thông số này?',
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
                            	$.alert({
                                    title: 'Thông báo!',
                                    content: res.message
                                });
                                if (res.code == 403) {
                                    
                                }  else {
                                	 $('#modal').modal('toggle');
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
        $('#modal-title').text('Thêm mới cài đặt');
        setValue("", "", "", true, "");
        $("#button-add").show();
        $("#button-edit").hide();
    });
    $('#tbl-settings').on("click", '.btn-edit', function() {
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
                $('#modal-title').text('Thay đổi cài đặt');
                $('#name').val(res.element.name);
                $('#code').val(res.element.code);
                $('#description').val(res.element.description);
                $('#id').val(res.element.id);
                $('#value').val(res.element.value);
               
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
        var value = $("#value").val();
        var description = $("#description").val();
        var id = $("#id").val();
        var data = {'id': id, 'name':name, 'code': code, 'value': value, 'description': description};
        $.confirm({
            title: 'Xác nhận!',
            content: 'Bạn muốn thay đổi cài đặt này?',
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
                            	$.alert({
                                    title: 'Thông báo!',
                                    content: res.message
                                });
                                if (res.code != 403) {
                                	  $('#modal').modal('toggle');
                                      table.destroy();
                                      table = loadData(name, code);
                                      $("#tbl-settings_length").append('<button class="float-right btn btn-success" style="margin-bottom:5px" data-toggle="modal" data-target="#modal" id="btn-modal-add">Thêm mới</button>');  
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
        $('#id').val(id);
        $('#name').val(name);
        $('#code').val(code);
        $('#description').val(description);
    }
})