package com.hust.daotao.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hust.daotao.entity.UserProfile;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Integer> {
	@Transactional
	@Modifying
	@Query("Update UserProfile as up Set up.value =:value, up.updatedAt =:updated_at  Where up.profile.id =:profile_id and up.user.id =:user_id")
	public void update(@Param("value") String value, @Param("updated_at") Date timestamp,
			@Param("profile_id") Integer profileId, @Param("user_id") Integer userId);

	@Query("select up from UserProfile up where up.deletedAt is null and up.user.id =:user_id and up.profile.id =:profile_id")
	public UserProfile findByUserAndProfile(@Param("profile_id") Integer profileId, @Param("user_id") Integer userId);

	@Query("select up from UserProfile up where up.deletedAt is null and up.user.id =:user_id and up.profile.status = true order by up.profile.id asc")
	public List<UserProfile> findByUser(@Param("user_id") Integer userId);

	@Transactional
	@Modifying
	@Query("Update UserProfile as up Set up.deletedAt =:deleted_at Where up.profile.id =:profile_id")
	public void deleteByProfile(@Param("deleted_at") Date timestamp, @Param("profile_id") Integer profileId);

	@Transactional
	@Modifying
	@Query("Update UserProfile as up Set up.deletedAt =:deleted_at Where up.profile.id =:profile_id and up.value =:value")
	public void deleteByProfileAndValue(@Param("deleted_at") Date timestamp, @Param("profile_id") Integer profileId,
			@Param("value") Integer value);
	
	@Transactional
	@Modifying
	@Query("Update UserProfile as up Set up.deletedAt =:deleted_at Where up.user.id =:user_id")
	public void deleteByUser(@Param("deleted_at") Date timestamp, @Param("user_id") Integer userId);
}
