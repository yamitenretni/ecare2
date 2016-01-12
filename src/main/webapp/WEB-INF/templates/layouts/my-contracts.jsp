<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<div class="section-body">
    <div class="row">
        <div class="col-lg-9">
            <div class="card">
                <div class="card-head">
                    <header><spring:message code="label.my.contracts"/></header>
                </div>
                <div class="card-body">
                    <table class="table">
                        <thead>
                        <tr>
                            <th><spring:message code="label.contract.number"/></th>
                            <th><spring:message code="label.tariff"/></th>
                            <th><spring:message code="label.active.options"/></th>
                            <th><spring:message code="label.monthly.cost"/></th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${contractList}" var="contract">
                            <tr>
                                <td>
                                    <div>
                                        <a href="${pageContext.request.contextPath}/my/contract/${contract.id}"
                                           class="text-info">+${contract.number}</a>
                                    </div>
                                    <c:choose>
                                        <c:when test="${contract.blockedByClient}">
                                            <code>
                                                <i class="fa fa-lock"></i> <spring:message code="label.blocked"/> <spring:message code="label.by.you"/> <spring:message code="label.since"/> <fmt:formatDate pattern="dd.MM.yyyy"
                                                                                                                                 value="${contract.blockingDate}"/>
                                            </code>
                                        </c:when>
                                        <c:when test="${contract.blockedByOperator}">
                                            <code>
                                                <i class="fa fa-lock"></i> <spring:message code="label.blocked"/> <spring:message code="label.since"/> <fmt:formatDate pattern="dd.MM.yyyy"
                                                                              value="${contract.blockingDate}"/>, <spring:message code="label.contact.support"/>
                                            </code>
                                        </c:when>
                                    </c:choose>
                                </td>
                                <td>${contract.tariff.name}</td>
                                <td>
                                    <c:forEach items="${contract.activatedOptions}" var="option">
                                        <div>${option.name}</div>
                                    </c:forEach>
                                </td>
                                <td>$<fmt:formatNumber value="${contract.totalRegularCost}"
                                                       type="number"
                                                       minFractionDigits="2"
                                                       maxFractionDigits="2"/></td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="col-lg-9">
            <tiles:insertAttribute name="cart"/>
        </div>
    </div>
</div>