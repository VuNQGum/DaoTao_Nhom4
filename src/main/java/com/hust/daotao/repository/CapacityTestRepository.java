package com.hust.daotao.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hust.daotao.entity.CapacityTest;

@Repository
public interface CapacityTestRepository extends JpaRepository<CapacityTest, Integer> {
	@Query("SELECT pv FROM CapacityTest pv WHERE pv.question like %:question% AND pv.deletedAt is null AND (:status is null or pv.status =:status)")
	Page<CapacityTest> getListQuestion(@Param("question") String question, @Param("status") Boolean status,
			Pageable pageable);

	@Query("SELECT count(pv) FROM CapacityTest pv WHERE pv.question like %:question% AND pv.deletedAt is null AND (:status is null or pv.status =:status)")
	Long countListProfileValues(@Param("question") String question, @Param("status") Boolean status);

	CapacityTest findByIdAndDeletedAt(Integer id, Date deletedAt);

	List<CapacityTest> findByStatusAndDeletedAt(Pageable pageable, Boolean status, Date deletedAt);

}
