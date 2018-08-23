<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<h2 style="text-align: center;">Review Migration Plan</h2>



<form target="_self" method="post">
    <pre>
        ${migrationPlanInJson}
    </pre>

    <c:if test="${not empty validationError}">
        <h3 style="text-align: left;">There is error in your plan, please fix it</h3>
        <pre>
            ${validationError}
        </pre>
    </c:if>
    <c:if test="${empty validationError}">
       
        <div style="margin: 10px auto; width: 100%; text-align: center;">
            <button name="executePlan" value="true">Execute this plan</button>
        </div>    
    </c:if>
</form>