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

import com.hust.daotao.dto.ProfileValueDto;
import com.hust.daotao.entity.Profile;
import com.hust.daotao.entity.ProfileValue;
import com.hust.daotao.repository.ProfileValueRepository;
import com.hust.daotao.response.ProfileValueResponse;
import com.hust.daotao.util.Constants;

@Service
public class ProfileValueService {
	@Autowired
	private ProfileValueRepository profileValueRepository;
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private ProfileService profileSerivce;

	public ProfileValueResponse getList(Integer page, String name, String code, Boolean status, Integer profileId) {
		Sort sortable = null;
		sortable = Sort.by("id").ascending();
		Pageable pageable = PageRequest.of(page, Constants.pageSize, sortable);
		List<ProfileValue> listProfileValue = profileValueRepository
				.getListProfileValues(name, code, status, profileId, pageable).getContent();
		ProfileValueResponse profilValueResponse = new ProfileValueResponse();
		List<ProfileValueDto> profileValueDtos = new ArrayList<ProfileValueDto>();
		if (listProfileValue != null) {
			if (!CollectionUtils.isEmpty(listProfileValue)) {
				profileValueDtos = listProfileValue.parallelStream().map(profileValue -> convertBoToDto(profileValue))
						.collect(Collectors.toList());
			}
		}
		profilValueResponse.setAaData(profileValueDtos);
		return profilValueResponse;
	}

	public Long countList(String name, String code, Boolean status, Integer profileId) {
		return profileValueRepository.countListProfileValues(name, code, status, profileId);
	}

	public ProfileValueDto findByCodeAndProfile(String code, Integer profileId) {
		ProfileValue profileValue = profileValueRepository.findByCodeAndProfile(code, profileId);
		return (profileValue != null ? convertBoToDto(profileValue) : null);
	}

	public ProfileValueDto findByValueAndProfile(Integer value, Integer profileId) {
		ProfileValue profileValue = profileValueRepository.findByValueAndProfile(value, profileId);
		return (profileValue != null ? convertBoToDto(profileValue) : null);
	}

	public ProfileValueDto findByIdDto(Integer id) {
		ProfileValue profileValue = findById(id);
		if (profileValue == null)
			return null;
		ProfileValueDto profileValueDto = convertBoToDto(profileValue);
		profileValueDto.setProfileDto(profileSerivce.convertBoToDto(profileValue.getProfile()));
		profileValueDto.setProfileId(profileValue.getProfile().getId());
		return profileValueDto;
	}

	public Boolean delete(Integer id) {
		ProfileValue profileValue = findById(id);
		if (profileValue == null) {
			return false;
		}
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		profileValue.setDeletedAt(timestamp);
//		userProfileService.deleteByProfileAndValue(timestamp.toString(), profileValue.getProfile(),
//				profileValue.getValue());
//		courseProfileService.deleteByProfileAndValue(timestamp.toString(), profileValue.getProfile(),
//				profileValue.getValue());
		profileValueRepository.save(profileValue);
		return true;
	}

	public Boolean changeStatus(Integer id) {
		ProfileValue profileValue = findById(id);
		if (profileValue == null)
			return false;
		profileValue.setStatus(!profileValue.getStatus());
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		profileValue.setUpdatedAt(timestamp);
		profileValueRepository.save(profileValue);
		return true;
	}

	public ProfileValueDto save(ProfileValueDto profileValueDto) {
		ProfileValue profileValue = convertDtoToBo(profileValueDto);
		Profile profile = profileSerivce.findById(profileValueDto.getProfileId());
		profileValue.setProfile(profile);
		return convertBoToDto(profileValueRepository.save(profileValue));
	}

	public ProfileValueDto update(ProfileValueDto profileValueDto) {
		ProfileValue profileValue = convertDtoToBo(profileValueDto);
		Profile profile = profileSerivce.findById(profileValueDto.getProfileId());
		profileValue.setProfile(profile);
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		profileValue.setUpdatedAt(timestamp);
		return convertBoToDto(profileValueRepository.save(profileValue));
	}

	public ProfileValue findById(Integer id) {
		return profileValueRepository.findByIdAndDeletedAt(id, null);
	}

	public void deleteByProfile(Profile profile) {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		profileValueRepository.deleteByProfile(timestamp, profile.getId());
	}

	public ProfileValue convertDtoToBo(ProfileValueDto profileValueDto) {
		return modelMapper.map(profileValueDto, ProfileValue.class);
	}

	public ProfileValueDto convertBoToDto(ProfileValue profileValue) {
		return modelMapper.map(profileValue, ProfileValueDto.class);
	}

	public List<ProfileValue> findByStatusAndProfile(Profile profile) {
		return profileValueRepository.findByStatusAnProfile(profile.getId());
	}

	public List<ProfileValueDto> listProfileValueDto(List<ProfileValue> profileValues) {
		List<ProfileValueDto> profileValueDtos = new ArrayList<ProfileValueDto>();
		if (profileValues != null) {
			if (!CollectionUtils.isEmpty(profileValues)) {
				profileValueDtos = profileValues.parallelStream()
						.map(profileValue -> modelMapper.map(profileValue, ProfileValueDto.class))
						.collect(Collectors.toList());
			}
		}
		return profileValueDtos;
	}
}
