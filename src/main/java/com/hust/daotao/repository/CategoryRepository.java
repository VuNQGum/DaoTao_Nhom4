package com.hust.thesis.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hust.thesis.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
	@Query("SELECT t FROM Category t WHERE t.name like %:name% AND t.code like %:code% AND t.deletedAt is null AND (:status is null or status = :status)")
	Page<Category> getList(@Param("name") String name, @Param("code") String code, @Param("status") Boolean status,
			Pageable pageable);

	@Query("SELECT count(t) FROM Category t WHERE t.name like %:name% AND t.code like %:code% AND t.deletedAt is null AND (:status is null or status = :status)")
	Long countList(@Param("name") String name, @Param("code") String code, @Param("status") Boolean status);

	Category findByIdAndDeletedAt(Integer id, Date deleteAt);

	Category findByCodeAndDeletedAt(String code, Date deletedAt);

	List<Category> findByStatusAndDeletedAt(Boolean status, Date deletedAt);

	@Query("Select c From Category c Where deletedAt is null And status = true And id < 7 And id > 1")
	List<Category> getListCategorySoict();

	List<Category> findByDeletedAt(Date deletedAt);
}
