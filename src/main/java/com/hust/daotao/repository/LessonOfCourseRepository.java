package com.hust.thesis.repository;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hust.thesis.entity.LessonOfCourse;

@Repository
public interface LessonOfCourseRepository extends JpaRepository<LessonOfCourse, Integer> {
	@Query("SELECT l FROM LessonOfCourse l WHERE l.name like %:name% AND l.code like %:code% AND l.deletedAt is null AND (:status is null or status = :status) AND l.courseOfLesson.id =:course_id")
	Page<LessonOfCourse> getList(@Param("name") String name, @Param("code") String code,
			@Param("status") Boolean status, @Param("course_id") Integer courseId, Pageable pageable);

	@Query("SELECT count(l) FROM LessonOfCourse l WHERE l.name like %:name% AND l.code like %:code% AND l.deletedAt is null AND (:status is null or status = :status) AND l.courseOfLesson.id =:course_id")
	Long countList(@Param("name") String name, @Param("code") String code, @Param("status") Boolean status, @Param("course_id") Integer courseId);

	LessonOfCourse findByCodeAndDeletedAt(String code, Date deletedAt);

	LessonOfCourse findByIdAndDeletedAt(Integer id, Date deletedAt);
	
	@Transactional
	@Modifying
	@Query("Update LessonOfCourse as l Set l.deletedAt =:deleted_at Where l.courseOfLesson.id =:course_id")
	public void deleteByCourse(@Param("deleted_at") Date timestamp, @Param("course_id") Integer courseId);

}
