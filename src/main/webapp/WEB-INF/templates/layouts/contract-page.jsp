<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<div class="section-body">
    <div class="row">
        <div class="col-xs-12">
            <h1 class="text-primary"><spring:message code="label.contract"/> +${contract.number}</h1>
            <security:authorize access="hasRole('ROLE_EMPLOYEE')">
                <p class="text-lg">
                    <span class="opacity-50"><spring:message code="label.owner"/>:</span>
                    <a href="${pageContext.request.contextPath}/staff/client/${contract.client.id}"
                       class="text-info">
                            ${contract.client.user.firstName} ${contract.client.user.lastName}
                    </a>
                </p>
            </security:authorize>
        </div>
    </div>
    <div class="row">
        <div class="col-lg-6">
            <security:authorize access="hasRole('ROLE_CLIENT')">
                <c:choose>
                    <c:when test="${contract.blockedByClient}">
                        <c:set var="blockText"><spring:message code="label.by.you"/> <fmt:formatDate
                                pattern="dd.MM.yyyy"
                                value="${contract.blockingDate}"/></c:set>
                    </c:when>
                    <c:when test="${contract.blockedByOperator}">
                        <c:set var="blockText"><spring:message code="label.since"/> <fmt:formatDate pattern="dd.MM.yyyy"
                                                                                                    value="${contract.blockingDate}"/>,
                            <spring:message code="label.contact.support"/></c:set>
                    </c:when>
                </c:choose>
            </security:authorize>
            <security:authorize access="hasRole('ROLE_EMPLOYEE')">
                <c:choose>
                    <c:when test="${contract.blockedByClient}">
                        <c:set var="blockText"> <spring:message code="label.by.client"/> <spring:message
                                code="label.since"/> <fmt:formatDate pattern="dd.MM.yyyy"
                                                                     value="${contract.blockingDate}"/></c:set>
                    </c:when>
                    <c:when test="${contract.blockedByOperator}">
                        <c:set var="blockText"> <spring:message
                                code="label.by.user"/>${contract.blockingUser.firstName} ${contract.blockingUser.lastName}
                            <spring:message code="label.since"/> <fmt:formatDate pattern="dd.MM.yyyy"
                                                                                 value="${contract.blockingDate}"/></c:set>
                    </c:when>
                </c:choose>
            </security:authorize>
            <div class="card">
                <div class="card-head">
                    <header><spring:message code="label.current.tariff.options"/></header>
                </div>
                <div class="card-body">
                    <c:if test="${contract.blocked}">
                        <div class="alert alert-callout alert-danger">
                            <strong><spring:message code="label.contract.blocked"/></strong> ${blockText}
                        </div>
                    </c:if>
                    <table class="table">
                        <thead>
                        <tr>
                            <th><spring:message code="label.name"/></th>
                            <th><spring:message code="label.monthly.cost"/></th>
                            <th></th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr class="info">
                            <td>${contract.tariff.name}</td>
                            <td>$<fmt:formatNumber value="${contract.tariff.regularCost}"
                                                   type="number"
                                                   minFractionDigits="2"
                                                   maxFractionDigits="2"/></td>
                            <td><p></p></td>
                        </tr>
                        <c:if test="${not empty contract.activatedOptions}">

                            <c:forEach items="${contract.activatedOptions}" var="option">
                                <tr>
                                    <td>${option.name}</td>
                                    <td>$<fmt:formatNumber value="${option.regularCost}"
                                                           type="number"
                                                           minFractionDigits="2"
                                                           maxFractionDigits="2"/></td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${nonAvailableOptions.contains(option)}">
                                                <spring:message code="label.deactivated"/>
                                            </c:when>
                                            <c:when test="${not contract.blocked}">
                                                <a href="${pageContext.request.contextPath}/cart/${contract.id}/deactivate/${option.id}"
                                                   class="btn btn-icon-toggle"
                                                   data-toggle="tooltip" data-placement="top"
                                                   data-original-title="<spring:message code="label.option.deactivate"/>">
                                                    <i class="fa fa-trash-o"></i>
                                                </a>
                                            </c:when>
                                        </c:choose>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:if>
                        <tr class="text-bold">
                            <td><spring:message code="label.total"/>:</td>
                            <td>$<fmt:formatNumber value="${contract.totalRegularCost}"
                                                   type="number"
                                                   minFractionDigits="2"
                                                   maxFractionDigits="2"/></td>
                            <td></td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <c:if test="${not contract.client.blocked}">
                    <div class="card-actionbar">
                        <div class="card-actionbar-row">
                            <security:authorize access="hasRole('ROLE_EMPLOYEE')">
                                <c:choose>
                                    <c:when test="${not contract.blocked}">
                                        <a class="btn btn-danger ink-reaction"
                                           href="${pageContext.request.contextPath}/staff/contract/${contract.id}/block">
                                            <spring:message code="label.contract.block"/>
                                        </a>
                                    </c:when>
                                    <c:otherwise>
                                        <a class="btn btn-success ink-reaction"
                                           href="${pageContext.request.contextPath}/staff/contract/${contract.id}/unlock">
                                            <spring:message code="label.contract.unlock"/>
                                        </a>
                                    </c:otherwise>
                                </c:choose>
                            </security:authorize>

                            <security:authorize access="hasRole('ROLE_CLIENT')">
                                <c:choose>
                                    <c:when test="${not contract.blocked}">
                                        <a class="btn btn-danger ink-reaction"
                                           href="${pageContext.request.contextPath}/my/contract/${contract.id}/block">
                                            <spring:message code="label.contract.block"/>
                                        </a>
                                    </c:when>
                                    <c:when test="${contract.blocked and contract.blockingUser.login.equals(currentUser.login)}">
                                        <a class="btn btn-success ink-reaction"
                                           href="${pageContext.request.contextPath}/my/contract/${contract.id}/unlock">
                                            <spring:message code="label.contract.unlock"/>
                                        </a>
                                    </c:when>
                                </c:choose>
                            </security:authorize>
                        </div>
                    </div>
                </c:if>
            </div>
            <c:if test="${not contract.blocked}">
                <div class="card">
                    <div class="card-head">
                        <header><spring:message code="label.contract.change"/></header>
                    </div>
                    <div class="card-body">
                        <form class="form" action="${pageContext.request.contextPath}/cart/${contract.id}/newtariff"
                              method="get"
                              accept-charset="utf-8">
                            <div>
                                <div class="input-group">
                                    <div class="input-group-content">
                                        <select name="newTariff" id="newTariff" class="form-control" required>
                                            <option value="">-- <spring:message code="label.select.new.tariff"/> --
                                            </option>
                                            <c:forEach items="${tariffs}" var="tariff">
                                                <option value="${tariff.id}">${tariff.name}: $${tariff.regularCost}
                                                    <spring:message code="label.every.month"/>
                                                </option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                    <div class="form-control-line"></div>
                                    <div class="input-group-btn">
                                        <button type="submit" class="btn btn-default" name="requestType" value="submit">
                                            <spring:message code="label.change"/>
                                        </button>
                                    </div>

                                </div>
                            </div>
                        </form>
                        <c:if test="${not empty allAvailableOptions}">
                            <table class="table">
                                <thead>
                                <tr>
                                    <th><spring:message code="label.available.option"/></th>
                                    <th><spring:message code="label.connection.cost"/></th>
                                    <th><spring:message code="label.monthly.cost"/></th>
                                    <th></th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach items="${allAvailableOptions}" var="optionPosition">
                                    <tr <c:if test="${not optionPosition.available}">style="color: #ccc;"</c:if>>
                                        <td>${optionPosition.option.name}</td>
                                        <td>$<fmt:formatNumber value="${optionPosition.option.connectionCost}"
                                                               type="number"
                                                               minFractionDigits="2"
                                                               maxFractionDigits="2"/></td>
                                        <td>$<fmt:formatNumber value="${optionPosition.option.regularCost}"
                                                               type="number"
                                                               minFractionDigits="2"
                                                               maxFractionDigits="2"/></td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${optionPosition.available}">
                                                    <a href="${pageContext.request.contextPath}/cart/${contract.id}/add/${optionPosition.option.id}"
                                                       class="btn btn-icon-toggle"
                                                       data-toggle="tooltip" data-placement="top"
                                                       data-original-title="<spring:message code="label.option.activate"/>">
                                                        <i class="fa fa-cart-plus"></i>
                                                    </a>
                                                </c:when>
                                                <c:otherwise>
                                                    <c:if test="${not empty optionPosition.incompatibleList}">
                                                        <div><b><spring:message code="label.incompatible.with"/>:</b>
                                                        </div>
                                                        <c:forEach items="${optionPosition.incompatibleList}"
                                                                   var="incOption">
                                                            <div>${incOption.name}</div>
                                                        </c:forEach>
                                                    </c:if>
                                                    <c:if test="${not empty optionPosition.mandatoryList}">
                                                        <div><b><spring:message code="label.depend.on"/>:</b></div>
                                                        <c:forEach items="${optionPosition.mandatoryList}"
                                                                   var="manOption">
                                                            <div>${manOption.name}</div>
                                                        </c:forEach>
                                                    </c:if>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                            </table>
                        </c:if>

                    </div>
                </div>
            </c:if>

        </div>
        <div class="col-lg-6">
            <tiles:insertAttribute name="cart"/>
        </div>
    </div>
</div>