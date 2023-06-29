package com.hust.thesis.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "profile")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Profile {
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
	@Column(name = "description")
	private String description;
	@Column(name = "status")
	private Boolean status;
	@Column(name = "is_multiple")
	private Boolean isMultiple;
	@Column(name = "weight")
	private Double weight;
	@Column(name = "updated_at")
	private Date updatedAt;
	@Column(name = "deleted_at")
	private Date deletedAt;
	@JsonBackReference
	@OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<ProfileValue> profileValues;

	@Column(name = "show")
	private Integer show;
}