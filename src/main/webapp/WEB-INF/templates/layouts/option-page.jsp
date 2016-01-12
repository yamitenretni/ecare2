<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<div class="section-body">
    <div class="row">
        <div class="col-lg-6">
            <div class="card">
                <div class="card-body">
                    <form class="form" method="post" action="${pageContext.request.contextPath}/staff/option">
                        <input type="hidden" name="id" value="${option.id}">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

                        <c:set var="errorName" value="${bindingResult.getFieldError('name').defaultMessage}"/>
                        <div class="form-group <c:if test="${not empty errorName}">has-error</c:if>">
                            <input type="text" class="form-control" id="name" name="name" placeholder="Name"
                                   value="${option.name}">
                            <span class="help-block">${errorName}</span>
                            <label for="name"><spring:message code="label.option.name"/></label>
                        </div>

                        <div class="form-group">
                            <input type="number" min="0" step="0.01" class="form-control" id="connectionCost"
                                   name="connectionCost" placeholder="Connection cost" value="${option.connectionCost}">
                            <label for="connectionCost"><spring:message code="label.connection.cost"/></label>
                        </div>

                        <div class="form-group">
                            <input type="number" min="0" step="0.01" class="form-control" id="regularCost"
                                   name="regularCost" placeholder="Regular cost" value="${option.regularCost}">
                            <label for="regularCost"><spring:message code="label.monthly.cost"/></label>
                        </div>

                        <c:if test="${not empty optionList}">
                            <div class="form-group checkboxes">
                                <label class="control-label"><spring:message code="label.incompatible.options"/></label>
                                <c:forEach items="${optionList}" var="incOption" varStatus="incOptionStatus">
                                    <div class="checkbox checkbox-styled">
                                        <label>
                                            <input type="checkbox"
                                                   name="incompatibleOptions"
                                                   id="incompatibleOptions<c:out value="${incOption.id}"/>"
                                                   value="${incOption.id}"
                                                   <c:if test="${option.incompatibleOptions.contains(incOption)}">checked</c:if>
                                                   <c:if test="${option.mandatoryOptions.contains(incOption)}">disabled</c:if>
                                            >
                                            <span>${incOption.name}: ${incOption.connectionCost} once + ${incOption.regularCost} every month</span>
                                        </label>
                                    </div>
                                </c:forEach>
                                <input type="hidden" name="_incompatibleOptions" value="on">
                            </div>
                        </c:if>

                        <c:if test="${not empty optionList}">
                            <div class="form-group checkboxes">
                                <label class="control-label"><spring:message code="label.mandatory.options"/></label>
                                    ${bindingResult.getFieldError("mandatoryOptions").defaultMessage}
                                <c:forEach items="${optionList}" var="manOption" varStatus="manOptionStatus">
                                    <div class="checkbox checkbox-styled">
                                        <label>
                                            <input type="checkbox"
                                                   name="mandatoryOptions"
                                                   id="mandatoryOptions<c:out value="${manOption.id}"/>"
                                                   value="${manOption.id}"
                                                   <c:if test="${option.mandatoryOptions.contains(manOption)}">checked</c:if>
                                                   <c:if test="${option.incompatibleOptions.contains(manOption)}">disabled</c:if>
                                            >
                                            <span>${manOption.name}: ${manOption.connectionCost} once + ${manOption.regularCost} every month</span>
                                        </label>
                                    </div>
                                </c:forEach>
                                <input type="hidden" name="_mandatoryOptions" value="on">
                            </div>
                        </c:if>

                        <button type="submit" class="btn btn-primary ink-reaction"><spring:message code="label.option.save"/></button>

                    </form>
                </div>
            </div>
        </div>
        <div class="col-lg-6" >
            <tiles:insertAttribute name="cart"/>
        </div>
    </div>
</div>