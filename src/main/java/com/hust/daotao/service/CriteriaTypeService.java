package com.hust.thesis.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.hust.thesis.dto.CriteriaTypeDto;
import com.hust.thesis.entity.CriteriaType;
import com.hust.thesis.repository.CriteriaTypeRepository;
import com.hust.thesis.response.CriteriaTypeResponse;
import com.hust.thesis.util.Constants;

@Service
public class CriteriaTypeService {
	@Autowired
	private CriteriaTypeRepository criteriaTypeRepository;
	@Autowired
	private ModelMapper modelMapper;


	public CriteriaTypeResponse getList(Integer page, String name, String code, Boolean status) {
		Sort sortable = null;
		sortable = Sort.by("id").ascending();
		Pageable pageable = PageRequest.of(page, Constants.pageSize, sortable);
		List<CriteriaType> listCriteriaType = criteriaTypeRepository.getList(name, code, status, pageable).getContent();
		CriteriaTypeResponse res = new CriteriaTypeResponse();
		List<CriteriaTypeDto> dtos = new ArrayList<CriteriaTypeDto>();
		if (listCriteriaType != null) {

			dtos = convertListBoToListDto(listCriteriaType);

		}
		res.setAaData(dtos);
		return res;
	}

//	public List<ProfileDto> getByStatusAndProfile() {
//		List<Profile> listProfile = criteriaTypeRepository.getByStatus(true);
//		List<ProfileDto> profileDtos = new ArrayList<ProfileDto>();
//		if (listProfile != null) {
//			for (Profile profile : listProfile) {
//				profile.setProfileValues(profileValueService.findByStatusAndProfile(profile));
//				ProfileDto profileDto = convertBoToDto(profile);
//				profileDto.setProfileValueDtos(profileValueService.listProfileValueDto(profile.getProfileValues()));
//				profileDtos.add(profileDto);
//			}
//		}
//
//		return profileDtos;
//	}
//
	public Long countList(String name, String code, Boolean status) {
		return criteriaTypeRepository.countList(name, code, status);
	}

	public CriteriaTypeDto findByCode(String code) {
		CriteriaType criteriaType = criteriaTypeRepository.findByCodeAndDeletedAt(code, null);
		if (criteriaType == null)
			return null;
//		List<ProfileValue> values = new ArrayList<ProfileValue>();
//		for (ProfileValue profileValue : profile.getProfileValues()) {
//			if (profileValue.getDeletedAt() == null)
//				values.add(profileValue);
//		}
//		profile.setProfileValues(values);
		return convertBoToDto(criteriaType);
	}

	public CriteriaTypeDto findByIdDto(Integer id) {
		CriteriaType criteriaType = findById(id);
		if (criteriaType == null)
			return null;
//		List<ProfileValue> values = new ArrayList<ProfileValue>();
//		for (ProfileValue profileValue : profile.getProfileValues()) {
//			if (profileValue.getDeletedAt() == null)
//				values.add(profileValue);
//		}
//		profile.setProfileValues(values);
		CriteriaTypeDto criteriaTypeDto = convertBoToDto(criteriaType);
//		profileDto.setProfileValueDtos(profileValueService.listProfileValueDto(profile.getProfileValues()));
		return criteriaTypeDto;
	}

	public Boolean delete(Integer id) {
		CriteriaType criteriaType = findById(id);
		if (criteriaType == null) {
			return false;
		}
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		criteriaType.setDeletedAt(timestamp);
		criteriaTypeRepository.save(criteriaType);
//		kanseiWordService.deleteByCriteria(timestamp.toString(), id);
		return true;
	}

	public Boolean changeStatus(Integer id) {
		CriteriaType criteriaType = findById(id);
		if (criteriaType == null)
			return false;
		criteriaType.setStatus(!criteriaType.getStatus());
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		criteriaType.setUpdatedAt(timestamp);
		criteriaTypeRepository.save(criteriaType);
		criteriaTypeRepository.updateStatus(id);
		return true;
	}

	public CriteriaTypeDto save(CriteriaTypeDto dto) {
		return convertBoToDto(criteriaTypeRepository.save(convertDtoToBo(dto)));
	}

	public CriteriaTypeDto update(CriteriaTypeDto dto) {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		dto.setUpdatedAt(timestamp.toString());
		return convertBoToDto(criteriaTypeRepository.save(convertDtoToBo(dto)));
	}

	public CriteriaType findById(Integer id) {
		return criteriaTypeRepository.findByIdAndDeletedAt(id, null);
	}

	public CriteriaType convertDtoToBo(CriteriaTypeDto dto) {
		return modelMapper.map(dto, CriteriaType.class);
	}

	public CriteriaTypeDto convertBoToDto(CriteriaType bo) {
		return modelMapper.map(bo, CriteriaTypeDto.class);
	}

	public List<CriteriaTypeDto> convertListBoToListDto(List<CriteriaType> bos) {
		List<CriteriaTypeDto> dtos = new ArrayList<CriteriaTypeDto>();
		if (bos != null) {
			for (CriteriaType bo : bos) {
				CriteriaTypeDto dto = convertBoToDto(bo);
//				courseDto.setCategoryDto(categorySerivce.convertBoToDto(course.getCategoryOfCourse()));
//				courseDto.setTeacherDto(userSerivce.convertBoToDto(course.getTeacher()));
				dtos.add(dto);

			}
		}
		return dtos;
	}
	
	public Integer countCriteriaType() {
		return criteriaTypeRepository.countCriteriaType();
	}
	
	public CriteriaType getCriteriaTypeUse() {
		return criteriaTypeRepository.getCriteraTypeUse();
	}
	
	public List<CriteriaType> getCriterias() {
		return criteriaTypeRepository.getCriteraTypes();
	}
}
