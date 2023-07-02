var code = "";
var name = "";
var status = "";
var api_url = "/api/admin/criterias/";
$(document).ready(function() {
	var type_id = $('#criteria_type_id').val()
    var table = loadData(name, code, status);
    $("#tbl-criterias_length").append('<button class="float-right btn btn-success" style="margin-bottom:5px" data-toggle="modal" data-target="#modal" id="btn-modal-add">Thêm mới</button>');

    function loadData(name, code, status) {
        return $('#tbl-criterias').DataTable({
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
                    "type_id": type_id
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
							+ row.id
							+ '"  title="Xem danh sách các từ kansei"><i class="fa fa-file-word"></i></a>  <a  class="btn btn-primary  btn-edit button_action" href="javascript:;" id="'
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
        $("#tbl-criterias_length").append('<button class="float-right btn btn-success" style="margin-bottom:5px" data-toggle="modal" data-target="#modal" id="btn-modal-add">Thêm mới</button>');

    });
    $('#btn-reset').on("click", function() {
        $('#input-name').val("");
        $('#input-code').val("");
        $("#status").val("");
        table.destroy();
        table = loadData("", "", "");
        $("#tbl-criterias_length").append('<button class="float-right btn btn-success" style="margin-bottom:5px" data-toggle="modal" data-target="#modal" id="btn-modal-add">Thêm mới</button>');

    });
    $('#tbl-criterias').on("click", '.btn-delete', function() {
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
    $('#tbl-criterias').on("click", '.btn-status', function() {
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
    $('#tbl-criterias').on("click", '.btn-values', function() {
    	$(".kansei").prop( "checked", false );
        var id = $(this).attr('id');
        $('#modal-kansei').modal('show');
        $.ajax({
            type: "GET",
            processData: false,
            contentType: false,
            url: api_url+id+"/kansei",
            success: function(res) {
            	$('#criteria-kansei-id').val(id);
            	var kanseis = res.aaData;
            	kanseis.forEach(function(item){
            		$("#kansei_"+item.id).prop( "checked", true );
            	});
            },
            error: function() {
                $.alert({
                    title: "Thông báo!",
                    content: "Lỗi server"
                });
            }
        })

    });

    $('#btn-reset-modal-add').click(function() {
        setValue("", "", "", true, "");
    });

    $('#btn-add').click(function() {
        var name = $("#name").val();
        var code = $("#code").val();
        var status = $("input[name='status']:checked").val() < 1 ? false : true;
        var description = $("#profile-description").val();
        var weight = $('#weight').val();

        var data = {'name':name, 'code': code, 'status': status, 'description': description, 'typeId': type_id, 'weight': weight};
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
                                	 table.destroy();
                                	 var statusValue = status ? 1:0;
                                     table = loadData(name, code, statusValue);
                                     $("#tbl-criterias_length").append('<button class="float-right btn btn-success" style="margin-bottom:5px" data-toggle="modal" data-target="#modal" id="btn-modal-add">Thêm mới</button>');
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
        $('#modal-title').text('Thêm mới tiêu chí');
        setValue("", "", "", true, "");
        $("#button-add").show();
        $("#button-edit").hide();
    });
    $('#tbl-criterias').on("click", '.btn-edit', function() {
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
                $('#modal-title').text('Thay đổi tiêu chí');
                $('#name').val(res.element.name);
                $('#code').val(res.element.code);
                $('#weight').val(res.element.weight);
                $('#description').val(res.element.description);
                $('#criteria-id').val(res.element.id);
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
        var id = $("#criteria-id").val();
        var weight = $('#weight').val();
        var data = {'id': id, 'name':name, 'code': code, 'status': status, 'description': description, 'typeId': type_id, 'weight': weight};
        
        $.confirm({
            title: 'Xác nhận!',
            content: 'Bạn muốn thay đổi tiêu chí này?',
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
                                    var statusValue = status ? 1:0;
                                    table = loadData(name, code, statusValue);
                                    $("#tbl-criterias_length").append('<button class="float-right btn btn-success" style="margin-bottom:5px" data-toggle="modal" data-target="#modal" id="btn-modal-add">Thêm mới</button>');

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


    function setValue(id, name, code, status, description) {
        $('#id').val(id);
        $('#name').val(name);
        $('#code').val(code);
        $('#description').val(description);
        $('#weight').val('0.5');
        status ? $("#status_active").prop("checked", true) : $("#status_no_active").prop("checked", true);
    }
    
    $('.kansei').change( function(){
       var split_id = $(this).attr('id').split('_');
       var id = split_id[1];
       var criteria_id = $('#criteria-kansei-id').val();
       var formData = new FormData();
       formData.append('kansei_id', id);
       formData.append('criteria_id', criteria_id);
       $.ajax({
           type: "put",
           processData: false,
           contentType: false,
           url: api_url+"change-kansei-word",
           data: formData,
           success: function(res) {              

           },
           error: function() {
               $.alert({
                   title: "Thông báo!",
                   content: "Server error."
               });
           }
       })
    });
})