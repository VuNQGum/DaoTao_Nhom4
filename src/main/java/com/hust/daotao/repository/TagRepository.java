package com.hust.thesis.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.hust.thesis.entity.Tag;
public interface TagRepository extends JpaRepository<Tag, Integer> {
	Tag findByIdAndDeletedAt(Integer id, Date deletedAt);
	List<Tag> findByDeletedAt(Date deletedAt);

}
