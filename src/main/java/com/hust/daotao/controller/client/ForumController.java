package com.hust.thesis.controller.client;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.hust.thesis.dto.DataCrawSoict;
import com.hust.thesis.dto.Panigation;
import com.hust.thesis.entity.Article;
import com.hust.thesis.entity.Tag;
import com.hust.thesis.entity.User;
import com.hust.thesis.response.ArticleResponse;
import com.hust.thesis.service.ArticleService;
import com.hust.thesis.service.TagService;
import com.hust.thesis.service.UserService;
import com.hust.thesis.util.GetDocumentFromURL;

@Controller
public class ForumController {
	@Autowired
	private TagService tagService;
	@Autowired
	private ArticleService articleService;
	@Autowired
	private UserService userService;

	@GetMapping("/forum")
	public String forum(Model model, @RequestParam(name = "page", defaultValue = "1") Integer page,
			@RequestParam(name = "showing", defaultValue = "4") Integer quantityShowing) {
		ArticleResponse articleResponse = articleService.getList(page, 10, null);
		Panigation panigation = new Panigation(page, articleResponse.getITotalPage());
		model.addAttribute("response", articleResponse);
		model.addAttribute("panigation", panigation);
//		model.addAttribute("teachers", userService.getListTeacher());

		List<DataCrawSoict> newsHome = GetDocumentFromURL.getNewsBySoictHome();
		model.addAttribute("newsHome", newsHome);
		model.addAttribute("title", "Diễn đàn");
		model.addAttribute("isForum", true);

		return "client/forum/forum";
	}

	@GetMapping("/forum/post")
	public String post(Model model) {
		List<Tag> tags = tagService.getListTag();
		model.addAttribute("title", "Đăng bài");
		model.addAttribute("tags", tags);
		model.addAttribute("isForum", true);

		return "client/forum/post";
	}

	@GetMapping("/forum/article/{id}")
	public String article(Model model, @PathVariable("id") Integer id, HttpServletRequest request) {
		Article article = articleService.findById(id);
		List<DataCrawSoict> newsHome = GetDocumentFromURL.getNewsBySoictHome();
		model.addAttribute("newsHome", newsHome);
		model.addAttribute("article", article);
		model.addAttribute("title", "Xem bài viết");
		model.addAttribute("isForum", true);
		articleService.updateView(article);
		String url = request.getRequestURL().toString();
		model.addAttribute("urlWeb", url);
		return "client/forum/article";
	}

	@GetMapping("/my-articles")
	public String myArticles(Model model) {
		User user = userService.getUserCurrentLogin();
		model.addAttribute("userId", user.getId());
		model.addAttribute("title", "Bài viết của tôi");
		return "client/forum/my-articles";
	}

	@GetMapping("/forum/article/{id}/edit")
	public String edit(Model model, @PathVariable("id") Integer id) {
		Article article = articleService.findById(id);
		User user = userService.getUserCurrentLogin();
		if (article.getUserPostArticle().getId() != user.getId())
			return "redirect:/";
		model.addAttribute("article", article);
		List<Tag> tags = tagService.getListTag();
		model.addAttribute("title", "Chỉnh sửa bài viết");
		model.addAttribute("tags", tags);
		return "client/forum/edit";
	}
}
