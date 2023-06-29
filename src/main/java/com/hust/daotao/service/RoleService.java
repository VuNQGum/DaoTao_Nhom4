package com.hust.daotao.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hust.daotao.entity.Role;
import com.hust.daotao.repository.RoleRepository;

@Service
public class RoleService {
	@Autowired
	private RoleRepository roleRepository;
	
	public Role findById(Integer id) {
		return roleRepository.findById(id).get();
	}
}
