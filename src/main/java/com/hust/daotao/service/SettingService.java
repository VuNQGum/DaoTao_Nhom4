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

import com.hust.daotao.dto.SettingDto;
import com.hust.daotao.entity.Setting;
import com.hust.daotao.repository.SettingRepository;
import com.hust.daotao.response.SettingResponse;
import com.hust.daotao.util.Constants;

@Service
public class SettingService {
	@Autowired
	private SettingRepository settingRepository;
	@Autowired
	private ModelMapper modelMapper;

	public SettingResponse getList(Integer page, String name, String code) {
		Sort sortable = null;
		sortable = Sort.by("updatedAt").descending();
		Pageable pageable = PageRequest.of(page, Constants.pageSize, sortable);
		List<Setting> bos = settingRepository.getList(name, code, pageable).getContent();
		SettingResponse settingResponse = new SettingResponse();
		List<SettingDto> dtos = new ArrayList<SettingDto>();
		if (bos != null) {
			dtos = convertListBoToListDto(bos);
		}
		settingResponse.setAaData(dtos);
		return settingResponse;
	}

	public Long countList(String name, String code) {
		return settingRepository.countList(name, code);
	}

	public SettingDto save(SettingDto dto) {
		Setting bo = convertDtoToBo(dto);
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		bo.setUpdatedAt(timestamp);
		Setting result = settingRepository.save(bo);
		if (result == null)
			return null;
		return convertBoToDto(result);

	}

	public Boolean delete(Integer id) {
		Setting setting = findById(id);
		if (setting == null) {
			return false;
		}
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		setting.setDeletedAt(timestamp);
		settingRepository.save(setting);
//		kanseiWordService.deleteByCriteria(timestamp.toString(), id);
		return true;
	}

	public Setting convertDtoToBo(SettingDto dto) {
		return modelMapper.map(dto, Setting.class);
	}

	public SettingDto convertBoToDto(Setting bo) {
		return modelMapper.map(bo, SettingDto.class);
	}

	public Setting findById(Integer id) {
		return settingRepository.findByIdAndDeletedAt(id, null);
	}

	public Setting findByCode(String code) {
		return settingRepository.findByCodeAndDeletedAt(code, null);
	}

	public List<SettingDto> convertListBoToListDto(List<Setting> bos) {
		List<SettingDto> dtos = new ArrayList<SettingDto>();
		if (bos != null) {
			for (Setting bo : bos) {
				SettingDto dto = convertBoToDto(bo);
				dtos.add(dto);

			}
		}
		return dtos;
	}

	public SettingDto findByIdDto(Integer id) {
		Setting bo = findById(id);
		if (bo == null)
			return null;
		return convertBoToDto(bo);
	}

	public SettingDto update(SettingDto dto) {

		return save(dto);
	}
}
