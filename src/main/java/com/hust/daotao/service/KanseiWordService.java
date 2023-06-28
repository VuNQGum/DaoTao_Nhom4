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

import com.hust.daotao.dto.KanseiWordDto;
import com.hust.daotao.entity.KanseiWord;
import com.hust.daotao.repository.KanseiWordRepository;
import com.hust.daotao.response.KanseiWordResponse;
import com.hust.daotao.util.Constants;

@Service
public class KanseiWordService {
	@Autowired
	private KanseiWordRepository kanseiWordRepository;

	@Autowired
	private ModelMapper modelMapper;


	public KanseiWordResponse getList(Integer page, String negative, String positive, Boolean status) {
		Sort sortable = null;
		sortable = Sort.by("id").ascending();
		Pageable pageable = PageRequest.of(page, Constants.pageSize, sortable);
		List<KanseiWord> list = kanseiWordRepository.getList(negative, positive, status, pageable).getContent();
		KanseiWordResponse kanseiResponse = new KanseiWordResponse();
		List<KanseiWordDto> dtos = new ArrayList<KanseiWordDto>();
		if (list != null) {
			dtos = convertListBoToListDto(list);
		}
		kanseiResponse.setAaData(dtos);
		return kanseiResponse;
	}

//	public List<ProfileDto> getByStatusAndProfile() {
//		List<Profile> listProfile = kanseiWordRepository.getByStatus(true);
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
	public Long countList(String negative, String positive, Boolean status) {
		return kanseiWordRepository.countList(negative, positive, status);
	}

	public List<KanseiWord> getListKanseiShow() {
		return kanseiWordRepository.findByDeletedAtAndStatus(null, true);
	}

//	public KanseiWordDto findByCode(String code) {
//		KanseiWord bo = kanseiWordRepository.findByCodeAndDeletedAt(code, null);
//		if (bo == null)
//			return null;
////		List<ProfileValue> values = new ArrayList<ProfileValue>();
////		for (ProfileValue profileValue : profile.getProfileValues()) {
////			if (profileValue.getDeletedAt() == null)
////				values.add(profileValue);
////		}
////		profile.setProfileValues(values);
//		return convertBoToDto(bo);
//	}

	public KanseiWordDto findByIdDto(Integer id) {
		KanseiWord bo = findById(id);
		if (bo == null)
			return null;
//		List<ProfileValue> values = new ArrayList<ProfileValue>();
//		for (ProfileValue profileValue : profile.getProfileValues()) {
//			if (profileValue.getDeletedAt() == null)
//				values.add(profileValue);
//		}
//		profile.setProfileValues(values);
		KanseiWordDto dto = convertBoToDto(bo);
//		profileDto.setProfileValueDtos(profileValueService.listProfileValueDto(profile.getProfileValues()));
		return dto;
	}

	public Boolean delete(Integer id) {
		KanseiWord kansei = findById(id);
		if (kansei == null)
			return false;
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		kansei.setDeletedAt(timestamp);
		kanseiWordRepository.save(kansei);
//		kanseiWordService.deleteByCriteria(timestamp.toString(), id);
		return true;
	}

	public Boolean changeStatus(Integer id) {
		KanseiWord kansei = findById(id);
		if (kansei == null)
			return false;
		kansei.setStatus(!kansei.getStatus());
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		kansei.setUpdatedAt(timestamp);
		kanseiWordRepository.save(kansei);
		return true;
	}

	public KanseiWordDto save(KanseiWordDto kanseiDto) {
		KanseiWord kansei = convertDtoToBo(kanseiDto);
		return convertBoToDto(kanseiWordRepository.save(kansei));
	}

	public KanseiWordDto update(KanseiWordDto kanseiDto) {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		kanseiDto.setUpdatedAt(timestamp.toString());
		KanseiWord kansei = convertDtoToBo(kanseiDto);
		return convertBoToDto(kanseiWordRepository.save(kansei));
	}

	public KanseiWord findById(Integer id) {
		return kanseiWordRepository.findByIdAndDeletedAt(id, null);
	}

	public KanseiWord convertDtoToBo(KanseiWordDto dto) {
		return modelMapper.map(dto, KanseiWord.class);
	}

	public KanseiWordDto convertBoToDto(KanseiWord bo) {
		return modelMapper.map(bo, KanseiWordDto.class);
	}

	public List<KanseiWordDto> convertListBoToListDto(List<KanseiWord> bos) {
		List<KanseiWordDto> dtos = new ArrayList<KanseiWordDto>();
		if (bos != null) {
			for (KanseiWord bo : bos) {
				KanseiWordDto dto = convertBoToDto(bo);
//				courseDto.setCategoryDto(categorySerivce.convertBoToDto(course.getCategoryOfCourse()));
//				courseDto.setTeacherDto(userSerivce.convertBoToDto(course.getTeacher()));
				dtos.add(dto);

			}
		}
		return dtos;
	}
}
