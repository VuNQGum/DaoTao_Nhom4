
package com.hust.daotao.repository;


import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import com.hust.daotao.entity.StudentCourse;


@Repository
public interface StudentCourseRepository extends JpaRepository<StudentCourse, Integer> {
	@Query("Select sc From StudentCourse sc Where sc.student.id =:student_id And sc.courseOfStudent.id =:course_id And sc.deletedAt is null")
	public StudentCourse findByCourseAndStudent(@Param("student_id") Integer studentId,
			@Param("course_id") Integer courseId);

	@Query("Select sc From StudentCourse sc Where sc.student.id =:student_id And sc.status = true And sc.deletedAt is null")
	public List<StudentCourse> getListCourseOfStudent(@Param("student_id") Integer studentId);

	@Query("Select sc From StudentCourse sc Where sc.student.id =:student_id And sc.courseOfStudent.id =:course_id And sc.deletedAt is null And sc.status = true")
	public StudentCourse findByCourseAndStudentLearn(@Param("student_id") Integer studentId,
			@Param("course_id") Integer courseId);

	@Query("SELECT sc FROM StudentCourse sc WHERE sc.student.fullName like %:name% AND sc.student.email like %:email% AND sc.deletedAt is null AND (:status is null or sc.status = :status) And sc.courseOfStudent.id =:course_id")
	Page<StudentCourse> getList(@Param("name") String name, @Param("email") String email,
			@Param("status") Boolean status, @Param("course_id") Integer courseId, Pageable pageable);

	@Query("SELECT count(sc) FROM StudentCourse sc WHERE sc.student.fullName like %:name% AND sc.student.email like %:email% AND sc.deletedAt is null AND (:status is null or sc.status = :status) And sc.courseOfStudent.id =:course_id")
	Long countList(@Param("name") String name, @Param("email") String email, @Param("status") Boolean status,
			@Param("course_id") Integer courseId);

	public StudentCourse findByIdAndDeletedAt(Integer id, Date deletedAt);
	
	
}
