
package com.hust.daotao.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import com.hust.daotao.entity.Role;


@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

}
