package com.hust.daotao.util;

import com.hust.daotao.dto.DataCrawSoict;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GetDocumentFromURL {
	public static List<DataCrawSoict> getNewsBySoictHome() {
		Document doc = null;
		List<DataCrawSoict> datas = new ArrayList<DataCrawSoict>();
		try {
			doc = Jsoup.connect("https://soict.hust.edu.vn/").get();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (doc != null) {
			Elements news = doc.getElementsByClass("post-item");
			for (int i = 0; i < 4; i++) {
				DataCrawSoict data = new DataCrawSoict(news.get(i).select(".post-title").text(),
						news.get(i).select(".image-cover img").attr("data-lazy-src"),
						news.get(i).select(".from_the_blog_excerpt").text(), news.get(i).select(".col-inner > a").attr("href"));
   				datas.add(data);
			}
			return datas;
		}
		return null;

	}

	public static void main(String[] args) {
		getNewsBySoictHome();
	}
}
