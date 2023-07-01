package com.hust.daotao.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "student_course")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentCourse {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@JsonManagedReference
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "student_id")
	private User student;

	@JsonManagedReference
	@ManyToOne
	@JoinColumn(name = "course_id")
	private Course courseOfStudent;

	@Column(name = "status")
	private Boolean status;
	@Column(name = "updated_at")
	private Date updatedAt;
	@Column(name = "deleted_at")
	private Date deletedAt;

	public StudentCourse(User student, Course courseOfStudent, Boolean status) {
		super();
		this.student = student;
		this.courseOfStudent = courseOfStudent;
		this.status = status;
	}

}