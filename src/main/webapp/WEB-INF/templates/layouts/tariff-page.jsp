<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<div class="section-body">
    <div class="row">
        <div class="col-lg-6">
            <div class="card">
                <div class="card-body">
                    <form class="form" method="post" action="${pageContext.request.contextPath}/staff/tariff">
                        <input type="hidden" name="id" value="${tariff.id}">
                        <input type="hidden" name="${_csrf.parameterName}"
                               value="${_csrf.token}"/>

                        <c:set var="errorName" value="${bindingResult.getFieldError('name').defaultMessage}"/>
                        <div class="form-group <c:if test="${not empty errorName}">has-error</c:if>">
                            <input type="text" class="form-control" id="name" name="name" placeholder="<spring:message code="label.name"/>"
                                   value="${tariff.name}">
                                <span class="help-block">${errorName}</span>
                                <label for="name"><spring:message code="label.tariff.name"/></label>
                        </div>

                        <div class="form-group">
                            <input type="number" min="0" step="0.01" class="form-control" id="regularCost"
                                   name="regularCost"
                                   placeholder="<spring:message code="label.monthly.cost"/>" value="${tariff.regularCost}">
                            <label for="regularCost"><spring:message code="label.monthly.cost"/></label>
                        </div>

                        <c:if test="${not empty optionList}">
                        <div class="form-group checkboxes">
                            <label class="control-label"><spring:message code="label.available.options"/></label>
                            <c:forEach items="${optionList}" var="avOption">
                                <div class="checkbox checkbox-styled">
                                    <label>
                                        <input type="checkbox"
                                               name="availableOptions"
                                               id="availableOptions<c:out value="${avOption.id}"/>"
                                               value="${avOption.id}"
                                               <c:if test="${tariff.availableOptions.contains(avOption)}">checked</c:if>
                                        >
                                        <span>${avOption.name}: ${avOption.connectionCost} <spring:message code="label.once"/> + ${avOption.regularCost}
                                        <spring:message code="label.every.month"/></span>
                                    </label>
                                </div>
                            </c:forEach>
                            <input type="hidden" name="_availableOptions" value="on">
                            </div>
                        </c:if>
                        <button type="submit" class="btn btn-primary ink-reaction"><spring:message code="label.tariff.save"/></button>
                    </form>
                </div>
            </div>
        </div>
        <div class="col-lg-6" >
            <tiles:insertAttribute name="cart"/>
        </div>
    </div>
</div>