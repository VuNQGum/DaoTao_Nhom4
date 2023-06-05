package com.hust.thesis.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hust.thesis.entity.CourseProfile;

@Repository
public interface CourseProfileRepository extends JpaRepository<CourseProfile, Integer> {
	@Transactional
	@Modifying
	@Query("Update CourseProfile as cp Set cp.deletedAt =:deleted_at Where cp.profile.id =:profile_id")
	public void deleteByProfile(@Param("deleted_at") Date timestamp, @Param("profile_id") Integer profileId);

	@Transactional
	@Modifying
	@Query("Update CourseProfile as cp Set cp.deletedAt =:deleted_at Where cp.profile.id =:profile_id and cp.value =:value")
	public void deleteByProfileAndValue(@Param("deleted_at") Date timestamp, @Param("profile_id") Integer profileId,
			@Param("value") Integer value);

	@Transactional
	@Modifying
	@Query("Update CourseProfile as cp Set cp.deletedAt =:deleted_at Where cp.course.id =:course_id")
	public void deleteByCourse(@Param("deleted_at") Date timestamp, @Param("course_id") Integer courseId);

	@Query("select cp from CourseProfile cp where cp.deletedAt is null and cp.course.id =:course_id and cp.profile.status = true order by cp.profile.id asc")
	public List<CourseProfile> findByCourse(@Param("course_id") Integer courseId);

	@Transactional
	@Modifying
	@Query("Update CourseProfile as cp Set cp.value =:value Where cp.course.id =:course_id And cp.profile.id =:profile_id")
	public void updateByCourse(@Param("value") String value, @Param("course_id") Integer courseId,
			@Param("profile_id") Integer profileId);
	
	@Query("select cp from CourseProfile cp where cp.deletedAt is null and cp.course.id =:course_id and cp.profile.status = true and cp.profile.id =:profile_id")
	public CourseProfile findByCourseAndProfileId(@Param("course_id") Integer courseId, @Param("profile_id") Integer profileId);
}
