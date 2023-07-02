package com.hust.daotao.rest.admin;

import com.hust.daotao.dto.CategoryDto;
import com.hust.daotao.response.CategoryResponse;
import com.hust.daotao.service.AmazonUploadService;
import com.hust.daotao.service.CategoryService;
import com.hust.daotao.util.Constants;
import com.hust.daotao.util.Message;
import com.hust.daotao.util.Status;
import com.hust.daotao.util.UtilMethod;
import com.hust.daotao.validator.CategoryValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/admin/categories")
public class CategoryRestController {
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private AmazonUploadService amazonUploadService;

	@GetMapping("")
	public ResponseEntity<CategoryResponse> index(@RequestParam(name = "code", defaultValue = "") String code,
			@RequestParam(name = "name", defaultValue = "") String name,
			@RequestParam(name = "status", defaultValue = "") String statusValue,
			@RequestParam(name = "length", required = false) Integer length,
			@RequestParam(name = "start", required = false) Integer start) {

		Boolean status = null;
		if (!statusValue.equals("")) {
			if (Integer.parseInt(statusValue) == 1) {
				status = true;
			} else if (Integer.parseInt(statusValue) == 0) {
				status = false;
			}
		}
		Integer pageDisplayLength = 10;
		if (length != null)
			pageDisplayLength = length;
		Constants.pageSize = pageDisplayLength;
		Integer iDisplayStart = 1;
		if (start != null)
			iDisplayStart = start;
		Integer page = (iDisplayStart / pageDisplayLength);
		Long count = categoryService.countList(name, code, status);
		CategoryResponse categoryResponse = categoryService.getList(page, name, code, status);
		categoryResponse.setiTotalDisplayRecords(count);
		categoryResponse.setiTotalRecords(count);
		return new ResponseEntity<CategoryResponse>(categoryResponse, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<CategoryResponse> show(@PathVariable("id") Integer id) {
		if (id == null) {
			CategoryResponse categoryResponse = new CategoryResponse(Message.MESSAGE_NULL, Status.STATUS_NULL);
			return new ResponseEntity<CategoryResponse>(categoryResponse, HttpStatus.OK);

		}
		CategoryDto topicDto = categoryService.findByIdDto(id);
		if (topicDto == null) {
			CategoryResponse categoryResponse = new CategoryResponse(Message.MESSAGE_ENTITY_NOT_EXIST,
					Status.STATUS_ENTITY_NOT_EXIST);
			return new ResponseEntity<CategoryResponse>(categoryResponse, HttpStatus.OK);

		}
		CategoryResponse categoryResponse = new CategoryResponse(Message.MESSAGE_GET_ENTITY_SUCCESS,
				Status.STATUS_GET_ENTITY_SUCCESS, topicDto);
		return new ResponseEntity<CategoryResponse>(categoryResponse, HttpStatus.OK);
	}

	@PostMapping("")
	public ResponseEntity<CategoryResponse> save(@RequestParam(name = "name", required = false) String name,
			@RequestParam(name = "code") String code, @RequestParam(name = "status") Boolean status,
			@RequestParam(name = "description") String description,
			@RequestParam(name = "image", required = false) MultipartFile image) {
		name = UtilMethod.setValueIsNull(name);
		code = UtilMethod.setValueIsNull(code);
		description = UtilMethod.setValueIsNull(description);
		String urlImage = (image != null ? this.amazonUploadService.uploadFile(image)
				: Constants.IMAGE_DEFAULT);
		if (CategoryValidator.isEmpty(name, code)) {
			CategoryResponse categoryResponse = new CategoryResponse(Message.MESSAGE_REQUEST_EMPTY,
					Status.STATUS_REQUEST_ERROR);
			return new ResponseEntity<CategoryResponse>(categoryResponse, HttpStatus.OK);
		}
		if (categoryService.findByCode(code) != null) {
			CategoryResponse categoryResponse = new CategoryResponse(Message.MESSAGE_EXIST_CODE,
					Status.STATUS_REQUEST_ERROR);
			return new ResponseEntity<CategoryResponse>(categoryResponse, HttpStatus.OK);
		}
		CategoryDto topicDto = new CategoryDto(name, code, description, urlImage, status);
		CategoryResponse categoryResponse = new CategoryResponse(Message.MESSAGE_ADD_SUCCESS,
				Status.STATUS_ADD_SUCCESS);
		categoryResponse.setElement(categoryService.save(topicDto));
		return new ResponseEntity<CategoryResponse>(categoryResponse, HttpStatus.OK);
	}

	@PutMapping("")
	public ResponseEntity<CategoryResponse> update(@RequestParam(name = "id", required = false) Integer id,
			@RequestParam(name = "name", required = false) String name, @RequestParam(name = "code") String code,
			@RequestParam(name = "status") Boolean status, @RequestParam(name = "description") String description,
			@RequestParam(name = "image", required = false) MultipartFile image,
			@RequestParam(name = "imageUrlCurrent") String imageUrlCurrent) {
		name = UtilMethod.setValueIsNull(name);
		code = UtilMethod.setValueIsNull(code);
		description = UtilMethod.setValueIsNull(description);
		String urlImage = (image != null ? this.amazonUploadService.uploadFile(image) : imageUrlCurrent);
		if (CategoryValidator.isEmpty(name, code)) {
			CategoryResponse categoryResponse = new CategoryResponse(Message.MESSAGE_REQUEST_EMPTY,
					Status.STATUS_REQUEST_ERROR);
			return new ResponseEntity<CategoryResponse>(categoryResponse, HttpStatus.OK);
		}
		CategoryDto resulTopicDto = categoryService.findByCode(code);
		if (resulTopicDto != null && !id.equals(resulTopicDto.getId())) {
			CategoryResponse categoryResponse = new CategoryResponse(Message.MESSAGE_EXIST_CODE,
					Status.STATUS_REQUEST_ERROR);
			return new ResponseEntity<CategoryResponse>(categoryResponse, HttpStatus.OK);
		}
		CategoryDto topicDto = new CategoryDto(name, code, description, urlImage, status);
		topicDto.setId(id);
		CategoryResponse categoryResponse = new CategoryResponse(Message.MESSAGE_UPDATE_SUCCESS,
				Status.STATUS_UPDATE_SUCCESS);
		categoryResponse.setElement(categoryService.update(topicDto));
		return new ResponseEntity<CategoryResponse>(categoryResponse, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<CategoryResponse> delete(@PathVariable("id") Integer id) {
		if (id == null) {
			CategoryResponse categoryResponse = new CategoryResponse(Message.MESSAGE_NULL, Status.STATUS_NULL);
			return new ResponseEntity<CategoryResponse>(categoryResponse, HttpStatus.OK);

		} else if (!categoryService.delete(id)) {
			CategoryResponse categoryResponse = new CategoryResponse(Message.MESSAGE_DELETE_FAIL,
					Status.STATUS_DELETE_FAIL);
			return new ResponseEntity<CategoryResponse>(categoryResponse, HttpStatus.OK);
		}
		CategoryResponse categoryResponse = new CategoryResponse(Message.MESSAGE_DELETE_SUCCESS,
				Status.STATUS_DELETE_SUCCESS);
		return new ResponseEntity<CategoryResponse>(categoryResponse, HttpStatus.OK);
	}

	@PutMapping("/{id}/change-status")
	public ResponseEntity<CategoryResponse> changeStatus(@PathVariable("id") Integer id) {
		if (id == null) {
			CategoryResponse categoryResponse = new CategoryResponse(Message.MESSAGE_NULL, Status.STATUS_NULL);
			return new ResponseEntity<CategoryResponse>(categoryResponse, HttpStatus.OK);

		} else if (!categoryService.changeStatus(id)) {
			CategoryResponse categoryResponse = new CategoryResponse(Message.MESSAGE_CHANGE_STATUS_FAIL,
					Status.STATUS_CHANGE_STATUS_FAIL);
			return new ResponseEntity<CategoryResponse>(categoryResponse, HttpStatus.OK);
		}
		CategoryResponse categoryResponse = new CategoryResponse(Message.MESSAGE_CHANGE_STATUS_SUCCESS,
				Status.STATUS_CHANGE_STATUS_SUCCESS);
		return new ResponseEntity<CategoryResponse>(categoryResponse, HttpStatus.OK);
	}

}
