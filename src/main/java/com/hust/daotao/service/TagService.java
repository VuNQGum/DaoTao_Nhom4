package com.hust.daotao.service;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hust.daotao.dto.TagDto;
import com.hust.daotao.entity.Tag;
import com.hust.daotao.repository.TagRepository;

@Service
public class TagService {
	@Autowired
	private TagRepository tagRepository;
	@Autowired
	private ModelMapper modelMapper;
	
	public Tag findById(Integer id) {
		return tagRepository.findByIdAndDeletedAt(id, null);
	}
	
	public List<Tag> getListTag() {
		return tagRepository.findByDeletedAt(null);
	}
	public Tag convertDtoToBo(TagDto dto) {
		return modelMapper.map(dto, Tag.class);
	}

	public TagDto convertBoToDto(Tag bo) {
		return modelMapper.map(bo, TagDto.class);

	}
	
	public List<TagDto> convertListBoToListDto(List<Tag> bos) {
		List<TagDto> dtos = new ArrayList<TagDto>();
		if (bos != null) {
			for (Tag a : bos) {
				TagDto dto = convertBoToDto(a);
//				courseDto.setCategoryDto(categorySerivce.convertBoToDto(course.getCategoryOfCourse()));
//				courseDto.setTeacherDto(userSerivce.convertBoToDto(course.getTeacher()));
				dtos.add(dto);

			}
		}
		return dtos;
	}
}
