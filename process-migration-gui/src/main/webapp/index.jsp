<%@page import="com.redhat.syseng.soleng.rhpam.util.MigrationUtils"%>
<%@page import="com.redhat.syseng.soleng.rhpam.RestClient"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <title>Process Instance Migration </title>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <head>
    <body>


        <c:if test="${empty kieContainers}">
            <%
                //RestClient.getProcessDefinitionsFromApi(request);
                RestClient.getProcessDefinitionsFromRest(request, response);
            %>   
        </c:if>

        <c:choose>

            <c:when test="${param.getInstance}">
                <%
                    RestClient.getRunningInstances(request, response);
                %>   
            </c:when>               
            <c:when test="${param.generate}">
                <%
                    RestClient.generatePlan(request, response);
                %>

            </c:when>
            <c:when test="${param.executePlan}">
                <%
                    RestClient.executePlan(request, response);
                %>

            </c:when>        
            <c:when test="${param.cleanSession}">
                <%
                    RestClient.cleanSession(request, response);
                %>

            </c:when>   
        </c:choose>



    </body>
        <%@include file="header.jsp"%>
</html>