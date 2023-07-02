var positive = "";
var negative = "";
var status = "";
var api_url = "/api/admin/kansei-words/";
$(document).ready(function() {
    var table = loadData(negative, positive, status);
    $("#tbl-kansei_length").append('<button class="float-right btn btn-success" style="margin-bottom:5px" data-toggle="modal" data-target="#modal" id="btn-modal-add">Thêm mới</button>');

    function loadData(negative, positive, status) {
        return $('#tbl-kansei').DataTable({
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
                    "negative": negative,
                    "positive": positive,
                    "status": status
                }
            },
            "columnDefs": [{
                "classnegative": "text-left",
                "targets": 0,
                "orderable": false,
            }, {
                "classnegative": "text-left",
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
            },],
            "columns": [{
                "data": "negativeWord"
            }, {
                "data": "positiveWord"
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
        negative = $('#input-negatvie').val();
        positive = $('#input-positive').val();
        status = $("#status").val();
        table.destroy();
        table = loadData(negative, positive, status);
        $("#tbl-kansei_length").append('<button class="float-right btn btn-success" style="margin-bottom:5px" data-toggle="modal" data-target="#modal" id="btn-modal-add">Thêm mới</button>');

    });
    $('#btn-reset').on("click", function() {
        $('#input-negatvie').val("");
        $('#input-positive').val("");
        $("#status").val("");
        table.destroy();
        table = loadData("", "", "");
        $("#tbl-kansei_length").append('<button class="float-right btn btn-success" style="margin-bottom:5px" data-toggle="modal" data-target="#modal" id="btn-modal-add">Thêm mới</button>');

    });
    $('#tbl-kansei').on("click", '.btn-delete', function() {
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
    $('#tbl-kansei').on("click", '.btn-status', function() {
        var id = $(this).attr('id');
        $.confirm({
            title: 'Xác nhận!',
            content: 'Bạn muốn thay đổi trạng thái cặp từ này?',
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
        setValue("", "", "", true);
    });

    $('#btn-add').click(function() {
        var negative = $("#negative").val();
        var positive = $("#positive").val();
        var status = $("input[negative='status']:checked").val() < 1 ? false : true;
        var data = {'negativeWord':negative, 'positiveWord': positive, 'status': status};
        $.confirm({
            title: 'Xác nhận!',
            content: 'Bạn muốn thêm mới cặp từ Kansei này?',
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
                                if (res.positive == 403) {
                                    $.alert({
                                        title: 'Thông báo!',
                                        content: res.message
                                    });
                                } else {
                                	
                                	 $.alert({
                                         title: "Thông báo!",
                                         content: res.message
                                     });
                                	 var positive = res.element.positiveWord;
                                     var status = res.element.status ? 1 : 0;
                                     var negative = res.element.negativeWord;
                                     $('#input-negatvie').val(negative);
                                     $('#input-positive').val(positive);
                                     $('#status').val(status);
                                     table.destroy();
                                     table = loadData(negative, positive, status);
                                     $("#tbl-kansei_length").append('<button class="float-right btn btn-success" style="margin-bottom:5px" data-toggle="modal" data-target="#modal" id="btn-modal-add">Thêm mới</button>');
                                     setValue("", "", "", true);
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
        $('#modal-title').text('Thêm mới cặp từ');
        setValue("", "", "", true, "");
        $("#button-add").show();
        $("#button-edit").hide();
    });
    $('#tbl-kansei').on("click", '.btn-edit', function() {
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
                $('#modal-title').text('Thay đổi cặp từ Kansei');
                $('#negative').val(res.element.negativeWord);
                $('#positive').val(res.element.positiveWord);
                $('#id').val(res.element.id);
                res.element.status ? $("#status_active").prop("checked", true) : $("#status_no_active").prop("checked", true);
                $('#btn-reset-modal-edit').click(function() {
                    setValue(res.element.id, res.element.negative, res.element.positive, true);
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
        var negative = $("#negative").val();
        var positive = $("#positive").val();
        var status = $("input[negative='status']:checked").val() < 1 ? false : true;
        var id = $("#id").val();
        var data = {'id': id, 'negativeWord':negative, 'positiveWord': positive, 'status': status};
        $.confirm({
            title: 'Xác nhận!',
            content: 'Bạn muốn thay đổi cặp từ Kansei này?',
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
                                if (res.positive == 403) {
                                    $.alert({
                                        title: 'Thông báo!',
                                        content: res.message
                                    });
                                } else {
                                    $('#modal').modal('toggle');
                                    var positive = res.element.positiveWord;
                                    var status = res.element.status ? 1 : 0;
                                    var negative = res.element.negativeWord;
                                    $('#input-negatvie').val(negative);
                                    $('#input-positive').val(positive);
                                    $('#status').val(status);
                                    table.destroy();
                                    table = loadData(negative, positive, status);
                                    $("#tbl-kansei_length").append('<button class="float-right btn btn-success" style="margin-bottom:5px" data-toggle="modal" data-target="#modal" id="btn-modal-add">Thêm mới</button>');

                                    $.alert({
                                        title: "Thông báo!",
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


    function setValue(id, negative, positive, status) {
        $('#id').val(id);
        $('#negative').val(negative);
        $('#positive').val(positive);
        status ? $("#status_active").prop("checked", true) : $("#status_no_active").prop("checked", true);
    }
})