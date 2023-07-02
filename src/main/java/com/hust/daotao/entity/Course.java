package com.hust.daotao.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "course")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Course {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	@Column(name = "name")
	private String name;
	@Column(name = "code")
	private String code;
	@Column(name = "quantity_student")
	private Integer quantityStudent;
	@Column(name = "quantity_lesson")
	private Integer quantityLesson;
	@Column(name = "status")
	private Boolean status;
	@Column(name = "description")
	private String description;
	@Column(name = "image")
	private String image;
	@Column(name = "updated_at")
	private Date updatedAt;
	@Column(name = "deleted_at")
	private Date deletedAt;
	@JsonManagedReference
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User teacher;
	@JsonManagedReference
	@ManyToOne
	@JoinColumn(name = "category_id")
	private Category categoryOfCourse;

	@JsonBackReference
	@OneToMany(mappedBy = "course")
	private List<CourseProfile> courseProfiles;

	@JsonBackReference
	@OneToMany(mappedBy = "courseOfLesson")
	private List<LessonOfCourse> lessonOfCourses;

	@JsonBackReference
	@OneToMany(mappedBy = "courseOfStudent", fetch = FetchType.EAGER)
	private List<StudentCourse> studentOfCourses;

	@Column(name = "result_evaluate")
	private Double resultEvaluate;

	@Column(name = "link")
	private String link;
	
}
