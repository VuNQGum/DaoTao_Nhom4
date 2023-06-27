package com.hust.daotao.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.hust.daotao.dto.ArticleDto;
import com.hust.daotao.entity.Article;
import com.hust.daotao.entity.Tag;
import com.hust.daotao.repository.ArticleRepository;
import com.hust.daotao.response.ArticleResponse;
import com.hust.daotao.util.Constants;

@Service
public class ArticleService {
	@Autowired
	private ArticleRepository articleRepository;
	@Autowired
	private TagService tagService;
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private UserService userService;

	public ArticleResponse getList(Integer page, Integer quantityShowing, Integer tagId) {
		Sort sortable = null;
		sortable = Sort.by("updatedAt").descending().and(Sort.by("id").descending());
		Pageable pageable = PageRequest.of(page - 1, quantityShowing, sortable);

		Page<Article> panigation = articleRepository.getListCourse(pageable);
		List<Article> articles = panigation.getContent();
		List<ArticleDto> dtos = new ArrayList<ArticleDto>();
		if (articles != null) {
			dtos = convertListBoToListDto(articles);
		}
		ArticleResponse res = new ArticleResponse((long) panigation.getTotalElements(),
				(long) panigation.getTotalPages(), dtos);
		return res;
	}

	public ArticleResponse getList(Integer page, String title, Integer userId) {
		Sort sortable = null;
		sortable = Sort.by("updatedAt").descending();
		Pageable pageable = PageRequest.of(page, Constants.pageSize, sortable);
		List<Article> articles = articleRepository.getList(title, userId, pageable).getContent();
		ArticleResponse res = new ArticleResponse();
		List<ArticleDto> dtos = new ArrayList<ArticleDto>();
		if (articles != null) {
			dtos = convertListBoToListDto(articles);
		}
		res.setAaData(dtos);
		return res;
	}

	public Long countList(String title) {
		return articleRepository.countList(title);
	}

	public ArticleDto save(ArticleDto dto) {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		Article article = convertDtoToBo(dto);
		List<Integer> tagIds = dto.getTagIds();
		List<Tag> tags = new ArrayList<Tag>();
		for (Integer tagId : tagIds) {
			tags.add(tagService.findById(tagId));
		}
		article.setUpdatedAt(timestamp);
		article.setTags(tags);
		article.setUserPostArticle(userService.getUserCurrentLogin());
		article.setViews(0);
		Article result = articleRepository.save(article);
		if (result != null)
			return convertBoToDto(result);
		else
			return null;
	}

	public Article findById(Integer id) {
		return articleRepository.findByIdAndDeletedAt(id, null);
	}

	public Article convertDtoToBo(ArticleDto dto) {
		return modelMapper.map(dto, Article.class);
	}

	public ArticleDto convertBoToDto(Article bo) {
		return modelMapper.map(bo, ArticleDto.class);

	}

	public List<ArticleDto> convertListBoToListDto(List<Article> bos) {
		List<ArticleDto> dtos = new ArrayList<ArticleDto>();
		if (bos != null) {
			for (Article a : bos) {
				ArticleDto dto = convertBoToDto(a);
//				courseDto.setCategoryDto(categorySerivce.convertBoToDto(course.getCategoryOfCourse()));
//				courseDto.setTeacherDto(userSerivce.convertBoToDto(course.getTeacher()));
				dtos.add(dto);

			}
		}
		return dtos;
	}

	public Article updateView(Article article) {
		article.setViews(article.getViews() + 1);
		return articleRepository.save(article);
	}

	public List<Article> getListArticleShowClient() {
		Sort sortable = null;
		sortable = Sort.by("updatedAt").descending().and(Sort.by("id").descending());
		Pageable pageable = PageRequest.of(0, 4, sortable);
		return articleRepository.getListArticleShowClient(pageable);
	}

	public Boolean delete(Integer id) {
		Article article = findById(id);
		if (article == null) {
			return false;
		}
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		article.setDeletedAt(timestamp);
		articleRepository.save(article);
		return true;
	}
	
	public ArticleDto update(ArticleDto dto) {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		Article articleCurrent = findById(dto.getId());
		Article article = convertDtoToBo(dto);
		List<Integer> tagIds = dto.getTagIds();
		List<Tag> tags = new ArrayList<Tag>();
		for (Integer tagId : tagIds) {
			tags.add(tagService.findById(tagId));
		}
		article.setUpdatedAt(timestamp);
		article.setTags(tags);
		article.setUserPostArticle(articleCurrent.getUserPostArticle());
		article.setViews(articleCurrent.getViews());
		Article result = articleRepository.save(article);
		if (result != null)
			return convertBoToDto(result);
		else
			return null;
	}


}