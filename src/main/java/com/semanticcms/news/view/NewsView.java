/*
 * semanticcms-news-view - SemanticCMS view of all news in the current page and all children.
 * Copyright (C) 2016  AO Industries, Inc.
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
 * along with semanticcms-news-view.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.semanticcms.news.view;

import com.aoindustries.servlet.http.Dispatcher;
import com.semanticcms.core.model.Page;
import com.semanticcms.core.servlet.PageUtils;
import com.semanticcms.core.servlet.View;
import com.semanticcms.news.model.News;
import java.io.IOException;
import java.util.Collections;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.SkipPageException;

public class NewsView extends View {

	static final String VIEW_NAME = "news";

	private static final String JSPX_TARGET = "/semanticcms-news-view/view.inc.jspx";

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
		return VIEW_NAME;
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

	@Override
	public String getTitle(
		ServletContext servletContext,
		HttpServletRequest request,
		HttpServletResponse response,
		Page page
	) {
		return "What's New" + TITLE_SEPARATOR + page.getPageRef().getBook().getTitle();
	}

	@Override
	public String getDescription(Page page) {
		return null;
	}

	@Override
	public String getKeywords(Page page) {
		return null;
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
	public void doView(ServletContext servletContext, HttpServletRequest request, HttpServletResponse response, Page page) throws ServletException, IOException, SkipPageException {
		Dispatcher.include(
			servletContext,
			JSPX_TARGET,
			request,
			response,
			Collections.singletonMap("page", page)
		);
	}
}
