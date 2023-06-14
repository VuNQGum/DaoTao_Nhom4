package com.hust.thesis.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hust.thesis.entity.KanseiWord;

@Repository
public interface KanseiWordRepository extends JpaRepository<KanseiWord, Integer> {
//	@Transactional
//	@Modifying
//	@Query("Update CriteriaKanseiWord as c Set c.deletedAt =:deleted_at Where c.criteria.id =:criteria_id")
//	public void deleteByCriteria(@Param("deleted_at") String timestamp, @Param("criteria_id") Integer criteriaId);

//	@Query("SELECT c FROM CriteriaKanseiWord c WHERE c.name like %:name% AND c.code like %:code% AND c.deletedAt is null AND (:status is null or c.status = :status) And c.criteriaType.id =:type_id")
//	Page<KanseiWord> getList(@Param("name") String name, @Param("code") String code, @Param("status") Boolean status,
//			Pageable pageable, @Param("type_id") Integer typeId);
//
//	@Query("SELECT count(c) FROM CriteriaKanseiWord c WHERE c.name like %:name% AND c.code like %:code% AND c.deletedAt is null AND (:status is null or c.status = :status) And c.criteriaType.id =:type_id")
//	Long countList(@Param("name") String name, @Param("code") String code, @Param("status") Boolean status,
//			@Param("type_id") Integer typeId);

	KanseiWord findByIdAndDeletedAt(Integer id, Date deleteAt);

//	KanseiWord findByCodeAndDeletedAt(String code, String deletedAt);

	@Query("SELECT c FROM KanseiWord c WHERE c.negativeWord like %:negative% AND c.positiveWord like %:positive% AND c.deletedAt is null AND (:status is null or c.status = :status)")
	Page<KanseiWord> getList(@Param("negative") String negative, @Param("positive") String positive, @Param("status") Boolean status,
			Pageable pageable);

	@Query("SELECT count(c) FROM KanseiWord c WHERE c.negativeWord like %:negative% AND c.positiveWord like %:positive% AND c.deletedAt is null AND (:status is null or c.status = :status) ")
	Long countList(@Param("negative") String negative, @Param("positive") String positive, @Param("status") Boolean status);
	
	List<KanseiWord> findByDeletedAtAndStatus(Date deltedAt, Boolean status);
}
