package com.hust.thesis.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hust.thesis.dto.ProfileDto;
import com.hust.thesis.entity.Course;
import com.hust.thesis.entity.CourseProfile;
import com.hust.thesis.entity.Profile;
import com.hust.thesis.repository.CourseProfileRepository;

@Service
public class CourseProfileService {
	@Autowired
	private ProfileService profileService;
	@Autowired
	private CourseProfileRepository courseProfileRepository;
	@Autowired
	private CourseService courseService;

	public CourseProfile setProfile(String profileCode, CourseProfile courseProfile) {
		ProfileDto profileDto = profileService.findByCode(profileCode);
		courseProfile.setProfile(profileService.convertDtoToBo(profileDto));
		return courseProfile;
	}

	public CourseProfile save(CourseProfile courseProfile) {
		return courseProfileRepository.save(courseProfile);
	}

	public void updateByCourse(String value, Integer courseId, Integer profileId) {
		if (courseProfileRepository.findByCourseAndProfileId(courseId, profileId) == null) {
			Profile profile = profileService.findById(profileId);
			Course course = courseService.findById(courseId);
			CourseProfile courserProfile = new CourseProfile(course, profile, value);
			save(courserProfile);
		}
		courseProfileRepository.updateByCourse(value, courseId, profileId);
	}

	public void deleteByProfile(Date timestamp, Profile profile) {
		courseProfileRepository.deleteByProfile(timestamp, profile.getId());
	}

	public void deleteByProfileAndValue(Date timestamp, Profile profile, Integer value) {
		courseProfileRepository.deleteByProfileAndValue(timestamp, profile.getId(), value);
	}

	public void deleteByCourse(Date timestamp, Course course) {
		courseProfileRepository.deleteByCourse(timestamp, course.getId());
	}

	public List<CourseProfile> findByCourse(Course course) {
		List<CourseProfile> courseProfiles = courseProfileRepository.findByCourse(course.getId());
		return courseProfiles;
	}
}
