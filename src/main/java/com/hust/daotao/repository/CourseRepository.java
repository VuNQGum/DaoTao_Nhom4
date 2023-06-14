package com.hust.thesis.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hust.thesis.entity.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {
	@Query("SELECT c FROM Course c WHERE c.name like %:name% AND c.code like %:code% AND c.deletedAt is null AND (:status is null or c.status = :status) AND (:category_id is null or c.categoryOfCourse.id = :category_id) AND (:user_id is null or c.teacher.id = :user_id)")
	Page<Course> getList(@Param("name") String name, @Param("code") String code, @Param("status") Boolean status,
			@Param("category_id") Integer categoryId, @Param("user_id") Integer userId, Pageable pageable);

	@Query("SELECT count(c) FROM Course c WHERE c.name like %:name% AND c.code like %:code% AND c.deletedAt is null AND (:status is null or c.status = :status) AND (:category_id is null or c.categoryOfCourse.id = :category_id) AND (:user_id is null or c.teacher.id = :user_id)")
	Long countList(@Param("name") String name, @Param("code") String code, @Param("status") Boolean status,
			@Param("category_id") Integer categoryId, @Param("user_id") Integer userId);

	Course findByIdAndDeletedAt(Integer id, Date deleteAt);

	Course findByCodeAndDeletedAt(String code, Date deleteAt);

	List<Course> findByStatusAndDeletedAtOrderByUpdatedAtDesc(Boolean status, Date deleteAt);

	@Transactional
	@Modifying
	@Query("Update Course as c Set c.deletedAt =:deleted_at Where c.teacher.id =:user_id")
	public void deleteByUser(@Param("deleted_at") Date timestamp, @Param("user_id") Integer userId);

//	@Query("Select c From Course as c Where c.deletedAt is null And c.user.id =:user_id")
//	List<Course> findByUser(@Param("user_id") Integer userId);

	@Transactional
	@Modifying
	@Query("Update Course as c Set c.deletedAt =:deleted_at Where c.categoryOfCourse.id =:category_id")
	public void deleteByCategory(@Param("deleted_at") Date timestamp, @Param("category_id") Integer categoryId);

//	@Query("Select c From Course as c Where c.deletedAt is null And c.category.id =:category_id")
//	List<Course> findByCategory(@Param("category_id") Integer categoryId);
	@Query("Select c From Course as c Where c.deletedAt is null And c.status = true and c.name like %:name%  and (:category_id is null or c.categoryOfCourse.id = :category_id) AND (:user_id is null or c.teacher.id = :user_id)")
	public Page<Course> getListCourse(Pageable pageable, @Param("name") String name,
			@Param("category_id") Integer categoryId, @Param("user_id") Integer userId);

	@Query("Select c.name From Course c Where c.deletedAt is null And c.status = true")
	public List<String> getListNameCourse();

	Course findByCodeAndDeletedAtAndStatus(String code, Date deleteAt, Boolean status);

	@Query("Select c From Course c Where c.deletedAt is null And c.status = true")
	public List<Course> getListCourseByQuantityStudent(Pageable pageable);

	@Query("Select c From Course c Where c.deletedAt is null And c.status = true")
	public List<Course> getListCourseByQuantityEvaluate(Pageable pageable);

	@Query("Select count(c) From Course c Where c.deletedAt is null")
	public Integer countCourse();
	
	public List<Course> findCourseByDeletedAt(Date deletedAt);
	
	@Query("Select c From Course c Where c.deletedAt is null And c.categoryOfCourse.id =:category_id")
	public List<Course> getListCourseByCategory(@Param("category_id") Integer categoryId);
	@Query("Select count(*) From Course c Where c.deletedAt is null And c.categoryOfCourse.id =:category_id")
	public Integer countByCategory(@Param("category_id") Integer categoryId);


}
