package com.hust.daotao.entity;

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
@Table(name = "profile_value")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileValue {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	@Column(name = "name")
	private String name;
	@Column(name = "name_display_student_side")
	private String nameDisplayStudentSide;
	@Column(name = "name_display_teacher_side")
	private String nameDisplayTeacherSide;
	@Column(name = "code")
	private String code;
	@Column(name = "value")
	private Integer value;
	@Column(name = "status")
	private Boolean status;
	@Column(name = "updated_at")
	private Date updatedAt;
	@Column(name = "deleted_at")
	private Date deletedAt;
	@JsonManagedReference
	@ManyToOne
	@JoinColumn(name = "profile_id")
	private Profile profile;
}