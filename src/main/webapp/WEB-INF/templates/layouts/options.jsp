<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<div class="section-body">
    <div class="row">
        <div class="col-lg-9">
            <div class="card">
                <div class="card-head">
                    <header><spring:message code="label.option.list"/></header>
                </div>
                <div class="card-body">
                    <table class="table">
                        <thead>
                        <tr>
                            <th><spring:message code="label.name"/></th>
                            <th><spring:message code="label.connection.cost"/></th>
                            <th><spring:message code="label.monthly.cost"/></th>
                            <th><spring:message code="label.incompatible.options"/></th>
                            <th><spring:message code="label.mandatory.options"/></th>
                            <th><spring:message code="label.depend.options"/></th>
                            <th></th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${optionList}" var="curOption">
                            <tr>
                                <td>${curOption.name}</td>
                                <td>$<fmt:formatNumber value="${curOption.connectionCost}"
                                                       type="number"
                                                       minFractionDigits="2"
                                                       maxFractionDigits="2"/></td>
                                <td>$<fmt:formatNumber value="${curOption.regularCost}"
                                                       type="number"
                                                       minFractionDigits="2"
                                                       maxFractionDigits="2"/></td>
                                <td>
                                        <%--<c:if test="${not empty curOption.incompatibleOptions}">--%>
                                    <c:forEach items="${curOption.incompatibleOptions}" var="incOption">
                                        ${incOption.name}<br>
                                    </c:forEach>
                                        <%--</c:if>--%>
                                </td>
                                <td>
                                    <c:forEach items="${curOption.mandatoryOptions}" var="manOption">
                                        ${manOption.name}<br>
                                    </c:forEach>
                                </td>
                                <td>
                                    <c:forEach items="${curOption.dependOptions}" var="depOption">
                                        ${depOption.name}<br>
                                    </c:forEach>
                                </td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/staff/option/${curOption.id}/edit"
                                       class="btn btn-icon-toggle" data-toggle="tooltip" data-placement="top"
                                       data-original-title="<spring:message code="label.option.edit"/>">
                                        <i class="fa fa-pencil"></i>
                                    </a>
                                    <a href="${pageContext.request.contextPath}/staff/option/${curOption.id}/delete"
                                       class="btn btn-icon-toggle" data-toggle="tooltip" data-placement="top"
                                       data-original-title="<spring:message code="label.option.delete"/>">
                                        <i class="fa fa-trash-o"></i>
                                    </a>
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
        <div class="col-lg-9">
            <tiles:insertAttribute name="cart"/>
        </div>
    </div>
</div>