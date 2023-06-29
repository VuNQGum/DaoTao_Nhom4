
package com.hust.daotao.repository;


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


import com.hust.daotao.entity.ProfileValue;


@Repository
public interface ProfileValueRepository extends JpaRepository<ProfileValue, Integer> {
	@Transactional
	@Modifying
	@Query("Update ProfileValue as pv Set pv.deletedAt =:deleted_at Where pv.profile.id =:profile_id")
	public void deleteByProfile(@Param("deleted_at") Date timestamp, @Param("profile_id") Integer profileId);
	
	@Query("SELECT pv FROM ProfileValue pv WHERE pv.name like %:name% AND pv.code like %:code% AND pv.deletedAt is null AND (:status is null or pv.status = :status) AND pv.profile.id =:profile_id")
	Page<ProfileValue> getListProfileValues(@Param("name") String name, @Param("code") String code, @Param("status") Boolean status, @Param("profile_id") Integer profileId,
			Pageable pageable);

	@Query("SELECT count(pv) FROM ProfileValue pv WHERE pv.name like %:name% AND pv.code like %:code% AND pv.deletedAt is null AND (:status is null or pv.status = :status) AND pv.profile.id =:profile_id")
	Long countListProfileValues(@Param("name") String name, @Param("code") String code, @Param("status") Boolean status, @Param("profile_id") Integer profileId);
	
	ProfileValue findByIdAndDeletedAt(Integer id, Date deleteAt);
	
	@Query("SELECT pv FROM ProfileValue pv WHERE pv.deletedAt is null AND pv.code =:code AND pv.profile.id =:profile_id")
	ProfileValue findByCodeAndProfile(@Param("code") String code, @Param("profile_id") Integer profileId);
	
	@Query("SELECT pv FROM ProfileValue pv WHERE pv.deletedAt is null AND pv.value =:value AND pv.profile.id =:profile_id")
	ProfileValue findByValueAndProfile(@Param("value") Integer value, @Param("profile_id") Integer profileId);
	
	@Query("SELECT pv from ProfileValue pv where pv.status = true and pv.profile.id =:profile_id and deletedAt is null")
	List<ProfileValue> findByStatusAnProfile(@Param("profile_id") Integer id);
}
