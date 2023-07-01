package com.hust.daotao.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "criteria")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Criteria {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	@Column(name = "name")
	private String name;
	@Column(name = "code")
	private String code;
	@Column(name = "description")
	private String description;
	@Column(name = "weight")
	private Double weight;
	@Column(name = "status")
	private Boolean status;
	@Column(name = "updated_at")
	private Date updatedAt;
	@Column(name = "deleted_at")
	private Date deletedAt;
	// @JsonBackReference
	// @OneToMany(mappedBy = "criteria", cascade = CascadeType.ALL, fetch =
	// FetchType.LAZY)
	// private List<KanseiWord> kanseiWords;
	@JsonManagedReference
	@ManyToOne
	@JoinColumn(name = "type_id")
	private CriteriaType criteriaType;

	@ManyToMany
	@JoinTable(name = "criteria_kansei", joinColumns = @JoinColumn(name = "criteria_id"), inverseJoinColumns = @JoinColumn(name = "kansei_id"))
	private List<KanseiWord> kanseiWords;

}
