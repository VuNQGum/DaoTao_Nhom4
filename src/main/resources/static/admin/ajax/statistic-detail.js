$(document).ready(function() {
	var course_id = $('#course_id').val();
	$.ajax({
        type: "GET",
        processData: false,
        contentType: false,
        url: "/api/admin/statistic/"+course_id,
        success: function(res) {
            var evaluate_ict = document.getElementById('evaluate_ict').getContext('2d');
            var evaluate_hust = document.getElementById('evaluate_hust').getContext('2d');
            var evaluate_ict = new Chart(evaluate_ict, {
                type: 'bar',
                data: {
                    labels: res.evaluate_ict.titles,
                    datasets: [{
                        label: '# điểm',
                        data: res.evaluate_ict.valuesDouble,
                        backgroundColor: 'rgba(60,141,188,0.9)',
                        borderColor: 'rgba(60,141,188,0.9)',
                        borderWidth: 1
                    }]
                },
                options: {
                    scales: {
                        yAxes: [{
                            ticks: {
                                beginAtZero: true
                            }
                        }]
                    }
                }
            });
            
            var evaluate_hust = new Chart(evaluate_hust, {
                type: 'bar',
                data: {
                    labels: res.evaluate_hust.titles,
                    datasets: [{
                        label: '# điểm',
                        data: res.evaluate_hust.valuesDouble,
                        backgroundColor:'#008800',
                        borderColor: '#008800',
                        borderWidth: 1
                    }]
                },
                options: {
                    scales: {
                        yAxes: [{
                            ticks: {
                                beginAtZero: true
                            }
                        }]
                    }
                }
            });

        },
        error: function() {
            $.alert({
                title: 'Thông báo!',
                content: 'Error server'
            });
        }
   })
});