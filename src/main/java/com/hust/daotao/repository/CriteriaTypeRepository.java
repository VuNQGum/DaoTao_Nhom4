
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


import com.hust.daotao.entity.CriteriaType;


@Repository
public interface CriteriaTypeRepository extends JpaRepository<CriteriaType, Integer> {
	@Query("SELECT c FROM CriteriaType c WHERE c.name like %:name% AND c.code like %:code% AND c.deletedAt is null AND (:status is null or c.status = :status)")
	Page<CriteriaType> getList(@Param("name") String name, @Param("code") String code, @Param("status") Boolean status,
			Pageable pageable);

	@Query("SELECT count(c) FROM CriteriaType c WHERE c.name like %:name% AND c.code like %:code% AND c.deletedAt is null AND (:status is null or c.status = :status)")
	Long countList(@Param("name") String name, @Param("code") String code, @Param("status") Boolean status);

	CriteriaType findByIdAndDeletedAt(Integer id, Date deleteAt);

	CriteriaType findByCodeAndDeletedAt(String code, Date deletedAt);

	@Query("SELECT count(c) FROM CriteriaType c WHERE c.deletedAt is null")
	Integer countCriteriaType();

	@Transactional
	@Modifying
	@Query("Update CriteriaType as cp Set status = false Where id !=:id")
	public void updateStatus(@Param("id") Integer id);

	@Query("Select t From CriteriaType t WHERE t.deletedAt is null And t.status = true")
	public CriteriaType getCriteraTypeUse();
	
	@Query("Select t From CriteriaType t WHERE t.deletedAt is null")
	public List<CriteriaType> getCriteraTypes();

}
