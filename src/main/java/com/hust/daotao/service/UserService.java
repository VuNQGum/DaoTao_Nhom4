package com.hust.daotao.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.thymeleaf.context.Context;

import com.hust.daotao.dto.EmailDto;
import com.hust.daotao.dto.GraphDto;
import com.hust.daotao.dto.UserDto;
import com.hust.daotao.entity.User;
import com.hust.daotao.entity.UserProfile;
import com.hust.daotao.repository.UserRepository;
import com.hust.daotao.response.UserResponse;
import com.hust.daotao.util.Constants;
import com.hust.daotao.util.UtilMethod;

@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private RoleService roleService;
	@Autowired
	private UserProfileService userProfileService;
	@Autowired
	private CourseService courseService;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private MailService mailService;

	public UserResponse getList(Integer page, String name, String email, Boolean status, Integer roleId) {
		Sort sortable = null;
		sortable = Sort.by("updatedAt").descending();
		Pageable pageable = PageRequest.of(page, Constants.pageSize, sortable);
		List<User> listUser = userRepository.getList(name, email, status, roleId, pageable).getContent();
		UserResponse userResponse = new UserResponse();
		List<UserDto> userDtos = new ArrayList<UserDto>();
		if (listUser != null) {
			if (!CollectionUtils.isEmpty(listUser)) {
				userDtos = listUser.parallelStream().map(user -> convertBoToDto(user)).collect(Collectors.toList());
			}
		}
		userResponse.setAaData(userDtos);
		return userResponse;
	}

	public Long countList(String name, String email, Boolean status, Integer roleId) {
		return userRepository.countList(name, email, status, roleId);
	}

	public User findByEmail(String email) {
		return userRepository.findByEmailAndDeletedAt(email);
	}

	public UserDto save(UserDto userDto, List<UserProfile> userProfiles) {
		User user = convertDtoToBo(userDto);
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		user.setUpdatedAt(timestamp);
		user.setRole(roleService.findById(userDto.getRoleId()));
		user.setSubject(categoryService.findById(userDto.getSubjectId()));
		User userResult = userRepository.save(user);
		if (user.getRole().getId() == Constants.ROLE_STUDENT) {
			for (UserProfile userProfile : userProfiles) {
				userProfile.setUser(userResult);
				userProfileService.save(userProfile);
			}
		}
		UserDto resultDto = convertBoToDto(userResult);
		return resultDto;
	}

	public UserDto save(UserDto userDto) {
		userDto.setPassword(UtilMethod.encode(Constants.PASSWORD_DEFAULT));
		userDto.setImage(Constants.IMAGE_DEFAULT);
		User user = convertDtoToBo(userDto);
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		user.setUpdatedAt(timestamp);
		user.setRole(roleService.findById(userDto.getRoleId()));
		user.setSubject(categoryService.findById(userDto.getSubjectId()));
		User userResult = userRepository.save(user);
		UserDto resultDto = convertBoToDto(userResult);
		return resultDto;
	}

	public UserDto update(UserDto userDto, List<UserProfile> userProfiles) {
		User user = convertDtoToBo(userDto);
		User userCurrentLogin = getUserCurrentLogin();
		user.setResultTest(userCurrentLogin.getResultTest());
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		user.setUpdatedAt(timestamp);
		User userResult = userRepository.save(user);
		if (userProfiles != null) {
			for (UserProfile userProfile : userProfiles) {
				userProfile.setUser(userResult);
				if (userProfileService.findByUserAndProfile(userProfile) != null) {
					userProfileService.update(userProfile);
				} else
					userProfileService.save(userProfile);
			}
		}
		UserDto resultDto = convertBoToDto(userResult);
		return resultDto;
	}

	public List<UserDto> getListTeacher() {
		List<User> listUser = userRepository.getByRole(Constants.ROLE_TEACHER);
		List<UserDto> userDtos = new ArrayList<UserDto>();
		if (listUser != null) {
			if (!CollectionUtils.isEmpty(listUser)) {
				userDtos = listUser.parallelStream().map(user -> convertBoToDto(user)).collect(Collectors.toList());
			}
		}
		return userDtos;
	}

	public User getUserCurrentLogin() {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		User user = userRepository.findByEmailAndDeletedAt(email);
		return user;
	}

	public Integer getRoleUserCurrentLogin() {
		User user = getUserCurrentLogin();
		return user.getRole().getId();
	}

	public UserDto updatePassword(User user) {
		if (user == null)
			return null;
		User result = userRepository.save(user);
		return convertBoToDto(result);
	}

	public Boolean changeStatus(Integer id) {
		User user = findById(id);
		if (user == null)
			return false;
		user.setStatus(!user.getStatus());
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		user.setUpdatedAt(timestamp);
		userRepository.save(user);
		return true;
	}

	public User findById(Integer id) {
		return userRepository.findByIdAndDeletedAt(id, null);
	}

	public Boolean delete(Integer id) {
		User user = findById(id);
		if (user == null) {
			return false;
		}
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		user.setDeletedAt(timestamp);
		userProfileService.deleteByUser(timestamp, user);
		courseService.deleteByUser(timestamp, user);
		userRepository.save(user);
		return true;
	}

	public UserDto forgotPassword(User user) {
		String newPassword = UtilMethod.randomString(10);
		user.setPassword(UtilMethod.encode(newPassword));
		Context context = new Context();
		context.setVariable("name", user.getFullName());
		context.setVariable("password", newPassword);
		EmailDto emailDto = new EmailDto(user.getEmail(), "Lấy lại mật khẩu", "forgot-password", context);
		mailService.sendMail(emailDto);
		return updatePassword(user);
	}

	public User convertDtoToBo(UserDto userDto) {
		return modelMapper.map(userDto, User.class);
	}

	public UserDto convertBoToDto(User user) {
		return modelMapper.map(user, UserDto.class);
	}

	public Boolean isLogin() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken)
			return false;
		return true;
	}

	public List<User> getListEmailStudent() {
		return userRepository.getListStudent();
	}

	public UserDto getInfoStudentByEmail(String email) {
		User student = userRepository.findByEmailAndDeletedAtAndStatus(email, null, true);
		if (student == null)
			return null;

		return convertBoToDto(student);
	}

	public Integer countStudent() {
		return userRepository.countStudent();
	}

	public Integer countTeacher() {
		return userRepository.countTeacher();
	}

	public User save(User user) {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		user.setUpdatedAt(timestamp);
		return userRepository.save(user);
	}

	public GraphDto countStudentByCapacity() {
		List<User> students = userRepository.getListStudent();
		List<Integer> count = new ArrayList<Integer>();
		Integer least = 0;
		Integer medium = 0;
		Integer rather = 0;
		Integer good = 0;
		Integer veryGood = 0;
		Integer notTest = 0;
		for (User student : students) {
			Integer result = student.getResultTest();
			if (result == null)
				notTest++;
			else if (result >= 95)
				veryGood++;
			else if (result < 95 && result >= 85)
				good++;
			else if (result < 85 && result >= 70)
				rather++;
			else if (result < 70 && result >= 50)
				medium++;
			else
				least++;
		}
		count.add(notTest);
		count.add(least);
		count.add(medium);
		count.add(rather);
		count.add(good);
		count.add(veryGood);
		GraphDto graphDto = new GraphDto();
		String[] titles = { "Chưa kiểm tra", "Kém", "Trung bình", "Khá", "Giỏi", "Xuất sắc" };
		graphDto.setTitles(Arrays.asList(titles));
		graphDto.setValuesInteger(count);
		return graphDto;
	}

	public List<User> getListStudent() {
		List<User> users = userRepository.getByRole(Constants.ROLE_STUDENT);
		return users;
	}

	public String formatResultTest(Integer result) {

		if (result == null)
			return "Chưa làm kiểm tra";
		else if (result >= 95)
			return "Xuất sắc";
		else if (result < 95 && result >= 85)
			return "Giỏi";
		else if (result < 85 && result >= 70)
			return "Khá";
		else if (result < 70 && result >= 50)
			return "Trung bình";
		else
			return "Kém";
	}

}
