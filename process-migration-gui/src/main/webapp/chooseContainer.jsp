<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<h1 style="text-align: center;">Process Definitions 1</h1>


<form target="_self" method="post">

    <table style="margin: 0px auto; width: 80%; border: 1px solid blue;">
        <tr>
            <td style="border: 1px solid black; padding: 5px"><b>Container ID</b></td>
            <td style="border: 1px solid black; padding: 5px"><b>Process ID</b></td>
            <td style="border: 1px solid black; padding: 5px"><b>Version</b></td>            
            <td style="border: 1px solid black; padding: 5px"><b>Container Alias</b></td>
            <td style="border: 1px solid black; padding: 5px"><b>Group ID</b></td>
            <td style="border: 1px solid black; padding: 5px"><b>Artifact ID</b></td>
        </tr>        
        <c:forEach var="container" items="${kieContainers}">
            <tr style="border: 1px solid black;">
                <td style="border: 1px solid black; padding: 5px">${container.getContainerId()}</td>
                <td style="border: 1px solid black; padding: 5px">${container.getProcessId()}</td>
                <td style="border: 1px solid black; padding: 5px">${container.getVersion()}</td>
                <td style="border: 1px solid black; padding: 5px">${container.getContainerAlias()}</td>
                <td style="border: 1px solid black; padding: 5px">${container.getGroupId()}</td>
                <td style="border: 1px solid black; padding: 5px">${container.getArtifiactId()}</td>
            </tr>
        </c:forEach>
    </table>           
    <br />
    <br />
    <h2 style="text-align: left;">Migration Form 2 -- choose process</h2>



    <table style="margin: 0px auto; width: 80%; border: 2px solid blue;">
        <tr style="border: 1px solid black;">
            <td style="border: 1px solid black; padding: 5px; min-width: 8em;">Container ID</td>
            <td style="border: 1px solid black; padding: 5px;"><input
                    name="containerId" type="text" size="30" value="evaluation_1.0.0-SNAPSHOT"/></td>
         </tr>
        <tr style="border: 1px solid black;">
           <td style="border: 1px solid black; padding: 5px; min-width: 8em;">Target Container ID</td>
            <td style="border: 1px solid black; padding: 5px;"><input
                    name="targetContainerId" type="text" size="30" value="evaluation_2.0.0-SNAPSHOT"/></td>
        </tr>
    </table>        

    <div style="margin: 10px auto; width: 100%; text-align: center;">
        <button name="getInstance" value="true">Submit</button>
        <button name="cleanSession" value="false">Clean</button>
    </div>
    
    
    <table style="margin: 0px auto; width: 80%; border: 1px solid blue;">
        <tr>
            <td style="border: 1px solid black; padding: 5px"><b>key</b></td>
            <td style="border: 1px solid black; padding: 5px"><b>value</b></td>
        </tr>        
        <c:forEach var="env" items="${envs}">
            <tr style="border: 1px solid black;">
                <td style="border: 1px solid black; padding: 5px">${env.key}</td>
                <td style="border: 1px solid black; padding: 5px">${env.value}</td>
            </tr>        

        </c:forEach>
    </table>           
    
    
</form>
