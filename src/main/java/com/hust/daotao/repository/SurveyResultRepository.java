package com.hust.thesis.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hust.thesis.entity.SurveyResult;

@Repository
public interface SurveyResultRepository extends JpaRepository<SurveyResult, Integer> {
	@Query("select sr From SurveyResult sr Where sr.user.id =:userId and sr.criteria.id =:criteriaId And sr.deletedAt is null")
	SurveyResult getByUserIdAndCriteriaId(@Param("userId") Integer userId, @Param("criteriaId") Integer criteriaId);
	
	@Query("select sr From SurveyResult sr Where sr.user.id =:userId And sr.criteria.criteriaType.status =true And sr.deletedAt is null")
	List<SurveyResult> getByUserIdAndCriteriaType(@Param("userId") Integer userId);
	
	@Query("select sr From SurveyResult sr Where sr.criteria.criteriaType.id =:typeId And sr.deletedAt is null And sr.criteria.deletedAt is null order by sr.criteria.id")
	List<SurveyResult> getByCriteriaType(@Param("typeId") Integer typeId);
	
	@Query("select sr From SurveyResult sr Where sr.criteria.criteriaType.id =:typeId And sr.deletedAt is null And sr.criteria.deletedAt is null order by sr.user.id desc")
	List<SurveyResult> getByCriteriaTypeOrderByUser(@Param("typeId") Integer typeId);
}
