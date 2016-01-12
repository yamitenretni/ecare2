<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<div class="headerbar">
    <div class="headerbar-left">
        <ul class="header-nav">
            <li class="header-nav-brand">
                <div class="brand-holder">
                    <span class="text-primary text-bold text-lg"><a href="${pageContext.request.contextPath}/">eCARE</a></span>
                </div>
            </li>
        </ul>
    </div>
    <div class="headerbar-right">
        <ul class="header-nav header-nav-options">
            <security:authorize access="hasRole('ROLE_EMPLOYEE')">
                <li>
                    <form class="navbar-search" action="${pageContext.request.contextPath}/staff/clients/search"
                          method="get">
                        <div class="form-group">
                            <input type="text" class="form-control" placeholder="<spring:message code="label.search.by.contract"/>"
                                   name="contractNumber">
                        </div>
                        <button class="btn btn-icon-toggle ink-reaction" type="submit">
                            <i class="fa fa-search"></i>
                        </button>
                    </form>
                </li>
            </security:authorize>
            <li>
                <div class="btn-group">
                <button type="button" class="btn btn-icon-toggle ink-reaction" data-toggle="dropdown">
                    <i class="fa fa-globe"></i>
                </button>
                    <ul class="dropdown-menu dropdown-menu-right" role="menu">
                        <li>
                            <a href="?language=en">English</a>
                        </li>
                        <li>
                            <a href="?language=ru">Русский</a>
                        </li>
                    </ul>
                </div>
            </li>
            <c:if test="${not empty currentUser}">
                <li>
                    <div class="text-lg user-info"><spring:message code="label.welcome"/>, ${currentUser.firstName} ${currentUser.lastName}</div>
                </li>
                <li>
                    <form action="${pageContext.request.contextPath}/logout" id="logout" method="post">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                        <button class="btn btn-icon-toggle ink-reaction" type="submit">
                            <i class="fa fa-power-off"></i>
                        </button>
                    </form>
                </li>
            </c:if>
        </ul>
    </div>
</div>
