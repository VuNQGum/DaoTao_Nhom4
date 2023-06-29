package com.hust.daotao.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.hust.daotao.dto.ProfileDto;
import com.hust.daotao.entity.Profile;
import com.hust.daotao.entity.ProfileValue;
import com.hust.daotao.repository.ProfileRepository;
import com.hust.daotao.response.ProfileResponse;
import com.hust.daotao.util.Constants;

@Service
public class ProfileService {
	@Autowired
	private ProfileRepository profileRepository;
	@Autowired
	private ProfileValueService profileValueService;
	@Autowired
	private UserProfileService userProfileService;
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private CourseProfileService courseProfileService;

	public ProfileResponse getList(Integer page, String name, String code, Boolean status) {
		Sort sortable = null;
		sortable = Sort.by("id").ascending();
		Pageable pageable = PageRequest.of(page, Constants.pageSize, sortable);
		List<Profile> listProfile = profileRepository.getListProfiles(name, code, status, pageable).getContent();
		ProfileResponse profileResponse = new ProfileResponse();
		List<ProfileDto> profileDtos = new ArrayList<ProfileDto>();
		if (listProfile != null) {
			if (!CollectionUtils.isEmpty(listProfile)) {
				profileDtos = listProfile.parallelStream().map(profile -> convertBoToDto(profile))
						.collect(Collectors.toList());
			}
		}
		profileResponse.setAaData(profileDtos);
		return profileResponse;
	}

	public List<ProfileDto> getByStatusAndProfile() {
		List<Profile> listProfile = profileRepository.getByStatus(true);
		List<ProfileDto> profileDtos = new ArrayList<ProfileDto>();
		if (listProfile != null) {
			for (Profile profile : listProfile) {
				profile.setProfileValues(profileValueService.findByStatusAndProfile(profile));
				ProfileDto profileDto = convertBoToDto(profile);
				profileDto.setProfileValueDtos(profileValueService.listProfileValueDto(profile.getProfileValues()));
				profileDtos.add(profileDto);
			}
		}

		return profileDtos;
	}
	
	public List<ProfileDto> getListProfileOfStudent() {
		List<Profile> listProfile = profileRepository.getListProfileOfStudent(true);
		List<ProfileDto> profileDtos = new ArrayList<ProfileDto>();
		if (listProfile != null) {
			for (Profile profile : listProfile) {
				profile.setProfileValues(profileValueService.findByStatusAndProfile(profile));
				ProfileDto profileDto = convertBoToDto(profile);
				profileDto.setProfileValueDtos(profileValueService.listProfileValueDto(profile.getProfileValues()));
				profileDtos.add(profileDto);
			}
		}

		return profileDtos;
	}
	
	public List<ProfileDto> getListProfileOfCourse() {
		List<Profile> listProfile = profileRepository.getListProfileOfCourse(true);
		List<ProfileDto> profileDtos = new ArrayList<ProfileDto>();
		if (listProfile != null) {
			for (Profile profile : listProfile) {
				profile.setProfileValues(profileValueService.findByStatusAndProfile(profile));
				ProfileDto profileDto = convertBoToDto(profile);
				profileDto.setProfileValueDtos(profileValueService.listProfileValueDto(profile.getProfileValues()));
				profileDtos.add(profileDto);
			}
		}

		return profileDtos;
	}

	public Long countList(String name, String code, Boolean status) {
		return profileRepository.countListProfile(name, code, status);
	}

	public ProfileDto findByCode(String code) {
		Profile profile = profileRepository.findByCodeAndDeletedAt(code, null);
		if (profile == null)
			return null;
		List<ProfileValue> values = new ArrayList<ProfileValue>();
		for (ProfileValue profileValue : profile.getProfileValues()) {
			if (profileValue.getDeletedAt() == null)
				values.add(profileValue);
		}
		profile.setProfileValues(values);
		return convertBoToDto(profile);
	}
	

	public ProfileDto findByIdDto(Integer id) {
		Profile profile = findById(id);
		if (profile == null)
			return null;
		List<ProfileValue> values = new ArrayList<ProfileValue>();
		for (ProfileValue profileValue : profile.getProfileValues()) {
			if (profileValue.getDeletedAt() == null)
				values.add(profileValue);
		}
		profile.setProfileValues(values);
		ProfileDto profileDto = convertBoToDto(profile);
		profileDto.setProfileValueDtos(profileValueService.listProfileValueDto(profile.getProfileValues()));
		return profileDto;
	}

	public Boolean delete(Integer id) {
		Profile profile = findById(id);
		if (profile == null) {
			return false;
		}
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		profile.setDeletedAt(timestamp);
		profileRepository.save(profile);
		profileValueService.deleteByProfile(profile);
		userProfileService.deleteByProfile(timestamp, profile);
		courseProfileService.deleteByProfile(timestamp, profile);
		return true;
	}

	public Boolean changeStatus(Integer id) {
		Profile profile = findById(id);
		if (profile == null)
			return false;
		profile.setStatus(!profile.getStatus());
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		profile.setUpdatedAt(timestamp);
		profileRepository.save(profile);
		return true;
	}

	public ProfileDto save(ProfileDto profileDto) {
		return convertBoToDto(profileRepository.save(convertDtoToBo(profileDto)));
	}

	public ProfileDto update(ProfileDto profileDto) {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		profileDto.setUpdatedAt(timestamp.toString());
		return convertBoToDto(profileRepository.save(convertDtoToBo(profileDto)));
	}

	public Profile findById(Integer id) {
		return profileRepository.findByIdAndDeletedAt(id, null);
	}

	public Profile convertDtoToBo(ProfileDto profileDto) {
		return modelMapper.map(profileDto, Profile.class);
	}

	public ProfileDto convertBoToDto(Profile profile) {
		return modelMapper.map(profile, ProfileDto.class);
	}
	
	public List<Profile> getListProfileShow() {
		return profileRepository.getByStatus(true);
		
	}
}
