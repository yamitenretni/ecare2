<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<tiles:importAttribute name="javascripts"/>

<html>
<head>
    <title>
        <tiles:getAsString name="title"/>
    </title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <link rel="stylesheet" href="/eCare/resources/bootstrap.css">
    <link rel="stylesheet" href="/eCare/resources/materialadmin.css">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.5.0/css/font-awesome.min.css">
    <link rel="stylesheet" href="/eCare/resources/layout.css">
    <link rel="stylesheet" href="/eCare/resources/toastr.css">
</head>
<body class="menubar-hoverable header-fixed menubar-pin <tiles:getAsString name="bodystyle"/>">
<header id="header">
    <tiles:insertAttribute name="header"/>
</header>
<div id="base">
    <div id="content">
        <section>
            <tiles:insertAttribute name="content"/>
        </section>
    </div>
    <tiles:insertAttribute name="navigation"/>
</div>

<c:forEach var="script" items="${javascripts}">
    <script src="<c:url value="${script}"/>"></script>
</c:forEach>
</body>
</html>
