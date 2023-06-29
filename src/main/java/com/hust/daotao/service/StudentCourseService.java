package com.hust.daotao.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import com.hust.daotao.dto.CourseDto;
import com.hust.daotao.dto.EmailDto;
import com.hust.daotao.dto.StudentCourseDto;
import com.hust.daotao.entity.Course;
import com.hust.daotao.entity.StudentCourse;
import com.hust.daotao.entity.User;
import com.hust.daotao.repository.StudentCourseRepository;
import com.hust.daotao.response.StudentCourseResponse;
import com.hust.daotao.util.Constants;

@Service
public class StudentCourseService {
	@Autowired
	private StudentCourseRepository studentCourseRepository;
	@Autowired
	private UserService userService;
	@Autowired
	private CourseService courseService;
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private MailService mailService;
	@Autowired
	private ContextMatching contextMatching;

	public StudentCourseResponse getList(Integer page, String name, String email, Boolean status, Integer courseId) {
		Sort sortable = null;
		sortable = Sort.by("status").ascending();
		Pageable pageable = PageRequest.of(page, Constants.pageSize, sortable);
		List<StudentCourse> list = studentCourseRepository.getList(name, email, status, courseId, pageable)
				.getContent();
		StudentCourseResponse response = new StudentCourseResponse();
		List<StudentCourseDto> dtos = new ArrayList<StudentCourseDto>();
		if (list != null) {
			dtos = convertListBo(list);
		}
		response.setAaData(dtos);
		return response;
	}

	public Long countList(String name, String email, Boolean status, Integer courseId) {
		return studentCourseRepository.countList(name, email, status, courseId);
	}

	public StudentCourseDto registerCourse(StudentCourseDto studentCourseDto) {
		StudentCourse scRegister = studentCourseRepository.findByCourseAndStudent(studentCourseDto.getStudentId(),
				studentCourseDto.getCourseOfStudentId());
		Course course = courseService.findById(studentCourseDto.getCourseOfStudentId());
		User student = userService.findById(studentCourseDto.getStudentId());
		StudentCourse result = null;
		if (scRegister == null) {
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			StudentCourse sc = new StudentCourse(student, course, false);
			sc.setUpdatedAt(timestamp);
			result = studentCourseRepository.save(sc);
		}
		Context context = new Context();
		context.setVariable("teacher", course.getTeacher());
		context.setVariable("student", student);
		context.setVariable("course", course);
		EmailDto emailDto = new EmailDto("trungnn160697@gmail.com",
				"Thông báo sinh viên đăng ký khóa học " + course.getName() + ".", "notification-register-course",
				context);
		mailService.sendMail(emailDto);
		if (result == null)
			return convertBoToDto(scRegister);
		return convertBoToDto(result);
	}

	public StudentCourse findByCourseAndStudent(Integer studentId, Integer courseId) {
		StudentCourse studentCourse = studentCourseRepository.findByCourseAndStudent(studentId, courseId);
		return studentCourse;
	}

	public List<StudentCourseDto> getListCourseOfStudent() {
		User user = userService.getUserCurrentLogin();
		List<StudentCourse> list = studentCourseRepository.getListCourseOfStudent(user.getId());
		List<StudentCourseDto> dtos = new ArrayList<StudentCourseDto>();
		if (list != null) {
			for (StudentCourse bo : list) {
				StudentCourseDto dto = convertBoToDto(bo);
				Course course = bo.getCourseOfStudent();
				CourseDto courseDto = courseService.convertBoToDto(course);
				courseDto.setQuantityLesson(course.getLessonOfCourses().size());
				courseDto.setQuantityStudent(course.getStudentOfCourses().size());
				dto.setCourseDto(courseDto);
				dtos.add(dto);
			}
		}
		return dtos;
	}

	public StudentCourse findByCourseAndStudentLearn(Integer courseId, Integer studentId) {
		StudentCourse result = studentCourseRepository.findByCourseAndStudent(studentId, courseId);
		return result;
	}

	public Boolean changeStatus(Integer id) {
		StudentCourse sc = findById(id);
		if (sc == null)
			return false;
		sc.setStatus(!sc.getStatus());
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		sc.setUpdatedAt(timestamp);
		StudentCourse result = studentCourseRepository.save(sc);
		Context context = new Context();
		context.setVariable("name", result.getStudent().getFullName());
		context.setVariable("course", result.getCourseOfStudent());
		List<Course> courseRecommend = contextMatching.contextMatching(result.getStudent(), 3);
		context.setVariable("courseRecommend", courseRecommend);
		EmailDto emailDto = new EmailDto(result.getStudent().getEmail(), "Xác nhận tham gia khóa học.", "accept-course",
				context);
		mailService.sendMail(emailDto);
		return true;
	}

	public Boolean delete(Integer id) {
		StudentCourse sc = findById(id);
		if (sc == null) {
			return false;
		}
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		sc.setDeletedAt(timestamp);
		studentCourseRepository.save(sc);
		Course course = sc.getCourseOfStudent();
		course.setQuantityStudent(course.getQuantityStudent() - 1);
		courseService.save(course);
		return true;
	}

	public StudentCourse findById(Integer id) {
		return studentCourseRepository.findByIdAndDeletedAt(id, null);
	}

	public StudentCourse convertDtoToBo(StudentCourseDto dto) {
		return modelMapper.map(dto, StudentCourse.class);
	}

	public StudentCourseDto convertBoToDto(StudentCourse bo) {
		return modelMapper.map(bo, StudentCourseDto.class);
	}

	public List<StudentCourseDto> convertListBo(List<StudentCourse> bos) {
		List<StudentCourseDto> dtos = new ArrayList<StudentCourseDto>();
		for (StudentCourse bo : bos) {
			StudentCourseDto dto = convertBoToDto(bo);
			dto.setStudentDto(userService.convertBoToDto(bo.getStudent()));
			dtos.add(dto);
		}
		return dtos;
	}

	public boolean addStudent(Integer courseId, String[] listStudentId) {
		Course course = courseService.findById(courseId);
		for (String studentId : listStudentId) {
			Integer id = Integer.parseInt(studentId);
			User student = userService.findById(id);
			StudentCourse sc = new StudentCourse(student, course, true);
//			StudentCourse result = studentCourseRepository.save(sc);
			StudentCourse result = studentCourseRepository.save(sc);
			Context context = new Context();
			context.setVariable("name", result.getStudent().getFullName());
			context.setVariable("course", result.getCourseOfStudent());
			EmailDto emailDto = new EmailDto(result.getStudent().getEmail(), "Xác nhận tham gia khóa học.",
					"accept-course", context);
			mailService.sendMail(emailDto);
		}
		return true;
	}

}
