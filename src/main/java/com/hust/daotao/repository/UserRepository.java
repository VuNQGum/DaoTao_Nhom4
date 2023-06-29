
package com.hust.daotao.repository;


import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import com.hust.daotao.entity.User;


@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
	@Query("select u from User u where u.email =:email and deletedAt is null")
	public User findByEmailAndDeletedAt(@Param("email") String email);

	@Query("SELECT u FROM User u WHERE u.fullName like %:name% AND u.email like %:email% AND u.deletedAt is null AND (:status is null or status = :status) AND (:role_id is null or u.role.id = :role_id) AND u.role.id > 1")
	Page<User> getList(@Param("name") String name, @Param("email") String email, @Param("status") Boolean status,
			@Param("role_id") Integer roleId, Pageable pageable);

	@Query("SELECT count(u) FROM User u WHERE u.fullName like %:name% AND u.email like %:email% AND u.deletedAt is null AND (:status is null or status = :status) AND (:role_id is null or u.role.id = :role_id) AND u.role.id > 1")
	Long countList(@Param("name") String name, @Param("email") String email, @Param("status") Boolean status,
			@Param("role_id") Integer roleId);

	User findByIdAndDeletedAt(Integer id, Date deleteAt);

	@Query("SELECT u FROM User u WHERE u.deletedAt is null and u.role.id =:role_id and u.status = true")
	List<User> getByRole(@Param("role_id") Integer roleId);
	
	@Query("Select u From User u Where deletedAt is null And u.status = true And u.role.id = 3")
	List<User> getListStudent();
	
	User findByEmailAndDeletedAtAndStatus(String email, String deletedAt, Boolean status);
	
	@Query("Select count(u) From User u Where deletedAt is null And  u.role.id = 3")
	Integer countStudent();
	
	@Query("Select count(u) From User u Where deletedAt is null And  u.role.id  = 2")
	Integer countTeacher();
}
