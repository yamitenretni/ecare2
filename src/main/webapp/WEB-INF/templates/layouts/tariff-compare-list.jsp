<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<div class="section-body">
    <div class="row">
        <div class="col-xs-12">
            <div class="card">
                <div class="card-head">
                    <header><spring:message code="label.compare.list"/></header>
                </div>
                <div class="card-body">
                    <c:set var="compareTariffs" value="${compareList.compareTariffs}"/>
                    <c:choose>
                        <c:when test="${empty compareTariffs}">

                            <div class="alert alert-callout alert-info" role="alert">
                                <strong><spring:message code="label.compare.empty"/></strong> <spring:message
                                    code="label.can.add.compare"/> <a
                                    href="${pageContext.request.contextPath}/my/tariff"
                                    class="text-info"><spring:message code="label.here"/></a>.
                            </div>
                        </c:when>
                        <c:otherwise>
                            <table class="table compare-table">
                                <thead>
                                <tr>
                                    <th></th>
                                    <c:forEach items="${compareTariffs}" var="tariff">
                                        <th>${tariff.name}</th>
                                    </c:forEach>
                                </tr>
                                </thead>
                                <tbody>
                                <tr>
                                    <td><spring:message code="label.monthly.cost"/></td>
                                    <c:forEach items="${compareTariffs}" var="tariff">
                                        <td align="center">$${tariff.regularCost}</td>
                                    </c:forEach>
                                </tr>
                                <c:forEach items="${optionList}" var="option">
                                    <tr>
                                        <td>${option.name}</td>
                                        <c:forEach items="${compareTariffs}" var="tariff">
                                            <td align="center">
                                                <c:choose>
                                                    <c:when test="${tariff.availableOptions.contains(option)}">
                                                        <i class="fa fa-check text-accent"></i>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <i class="fa fa-times text-default-light text-light"></i>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                        </c:forEach>
                                    </tr>
                                </c:forEach>
                                <c:if test="${not empty currentClient.activeContracts}">
                                    <tr>
                                        <td></td>
                                        <c:forEach items="${compareTariffs}" var="tariff">
                                            <c:choose>
                                                <c:when test="${currentClient.activeContracts.size() > 1}">
                                                    <td align="center">
                                                        <div class="btn-group">
                                                            <button type="button"
                                                                    class="btn btn-primary dropdown-toggle"
                                                                    data-toggle="dropdown"
                                                                    aria-haspopup="true" aria-expanded="false">
                                                                <spring:message code="label.activate"/> <span class="caret"></span>
                                                            </button>
                                                            <ul class="dropdown-menu">
                                                                <c:forEach items="${currentClient.activeContracts}"
                                                                           var="contract">
                                                                    <li>
                                                                        <a href="${pageContext.request.contextPath}/cart/${contract.id}/newtariff?newTariff=${tariff.id}">+${contract.number}</a>
                                                                    </li>
                                                                </c:forEach>
                                                            </ul>
                                                        </div>
                                                    </td>
                                                </c:when>
                                                <c:otherwise>
                                                    <td align="center">
                                                        <c:forEach items="${currentClient.activeContracts}"
                                                                   var="contract">
                                                            <a href="${pageContext.request.contextPath}/cart/${contract.id}/newtariff?newTariff=${tariff.id}"
                                                               class="btn btn-primary"><spring:message code="label.activate"/></a>
                                                        </c:forEach>
                                                    </td>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:forEach>
                                    </tr>
                                </c:if>
                                </tbody>
                            </table>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="col-xs-12" >
            <tiles:insertAttribute name="cart"/>
        </div>
    </div>
</div>