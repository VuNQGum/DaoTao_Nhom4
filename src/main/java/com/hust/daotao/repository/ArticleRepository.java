package com.hust.daotao.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hust.daotao.entity.Article;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Integer> {
	Article findByIdAndDeletedAt(Integer id, Date deletedAt);

	@Query("Select c From Article as c Where c.deletedAt is null")
	public Page<Article> getListCourse(Pageable pageable);

	@Query("Select c From Article as c Where c.deletedAt is null")
	public List<Article> getListArticleShowClient(Pageable pageable);

	@Query("SELECT count(t) FROM Article t WHERE t.title like %:title% AND t.deletedAt is null")
	Long countList(@Param("title") String title);

	@Query("SELECT t FROM Article t WHERE t.title like %:title% AND t.deletedAt is null And t.userPostArticle.id =:userId")
	Page<Article> getList(@Param("title") String title, @Param("userId") Integer userId, Pageable pageable);
}
