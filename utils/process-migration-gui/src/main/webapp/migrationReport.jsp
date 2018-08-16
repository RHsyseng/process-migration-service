<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<h2 style="text-align: center;">Migration Reports</h2>


<form action="/Migration">   
    <table style="margin: 0px auto; width: 80%; border: 1px solid blue;">

        <pre>
            <c:forEach items="${migrationReports}" var="report" >
            <tr style="border: 1px solid black;">
                <td style="border: 1px solid black; padding: 5px">${report}</td>
            </tr>
            </c:forEach>
        </pre>
    </table>     
    <div style="margin: 10px auto; width: 100%; text-align: center;">
        <input type="submit"  value="Back to Home again" />
    </div>
</form>