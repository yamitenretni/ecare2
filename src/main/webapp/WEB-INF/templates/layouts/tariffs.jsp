<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<div class="section-body">
    <div class="row">
        <div class="col-lg-9">
            <div class="card">
                <div class="card-head">
                    <header><spring:message code="label.tariff.list"/></header>
                </div>
                <div class="card-body">
                    <table class="table table-hover">
                        <thead>
                        <tr>
                            <th><spring:message code="label.name"/></th>
                            <th><spring:message code="label.monthly.cost"/></th>
                            <th><spring:message code="label.available.options"/></th>
                            <th></th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${tariffList}" var="tariff">
                            <tr>
                                <td>${tariff.name}</td>
                                <td>$<fmt:formatNumber value="${tariff.regularCost}"
                                                       type="number"
                                                       minFractionDigits="2"
                                                       maxFractionDigits="2"/></td>
                                <td>
                                    <c:forEach items="${tariff.availableOptions}" var="avOption">
                                        <div>${avOption.name}</div>
                                    </c:forEach>
                                </td>
                                <td class="text-right">
                                    <security:authorize access="hasRole('ROLE_EMPLOYEE')">
                                        <a href="${pageContext.request.contextPath}/staff/tariff/${tariff.id}/edit"
                                           class="btn btn-icon-toggle" data-toggle="tooltip" data-placement="top"
                                           data-original-title="<spring:message code="label.tariff.edit"/>">
                                            <i class="fa fa-pencil"></i>
                                        </a>
                                        <a href="${pageContext.request.contextPath}/staff/tariff/${tariff.id}/delete"
                                           class="btn btn-icon-toggle" data-toggle="tooltip" data-placement="top"
                                           data-original-title="<spring:message code="label.tariff.delete"/>">
                                            <i class="fa fa-trash-o"></i>
                                        </a>
                                    </security:authorize>

                                    <security:authorize access="hasRole('ROLE_CLIENT')">
                                        <c:set var="inCompare" value="${compareList.compareTariffs.contains(tariff)}"/>
                                        <a href="${pageContext.request.contextPath}/my/tariff/${tariff.id}/cancel"
                                           class="compare-cancel <c:if test="${not inCompare}">hidden</c:if> btn btn-icon-toggle"
                                           data-toggle="tooltip" data-placement="top"
                                           data-original-title="<spring:message code="label.compare.remove"/>">
                                            <i class="fa fa-th-list text-success"></i>
                                        </a>
                                        <a href="${pageContext.request.contextPath}/my/tariff/${tariff.id}/compare"
                                           class="compare-add <c:if test="${inCompare}">hidden</c:if> btn btn-icon-toggle"
                                           data-toggle="tooltip" data-placement="top"
                                           data-original-title="<spring:message code="label.compare.add"/>">
                                            <i class="fa fa-th-list"></i>
                                        </a>
                                    </security:authorize>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
                <div class="card-actionbar">
                    <div class="card-actionbar-row">
                        <security:authorize access="hasRole('ROLE_CLIENT')">
                            <a href="${pageContext.request.contextPath}/my/compare-list" class="btn btn-default"><spring:message code="label.compare.list"/></a>
                        </security:authorize>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="col-lg-9" >
            <tiles:insertAttribute name="cart"/>
        </div>
    </div>
</div>