<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<c:set var="active" value="active expanded"/>

<c:if test="${not empty position}">
    <c:set var="tariffs" value="${position.contains('Tariff')}"/>
    <c:set var="allTariffs" value="${'allTariffs'.equals(position)}"/>
    <c:set var="addTariff" value="${'addTariff'.equals(position)}"/>
    <c:set var="options" value="${position.contains('Option')}"/>
    <c:set var="allOptions" value="${'allOptions'.equals(position)}"/>
    <c:set var="addOption" value="${'addOption'.equals(position)}"/>
    <c:set var="clients" value="${position.contains('Client')}"/>
    <c:set var="allClients" value="${'allClients'.equals(position)}"/>
    <c:set var="addClient" value="${'addClient'.equals(position)}"/>
    <c:set var="myContracts" value="${'myContracts'.equals(position)}"/>
    <c:set var="myTariffs" value="${'myTariffs'.equals(position)}"/>
</c:if>
<div id="menubar" class="menubar-inverse animate">
    <ul id="main-menu" class="gui-controls">
        <security:authorize access="hasRole('ROLE_EMPLOYEE')">
            <li class="gui-folder <c:if test="${clients}">${active}</c:if>">
                <a href="#">
                    <div class="gui-icon">
                        <i class="fa fa-users"></i>
                    </div>
                    <span class="title"><spring:message code="label.clients"/></span>
                </a>
                <ul>
                    <li <c:if test="${allClients}">class="${active}"</c:if>>
                        <a href="${pageContext.request.contextPath}/staff/client">
                            <span class="title"><spring:message code="label.clients.all"/></span>
                        </a>
                    </li>
                    <li <c:if test="${addClient}">class="${active}"</c:if>>
                        <a href="${pageContext.request.contextPath}/staff/client/add">
                            <span class="title"><spring:message code="label.client.add"/></span>
                        </a>
                    </li>
                </ul>
            </li>
            <li class="gui-folder <c:if test="${tariffs}">${active}</c:if> ">
                <a href="#">
                    <div class="gui-icon">
                        <i class="fa fa-briefcase"></i>
                    </div>
                    <span class="title"><spring:message code="label.tariffs"/></span>
                </a>
                <ul>
                    <li <c:if test="${allTariffs}">class="${active}"</c:if>>
                        <a href="${pageContext.request.contextPath}/staff/tariff">
                            <span class="title"><spring:message code="label.tariffs.all"/></span>
                        </a>
                    </li>
                    <li <c:if test="${addTariff}">class="${active}"</c:if>>
                        <a href="${pageContext.request.contextPath}/staff/tariff/add">
                            <span class="title"><spring:message code="label.tariff.add"/></span>
                        </a>
                    </li>
                </ul>
            </li>
            <li class="gui-folder <c:if test="${options}">${active}</c:if>">
                <a href="#">
                    <div class="gui-icon">
                        <i class="fa fa-tasks"></i>
                    </div>
                    <span class="title"><spring:message code="label.options"/></span>
                </a>
                <ul>
                    <li <c:if test="${allOptions}">class="${active}"</c:if>>
                        <a href="${pageContext.request.contextPath}/staff/option">
                            <span class="title"><spring:message code="label.options.all"/></span>
                        </a>
                    </li>
                    <li <c:if test="${addOption}">class="${active}"</c:if>>
                        <a href="${pageContext.request.contextPath}/staff/option/add">
                            <span class="title"><spring:message code="label.option.add"/></span>
                        </a>
                    </li>
                </ul>
            </li>
        </security:authorize>
        <security:authorize access="hasRole('ROLE_CLIENT')">
            <li <c:if test="${myContracts}">class="${active}"</c:if>>
                <a href="${pageContext.request.contextPath}/my/contract">
                    <div class="gui-icon">
                        <i class="fa fa-mobile fa-fw"></i>
                    </div>
                    <span class="title"><spring:message code="label.my.contracts"/></span>
                </a>
            </li>
            <li <c:if test="${myTariffs}">class="${active}"</c:if>>
                <a href="${pageContext.request.contextPath}/my/tariff">
                    <div class="gui-icon">
                        <i class="fa fa-briefcase fa-fw"></i>
                    </div>
                    <span class="title"><spring:message code="label.tariffs"/></span>
                </a>
            </li>
        </security:authorize>

    </ul>
</div>