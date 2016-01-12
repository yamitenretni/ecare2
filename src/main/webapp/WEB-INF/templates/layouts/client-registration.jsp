<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<div class="section-body">
    <div class="row">
        <div class="col-lg-6">
            <div class="card">
                <div class="card-head">
                    <header><spring:message code="label.client.registration"/></header>
                </div>
                <div class="card-body">
                    <form class="form" action="${pageContext.request.contextPath}/staff/client/new" method="POST"
                          accept-charset="UTF-8">
                        <input type="hidden" name="${_csrf.parameterName}"
                               value="${_csrf.token}"/>

                        <div class="row">
                            <div class="col-sm-6">
                                <spring:bind path="contract.client.user.firstName">
                                    <c:set var="errorFirstName"
                                           value="${bindingResult.getFieldError(status.expression).defaultMessage}"/>
                                    <div class="form-group <c:if test="${not empty errorFirstName}">has-error</c:if>">

                                        <input type="text" class="form-control" id="firstName"
                                               name="${status.expression}"
                                               placeholder="<spring:message code="label.first.name"/>" value="${status.value}">

                                        <span class="help-block">${errorFirstName}</span>
                                        <label for="firstName"><spring:message code="label.first.name"/></label>
                                    </div>
                                </spring:bind>
                            </div>

                            <div class="col-sm-6">
                                <spring:bind path="contract.client.user.lastName">
                                    <c:set var="errorLastName"
                                           value="${bindingResult.getFieldError(status.expression).defaultMessage}"/>
                                    <div class="form-group <c:if test="${not empty errorLastName}">has-error</c:if>">
                                        <input type="text" class="form-control" id="lastName"
                                               name="${status.expression}"
                                               placeholder="<spring:message code="label.last.name"/>" value="${status.value}">
                                        <span class="help-block">${errorLastName}</span>
                                        <label for="lastName"><spring:message code="label.last.name"/></label>
                                    </div>
                                </spring:bind>
                            </div>
                        </div>

                        <spring:bind path="contract.client.birthDate">
                            <c:set var="errorBirthDate"
                                   value="${bindingResult.getFieldError(status.expression).defaultMessage}"/>
                            <div class="form-group <c:if test="${not empty errorBirthDate}">has-error</c:if>">
                                <div class="input-group date">
                                    <div class="input-group-content">

                                        <input type="date" class="form-control" id="birthDate"
                                               name="${status.expression}"
                                               placeholder="<spring:message code="label.birthdate"/>"
                                               value="<fmt:formatDate pattern="yyyy-MM-dd" value="${status.value.time}" />"
                                        >

                                        <span class="help-block">${errorBirthDate}</span>
                                        <label for="birthDate"><spring:message code="label.birthdate"/></label>
                                    </div>
                                </div>
                            </div>
                        </spring:bind>

                        <spring:bind path="contract.client.passportData">
                            <c:set var="errorPassportData"
                                   value="${bindingResult.getFieldError(status.expression).defaultMessage}"/>
                            <div class="form-group <c:if test="${not empty errorPassportData}">has-error</c:if>">
                                <input type="text" class="form-control" id="passportData" name="${status.expression}"
                                       placeholder="<spring:message code="label.passportdata"/>" value="${status.value}">
                                <span class="help-block">${errorPassportData}</span>
                                <label for="passportData"><spring:message code="label.passport"/></label>
                            </div>
                        </spring:bind>

                        <spring:bind path="contract.client.address">
                            <c:set var="errorAddress"
                                   value="${bindingResult.getFieldError(status.expression).defaultMessage}"/>
                            <div class="form-group <c:if test="${not empty errorAddress}">has-error</c:if>">
                                <input type="text" class="form-control" id="address" name="${status.expression}"
                                       placeholder="<spring:message code="label.address"/>"
                                       value="${status.value}">
                                <span class="help-block">${errorAddress}</span>
                                <label for="address"><spring:message code="label.address"/></label>
                            </div>
                        </spring:bind>

                        <spring:bind path="contract.client.user.login">
                            <c:set var="errorLogin"
                                   value="${bindingResult.getFieldError(status.expression).defaultMessage}"/>
                            <div class="form-group <c:if test="${not empty errorLogin}">has-error</c:if>">

                                <input type="email" class="form-control" id="login" name="${status.expression}"
                                       placeholder="<spring:message code="label.email"/>"
                                       value="${status.value}">

                                <span class="help-block">${errorLogin}</span>
                                <label for="login"><spring:message code="label.email"/></label>
                            </div>
                        </spring:bind>

                        <spring:bind path="contract.client.user.password">
                            <c:set var="errorPassword"
                                   value="${bindingResult.getFieldError(status.expression).defaultMessage}"/>
                            <div class="form-group <c:if test="${not empty errorPassword}">has-error</c:if>">
                                <input type="password" class="form-control" id="password" name="${status.expression}"
                                       placeholder="<spring:message code="label.password"/>">
                                <span class="help-block">${errorPassword}</span>
                                <label for="password"><spring:message code="label.password"/></label>
                            </div>
                        </spring:bind>

                        <spring:bind path="contract.number">
                            <c:set var="errorNumber"
                                   value="${bindingResult.getFieldError(status.expression).defaultMessage}"/>
                            <div class="form-group <c:if test="${not empty errorNumber}">has-error</c:if>">
                                <input type="text" class="form-control" id="number" name="${status.expression}"
                                       placeholder="<spring:message code="label.contract.number"/>" value="${status.value}">
                                <span class="help-block">${errorNumber}</span>
                                <label for="number"><spring:message code="label.contract.number"/></label>
                            </div>
                        </spring:bind>

                        <spring:bind path="contract.tariff">
                            <c:set var="errorTariff"
                                   value="${bindingResult.getFieldError(status.expression).defaultMessage}"/>
                            <div class="form-group <c:if test="${not empty errorTariff}">has-error</c:if>">
                                <select name="${status.expression}" id="tariff" class="form-control">
                                    <option value="">-- <spring:message code="label.select.tariff"/> --</option>
                                    <c:forEach items="${tariffList}" var="curTariff">
                                        <option value="${curTariff.id}"
                                                <c:if test="${curTariff == status.value}">selected</c:if> >${curTariff.name}</option>
                                    </c:forEach>
                                </select>
                                <span class="help-block">${errorTariff}</span>
                                <label for="tariff"><spring:message code="label.tariff"/></label>
                            </div>
                        </spring:bind>
                        <button type="submit" class="btn btn-primary ink-reaction" name="requestType"><spring:message code="label.client.save"/></button>
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