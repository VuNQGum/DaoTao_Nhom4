package com.hust.daotao.rest.client;

import com.hust.daotao.dto.ArticleDto;
import com.hust.daotao.entity.User;
import com.hust.daotao.response.ArticleResponse;
import com.hust.daotao.service.AmazonUploadService;
import com.hust.daotao.service.ArticleService;
import com.hust.daotao.service.UserService;
import com.hust.daotao.util.Constants;
import com.hust.daotao.util.Message;
import com.hust.daotao.util.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/client/articles")
public class ArticleRestController {
	@Autowired
	private AmazonUploadService amazonSerivce;
	@Autowired
	private ArticleService articleService;
	@Autowired
	private UserService userService;
	@GetMapping("/upload-image")
	public ResponseEntity<String> upload(@RequestParam(name = "upload", required = false) MultipartFile image) {
		String url = amazonSerivce.uploadFile(image);
		return new ResponseEntity<String>(url, HttpStatus.OK);
	}

	@PostMapping(name = "", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ArticleResponse> create(@RequestBody ArticleDto articleDto) {
		ArticleDto result = articleService.save(articleDto);
		ArticleResponse res = new ArticleResponse(Status.STATUS_ADD_SUCCESS, Message.MESSAGE_ADD_SUCCESS, result);
		return new ResponseEntity<ArticleResponse>(res, HttpStatus.OK);
	}

	@GetMapping("")
	public ResponseEntity<ArticleResponse> index(@RequestParam(name = "title", defaultValue = "") String title,
			@RequestParam(name = "length", required = false) Integer length,
			@RequestParam(name = "start", required = false) Integer start) {
		User userLogin = userService.getUserCurrentLogin();
		Integer pageDisplayLength = 10;
		if (length != null)
			pageDisplayLength = length;
		Constants.pageSize = pageDisplayLength;
		Integer iDisplayStart = 1;
		if (start != null)
			iDisplayStart = start;
		Integer page = (iDisplayStart / pageDisplayLength);
		Long count = articleService.countList(title);
		ArticleResponse res = articleService.getList(page, title, userLogin.getId());
		res.setiTotalDisplayRecords(count);
		res.setiTotalRecords(count);
		return new ResponseEntity<ArticleResponse>(res, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ArticleResponse> delete(@PathVariable("id") Integer id) {
		if (id == null) {
			ArticleResponse res = new ArticleResponse(Message.MESSAGE_NULL, Status.STATUS_NULL);
			return new ResponseEntity<ArticleResponse>(res, HttpStatus.OK);

		} else if (!articleService.delete(id)) {
			ArticleResponse res = new ArticleResponse(Message.MESSAGE_DELETE_FAIL, Status.STATUS_DELETE_FAIL);
			return new ResponseEntity<ArticleResponse>(res, HttpStatus.OK);
		}
		ArticleResponse res = new ArticleResponse(Message.MESSAGE_DELETE_SUCCESS, Status.STATUS_DELETE_SUCCESS);
		return new ResponseEntity<ArticleResponse>(res, HttpStatus.OK);
	}

	@PutMapping(name = "", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ArticleResponse> update(@RequestBody ArticleDto articleDto) {
		ArticleDto result = articleService.update(articleDto);
		ArticleResponse res = new ArticleResponse(Status.STATUS_UPDATE_SUCCESS, "Cập nhật thành công.", result);
		return new ResponseEntity<ArticleResponse>(res, HttpStatus.OK);
	}

}
