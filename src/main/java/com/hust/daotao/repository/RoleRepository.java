package com.hust.thesis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hust.thesis.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

}
