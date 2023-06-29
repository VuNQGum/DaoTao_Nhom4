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

import com.hust.daotao.dto.CapacityTestDto;
import com.hust.daotao.dto.ProfileDto;
import com.hust.daotao.entity.CapacityTest;
import com.hust.daotao.entity.Profile;
import com.hust.daotao.entity.User;
import com.hust.daotao.entity.UserProfile;
import com.hust.daotao.repository.CapacityTestRepository;
import com.hust.daotao.response.CapacityTestResponse;
import com.hust.daotao.util.Constants;
import com.hust.daotao.util.UtilMethod;

@Service
public class CapacityTestService {
	@Autowired
	private CapacityTestRepository testRepository;
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private UserService userService;
	@Autowired
	private UserProfileService userProfileService;
	@Autowired
	private ProfileService profileService;

	public CapacityTestResponse getList(Integer page, String question, Boolean status) {
		Sort sortable = null;
		sortable = Sort.by("updatedAt").descending();
		Pageable pageable = PageRequest.of(page, Constants.pageSize, sortable);
		List<CapacityTest> bos = testRepository.getListQuestion(question, status, pageable).getContent();
		CapacityTestResponse res = new CapacityTestResponse();
		List<CapacityTestDto> dtos = new ArrayList<CapacityTestDto>();
		if (bos != null) {
			dtos = convertListBoToListDto(bos);
		}
		res.setAaData(dtos);
		return res;
	}

	public List<CapacityTest> getListQuestion() {
		Sort sortable = null;
		sortable = Sort.by("updatedAt").descending();
		Pageable pageable = PageRequest.of(0, 20, sortable);
		List<CapacityTest> bos = testRepository.findByStatusAndDeletedAt(pageable, true, null);
		return bos;
	}

	public Long countList(String question, Boolean status) {
		return testRepository.countListProfileValues(question, status);
	}

	public CapacityTest save(CapacityTestDto dto) {
		if (dto == null)
			return null;
		CapacityTest bo = convertDtoToBo(dto);
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		bo.setUpdatedAt(timestamp);
		bo.setStatus(false);
		return testRepository.save(bo);
	}

	public CapacityTest update(CapacityTestDto dto) {
		if (dto == null)
			return null;
		CapacityTest bo = convertDtoToBo(dto);
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		bo.setUpdatedAt(timestamp);
		CapacityTest test = testRepository.findByIdAndDeletedAt(dto.getId(), null);
		bo.setStatus(test.getStatus());
		return testRepository.save(bo);
	}

	public CapacityTest delete(Integer id) {
		if (id == null)
			return null;
		CapacityTest question = testRepository.findByIdAndDeletedAt(id, null);
		if (question == null)
			return null;
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());

		question.setDeletedAt(timestamp);
		return testRepository.save(question);
	}

	public CapacityTest changeStatus(Integer id) {
		if (id == null)
			return null;
		CapacityTest question = testRepository.findByIdAndDeletedAt(id, null);
		if (question == null)
			return null;
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());

		question.setUpdatedAt(timestamp);
		question.setStatus(!question.getStatus());
		return testRepository.save(question);
	}

	public CapacityTestDto findByIdDto(Integer id) {
		if (id == null)
			return null;
		CapacityTest bo = testRepository.findByIdAndDeletedAt(id, null);
		if (bo == null)
			return null;
		return convertBoToDto(bo);

	}

	public CapacityTest convertDtoToBo(CapacityTestDto dto) {
		return modelMapper.map(dto, CapacityTest.class);
	}

	public CapacityTestDto convertBoToDto(CapacityTest bo) {
		return modelMapper.map(bo, CapacityTestDto.class);
	}

	public List<CapacityTestDto> convertListBoToListDto(List<CapacityTest> bos) {
		List<CapacityTestDto> dtos = new ArrayList<CapacityTestDto>();
		if (bos != null) {
			for (CapacityTest a : bos) {
				CapacityTestDto dto = convertBoToDto(a);
				dtos.add(dto);

			}
		}
		return dtos;
	}

	public int resultTest(List<CapacityTestDto> answersOfUser) {
		List<CapacityTest> questions = getListQuestion();
		int point = 0;
		for (int i = 0; i < questions.size(); i++) {
			if (checkAnswer(questions.get(i).getAnswerTrue(), questions.get(i).getId(), answersOfUser)) {
				point++;
			}
		}
		int result = point * 5;
		User user = userService.getUserCurrentLogin();
		user.setResultTest(result);
		userService.save(user);
		ProfileDto profileDto = profileService.findByCode("difficult_level");
		if (profileDto != null) {
			Profile profile = profileService.convertDtoToBo(profileDto);
			Integer resultConvert = UtilMethod.convertTestResult(result);
			UserProfile up = new UserProfile(user, profile, resultConvert.toString());
			userProfileService.save(up);
		}
		return point;
	}

	public boolean checkAnswer(String answerTrue, Integer questionId, List<CapacityTestDto> answersOfUser) {
		for (CapacityTestDto answer : answersOfUser) {
			if (answer.getId() == questionId && answerTrue.equals(answer.getAnswerOfUser()))
				return true;
		}
		return false;
	}
}
