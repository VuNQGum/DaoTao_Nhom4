package com.hust.thesis.entity;

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
@Table(name="article")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Article {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private Integer id;
	@Column(name="title")
	private String title;
	@Column(name="description")
	private String description;
	@Column(name="content")
	private String content;
	@Column(name="views")
	private Integer views;
	@Column(name="updated_at")
	private Date updatedAt;
	@Column(name="deleted_at")
	private Date deletedAt;
	
	@ManyToMany
	@JoinTable(name = "article_tag", joinColumns = @JoinColumn(name = "article_id"), inverseJoinColumns = @JoinColumn(name = "tag_id"))
	private List<Tag> tags;
	
	@JsonManagedReference
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User userPostArticle;
}
