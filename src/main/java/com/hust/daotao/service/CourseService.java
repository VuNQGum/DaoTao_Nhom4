package com.hust.daotao.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.hust.daotao.dto.CourseDto;
import com.hust.daotao.dto.GraphDto;
import com.hust.daotao.dto.LessonOfCourseDto;
import com.hust.daotao.entity.Category;
import com.hust.daotao.entity.Course;
import com.hust.daotao.entity.CourseProfile;
import com.hust.daotao.entity.User;
import com.hust.daotao.repository.CourseRepository;
import com.hust.daotao.response.CourseResponse;
import com.hust.daotao.util.Constants;

@Service
public class CourseService {
	@Autowired
	private CourseRepository courseRepository;
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private CategoryService categorySerivce;
	@Autowired
	private UserService userSerivce;
	@Autowired
	private CourseProfileService courseProfileService;
	@Autowired
	private LessonOfCourseService lessonService;
	@Autowired
	private CourseEvaluateService evaluateService;
	@Autowired
	private ContextMatching contextMatching;

	public CourseResponse getList(Integer page, String name, String code, Integer categoryId, Integer userId,
			Boolean status) {
		Sort sortable = null;
		sortable = Sort.by("updatedAt").descending();
		Pageable pageable = PageRequest.of(page, Constants.pageSize, sortable);
		List<Course> listCourses = courseRepository.getList(name, code, status, categoryId, userId, pageable)
				.getContent();
		CourseResponse courseResponse = new CourseResponse();
		List<CourseDto> courseDtos = new ArrayList<CourseDto>();
		if (listCourses != null) {
			courseDtos = convertListBoToListDto(listCourses);
		}
		courseResponse.setAaData(courseDtos);
		return courseResponse;
	}

	public Long countList(String name, String code, Boolean status, Integer categoryId, Integer userId) {
		return courseRepository.countList(name, code, status, categoryId, userId);
	}

	public Boolean changeStatus(Integer id) {
		Course course = findById(id);
		if (course == null)
			return false;
		course.setStatus(!course.getStatus());
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		course.setUpdatedAt(timestamp);
		courseRepository.save(course);
		return true;
	}

	public Boolean delete(Integer id) {
		Course course = findById(id);
		if (course == null) {
			return false;
		}
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		course.setDeletedAt(timestamp);
		courseRepository.save(course);
		lessonService.deleteByCourse(timestamp, course.getId());
		courseProfileService.deleteByCourse(timestamp, course);
		return true;
	}

	public Course findById(Integer id) {
		return courseRepository.findByIdAndDeletedAt(id, null);
	}

	public Course findByCode(String code) {
		return courseRepository.findByCodeAndDeletedAt(code, null);
	}

	public CourseDto getCourseShow(String code) {
		Course course = courseRepository.findByCodeAndDeletedAtAndStatus(code, null, true);
		if (course == null)
			return null;
		CourseDto courseDto = convertBoToDto(course);
		List<LessonOfCourseDto> lessonDtos = lessonService.convertListBo(course.getLessonOfCourses());
		Collections.sort(lessonDtos);
		courseDto.setLessonOfCourseDtos(lessonDtos);
		courseDto.setQuantityLesson(lessonDtos.size());
		courseDto.setQuantityStudent(course.getStudentOfCourses().size());
		courseDto.setCategoryDto(categorySerivce.convertBoToDto(course.getCategoryOfCourse()));
		courseDto.setTeacherDto(userSerivce.convertBoToDto(course.getTeacher()));
		return courseDto;
	}

	public CourseDto save(CourseDto courseDto, List<CourseProfile> courseProfiles) {
		Course course = convertDtoToBo(courseDto);
		course.setTeacher(userSerivce.findById(courseDto.getUserId()));
		course.setCategoryOfCourse(categorySerivce.findById(courseDto.getCategoryId()));
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		course.setUpdatedAt(timestamp);
		Course result = courseRepository.save(course);
		for (CourseProfile courseProfile : courseProfiles) {
			courseProfile.setCourse(result);
			courseProfile.setUpdatedAt(timestamp);
			courseProfileService.save(courseProfile);
		}
		return convertBoToDto(result);
	}

	public CourseDto update(CourseDto courseDto, List<CourseProfile> courseProfiles) {
		Course course = convertDtoToBo(courseDto);
		course.setTeacher(userSerivce.findById(courseDto.getUserId()));
		course.setCategoryOfCourse(categorySerivce.findById(courseDto.getCategoryId()));
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		course.setUpdatedAt(timestamp);
		Course result = courseRepository.save(course);
		for (CourseProfile courseProfile : courseProfiles) {
			courseProfile.setCourse(result);
			courseProfile.setUpdatedAt(timestamp);
			courseProfileService.updateByCourse(courseProfile.getValue(), result.getId(),
					courseProfile.getProfile().getId());
		}
		return convertBoToDto(result);
	}

	public void deleteByUser(Date timestamp, User user) {
		List<Course> courses = user.getCourses();
		if (courses != null) {
			for (Course course : courses) {
				courseProfileService.deleteByCourse(timestamp, course);
			}
		}
		courseRepository.deleteByUser(timestamp, user.getId());

	}

	public void deleteByCategory(Date timestamp, Category category) {
		List<Course> courses = category.getCourses();
		if (courses != null) {
			for (Course course : courses) {
				courseProfileService.deleteByCourse(timestamp, course);
			}
		}
		courseRepository.deleteByCategory(timestamp, category.getId());
	}

	public CourseResponse getList(Integer page, Integer quantityShowing, String name, Integer categoryId,
			Integer userId) {
		Sort sortable = null;
		sortable = Sort.by("resultEvaluate").descending().and(Sort.by("updatedAt").descending());
		Pageable pageable = PageRequest.of(page - 1, quantityShowing, sortable);
		if (categoryId <= 0)
			categoryId = null;
		if (userId <= 0)
			userId = null;
		Page<Course> panigation = courseRepository.getListCourse(pageable, name, categoryId, userId);
		List<Course> listCourses = panigation.getContent();
		List<CourseDto> courseDtos = new ArrayList<CourseDto>();
		if (listCourses != null) {
			for (Course course : listCourses) {
				CourseDto courseDto = convertBoToDto(course);
				courseDto.setCategoryDto(categorySerivce.convertBoToDto(course.getCategoryOfCourse()));
				courseDto.setTeacherDto(userSerivce.convertBoToDto(course.getTeacher()));
				courseDto.setQuantityLesson(course.getLessonOfCourses().size());
				courseDto.setQuantityStudent(course.getStudentOfCourses().size());
				courseDtos.add(courseDto);

			}
		}
		CourseResponse courseReponse = new CourseResponse((long) panigation.getTotalElements(),
				(long) panigation.getTotalPages(), courseDtos);
		return courseReponse;
	}

	public Course convertDtoToBo(CourseDto dto) {
		return modelMapper.map(dto, Course.class);
	}

	public CourseDto convertBoToDto(Course bo) {
		return modelMapper.map(bo, CourseDto.class);
	}

	public List<CourseDto> convertListBoToListDto(List<Course> bos) {
		List<CourseDto> courseDtos = new ArrayList<CourseDto>();
		if (bos != null) {
			for (Course course : bos) {
				CourseDto courseDto = convertBoToDto(course);
				courseDto.setQuantityLesson(course.getLessonOfCourses().size());
				courseDto.setQuantityStudent(course.getStudentOfCourses().size());
				courseDto.setCategoryDto(categorySerivce.convertBoToDto(course.getCategoryOfCourse()));
				courseDto.setTeacherDto(userSerivce.convertBoToDto(course.getTeacher()));
				courseDtos.add(courseDto);

			}
		}
		return courseDtos;
	}

	public List<String> getListNameCourse() {
		return courseRepository.getListNameCourse();
	}

	public List<CourseDto> getListCourseByQuantityStudent() {
		Sort sortable = null;
		sortable = Sort.by("quantityStudent").descending().and(Sort.by("id").descending());
		Pageable pageable = PageRequest.of(0, 3, sortable);
		List<Course> courses = courseRepository.getListCourseByQuantityStudent(pageable);
		List<CourseDto> courseDtos = new ArrayList<CourseDto>();
		if (courses != null) {
			courseDtos = convertListBoToListDto(courses);
		}
		return courseDtos;
	}

	public List<CourseDto> getListCourseByEvaluate() {
		Sort sortable = null;
		sortable = Sort.by("resultEvaluate").descending()
				.and(Sort.by("quantityStudent").descending().and(Sort.by("updatedAt").descending()));
		Pageable pageable = PageRequest.of(0, 3, sortable);
		List<Course> courses = courseRepository.getListCourseByQuantityEvaluate(pageable);
		List<CourseDto> courseDtos = new ArrayList<CourseDto>();
		if (courses != null) {
			courseDtos = convertListBoToListDto(courses);
		}
		return courseDtos;
	}

	public Course save(Course course) {
		return courseRepository.save(course);
	}

	public Integer countCourse() {
		return courseRepository.countCourse();
	}

	public List<Course> getListCourseShow() {
		return courseRepository.findByStatusAndDeletedAtOrderByUpdatedAtDesc(true, null);
	}

	public GraphDto countCourseByCategory() {
		List<Category> categories = categorySerivce.getListCategory();
		GraphDto graphDto = new GraphDto();
		List<String> titles = new ArrayList<String>();
		List<Integer> valuesInteger = new ArrayList<Integer>();
		if (categories != null) {
			for (Category category : categories) {
				titles.add(category.getName());
				valuesInteger.add(countByCategory(category.getId()));
			}
		}
		graphDto.setTitles(titles);
		graphDto.setValuesInteger(valuesInteger);
		return graphDto;
	}

	public List<Course> getRankCousreByCriteriaType(int criteriaType) {
		List<Course> courses = courseRepository.findCourseByDeletedAt(null);
		HashMap<Course, Double> ranks = new HashMap<Course, Double>();
		for (Course course : courses) {
			Double avg = evaluateService.getAverage(course.getId(), criteriaType);
			if (avg == null)
				avg = 0.0;
			double val = (double) Math.round(avg * 100) / 100;
			course.setResultEvaluate(val);
			ranks.put(course, avg);
		}
		List<Course> list = contextMatching.getByQuantity(contextMatching.sortCourse(ranks), 5);
		return list;
	}

	public List<Course> getListCourseByCategory(Integer categoryId) {
		return courseRepository.getListCourseByCategory(categoryId);
	}

	public Integer countByCategory(Integer categoryId) {
		return courseRepository.countByCategory(categoryId);
	}
}