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
@Table(name = "user_profile")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@JsonManagedReference
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@JsonManagedReference
	@ManyToOne
	@JoinColumn(name = "profile_id")
	private Profile profile;

	@Column(name = "value")
	private String value;
	@Column(name = "updated_at")
	private Date updatedAt;
	@Column(name = "deleted_at")
	private Date deletedAt;

	public UserProfile(String value) {
		super();
		this.value = value;
	}

	public UserProfile(User user, Profile profile, String value) {
		super();
		this.user = user;
		this.profile = profile;
		this.value = value;
	}
	
	

}
