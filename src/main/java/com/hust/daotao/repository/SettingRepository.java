package com.hust.thesis.repository;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hust.thesis.entity.Setting;

@Repository
public interface SettingRepository extends JpaRepository<Setting, Integer> {
	@Query("SELECT s FROM Setting s WHERE s.name like %:name% And s.code like %:code% And s.deletedAt is null")
	Page<Setting> getList(@Param("name") String name, @Param("code") String code, Pageable pageable);

	@Query("SELECT count(s) FROM Setting s WHERE s.name like %:name% And s.code like %:code%  AND s.deletedAt is null")
	Long countList(@Param("name") String name, @Param("code") String code);

	Setting findByIdAndDeletedAt(Integer id, Date deletedAt);

	Setting findByCodeAndDeletedAt(String code, Date deletedAt);
}
