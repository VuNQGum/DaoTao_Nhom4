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

import com.hust.daotao.dto.CriteriaDto;
import com.hust.daotao.entity.Criteria;
import com.hust.daotao.entity.KanseiWord;
import com.hust.daotao.repository.CriteriaRepository;
import com.hust.daotao.response.CriteriaResponse;
import com.hust.daotao.util.Constants;

@Service
public class CriteriaService {
	@Autowired
	private CriteriaRepository criteriaRepository;
	@Autowired
	private KanseiWordService kanseiWordService;
//	@Autowired
//	private UserProfileService userProfileService;
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private CriteriaTypeService criteriaTypeService;

	public CriteriaResponse getList(Integer page, String name, String code, Boolean status, Integer type) {
		Sort sortable = null;
		sortable = Sort.by("id").ascending();
		Pageable pageable = PageRequest.of(page, Constants.pageSize, sortable);
		List<Criteria> listCriteria = criteriaRepository.getList(name, code, status, pageable, type).getContent();
		CriteriaResponse criteriaResponse = new CriteriaResponse();
		List<CriteriaDto> dtos = new ArrayList<CriteriaDto>();
		if (listCriteria != null) {
			if (!CollectionUtils.isEmpty(listCriteria)) {
				dtos = listCriteria.parallelStream().map(criteria -> convertBoToDto(criteria))
						.collect(Collectors.toList());
			}
		}
		criteriaResponse.setAaData(dtos);
		return criteriaResponse;
	}

	public Long countList(String name, String code, Boolean status, Integer type) {
		return criteriaRepository.countList(name, code, status, type);
	}

	public CriteriaDto findByCode(String code) {
		Criteria criteria = criteriaRepository.findByCodeAndDeletedAt(code, null);
		if (criteria == null)
			return null;

		return convertBoToDto(criteria);
	}

	public CriteriaDto findByIdDto(Integer id) {
		Criteria bo = findById(id);
		if (bo == null)
			return null;
//		List<ProfileValue> values = new ArrayList<ProfileValue>();
//		for (ProfileValue profileValue : profile.getProfileValues()) {
//			if (profileValue.getDeletedAt() == null)
//				values.add(profileValue);
//		}
//		profile.setProfileValues(values);
		CriteriaDto dto = convertBoToDto(bo);
//		profileDto.setProfileValueDtos(profileValueService.listProfileValueDto(profile.getProfileValues()));
		return dto;
	}

	public Boolean delete(Integer id) {
		Criteria criteria = findById(id);
		if (criteria == null) {
			return false;
		}
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		criteria.setDeletedAt(timestamp);
		criteriaRepository.save(criteria);
//		kanseiWordService.deleteByCriteria(timestamp.toString(), id);
		return true;
	}

	public Boolean changeStatus(Integer id) {
		Criteria criteria = findById(id);
		if (criteria == null)
			return false;
		criteria.setStatus(!criteria.getStatus());
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		criteria.setUpdatedAt(timestamp);
		criteriaRepository.save(criteria);
		return true;
	}

	public CriteriaDto save(CriteriaDto criteriaDto) {
		Criteria criteria = convertDtoToBo(criteriaDto);
		criteria.setCriteriaType(criteriaTypeService.findById(criteriaDto.getTypeId()));
		return convertBoToDto(criteriaRepository.save(criteria));
	}

	public CriteriaDto update(CriteriaDto criteriaDto) {
		Criteria criteria = findById(criteriaDto.getId());
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		criteriaDto.setUpdatedAt(timestamp.toString());
		Criteria result = convertDtoToBo(criteriaDto);
		result.setCriteriaType(criteriaTypeService.findById(criteriaDto.getTypeId()));
		result.setKanseiWords(criteria.getKanseiWords());
		return convertBoToDto(criteriaRepository.save(result));
	}

	public Criteria findById(Integer id) {
		return criteriaRepository.findByIdAndDeletedAt(id, null);
	}

	public Criteria convertDtoToBo(CriteriaDto dto) {
		return modelMapper.map(dto, Criteria.class);
	}

	public CriteriaDto convertBoToDto(Criteria bo) {
		return modelMapper.map(bo, CriteriaDto.class);
	}

	public boolean updateKanseiWord(Integer criteriaId, Integer kanseiId) {
		Criteria criteria = findById(criteriaId);
		if (criteria == null)
			return false;
		KanseiWord kanseiWord = kanseiWordService.findById(kanseiId);
		if (kanseiWord == null)
			return false;
		List<KanseiWord> kanseiWords = criteria.getKanseiWords();
		if (kanseiWords == null)
			kanseiWords = new ArrayList<KanseiWord>();
		if (kanseiWords.contains(kanseiWord)) {
			kanseiWords.remove(kanseiWord);
			criteria.setKanseiWords(kanseiWords);
			criteriaRepository.save(criteria);
		} else {
			kanseiWords.add(kanseiWord);
			criteria.setKanseiWords(kanseiWords);
			criteriaRepository.save(criteria);
		}
		return true;
	}

	public List<Criteria> getListCriteriaByType() {
		return criteriaRepository.getListCriteriaShow();
	}

	public List<Integer> listIdCriteria() {
		return criteriaRepository.getListCriteriaUse();
	}

	public List<Criteria> listCriteriaByType(Integer typeId) {
		return criteriaRepository.getListCriteriaByType(typeId);
	}

}
