 $(document).ready(function() {
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
		criteria_values.forEach(function(item) {
			var value = (item.value/item.count);
			data.push({'criteriaId':item.id, 'value': Math.round(value * 100) / 100});
		});
		$(document).ajaxStart(function(){
		    $("#wait").css("display", "block");
		  });
		  $(document).ajaxComplete(function(){
		    $("#wait").css("display", "none");
		 });
		$.ajax({
			type: "POST",
            processData: false,
            contentType: 'application/json; charset=utf-8',
            url: '/survey',
            data:  JSON.stringify(data),
            success: function(res) {
            	if(res.code == 400) {
            		$('.carousel').carousel(res.id);
            		 $.alert({
	                     title: 'Thông báo!',
	                     content: 'Bạn chưa trả lời tiêu chí '+(res.id+1)
	                 });
            	}
            	else{
	        		 $.alert({
	                     title: 'Thông báo!',
	                     content: 'Hoàn thành khảo sát'
	                 });
	            	 setTimeout(function(){ window.location.href = "/"}, 2000);
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
});