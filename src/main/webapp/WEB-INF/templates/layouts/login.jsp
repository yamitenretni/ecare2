<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<div class="section-body">
    <div class="row">
        <div class="contain-xs">
            <form class="form" action="${pageContext.request.contextPath}/login" method="post"
                  accept-charset="utf-8">
                <div class="card">
                    <div class="card-body">
                        <c:if test="${not empty error}">
                            <div class="alert alert-callout alert-danger"><spring:message code="${error}"/></div>
                        </c:if>
                        <c:if test="${not empty msg}">
                            <div class="alert alert-callout alert-success"><spring:message code="${msg}"/></div>
                        </c:if>
                        <div class="form-group">
                            <input type="text" class="form-control" id="login" name="login" placeholder="<spring:message code="label.email"/>">
                            <label for="login"><spring:message code="label.email"/></label>
                        </div>
                        <div class="form-group">
                            <input type="password" class="form-control" id="password" name="password"
                                   placeholder="<spring:message code="label.password"/>">
                            <label for="password"><spring:message code="label.password"/></label>
                        </div>
                        <input type="hidden" name="${_csrf.parameterName}"
                               value="${_csrf.token}"/>
                    </div>
                    <div class="card-actionbar">
                        <div class="card-actionbar-row">
                            <button type="submit" class="btn btn-primary"><spring:message code="label.login"/></button>
                        </div>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>