<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<div class="section-body">
    <div class="row">
        <div class="col-lg-6">
            <div class="card">
                <div class="card-head">
                    <header><spring:message code="label.new.contract.for"/> ${contract.client.user.firstName} ${contract.client.user.lastName}</header>
                </div>
                <div class="card-body">
                    <form class="form" action="${pageContext.request.contextPath}/staff/contract" method="POST"
                          accept-charset="utf-8">
                        <input type="hidden" id="client" name="client" value="${contract.client.id}"/>
                        <input type="hidden" name="${_csrf.parameterName}"
                               value="${_csrf.token}"/>

                        <c:set var="errorNumber" value="${bindingResult.getFieldError('number').defaultMessage}"/>
                        <div class="form-group <c:if test="${not empty errorNumber}">has-error</c:if>">
                            <input type="text" class="form-control" id="number" name="number"
                                   placeholder="<spring:message code="label.contract.number"/>" value="${contract.number}">
                            <span class="help-block">${errorNumber}</span>
                            <label for="number"><spring:message code="label.contract.number"/></label>
                        </div>

                        <c:set var="errorTariff" value="${bindingResult.getFieldError('tariff').defaultMessage}"/>
                        <div class="form-group <c:if test="${not empty errorTariff}">has-error</c:if>">
                            <select name="tariff" id="tariff" class="form-control">
                                <option value="">-- <spring:message code="label.select.tariff"/> --</option>
                                <c:forEach items="${tariffList}" var="curTariff">
                                    <option value="${curTariff.id}"
                                            <c:if test="${curTariff ==  contract.tariff}">selected</c:if> >${curTariff.name}</option>
                                </c:forEach>
                            </select>
                            <span class="help-block">${errorTariff}</span>
                            <label for="tariff"><spring:message code="label.tariff"/></label>
                        </div>
                        <button type="submit" class="btn btn-primary ink-reaction" name="requestType" value="submit">
                            <spring:message code="label.contract.save"/>
                        </button>
                    </form>
                </div>
            </div>
        </div>
        <div id="tariff-info" class="col-lg-3 <c:if test="${empty contract.tariff}">hidden</c:if>">
            <div class="card">
                <div class="card-head">
                    <header id="tariff-name">${contract.tariff.name}</header>
                </div>
                <div class="card-body">
                    <div class="row">
                        <div class="col-xs-5">
                            <span class="opacity-50"><spring:message code="label.monthly.cost"/>:</span>
                        </div>
                        <div class="col-xs-7">
                            <span id="tariff-cost">$${contract.tariff.regularCost}</span>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-xs-5">
                            <span class="opacity-50"><spring:message code="label.available.options"/>:</span>
                        </div>
                        <div id="tariff-options" class="col-xs-7">
                            <c:forEach items="${contract.tariff.availableOptions}" var="option">
                                <div>${option.name}</div>
                            </c:forEach>
                            <c:if test="${empty contract.tariff.availableOptions}">
                                <div>-</div>
                            </c:if>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="col-lg-6" >
            <tiles:insertAttribute name="cart"/>
        </div>
    </div>
</div>