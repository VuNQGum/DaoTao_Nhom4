
package com.hust.daotao.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import com.hust.daotao.entity.Criteria;


@Repository
public interface CriteriaRepository extends JpaRepository<Criteria, Integer> {
	@Query("SELECT c FROM Criteria c WHERE c.name like %:name% AND c.code like %:code% AND c.deletedAt is null AND (:status is null or c.status = :status) And c.criteriaType.id =:type_id")
	Page<Criteria> getList(@Param("name") String name, @Param("code") String code, @Param("status") Boolean status,
			Pageable pageable, @Param("type_id") Integer typeId);

	@Query("SELECT count(c) FROM Criteria c WHERE c.name like %:name% AND c.code like %:code% AND c.deletedAt is null AND (:status is null or c.status = :status) And c.criteriaType.id =:type_id")
	Long countList(@Param("name") String name, @Param("code") String code, @Param("status") Boolean status,
			@Param("type_id") Integer typeId);

	Criteria findByIdAndDeletedAt(Integer id, Date deleteAt);

	Criteria findByCodeAndDeletedAt(String code, Date deletedAt);

	@Query("SELECT c FROM Criteria c WHERE  c.deletedAt is null AND c.status = true And c.criteriaType.status = true order by c.id asc")
	List<Criteria> getListCriteriaShow();

	@Query("SELECT c.id FROM Criteria c WHERE  c.deletedAt is null AND c.status = true And c.criteriaType.status = true")
	List<Integer> getListCriteriaUse();

	@Query("SELECT c.id FROM Criteria c WHERE  c.deletedAt is null AND c.status = true And c.criteriaType.status = true")
	List<Integer> getListCriteriaIdUse();

	@Query("SELECT c FROM Criteria c WHERE  c.deletedAt is null AND c.status = true And c.criteriaType.id =:type_id")
	List<Criteria> getListCriteriaByType(@Param("type_id") Integer typeId);

}
