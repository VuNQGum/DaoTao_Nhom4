
package com.hust.daotao.repository;


import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import com.hust.daotao.entity.Profile;


@Repository
public interface ProfileRepository extends JpaRepository<Profile, Integer>{
	@Query("SELECT p FROM Profile p WHERE p.name like %:name% AND p.code like %:code% AND p.deletedAt is null AND (:status is null or p.status = :status)")
	Page<Profile> getListProfiles(@Param("name") String name, @Param("code") String code, @Param("status") Boolean status,
			Pageable pageable);

	@Query("SELECT count(p) FROM Profile p WHERE p.name like %:name% AND p.code like %:code% AND p.deletedAt is null AND (:status is null or p.status = :status)")
	Long countListProfile(@Param("name") String name, @Param("code") String code, @Param("status") Boolean status);

	Profile findByIdAndDeletedAt(Integer id, Date deleteAt);
	Profile findByCodeAndDeletedAt(String code, Date deletedAt);
	@Query("Select p from Profile p Where p.deletedAt is null And p.status =:status")
	List<Profile> getByStatus(Boolean status);
	
	@Query("Select p from Profile p Where p.deletedAt is null And p.status =:status And (p.show = 2 Or p.show = 0) order by id asc")
	List<Profile> getListProfileOfStudent(Boolean status);
	
	@Query("Select p from Profile p Where p.deletedAt is null And p.status =:status And (p.show = 2 Or p.show = 1) order by id asc")
	List<Profile> getListProfileOfCourse(Boolean status);
	
}
