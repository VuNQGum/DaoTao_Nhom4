package com.hust.thesis.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "course_evaluate")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseEvaluate {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@JsonManagedReference
	@ManyToOne
	@JoinColumn(name = "course_id")
	private Course course;

	@JsonManagedReference
	@ManyToOne
	@JoinColumn(name = "criteria_id")
	private Criteria criteria;

	@Column(name = "value")
	private Double value;
	@Column(name = "updated_at")
	private Date updatedAt;
	@Column(name = "deleted_at")
	private Date deletedAt;

	public CourseEvaluate(Double value) {
		super();
		this.value = value;
	}

	public CourseEvaluate(Course course, Criteria criteria, Double value) {
		super();
		this.course = course;
		this.criteria = criteria;
		this.value = value;
	}
}
