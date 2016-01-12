<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<div class="section-body">
    <div class="row">
        <div class="col-lg-9">
            <div class="card">
                <div class="card-head">
                    <header><spring:message code="label.clients"/></header>
                </div>
                <div class="card-body">
                    <table class="table">
                        <thead>
                        <tr>
                            <th><spring:message code="label.name"/></th>
                            <th><spring:message code="label.contracts"/></th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${clientList}" var="client">
                            <tr>
                                <td>
                                    <div>
                                        <a href="${pageContext.request.contextPath}/staff/client/${client.id}"
                                           class="text-info">${client.user.firstName} ${client.user.lastName}</a>
                                    </div>
                                    <c:if test="${client.blocked}">
                                        <code><i class="fa fa-lock"></i> <spring:message code="label.blocked"/> <spring:message code="label.since"/> <fmt:formatDate
                                                pattern="dd.MM.yyyy"
                                                value="${client.blockingDate}"/></code>
                                    </c:if>
                                </td>
                                <td>
                                    <c:forEach items="${client.contracts}" var="contract">
                                        <div>
                                            <a href="${pageContext.request.contextPath}/staff/contract/${contract.id}"
                                               class="text-info">+${contract.number}</a>
                                        </div>
                                        <c:choose>
                                            <c:when test="${contract.blockedByClient}">
                                                <code>
                                                    <i class="fa fa-lock"></i> <spring:message code="label.blocked"/> <spring:message code="label.by.client"/> <spring:message code="label.since"/> <fmt:formatDate pattern="dd.MM.yyyy"
                                                                                                                    value="${contract.blockingDate}"/>
                                                </code>
                                            </c:when>
                                            <c:when test="${contract.blockedByOperator}">
                                                <code>
                                                    <i class="fa fa-lock"></i> <spring:message code="label.blocked"/> <spring:message code="label.by.user"/> ${contract.blockingUser.firstName} ${contract.blockingUser.lastName} <spring:message code="label.since"/> <fmt:formatDate pattern="dd.MM.yyyy"
                                                                                                             value="${contract.blockingDate}"/>
                                                </code>
                                            </c:when>
                                        </c:choose>
                                    </c:forEach>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
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
