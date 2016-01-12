<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<c:if test="${not empty cartForm.cartContractForms}">
    <div class="card card-underline">
        <div class="card-head">
            <header><spring:message code="label.unsaved.changes"/></header>
            <ul class="nav nav-tabs pull-right" data-toggle="tabs">
                <c:forEach items="${cartForm.cartContractForms}" var="cartPosition" varStatus="positionLoop">
                    <li role="presentation"
                            <c:if test="${(empty contract and positionLoop.index == 0) or (contract.id == cartPosition.contract.id)}">
                                class="active"
                            </c:if>
                    >
                        <a href="#${cartPosition.contract.number}"
                           data-toggle="tab">+${cartPosition.contract.number}</a>
                    </li>
                </c:forEach>
            </ul>
        </div>
        <div class="card-body">
            <div class="tab-content">
                <c:forEach items="${cartForm.cartContractForms}" var="cartPosition" varStatus="positionLoop">
                    <div role="tabpanel"
                            <c:choose>
                                <c:when test="${empty contract and positionLoop.index == 0}">
                                    class="tab-pane active"
                                </c:when>
                                <c:when test="${contract.id == cartPosition.contract.id}">
                                    class="tab-pane active"
                                </c:when>
                                <c:otherwise>
                                    class="tab-pane"
                                </c:otherwise>
                            </c:choose>
                         id="${cartPosition.contract.number}"
                         aria-labelledby="${cartPosition.contract.number}-tab">

                        <table class="table table-condensed cart-table">
                            <thead>
                            <tr>
                                <th colspan="2"><spring:message code="label.position"/></th>
                                <th><spring:message code="label.connection.cost"/></th>
                                <th><spring:message code="label.monthly.cost"/></th>
                                <th></th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:if test="${cartPosition.newTariff != null}">
                                <tr>
                                    <td><span class="label label-warning"><spring:message code="label.changed"/></span>
                                    </td>
                                    <td colspan="2">${cartPosition.contract.tariff.name} &rightarrow; ${cartPosition.newTariff.name}
                                    </td>
                                    <td>$<fmt:formatNumber value="${cartPosition.newTariff.regularCost}"
                                                           type="number"
                                                           minFractionDigits="2"
                                                           maxFractionDigits="2"/></td>
                                    <td>
                                        <a href="${pageContext.request.contextPath}/cart/${cartPosition.contract.id}/newtariff/cancel"
                                           class="btn btn-icon-toggle"
                                           data-toggle="tooltip" data-placement="top"
                                           data-original-title="Cancel">
                                            <i class="fa fa-undo"></i>
                                        </a>
                                    </td>
                                </tr>
                            </c:if>

                            <c:forEach items="${cartPosition.contractOptions}" var="optionPosition">
                                <c:set var="trStyle" value=""/>
                                <c:if test="${optionPosition.adding}">
                                    <c:set var="newStyle" value=""/>
                                    <c:choose>
                                        <c:when test="${optionPosition.incompatible}">
                                            <c:set var="newStyle" value="label-default"/>
                                            <c:set var="trStyle" value="color: #ccc;"/>
                                        </c:when>
                                        <c:when test="${optionPosition.depending}">
                                            <c:set var="newStyle" value="label-default"/>
                                            <c:set var="trStyle" value="color: #ccc;"/>
                                        </c:when>
                                        <c:when test="${optionPosition.unsupported}">
                                            <c:set var="newStyle" value="label-default"/>
                                            <c:set var="trStyle" value="color: #ccc;"/>
                                        </c:when>
                                        <c:otherwise>
                                            <c:set var="newStyle" value="label-success"/>
                                        </c:otherwise>
                                    </c:choose>
                                </c:if>

                                <tr style="${trStyle}">
                                    <td>
                                        <c:if test="${optionPosition.deleting}">
                                            <span class="label label-danger"><spring:message
                                                    code="label.deleted"/></span>
                                        </c:if>
                                        <c:if test="${optionPosition.adding}">
                                            <span class="label ${newStyle}"><spring:message code="label.new"/></span>
                                        </c:if>
                                    </td>
                                    <td>${optionPosition.option.name}</td>
                                    <td>$<fmt:formatNumber value="${optionPosition.option.connectionCost}"
                                                           type="number"
                                                           minFractionDigits="2"
                                                           maxFractionDigits="2"/>
                                    </td>
                                    <td>$<fmt:formatNumber value="${optionPosition.option.regularCost}"
                                                           type="number"
                                                           minFractionDigits="2"
                                                           maxFractionDigits="2"/>
                                    </td>
                                    <td>

                                        <c:if test="${optionPosition.deleting}">
                                            <c:if test="${optionPosition.cancelable}">
                                                <a href="${pageContext.request.contextPath}/cart/${cartPosition.contract.id}/deactivate/${optionPosition.option.id}/cancel"
                                                   class="btn btn-icon-toggle"
                                                   data-toggle="tooltip" data-placement="top"
                                                   data-original-title="Cancel">
                                                    <i class="fa fa-undo"></i>
                                                </a>
                                            </c:if>
                                            <c:if test="${optionPosition.unsupported}">
                                                <div><b><spring:message code="label.not.available.for.tariff"/></b></div>
                                            </c:if>
                                            <c:if test="${optionPosition.depending}">
                                                <div><b><spring:message code="label.depend.on"/>:</b></div>
                                                <c:forEach
                                                        items="${cartForm.getMandatoryOptionsForDepend(cartPosition.contract, optionPosition.option)}"
                                                        var="mandatoryOption">
                                                    <div>${mandatoryOption.name}</div>
                                                </c:forEach>
                                            </c:if>
                                        </c:if>
                                        <c:if test="${optionPosition.adding}">
                                            <a href="${pageContext.request.contextPath}/cart/${cartPosition.contract.id}/add/${optionPosition.option.id}/cancel"
                                               class="btn btn-icon-toggle"
                                               data-toggle="tooltip" data-placement="top"
                                               data-original-title="Cancel">
                                                <i class="fa fa-undo"></i>
                                            </a>
                                            <c:if test="${optionPosition.unsupported}">
                                                <div><b><spring:message code="label.not.available.for.tariff"/></b></div>
                                            </c:if>
                                            <c:if test="${optionPosition.incompatible}">
                                                <div><b><spring:message code="label.incompatible.with"/>:</b></div>
                                                <c:forEach
                                                        items="${cartForm.getIncompatibleOptionsFor(cartPosition.contract, optionPosition.option)}"
                                                        var="incompatibleOption">
                                                    <div>${incompatibleOption.name}</div>
                                                </c:forEach>
                                            </c:if>
                                            <c:if test="${optionPosition.depending}">
                                                <div><b><spring:message code="label.depend.on"/>:</b></div>
                                                <c:forEach
                                                        items="${cartForm.getMandatoryOptionsForDepend(cartPosition.contract, optionPosition.option)}"
                                                        var="mandatoryOption">
                                                    <div>${mandatoryOption.name}</div>
                                                </c:forEach>
                                            </c:if>
                                        </c:if>
                                    </td>
                                </tr>
                            </c:forEach>
                            <tr class="text-bold">
                                <td colspan="2"><spring:message code="label.after.saving"/>:</td>
                                <td>$<fmt:formatNumber value="${cartPosition.totalConnectionCost}"
                                                       type="number"
                                                       minFractionDigits="2"
                                                       maxFractionDigits="2"/></td>
                                <td>$<fmt:formatNumber value="${cartPosition.futureContract.totalRegularCost}"
                                                       type="number"
                                                       minFractionDigits="2"
                                                       maxFractionDigits="2"/></td>
                                <td></td>
                            </tr>
                            </tbody>
                        </table>
                        <div class="pull-right">
                            <a href="${pageContext.request.contextPath}/cart/${cartPosition.contract.id}/save"
                               class="btn btn-primary"><spring:message code="label.contract.save"/></a>
                            <a href="${pageContext.request.contextPath}/cart/${cartPosition.contract.id}/clear"
                               class="btn btn-default"><spring:message code="label.clear.changes"/></a>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>
    </div>
</c:if>