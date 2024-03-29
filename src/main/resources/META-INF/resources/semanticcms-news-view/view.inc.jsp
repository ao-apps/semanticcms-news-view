<%--
semanticcms-news-view - SemanticCMS view of all news in the current page and all children.
Copyright (C) 2016, 2019, 2020, 2021, 2022, 2023  AO Industries, Inc.
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
along with semanticcms-news-view.  If not, see <https://www.gnu.org/licenses/>.
--%>
<%@ page language="java" pageEncoding="UTF-8" %>
<%@ page session="false" %>
<%@ taglib prefix="ao" uri="https://oss.aoapps.com/taglib/" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="core" uri="https://semanticcms.com/core/taglib/" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="joda" uri="http://www.joda.org/joda/time/tags" %>
<%@ taglib prefix="news" uri="https://semanticcms.com/news/taglib/" %>

<%--
Only the parts of the page that are specifically on this page are indexed (hopefully):
See googleon and googleoff tags:
  https://perishablepress.com/tell-google-to-not-index-certain-parts-of-your-page/ 
And the Google documentation:
  https://www.google.com/support/enterprise/static/gsa/docs/admin/70/gsa_doc_set/admin_crawl/preparing.html#1076243

Arguments:
  arg.view            The active view
  arg.page            The page this is showing news for
  arg.pageAllowRobots The resolved page allowRobots setting
  arg.isRssEnabled    boolean whether RSS module enabled
  arg.rssServletPath  The context-relative path to the RSS feed, only provided when RSS module enabled
  arg.rssType         The content type of the RSS feed, only provided when RSS module enabled
--%>
<c:set var="page" value="${arg.page}" />
<c:set var="pageRef" value="${page.pageRef}" />

<h1>What's New in <ao:out value="${page.title}" /></h1>
<c:if test="${arg.isRssEnabled}">
  <div class="semanticcms-news-view-rss-link">
    <ao:a href="${arg.rssServletPath}" addLastModified="false" type="${arg.rssType}"><span>Subscribe</span></ao:a>
  </div>
</c:if>
<c:forEach var="news" items="${news:findAllNews(page)}">
  <%-- Capture news now in "body" mode, since findAllNews only did meta for fast search --%>
  <%-- TODO: body: Is there a way to capture news at "body" level while other parts at "meta" level?
     This recapturing is clunky and full body capture of all would be inefficient. --%>
  <c:set var="news" value="${core:capturePageInBook(news.page.pageRef.bookName, news.page.pageRef.path, 'body').elementsById[news.id]}" />
  <c:set var="googleoff" value="${!news.page.equals(page) || !(news.allowRobots == null ? arg.pageAllowRobots : news.allowRobots)}" />
  <c:if test="${googleoff}">
<%-- Newline required before googleon, doing on googleoff just to be clear and safe --%>
<!--googleoff: all-->
  </c:if>
  <%-- See http://stackoverflow.com/questions/16101472/attribute-pubdate-not-allowed-on-element-time-at-this-point --%>
  <article
    class="semanticcms-news-view-article"
    itemscope="itemscope"
    itemtype="https://schema.org/BlogPosting"
    <%-- Include id for new elements on this page only, since only news on this page will be referenced externally and
         this assures ids are unique. --%>
    <c:if test="${news.page.equals(page)}">
      <%-- Tags-in-tag for conditional attribute: yeah JSP! --%>
      id="${fn:escapeXml(news.id)}"
    </c:if>
  >
    <h2 itemprop="headline"><core:link book="#{news.book}" page="#{news.targetPage}" element="#{news.element}" view="#{news.view}" allowGeneratedElement="true"><ao:out value="${news.title}" /></core:link></h2>
    <footer><time itemprop="datePublished" datetime="${fn:escapeXml(news.pubDate)}"><joda:format locale="${pageContext.response.locale}" value="${news.pubDate}" style="L-" /></time></footer>
    <%-- both description and body, use details/summary - maybe do in future when has better browser support
    <ao:choose>
      <ao:when test="#{!empty news.description && news.body.length > 0}">
        <details>
          <summary itemprop="description" class="semanticcms-news-view-description"><ao:out value="${news.description}" /></summary>
          <div itemprop="articleBody" class="semanticcms-news-view-body"><core:writeNodeBody node="#{news}" /></div>
        </details>
      </ao:when>
      <ao:otherwise>
    --%>
        <c:if test="${!empty news.description}">
          <div itemprop="description" class="semanticcms-news-view-description"><em><ao:out value="${news.description}" /></em></div>
        </c:if>
        <c:if test="${news.body.length > 0}">
          <%-- TODO: How to write node body with relative links being relative to the page instead of this view? --%>
          <div itemprop="articleBody" class="semanticcms-news-view-body"><core:writeNodeBody node="#{news}" /></div>
        </c:if>
    <%--
      </ao:otherwise>
    </ao:choose>
    --%>
  </article>
  <c:if test="${googleoff}">
<%-- Newline required before googleon --%>
<!--googleon: all-->
  </c:if>
</c:forEach>
