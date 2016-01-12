<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<div class="section-body">
    <div class="row">
        <div class="col-lg-9">
            <div class="card">
                <div class="card-head">
                    <header><spring:message
                            code="label.client"/> ${client.user.firstName} ${client.user.lastName}</header>
                </div>
                <div class="card-body">

                    <c:if test="${client.blocked}">
                        <div class=" col-xs-12 alert alert-callout alert-danger">
                            <strong><spring:message code="label.client.blocked"/></strong> <spring:message
                                code="label.since"/> <fmt:formatDate pattern="dd.MM.yyyy"
                                                                     value="${client.blockingDate}"/>
                        </div>
                    </c:if>

                    <p><span class="opacity-50"><spring:message code="label.birthdate"/>:</span>
                        <fmt:formatDate value="${client.birthDate.time}" type="date" dateStyle="short"/>

                    <p><span class="opacity-50"><spring:message
                            code="label.passportdata"/>:</span> ${client.passportData}</p>

                    <p><span class="opacity-50"><spring:message code="label.address"/>:</span> ${client.address}</p>

                    <p><span class="opacity-50"><spring:message code="label.email"/>:</span> ${client.user.login}</p>

                    <table class="table">
                        <thead>
                        <tr>
                            <th><spring:message code="label.contract.number"/></th>
                            <th><spring:message code="label.tariff"/></th>
                            <th><spring:message code="label.active.options"/></th>
                            <th><spring:message code="label.total.monthly.cost"/></th>
                            <th></th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${client.contracts}" var="contract">
                            <tr>
                                <td>
                                    <div>
                                        <a href="${pageContext.request.contextPath}/staff/contract/${contract.id}"
                                           class="text-info">+${contract.number}</a>
                                    </div>

                                    <c:choose>
                                        <c:when test="${contract.blockedByClient}">
                                            <code>
                                                <i class="fa fa-lock"></i> <spring:message code="label.blocked"/>
                                                <spring:message code="label.by.client"/> <spring:message
                                                    code="label.since"/> <fmt:formatDate
                                                    pattern="dd.MM.yyyy"
                                                    value="${contract.blockingDate}"/>
                                            </code>
                                        </c:when>
                                        <c:when test="${contract.blockedByOperator}">
                                            <code>
                                                <i class="fa fa-lock"></i> <spring:message code="label.blocked"/>
                                                <spring:message
                                                        code="label.by.user"/> ${contract.blockingUser.firstName} ${contract.blockingUser.lastName}
                                                <spring:message code="label.since"/> <fmt:formatDate
                                                    pattern="dd.MM.yyyy"
                                                    value="${contract.blockingDate}"/>
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
                                <td></td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
                <div class="card-actionbar">
                    <div class="card-actionbar-row">
                        <a class="btn btn-default ink-reaction"
                           href="${pageContext.request.contextPath}/staff/client/${client.id}/contract"><spring:message
                                code="label.contract.add"/></a>
                        <c:choose>
                            <c:when test="${not client.blocked}">
                                <a class="btn btn-danger ink-reaction"
                                   href="${pageContext.request.contextPath}/staff/client/${client.id}/block">
                                    <spring:message code="label.client.block"/>
                                </a>
                            </c:when>
                            <c:otherwise>
                                <a class="btn btn-success ink-reaction"
                                   href="${pageContext.request.contextPath}/staff/client/${client.id}/unlock">
                                    <spring:message code="label.client.unlock"/>
                                </a>
                            </c:otherwise>
                        </c:choose>
                    </div>
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