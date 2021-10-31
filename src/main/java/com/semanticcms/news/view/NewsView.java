/*
 * semanticcms-news-view - SemanticCMS view of all news in the current page and all children.
 * Copyright (C) 2016, 2017, 2019, 2020, 2021  AO Industries, Inc.
 *     support@aoindustries.com
 *     7262 Bull Pen Cir
 *     Mobile, AL 36695
 *
 * This file is part of semanticcms-news-view.
 *
 * semanticcms-news-view is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * semanticcms-news-view is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with semanticcms-news-view.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.semanticcms.news.view;

import com.aoapps.html.servlet.FlowContent;
import com.aoapps.servlet.http.Dispatcher;
import com.aoapps.servlet.lastmodified.AddLastModified;
import com.aoapps.taglib.ImmutableGlobalAttributes;
import com.aoapps.taglib.Link;
import com.semanticcms.core.model.Page;
import com.semanticcms.core.servlet.PageUtils;
import com.semanticcms.core.servlet.SemanticCMS;
import com.semanticcms.core.servlet.View;
import com.semanticcms.news.model.News;
import com.semanticcms.news.servlet.NewsUtils;
import com.semanticcms.news.servlet.RssUtils;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.SkipPageException;
import org.joda.time.ReadableInstant;

public class NewsView extends View {

	public static final String NAME = "news";

	private static final String JSP_TARGET = "/semanticcms-news-view/view.inc.jsp";

	@WebListener("Registers the \"" + NAME + "\" view in SemanticCMS.")
	public static class Initializer implements ServletContextListener {
		@Override
		public void contextInitialized(ServletContextEvent event) {
			SemanticCMS.getInstance(event.getServletContext()).addView(new NewsView());
		}
		@Override
		public void contextDestroyed(ServletContextEvent event) {
			// Do nothing
		}
	}

	private NewsView() {}

	@Override
	public Group getGroup() {
		return Group.FIXED;
	}

	@Override
	public String getDisplay() {
		return "News";
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public boolean isApplicable(
		ServletContext servletContext,
		HttpServletRequest request,
		HttpServletResponse response,
		Page page
	) throws ServletException, IOException {
		return PageUtils.hasElement(servletContext, request, response, page, News.class, true);
	}

	/**
	 * The last modified time of news view is the pubDate of the most recent news entry.
	 */
	@Override
	public ReadableInstant getLastModified(
		ServletContext servletContext,
		HttpServletRequest request,
		HttpServletResponse response,
		Page page
	) throws ServletException, IOException {
		List<News> news = NewsUtils.findAllNews(servletContext, request, response, page);
		return news.isEmpty() ? null : news.get(0).getPubDate();
	}

	@Override
	public String getTitle(
		ServletContext servletContext,
		HttpServletRequest request,
		HttpServletResponse response,
		Page page
	) {
		String bookTitle = page.getPageRef().getBook().getTitle();
		if(bookTitle != null && !bookTitle.isEmpty()) {
			return "What's New" + TITLE_SEPARATOR + page.getTitle() + TITLE_SEPARATOR + bookTitle;
		} else {
			return "What's New" + TITLE_SEPARATOR + page.getTitle();
		}
	}

	/**
	 * Description required by RSS channel.
	 */
	@Override
	public String getDescription(Page page) {
		return "What's New in " + page.getTitle();
	}

	@Override
	public String getKeywords(Page page) {
		return null;
	}

	@Override
	public Collection<Link> getLinks(
		ServletContext servletContext,
		HttpServletRequest request,
		HttpServletResponse response,
		Page page
	) throws ServletException, IOException {
		if(
			// RSS module must be loaded
			RssUtils.isRssEnabled(servletContext)
			// Only link to RSS when this view applies (has news on self or child page)
			&& isApplicable(servletContext, request, response, page)
		) {
			return Collections.singleton(
				new Link(
					ImmutableGlobalAttributes.EMPTY,
					RssUtils.getRssServletPath(page), // href
					false, // absolute
					false, // canonical
					null, // params
					AddLastModified.FALSE,
					(String)null, // hreflang
					"alternate", // rel
					RssUtils.CONTENT_TYPE,
					null, // media
					getTitle(servletContext, request, response, page)
				)
			);
		} else {
			return super.getLinks(servletContext, request, response, page);
		}
	}

	/**
	 * News entries are not displayed on their page, but rather on their news view.
	 * <p>
	 * If the page does not allow robots, this view will also not allow robots.
	 * </p>
	 * <p>
	 * If the page does not have any direct news (child news doesn't count), then robots will be excluded.
	 * This is to reduce the chances of duplicate content when a parent page also includes child page news.
	 * </p>
	 */
	@Override
	public boolean getAllowRobots(
		ServletContext servletContext,
		HttpServletRequest request,
		HttpServletResponse response,
		Page page
	) throws ServletException, IOException {
		return
			// If the page does not allow robots, this view will also not allow robots.
			PageUtils.findAllowRobots(servletContext, request, response, page)
			// If the page does not have any direct news (child news doesn't count), then robots will be excluded.
			&& PageUtils.hasElement(servletContext, request, response, page, News.class, false)
		;
	}

	@Override
	public <__ extends FlowContent<__>> void doView(ServletContext servletContext, HttpServletRequest request, HttpServletResponse response, __ flow, Page page) throws ServletException, IOException, SkipPageException {
		Map<String, Object> args = new LinkedHashMap<>();
		args.put("page", page);
		boolean isRssEnabled = RssUtils.isRssEnabled(servletContext);
		args.put("isRssEnabled", isRssEnabled);
		if(isRssEnabled) {
			args.put("rssServletPath", RssUtils.getRssServletPath(page));
			//args.put("rssTitle", getTitle(servletContext, request, response, page));
			args.put("rssType", RssUtils.CONTENT_TYPE);
		}
		// TODO: Set a LinkRenderer to rewrite relative links (including anchor-only) to be relative to the page, not this view
		Dispatcher.include(
			servletContext,
			JSP_TARGET,
			request,
			response,
			args
		);
	}
}
