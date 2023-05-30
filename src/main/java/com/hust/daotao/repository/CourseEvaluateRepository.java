package com.hust.thesis.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hust.thesis.entity.CourseEvaluate;

@Repository
public interface CourseEvaluateRepository extends JpaRepository<CourseEvaluate, Integer> {
	@Query("select sr From CourseEvaluate sr Where sr.course.id =:courseId and sr.criteria.id =:criteriaId And sr.deletedAt is null")
	CourseEvaluate findByCourseIdAndCriteriaId(Integer courseId, Integer criteriaId);

	@Query("select sr From CourseEvaluate sr Where sr.course.id =:courseId and sr.criteria.criteriaType.status = true And sr.deletedAt is null")
	List<CourseEvaluate> findByCourseIdAndCriteriaType(Integer courseId);

	@Query("select avg(e.value) From CourseEvaluate e Where e.course.id =:course_id And e.criteria.criteriaType.id =:criteria_type")
	Double getAverage(@Param("course_id") Integer courseId, @Param("criteria_type") Integer criteriaType);
	
	@Query("select sr From CourseEvaluate sr Where sr.criteria.criteriaType.id =:typeId And sr.deletedAt is null And sr.criteria.deletedAt is null order by sr.criteria.id")
	List<CourseEvaluate> getByCriteriaType(@Param("typeId") Integer typeId);
	
	@Query("select avg(sr.value) From CourseEvaluate sr Where sr.criteria.criteriaType.id =:typeId And sr.deletedAt is null And sr.criteria.deletedAt is null And sr.course.id =:course_id ")
	Double avgByCriteriaTypeAndCourse(@Param("typeId") Integer typeId, @Param("course_id") Integer courseId);
	
	@Query("select avg(sr.value) From CourseEvaluate sr Where sr.criteria.id =:criteria_id And sr.deletedAt is null And sr.criteria.deletedAt is null And sr.course.id =:course_id group by sr.criteria.id")
	Double avgByCourseAndCriteria(@Param("criteria_id") Integer criteriaId, @Param("course_id") Integer courseId);
	
	@Query("select sr From CourseEvaluate sr Where sr.criteria.criteriaType.id =:typeId And sr.deletedAt is null And sr.criteria.deletedAt is null order by sr.course.id desc")
	List<CourseEvaluate> getByCriteriaTypeOrderByCourse(@Param("typeId") Integer typeId);

}
