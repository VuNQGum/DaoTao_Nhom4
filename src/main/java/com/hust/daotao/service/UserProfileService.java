package com.hust.daotao.service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hust.daotao.dto.ProfileDto;
import com.hust.daotao.entity.Profile;
import com.hust.daotao.entity.User;
import com.hust.daotao.entity.UserProfile;
import com.hust.daotao.repository.UserProfileRepository;

@Service
public class UserProfileService {
	@Autowired
	private ProfileService profileService;
	@Autowired
	private UserProfileRepository userProfileRepository;

	public UserProfile setProfile(String profileCode, UserProfile userProfile) {
		ProfileDto profileDto = profileService.findByCode(profileCode);
		userProfile.setProfile(profileService.convertDtoToBo(profileDto));
		return userProfile;
	}

	public List<UserProfile> findByUser(User user) {
		List<UserProfile> userProfiles = userProfileRepository.findByUser(user.getId());
		return userProfiles;
	}

	public UserProfile save(UserProfile userProfile) {
		return userProfileRepository.save(userProfile);
	}

	public void deleteByProfile(Date timestamp, Profile profile) {
		userProfileRepository.deleteByProfile(timestamp, profile.getId());
	}

	public void deleteByProfileAndValue(Date timestamp, Profile profile, Integer value) {
		userProfileRepository.deleteByProfileAndValue(timestamp, profile.getId(), value);
	}

	public void deleteByUser(Date timestamp, User user) {
		userProfileRepository.deleteByUser(timestamp, user.getId());
	}

	public void update(UserProfile userProfile) {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		userProfileRepository.update(userProfile.getValue(), timestamp, userProfile.getProfile().getId(),
				userProfile.getUser().getId());
	}

	public UserProfile findByUserAndProfile(UserProfile userProfile) {
		return userProfileRepository.findByUserAndProfile(userProfile.getProfile().getId(),
				userProfile.getUser().getId());
	}
}
