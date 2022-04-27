/*
 * semanticcms-news-view - SemanticCMS view of all news in the current page and all children.
 * Copyright (C) 2021, 2022  AO Industries, Inc.
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
module com.semanticcms.news.view {
  exports com.semanticcms.news.view;
  // Direct
  requires com.aoapps.html.servlet; // <groupId>com.aoapps</groupId><artifactId>ao-fluent-html-servlet</artifactId>
  requires com.aoapps.servlet.lastmodified; // <groupId>com.aoapps</groupId><artifactId>ao-servlet-last-modified</artifactId>
  requires com.aoapps.servlet.util; // <groupId>com.aoapps</groupId><artifactId>ao-servlet-util</artifactId>
  requires com.aoapps.taglib; // <groupId>com.aoapps</groupId><artifactId>ao-taglib</artifactId>
  requires javax.servlet.api; // <groupId>javax.servlet</groupId><artifactId>javax.servlet-api</artifactId>
  requires javax.servlet.jsp.api; // <groupId>javax.servlet.jsp</groupId><artifactId>javax.servlet.jsp-api</artifactId>
  requires org.joda.time; // <groupId>joda-time</groupId><artifactId>joda-time</artifactId>
  requires joda.time.jsptags; // <groupId>joda-time</groupId><artifactId>joda-time-jsptags</artifactId>
  requires com.semanticcms.core.model; // <groupId>com.semanticcms</groupId><artifactId>semanticcms-core-model</artifactId>
  requires com.semanticcms.core.servlet; // <groupId>com.semanticcms</groupId><artifactId>semanticcms-core-servlet</artifactId>
  requires com.semanticcms.core.taglib; // <groupId>com.semanticcms</groupId><artifactId>semanticcms-core-taglib</artifactId>
  requires com.semanticcms.news.model; // <groupId>com.semanticcms</groupId><artifactId>semanticcms-news-model</artifactId>
  requires com.semanticcms.news.servlet; // <groupId>com.semanticcms</groupId><artifactId>semanticcms-news-servlet</artifactId>
  requires com.semanticcms.news.taglib; // <groupId>com.semanticcms</groupId><artifactId>semanticcms-news-taglib</artifactId>
  requires taglibs.standard.spec; // <groupId>org.apache.taglibs</groupId><artifactId>taglibs-standard-spec</artifactId>
} // TODO: Avoiding rewrite-maven-plugin-4.22.2 truncation
