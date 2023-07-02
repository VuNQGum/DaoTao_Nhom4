package com.hust.daotao.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "test")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CapacityTest {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	@Column(name = "question")
	private String question;
	@Column(name = "answer_a")
	private String answerA;
	@Column(name = "answer_b")
	private String answerB;
	@Column(name = "answer_c")
	private String answerC;
	@Column(name = "answer_d")
	private String answerD;
	@Column(name = "answer_true")
	private String answerTrue;
	@Column(name = "status")
	private Boolean status;
	@Column(name = "updated_at")
	private Date updatedAt;
	@Column(name = "deleted_at")
	private Date deletedAt;

}
