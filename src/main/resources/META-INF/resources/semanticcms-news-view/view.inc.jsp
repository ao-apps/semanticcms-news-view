<%--
semanticcms-news-view - SemanticCMS view of all news in the current page and all children.
Copyright (C) 2016  AO Industries, Inc.
	support@aoindustries.com
	7262 Bull Pen Cir
	Mobile, AL 36695

This file is part of semanticcms-news-view.

semanticcms-news-view is free software: you can redistribute it and/or modify
it under the terms of the GNU Lesser General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

semanticcms-news-view is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License
along with semanticcms-news-view.  If not, see <http://www.gnu.org/licenses/>.
--%>
<%@ page language="java" buffer="512kb" autoFlush="true" pageEncoding="UTF-8" session="false" %>
<%@ taglib prefix="ao" uri="http://aoindustries.com/ao-taglib" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="joda" uri="http://www.joda.org/joda/time/tags" %>
<%@ taglib prefix="news" uri="https://semanticcms.com/semanticcms-news-taglib" %>
<%@ taglib prefix="p" uri="https://semanticcms.com/semanticcms-core-taglib" %>

<%--
Only the parts of the page that are specifically on this page are indexed (hopefully):
See googleon and googleoff tags:
	https://perishablepress.com/tell-google-to-not-index-certain-parts-of-your-page/ 
And the Google documentation:
	https://www.google.com/support/enterprise/static/gsa/docs/admin/70/gsa_doc_set/admin_crawl/preparing.html#1076243
--%>
<c:set var="page" value="${arg.page}" />
<c:set var="pageRef" value="${page.pageRef}" />

<h1>What's New in <ao:out value="${page.title}" /></h1>
<c:forEach var="news" items="${news:findAllNews(page)}">
	<%-- Capture news now in "body" mode, since findAllNews only did meta for fast search --%>
	<%-- TODO: body: Is there a way to capture news at "body" level while other parts at "meta" level?
		 This recapturing is clunky and full body capture of all would be inefficient. --%>
	<c:set var="news" value="${p:capturePageInBook(news.page.pageRef.bookName, news.page.pageRef.path, 'body').elementsById[news.id]}" />
	<c:if test="${!news.page.equals(page)}">
<%-- Newline required before googleon, doing on googleoff just to be clear and safe --%>
<!--googleoff: all-->
	</c:if>
	<%-- See http://stackoverflow.com/questions/16101472/attribute-pubdate-not-allowed-on-element-time-at-this-point --%>
	<article
		itemscope="itemscope"
		itemtype="http://schema.org/BlogPosting"
		<%-- Include id for new elements on this page only, since only news on this page will be referenced externally and
		     this assures ids are unique. --%>
		<c:if test="${news.page.equals(page)}">
			<%-- Tags-in-tag for conditional attribute: yeah JSP! --%>
			id="${fn:escapeXml(news.id)}"
		</c:if>
	>
		<h2 itemprop="headline"><p:link book="${news.book}" page="${news.targetPage}" element="${news.element}" view="${news.view}" allowGeneratedElement="true"><ao:out value="${news.title}" /></p:link></h2>
		<%-- both description and body, use details/summary - maybe do in future when has better browser support
		<c:choose>
			<c:when test="${!empty news.description && news.body.length > 0}">
				<details>
					<summary itemprop="description" class="semanticcms-news-view-description"><ao:out value="${news.description}" /></summary>
					<div itemprop="articleBody" class="semanticcms-news-view-body"><p:writeNodeBody node="${news}" /></div>
				</details>
			</c:when>
			<c:otherwise>
		--%>
				<c:if test="${!empty news.description}">
					<div itemprop="description" class="semanticcms-news-view-description"><ao:out value="${news.description}" /></div>
				</c:if>
				<c:if test="${news.body.length > 0}">
					<div itemprop="articleBody" class="semanticcms-news-view-body"><p:writeNodeBody node="${news}" /></div>
				</c:if>
		<%--
			</c:otherwise>
		</c:choose>
		--%>
		<footer>Published <time itemprop="datePublished" datetime="${fn:escapeXml(news.pubDate)}"><joda:format value="${news.pubDate}" style="L-" /></time>.</footer>
	</article>
	<c:if test="${!news.page.equals(page)}">
<%-- Newline required before googleon --%>
<!--googleon: all-->
	</c:if>
</c:forEach>
