package com.hust.daotao.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
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

//, schema = "public"
@Entity
@Table(name = "user", schema = "public")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	@Column(name = "full_name")
	private String fullName;
	@Column(name = "email")
	private String email;
	@Column(name = "password")
	private String password;
	@Column(name = "student_code")
	private String studentCode;
	@Column(name = "phone")
	private String phone;
	@Column(name = "birthday")
	private String birthday;
	@Column(name = "image")
	private String image;
	@Column(name = "status")
	private Boolean status;
	@Column(name = "description")
	private String description;
	@Column(name = "updated_at")
	private Date updatedAt;
	@Column(name = "deleted_at")
	private Date deletedAt;
	@JsonManagedReference
	@ManyToOne
	@JoinColumn(name = "role_id")
	private Role role;

	@JsonManagedReference
	@ManyToOne
	@JoinColumn(name = "subject_id")
	private Category subject;

	@JsonBackReference
	@OneToMany(mappedBy = "user")
	private List<UserProfile> userProfiles;

	@JsonBackReference
	@OneToMany(mappedBy = "teacher")
	private List<Course> courses;

	@JsonBackReference
	@OneToMany(mappedBy = "student")
	private List<StudentCourse> studentOfCourses;

	@Column(name = "result_test")
	private Integer resultTest;

}
