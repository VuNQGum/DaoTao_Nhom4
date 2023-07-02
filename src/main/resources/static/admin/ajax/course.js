var code = "";
var name = "";
var status = "";
var category_id = "";
var user_id = "";
var imageDefault = "/images/no-image.jpg";
var api_url = "/api/admin/courses/";
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
	var user_role = $('#role_id').val();
    var table = loadData(name, code, status, category_id, user_id);
    var role_name = 'administrator'
    if(user_role == 2)
    	role_name = 'teacher'
    var btn_add = '<a class="float-right btn btn-success" style="margin-bottom:5px" href="/'+role_name+'/courses/create">Thêm mới</a>'
    $("#tbl-courses_length").append(btn_add);
    function loadData(name, code, status, category_id, user_id) {
        return $('#tbl-courses').DataTable({
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
                    "category_id": category_id,
                    "user_id": user_id,
                    "status": status,
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
                "className": "text-left",
                "targets": 2,
                "bSortable": false,
            }, {
                "className": "text-left",
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
                "data": "user",
                render: function(data, type, row) {
                    return row.teacher.fullName;
                }
            },{
                "data": "name"
            }, {
                "data": "code"
            }, {
                "data": "category",
                render: function(data, type, row) {
                    return row.categoryDto.name;
                }
            },{
                "data": "status",
                render: function(data, type, row) {
                    if (!row.status)
                        return ' <a class="btn-status switch"  href="javascript:;" id="' + row.id + '"><p ></p><span class="slider round"></span></label> ';
                    else return ' <a class="btn-status switch"  href="javascript:;" id="' + row.id + '"><p class="checked" ></p><span class="slider round"></span></label> ';
                }
            },{
                "data": "action",
                render: function(data, type, row) {
                	var btn_evaluate = '';
                	var btn_statistic = '';
                	if(user_role == role_admin){
                		btn_evaluate =  '<a  class="btn btn-warning btn-evaluate mr-1 button_action" href="javascript:;" id="'+ row.id+	'" title="Đánh giá khóa học"><i class="fa fa-star"></i></a>';
                		btn_statistic = '<a  class="btn btn-secondary btn-statistic mr-1 button_action" href="javascript:;" id="'+ row.id+	'" title="Thống kê chi tiết"><i class="fa fa fa-chart-line"></i></a>';
                	} 
                	
                	return '<a  class="btn btn-info  btn-lessons button_action" href="javascript:;" id="'
                	+ row.id
                	+	'" title="Danh sách bài học"><i class="fa fa-info-circle"></i></a><a  class="btn btn-success ml-1 btn-class button_action" href="javascript:;" id="'
					+ row.id
					+ '"  title="Danh sách sinh viên khóa học"><i class="fa fa-users"></i></a>  <a  class="btn btn-primary  btn-edit button_action" href="javascript:;" id="'
					+ row.id
					+ '"  title="Sửa"><i class="fa fa-pencil-square-o" aria-hidden="true"></i></a> <a id="'
					+ row.id
					+ '"  class="btn btn-danger btn-delete button_action mr-1" href="javascript:;" title="Xóa" ><i id="deleteUnit" class="fa fa-trash" aria-hidden="true"></i></a>'
					+ btn_evaluate + btn_statistic;
                }
            }, ]

        });
    }

    $('#btn-search').on("click", function() {
        name = $('#input-name').val();
        code = $('#input-code').val();
        status = $("#status").val();
        category_id = $("#category").val();
        user_id = $("#user").val();
        table.destroy();
        table = loadData(name, code, status, category_id, user_id);
        $("#tbl-courses_length").append(btn_add);

    });
    $('#btn-reset').on("click", function() {
        $('#input-name').val("");
        $('#input-email').val("");
        $("#status").val("");
        $("#category").val("");
        $("#user").val("");
        table.destroy();
        table = loadData("", "", "", "");
        $("#tbl-courses_length").append(btn_add);

    });
    $('#tbl-courses').on("click", '.btn-delete', function() {
        var id = $(this).attr('id');
        $.confirm({
            title: 'Xác nhận!',
            content: 'Bạn muốn xóa khóa học này?',
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
    $('#tbl-courses').on("click", '.btn-status', function() {
        var id = $(this).attr('id');
        $.confirm({
            title: 'Xác nhận!',
            content: 'Bạn muốn thay đổi trạng thái khóa học này?',
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

    $('#form-create-new').on("submit", function(e) {
		e.preventDefault();
		var form = $("#form-create-new").serializeArray();
		var formData = new FormData();
		$.each(form, function() {
        	if(formData.has(this.name)) {
        		var data = formData.get(this.name);
        		data = data + "," +this.value;
        		formData.set(this.name, data);
        	}
        	else {
            	formData.append(this.name, this.value)
        	}
        });
        formData.append("image", document.getElementById("upload-image").files[0]);
       
        formData.set("description", CKEDITOR.instances['description'].getData());
		$.confirm({
			title : 'Xác nhận!',
			content : 'Bạn muốn tạo khóa học này?',
			buttons : {
				cancel : function() {
					text: 'Hủy'
				},
				confirm : {
					text : 'Xác nhận',
					btnClass : 'btn-red',
					keys : [ 'enter', 'shift' ],
					action : function() {
						$.ajax({
							type: "POST",
                            processData: false,
                            contentType: false,
                            url: api_url,
                            data: formData,
							success : function(res) {
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
                                                text: 'Thêm bài giảng',
                                                btnClass: 'btn-green',
                                                action: function () {
                                                	window.location.href = "/"+role_name+"/courses/"+res.element.id+"/lessons";
                                                }
                                            }
                                        }
                                    });
                                	setTimeout(function(){ window.location.href = "/"+role_name+"/courses/"+res.element.id+"/lessons"}, 2000);
                                }
							},
							error : function() {
								$.alert({
									title : 'Thông báo!',
									content : 'Error server'
								});
							}
						})

					}
				}
			}
		});
	});
	
    $('#btn-modal-add').click(function() {
        $('#modal-title').text('Thêm mới khóa học');
        setValue("", "", "", true, 3);
        $("#button-add").show();
        $("#button-edit").hide();
    });
    $('#tbl-courses').on("click", '.btn-edit', function() {
        var id = $(this).attr('id');
        window.location.href = "/" + role_name + "/courses/"+id+"/edit"; 

    });
    $('#tbl-courses').on("click", '.btn-statistic', function() {
        var id = $(this).attr('id');
        window.location.href = "/" + role_name + "/statistic/"+id; 

    });

    $('#form-update').on("submit", function(e) {
		e.preventDefault();
		var form = $("#form-update").serializeArray();
		var formData = new FormData();
		$.each(form, function() {
        	if(formData.has(this.name)) {
        		var data = formData.get(this.name);
        		data = data + "," +this.value;
        		formData.set(this.name, data);
        	}
        	else {
            	formData.append(this.name, this.value)
        	}
        });
        formData.append("image", document.getElementById("upload-image").files[0]);
        formData.set("description", CKEDITOR.instances['description'].getData());
		$.confirm({
			title : 'Xác nhận!',
			content : 'Bạn muốn thay đổi?',
			buttons : {
				cancel : function() {
					text: 'Hủy'
				},
				confirm : {
					text : 'Xác nhận',
					btnClass : 'btn-green',
					keys : [ 'enter', 'shift' ],
					action : function() {
						$.ajax({
							type: "PUT",
                            processData: false,
                            contentType: false,
                            url: api_url,
                            data: formData,
							success : function(res) {
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
                                                text: 'Thay đổi bài giảng',
                                                btnClass: 'btn-green',
                                                action: function () {
                                                	window.location.href = "/"+role_name+"/courses/"+res.element.id+"/lessons";
                                                }
                                            },
		                                	'Back': {
		                                        text: 'Về trang quản lý',
		                                        btnClass: 'btn-red',
		                                        action: function () {
		                                        	window.location.href = "/"+role_name+"/courses/";
		                                        }
		                                    }
                                        }
                                    });
                                }
							},
							error : function() {
								$.alert({
									title : 'Thông báo!',
									content : 'Error server'
								});
							}
						})

					}
				}
			}
		});
	});
    $('#tbl-courses').on("click", '.btn-lessons', function() {
        var id = $(this).attr('id');
        if(user_role == role_admin)
        	window.location.href = "/administrator/courses/"+id+"/lessons";
        else window.location.href = "/teacher/courses/"+id+"/lessons";
    });
    function setValue(id, name, email, status, role) {
        $('#user-id').val(id);
        $('#user-name').val(name);
        $('#user-email').val(email);
        status ? $("#status_active").prop("checked", true) : $("#status_no_active").prop("checked", true);
        role > 2 ? $("#role_student").prop("checked", true) : $("#role_teacher").prop("checked", true);


    }
    
    $('#tbl-courses').on("click", '.btn-class', function() {
        var id = $(this).attr('id');
    	window.location.href = "/" + role_name + "/courses/"+id+"/class";

    });
    
    $('#tbl-courses').on("click", '.btn-evaluate', function() {
        var id = $(this).attr('id');
        $('#modal_evaluate').modal('show');
        $('#course_id').val(id);

    });
    
    $('.select').select2({
		placeholder: 'Lựa chọn giá trị'
	});
    $(".carousel").carousel({interval:false});
	$('#form-criteria').on("submit", function(e) {
		e.preventDefault();
		var form = $("#form-criteria").serializeArray();
		var criteria = [];
		var value = [];
		var criteria_values = [];
		form.forEach(function(item){
			  if(item.name.lastIndexOf("criteria") > -1){
				  item.name = item.name.slice(9);
    			  criteria.push(item);

			  }
			  if(item.name.lastIndexOf("value") > -1){
				  item.name = item.name.slice(6);
				  value.push(item);
			  }
		});
		value.forEach(function(item) {
			criteria.forEach(function(i){
				if(item.name == i.name) {
					var criteria_id = item.name.split("_")[0];
					var criteria = find(criteria_values, criteria_id)
					if(criteria == null) {
						criteria_values.push({'id': criteria_id, 'value':(item.value*i.value), 'count': 1})
					}
					
					else {
						var index = check(criteria.id, criteria_values);
						criteria.value = criteria.value + (item.value*i.value);
						criteria.count = criteria.count+1;
						criteria_values[index] = criteria;
						
					}
				}
			});
			
    		
    	});
		var data = [];
		var course_id = $('#course_id').val();
		criteria_values.forEach(function(item) {
			var value = (item.value/item.count);
			data.push({'courseId':course_id, 'criteriaId':item.id, 'value': Math.round(value * 100) / 100});
		});
		$.ajax({
			type: "POST",
            processData: false,
            contentType: 'application/json; charset=utf-8',
            url: '/evaluate',
            data:  JSON.stringify(data),
            success: function(res) {
            	if(res.code == 400) {
            		$('.carousel').carousel(res.id);
            		 $.alert({
	                     title: 'Thông báo!',
	                     content: 'Bạn chưa đánh giá tiêu chí '+(res.id+1)
	                 });
                	 

            	} else {
	            	 $.alert({
	                     title: 'Thông báo!',
	                     content: 'Hoàn thành đánh giá'
	                 });
	            	 $('#modal_evaluate').modal('hide');
	            	 $("#form-criteria").trigger("reset");
            	}
            },
            error: function() {
                $.alert({
                    title: 'Thông báo!',
                    content: 'Error server'
                });
            }
        })
	});
		
	
	function check(id, array) {
		array.forEach(function(item){
			if(id == item.id)
				return true;
		});
		return false;
	}
	function find(array, id) {
		for(var i = 0; i< array.length; i++) {
			if(array[i].id == id) {
				return array[i];
			}
		}
		return null;
	}
})