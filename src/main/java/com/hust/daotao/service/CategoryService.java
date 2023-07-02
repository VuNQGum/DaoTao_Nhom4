package com.hust.daotao.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.hust.daotao.dto.CategoryDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;


import com.hust.daotao.entity.Category;
import com.hust.daotao.repository.CategoryRepository;
import com.hust.daotao.response.CategoryResponse;
import com.hust.daotao.util.Constants;

@Service
public class CategoryService {
	@Autowired
	private CategoryRepository categoryRepository;
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private CourseService courseService;

	public CategoryResponse getList(Integer page, String name, String code, Boolean status) {
		Sort sortable = null;
		sortable = Sort.by("updatedAt").descending();
		Pageable pageable = PageRequest.of(page, Constants.pageSize, sortable);
		List<Category> listCategory = categoryRepository.getList(name, code, status, pageable).getContent();
		CategoryResponse categoryResponse = new CategoryResponse();
		List<CategoryDto> categoryDtos = new ArrayList<CategoryDto>();
		if (listCategory != null) {
			if (!CollectionUtils.isEmpty(listCategory)) {
				categoryDtos = listCategory.parallelStream().map(category -> convertBoToDto(category))
						.collect(Collectors.toList());
			}
		}
		categoryResponse.setAaData(categoryDtos);
		return categoryResponse;
	}

	public Long countList(String name, String code, Boolean status) {
		return categoryRepository.countList(name, code, status);
	}

	public CategoryDto findByCode(String code) {
		Category category = categoryRepository.findByCodeAndDeletedAt(code, null);
		return (category != null ? convertBoToDto(category) : null);
	}

	public CategoryDto findByIdDto(Integer id) {
		Category category = findById(id);
		return (category != null ? convertBoToDto(category) : null);
	}

	public Boolean delete(Integer id) {
		Category category = findById(id);
		if (category == null) {
			return false;
		}
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		courseService.deleteByCategory(timestamp, category);
		category.setDeletedAt(timestamp);
		categoryRepository.save(category);
		return true;
	}

	public Boolean changeStatus(Integer id) {
		Category category = findById(id);
		if (category == null)
			return false;
		category.setStatus(!category.getStatus());
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		category.setUpdatedAt(timestamp);
		categoryRepository.save(category);
		return true;
	}

	public CategoryDto save(CategoryDto categoryDto) {
		return convertBoToDto(categoryRepository.save(convertDtoToBo(categoryDto)));
	}

	public CategoryDto update(CategoryDto categoryDto) {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		categoryDto.setUpdatedAt(timestamp);
		return convertBoToDto(categoryRepository.save(convertDtoToBo(categoryDto)));
	}

	public Category findById(Integer id) {
		return categoryRepository.findByIdAndDeletedAt(id, null);
	}

	public List<CategoryDto> getCategoryIsShow() {
		List<Category> listCategory = categoryRepository.findByStatusAndDeletedAt(true, null);
		List<CategoryDto> categoryDtos = convertListCategory(listCategory);
		return categoryDtos;
	}
	public List<Category> getListCategory() {
		return categoryRepository.findByDeletedAt(null);
	}
	public List<CategoryDto> getCategoryIct() {
		List<Category> listCategory = categoryRepository.getListCategorySoict();
		List<CategoryDto> categoryDtos = convertListCategory(listCategory);
		return categoryDtos;
	}
	public Category convertDtoToBo(CategoryDto categoryDto) {
		return modelMapper.map(categoryDto, Category.class);
	}

	public CategoryDto convertBoToDto(Category category) {
		return modelMapper.map(category, CategoryDto.class);
	}
	
	public List<CategoryDto> convertListCategory(List<Category> bos) {
		List<CategoryDto> categoryDtos = new ArrayList<CategoryDto>();
		if (bos != null) {
			if (!CollectionUtils.isEmpty(bos)) {
				categoryDtos = bos.parallelStream().map(category -> convertBoToDto(category))
						.collect(Collectors.toList());
			}
		}
		return categoryDtos;
	}

}
