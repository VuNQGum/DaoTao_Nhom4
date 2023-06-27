package com.hust.thesis.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hust.thesis.entity.Course;
import com.hust.thesis.entity.CourseProfile;
import com.hust.thesis.entity.Criteria;
import com.hust.thesis.entity.Profile;
import com.hust.thesis.entity.Setting;
import com.hust.thesis.entity.User;
import com.hust.thesis.entity.UserProfile;

@Service
public class ContextMatching {
	@Autowired
	private CourseService courseService;
	@Autowired
	private ProfileService profileService;
	@Autowired
	private SurveyResultService surveyService;
	@Autowired
	private CourseEvaluateService evaluateService;
	@Autowired
	private CriteriaService criteriaService;
	@Autowired
	private SettingService settingService;

	public List<Course> contextMatching(User user, Integer quantity) {
		List<Course> courses = courseService.getListCourseShow();
		HashMap<Course, Double> results = new HashMap<Course, Double>();
		List<Profile> profiles = profileService.getListProfileShow();
		Map<Integer, Double> surveyResults = surveyService.getListSurveyOfUser(user.getId());
		List<Criteria> criterias = criteriaService.getListCriteriaByType();
		double mpv = calculateMpv(profiles);
		if (surveyResults != null && surveyResults.size() > 0) {
			mpv = mpv + calculateMpvKansei(criterias);
		}
		for (Course course : courses) {
			Map<Integer, Double> valueMatched = getValueMatched(user, course, profiles);

			double sav = calculateTotalAv(valueMatched, profiles);
			if (surveyResults != null && surveyResults.size() > 0) {
				Map<Integer, Double> valueMatchedKansei = getValueMatchedKansei(user, course, criterias);
				sav = sav + calculateTotalAvKansei(valueMatchedKansei, criterias);
			}
			if (getResultantValue(sav, mpv))
				results.put(course, (sav / mpv));
		}
		List<Course> list = getByQuantity(sortCourse(results), quantity);
		return list;
	}

	public Double calculateMpv(List<Profile> profiles) {
		Double mpv = 0.0;
		for (Profile profile : profiles) {
			mpv += profile.getWeight();
		}
		return mpv;
	}

	public boolean getResultantValue(Double sav, Double mpv) {
		Setting setting = settingService.findByCode("boundary_decision");
		Double valueMatch = setting.getValue();
		return (sav / mpv) >= valueMatch;
	}

	public double calculateAv(Double weight, Double e) {
		return weight * e;
	}

	public double calculateTotalAv(Map<Integer, Double> valueMatched, List<Profile> profiles) {
		double total = 0.0;
		if (valueMatched != null && valueMatched.size() > 0) {
			for (Profile profile : profiles) {
				if (valueMatched.get(profile.getId()) != null)
					total += profile.getWeight() * valueMatched.get(profile.getId());
			}
		}
		return total;
	}

	public Map<Integer, Double> getValueMatched(User user, Course course, List<Profile> profiles) {
		List<Course> courses = courseService.getListCourseShow();
		List<UserProfile> userProfiles = user.getUserProfiles();
		List<CourseProfile> courseProfiles = course.getCourseProfiles();
		Map<Integer, Double> valueMatched = new HashMap<Integer, Double>();
		for (UserProfile userProfile : userProfiles) {
			if (!userProfile.getProfile().getIsMultiple()) {
				if (compareUserProfileAndCourseProfile(userProfile, courseProfiles))
					valueMatched.put(userProfile.getProfile().getId(), 1.0);
				if (!compareUserProfileAndCourseProfile(userProfile, courseProfiles))
					valueMatched.put(userProfile.getProfile().getId(), 0.0);
			} else {
				valueMatched.put(userProfile.getProfile().getId(),
						getValueMatchedIsMultiple(userProfile, courseProfiles, courses));
			}
		}
		return valueMatched;
	}

	public boolean compareUserProfileAndCourseProfile(UserProfile userProfile, List<CourseProfile> courseProfiles) {
		for (CourseProfile courseProfile : courseProfiles) {

			if (userProfile.getProfile().getId() == courseProfile.getProfile().getId()) {
				int userProfileValue = Integer.parseInt(userProfile.getValue());
				int courseProfileValue = Integer.parseInt(courseProfile.getValue());
				if (userProfileValue >= courseProfileValue)
					return true;
			}
		}
		return false;
	}

	public double getValueMatchedIsMultiple(UserProfile userProfile, List<CourseProfile> courseProfiles,
			List<Course> courses) {
		for (CourseProfile courseProfile : courseProfiles) {
			if ((userProfile.getProfile().getId() == courseProfile.getProfile().getId())) {
				double value = calculateSameValueUserProfile(userProfile, courseProfile);
				double value2 = calculateFrequencyOccurrenceValueCourseProfile(userProfile.getValue(), courseProfile,
						courses);
				return 0.5 * (value + value2);
			}
		}
		return 0.0;
	}

	public double calculateSameValueUserProfile(UserProfile userProfile, CourseProfile courseProfile) {
		String[] valuesUserProfile = userProfile.getValue().split(",");
		double quantityValueUserProfile = (double) valuesUserProfile.length;
		double countSameValue = 0.0;
		for (String value : valuesUserProfile) {
			if (courseProfile.getValue().contains(value)) {
				countSameValue += 1.0;
			}
		}
		return (countSameValue / quantityValueUserProfile);
	}

	// tinh so lan xuat hien cua gia tri value trong thuoc tinh i cua tat ca cac
	// profile course
	public double calculateFrequencyOccurrence(String value, List<Course> courses, Integer profileId) {
		double count = 0.0;
		for (Course course : courses) {
			List<CourseProfile> courseProfiles = course.getCourseProfiles();
			for (CourseProfile courseProfile : courseProfiles) {
				if (courseProfile.getProfile().getId() == profileId && courseProfile.getValue().contains(value))
					count = count + 1.0;
			}
		}
		return count;
	}

	// tinh ti so giua tong so so lan xuat hien cua gia tri value same trong thuoc
	// tinh i trong cua tat ca cac profile course va tong so lan xuat hien cua cac
	// gia tri cua profile course
	public double calculateFrequencyOccurrenceValueCourseProfile(String userProfile, CourseProfile courseProfile,
			List<Course> courses) {
		String[] valuesCourseProfile = courseProfile.getValue().split(",");
		double totalFrequencyValue = 0.0;
		double totalFrequencyValueSame = 0.0;
		for (String value : valuesCourseProfile) {
			if (userProfile.contains(value)) {
				totalFrequencyValueSame += calculateFrequencyOccurrence(value, courses,
						courseProfile.getProfile().getId());
			}
			totalFrequencyValue += calculateFrequencyOccurrence(value, courses, courseProfile.getProfile().getId());
		}
		return totalFrequencyValueSame / totalFrequencyValue;
	}

	// context matching with kansei
	public Map<Integer, Double> getValueMatchedKansei(User user, Course course, List<Criteria> criterias) {
		Map<Integer, Double> surveyResult = surveyService.getListSurveyOfUser(user.getId());
		Map<Integer, Double> courseEvaluate = evaluateService.getListEvaluateOfCourse(course.getId());
		Map<Integer, Double> valueMatched = new HashMap<Integer, Double>();
		for (int i = 0; i < criterias.size(); i++) {
			Integer criteriaId = criterias.get(i).getId();
			if (surveyResult.containsKey(criteriaId) && courseEvaluate.containsKey(criteriaId)
					&& surveyResult.get(criteriaId) <= courseEvaluate.get(criteriaId)) {
				valueMatched.put(criteriaId, 1.0);
			} else
				valueMatched.put(criteriaId, 0.0);

		}
		return valueMatched;
	}

	public Double calculateMpvKansei(List<Criteria> criterias) {
		Double mpv = 0.0;
		for (Criteria criteria : criterias) {
			mpv += criteria.getWeight();
		}
		return mpv;
	}

	public double calculateTotalAvKansei(Map<Integer, Double> valueMatched, List<Criteria> criterias) {
		double total = 0.0;
		if (valueMatched != null && valueMatched.size() > 0) {
			for (Criteria criteria : criterias) {
				if (valueMatched.containsKey(criteria.getId()))
					total += criteria.getWeight() * valueMatched.get(criteria.getId());
			}
		}
		return total;
	}

	public List<Course> sortCourse(HashMap<Course, Double> map) {
		// Khởi tạo ra một Set entries
		Set<Entry<Course, Double>> entries = map.entrySet();

		// Tạo custom Comparator
		Comparator<Entry<Course, Double>> comparator = new Comparator<Entry<Course, Double>>() {
			@Override
			public int compare(Entry<Course, Double> e1, Entry<Course, Double> e2) {
				Double v1 = e1.getValue();
				Double v2 = e2.getValue();
				return -1 * v1.compareTo(v2);
			}
		};

		// Convert Set thành List
		List<Entry<Course, Double>> listEntries = new ArrayList<>(entries);

		// Sắp xếp List
		Collections.sort(listEntries, comparator);

		// Tạo một LinkedHashMap và put các entry từ List đã sắp xếp sang
		List<Course> results = new ArrayList<Course>();
		for (Entry<Course, Double> entry : listEntries) {
			results.add(entry.getKey());
		}
		return results;
	}

	public List<Course> getByQuantity(List<Course> courses, Integer quantity) {
		List<Course> results = new ArrayList<Course>();
		if (courses == null)
			return results;
		if (quantity >= courses.size())
			results = courses;
		else
			for (int i = 0; i < quantity; i++) {
				results.add(courses.get(i));
			}
		return results;
	}

}
