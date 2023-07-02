 $(document).ready(function() {
	 $(".carousel").carousel({interval:false});
	 var count = 1;
	 $("input").click(function() {
         // if (this.checked) {
         //     carouselControlNext.css("display", "flex");
         // }
		 if(count <= 20) {
		  $('#isReply').text(count);
	         count++;
	         $(".carousel-control-prev").css("display", "flex");
	         $(".carousel-control-next").css("display", "flex");
	         $(".carousel").delay(50).queue(function() {
	             $(this).carousel("next");
	             $(this).dequeue();
	         }); 
	         let currentIndex = $('div.active').index() + 1;
		 }
         
       
         
         // $("input:not(:checked)").parent().removeClass("checked");
         // $("input:checked").parent().addClass("checked");
     });
	$('#form-test').on("submit", function(e) {
		e.preventDefault();
		var form = $("#form-test").serializeArray();
		var data = [];
		console.log(form);
		form.forEach(function(item){
			var i = {'id': item.name, 'answerOfUser': item.value};
			data.push(i);
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
            url: '/api/client/test',
            data:  JSON.stringify(data),
            success: function(res) {
            	 $.alert({
                     title: 'Thông báo!',
                     content: 'Bạn trả lời đúng '+res.resultTest+ '/20 câu.'
                 });
            	 setTimeout(function(){ window.history.go(-1)}, 2000);
            },
            error: function() {
                $.alert({
                    title: 'Thông báo!',
                    content: 'Error server'
                });
            }
        })
	});
});