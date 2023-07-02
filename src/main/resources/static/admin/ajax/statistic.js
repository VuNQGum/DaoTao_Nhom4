$(document).ready(function() {
	$.ajax({
        type: "GET",
        processData: false,
        contentType: false,
        url: "/api/admin/statistic",
        success: function(res) {
            var student_capacity = document.getElementById('student_capacity').getContext('2d');
            var course_category = document.getElementById('course_category').getContext('2d');
            var evaluate = document.getElementById('evaluate').getContext('2d');
            var survey = document.getElementById('survey').getContext('2d');
            var student_capacity = new Chart(student_capacity, {
                type: 'bar',
                data: {
                    labels: res.capactity.titles,
                    datasets: [{
                        label: '# số sinh viên',
                        data: res.capactity.valuesInteger,
                        backgroundColor: 'rgba(60,141,188,0.9)',
                        borderColor: 'rgba(60,141,188,0.8)',
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
            
            var course_category = new Chart(course_category, {
                type: 'bar',
                data: {
                    labels: res.course.titles,
                    datasets: [{
                        label: '# số khóa học',
                        data: res.course.valuesInteger,
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
            var evaluate = new Chart(evaluate, {
                type: 'bar',
                data: {
                    labels: res.evaluate_hust.titles,
                    datasets: [{
                        label: '# Bộ tiêu chí ICT',
                        data: res.evaluate_ict.valuesDouble,
                        backgroundColor:'#0066FF',
                        borderColor: '#0066FF',
                        borderWidth: 1
                    	},
	                    {
	                        label: '# Bộ tiêu chí đại học Bách Khoa Hà Nội',
	                        data: res.evaluate_hust.valuesDouble,
	                        backgroundColor:'#CC0033',
	                        borderColor: '#CC0033',
	                        borderWidth: 1
	                    }
                    ]
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
            
            var survey = new Chart(survey, {
                type: 'bar',
                data: {
                    labels: res.survey_hust.titles,
                    datasets: [{
                        label: '# Bộ tiêu chí ICT',
	                        data: res.survey_ict.valuesDouble,
	                        backgroundColor:'#0066FF',
	                        borderColor: '#0066FF',
	                        borderWidth: 1
                    	},
	                    {
	                        label: '# Bộ tiêu chí đại học Bách Khoa Hà Nội',
	                        data: res.survey_hust.valuesDouble,
	                        backgroundColor:'#CC0033',
	                        borderColor: '#CC0033',
	                        borderWidth: 1
	                    }
                    ]
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