package com.hust.daotao.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.hust.daotao.dto.LessonOfCourseDto;
import com.hust.daotao.entity.LessonOfCourse;
import com.hust.daotao.repository.LessonOfCourseRepository;
import com.hust.daotao.response.LessonOfCourseResponse;
import com.hust.daotao.util.Constants;

@Service
public class LessonOfCourseService {
	@Autowired
	private LessonOfCourseRepository lessonOfCourseRepository;
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private CourseService courseService;

	public LessonOfCourseResponse getList(Integer page, String name, String code, Boolean status, Integer courseId) {
		Sort sortable = null;
		sortable = Sort.by("id").ascending();
		Pageable pageable = PageRequest.of(page, Constants.pageSize, sortable);
		List<LessonOfCourse> listLesson = lessonOfCourseRepository.getList(name, code, status, courseId, pageable)
				.getContent();
		LessonOfCourseResponse lessonResponse = new LessonOfCourseResponse();
		List<LessonOfCourseDto> lessonDtos = convertListBo(listLesson);
		lessonResponse.setAaData(lessonDtos);
		return lessonResponse;
	}

	public Long countList(String name, String code, Boolean status, Integer courseId) {
		return lessonOfCourseRepository.countList(name, code, status, courseId);
	}

	public LessonOfCourse findByCode(String code) {
		return lessonOfCourseRepository.findByCodeAndDeletedAt(code, null);
	}

	public LessonOfCourseDto save(LessonOfCourseDto lessonOfCourseDto) {
		LessonOfCourse lesson = convertDtoToBo(lessonOfCourseDto);
		lesson.setCourseOfLesson(courseService.findById(lessonOfCourseDto.getCourseOfLessonId()));
		LessonOfCourse result = lessonOfCourseRepository.save(lesson);
		LessonOfCourseDto resultDto = convertBoToDto(result);
		resultDto.setCourseOfLessonId(lessonOfCourseDto.getCourseOfLessonId());
		return resultDto;
	}

	public LessonOfCourseDto update(LessonOfCourseDto lessonOfCourseDto) {
		return save(lessonOfCourseDto);
	}

	public Boolean changeStatus(Integer id) {
		LessonOfCourse lesson = findById(id);
		if (lesson == null)
			return false;
		lesson.setStatus(!lesson.getStatus());
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		lesson.setUpdatedAt(timestamp);
		lessonOfCourseRepository.save(lesson);
		return true;
	}

	public Boolean delete(Integer id) {
		LessonOfCourse lesson = findById(id);
		if (lesson == null) {
			return false;
		}
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		lesson.setDeletedAt(timestamp);
		lessonOfCourseRepository.save(lesson);
		return true;
	}

	public LessonOfCourse findById(Integer id) {
		return lessonOfCourseRepository.findByIdAndDeletedAt(id, null);
	}

	public LessonOfCourseDto findByIdDto(Integer id) {
		LessonOfCourse lesson = findById(id);
		if (lesson == null)
			return null;
		LessonOfCourseDto dto = convertBoToDto(lesson);
		dto.setCourseOfLessonDto(courseService.convertBoToDto(lesson.getCourseOfLesson()));
		dto.setCourseOfLessonId(lesson.getCourseOfLesson().getId());
		return dto;
	}

	public LessonOfCourse convertDtoToBo(LessonOfCourseDto lessonOfCourseDto) {
		return modelMapper.map(lessonOfCourseDto, LessonOfCourse.class);
	}

	public LessonOfCourseDto convertBoToDto(LessonOfCourse lessonOfCourse) {
		return modelMapper.map(lessonOfCourse, LessonOfCourseDto.class);
	}

	public boolean deleteByCourse(Date timestamp, Integer courseId) {
		lessonOfCourseRepository.deleteByCourse(timestamp, courseId);
		return true;
	}

	public List<LessonOfCourseDto> convertListBo(List<LessonOfCourse> list) {
		List<LessonOfCourseDto> listDtos = new ArrayList<LessonOfCourseDto>();
		if (list != null) {
			if (!CollectionUtils.isEmpty(list)) {
				listDtos = list.parallelStream().map(bo -> convertBoToDto(bo)).collect(Collectors.toList());
			}
		}
		return listDtos;
	}
}
