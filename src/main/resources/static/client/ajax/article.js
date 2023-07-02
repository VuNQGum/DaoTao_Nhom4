 
CKEDITOR.replace( 'content', {
	    filebrowserUploadUrl: 'http://localhost:8080/api/client/postupload-image'
});
var user_of_articles = $('#user_id').val();
var title = "";
$(document).ready(function() {
    var table = loadData(title);
	function loadData(title) {
	        return $('#tbl-my-articles').DataTable({
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
	                "url": "/api/client/articles",
	                "type": "GET",

	                "data": {
	                    "title": title,
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
	                "orderable": false,
	            }, {
	                "className": "text-center",
	                "targets": 3,
	                "bSortable": false,
	            }, ],
	            "columns": [{
	                "data": "title",
	                render: function(data, type, row) {
	                	return ' <a  class="" style="color:black"  href="/forum/article/'+row.id+'" >'+row.title+'</a>';
	                }
	            }, {
	                "data": "description"
	            }, {
	                "data": "updatedAt",
	                render: function(data, type, row) {
	                	const date = new Date(row.updatedAt)
	                	return date.getDate()+"-"+(date.getMonth()+1)+"-"+date.getFullYear();
	                }
	            }, {
	                "data": "action",
	                render: function(data, type, row) {
	                    return ' <a  class="btn btn-primary  btn-edit button_action" href="javascript:;" id="' + row.id + '"  title="Sửa"><i class="fa fa-pencil-square-o" aria-hidden="true"></i></a> <a id="' + row.id + '"  class="btn btn-danger btn-delete button_action" href="javascript:;" title="Xóa" ><i id="deleteUnit" class="fa fa-trash" aria-hidden="true"></i></a>';
	                }
	            }, ]

	        });
	    }
    $('#btn-post').click(function() {
        var title = $("#title").val();
        var description = $("#description").val();
		var tagIds = $("#tag_ids").val()
        var content = CKEDITOR.instances['content'].getData();
        var data = {'title':title, 'description':description, 'tagIds': tagIds, 'content': content};
        $(document).ajaxStart(function(){
		    $("#wait").css("display", "block");
		  });
		  $(document).ajaxComplete(function(){
		    $("#wait").css("display", "none");
		 });
        $.confirm({
            title: 'Xác nhận!',
            content: 'Bạn muốn đăng bài này?',
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
                            type: "post",
                            processData: false,
                            contentType: 'application/json; charset=utf-8',
                            url: "/api/client/articles",
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
                                                text: 'Xem bài viết', // With spaces and symbols
                                                action: function () {
                                                	window.location.href = "/forum/article/"+res.element.id
                                                }
                                            }
                                        }
                                    });
                                	setTimeout(function(){ window.location.href = window.location.href = "/forum/article/"+res.element.id }, 2000);

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
    
    $('#tbl-my-articles').on("click", '.btn-delete', function() {
        var id = $(this).attr('id');
        $.confirm({
            title: 'Xác nhận!',
            content: 'Bạn muốn xóa bài viết này?',
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
                            url: "/api/client/articles/"+id,
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
    
    $('#tbl-my-articles').on("click", '.btn-edit', function() {
        var id = $(this).attr('id');
        window.location.href = "/forum/article/"+id+"/edit";
    });
    
    $('#btn-edit').click(function() {
        var title = $("#title").val();
        var id = $("#id").val();
        var description = $("#description").val();
		var tagIds = $("#tag_ids").val()
        var content = CKEDITOR.instances['content'].getData();
        var data = {'id':id,'title':title, 'description':description, 'tagIds': tagIds, 'content': content};
        console.log(data);
        $.confirm({
            title: 'Xác nhận!',
            content: 'Bạn muốn cập nhật bài viết này?',
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
                            type: "post",
                            processData: false,
                            contentType: 'application/json; charset=utf-8',
                            url: "/api/client/articles",
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
                                                text: 'Xem bài viết', // With spaces and symbols
                                                action: function () {
                                                	window.location.href = "/forum/article/"+res.element.id
                                                }
                                            }
                                        }
                                    });
                                	setTimeout(function(){ window.location.href = window.location.href = "/forum/article/"+res.element.id }, 2000);

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
})