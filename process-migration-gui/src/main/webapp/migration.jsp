<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>


<table>
    <tr>
        <td><img src="data:image/svg+xml;base64,${processInfo.getBase64ImageV1()}" />
        <td><img src="data:image/svg+xml;base64,${processInfo.getBase64ImageV2()}" />
    </tr>
    <tr>
        <td align="center"><B>Current Process Diagram2</B></td>
        <td align="center"><B>New Process Diagram</B></td>
    </tr>
</table>
<br />
<br />

<form target="_self" method="post">
    <h2 style="text-align: left;">Migration From 1 -- chosen process </h2>
    <input type="hidden" name="containerId" value="${processInfo.getContainerId()}" />
    <input type="hidden" name="targetContainerId" value="${processInfo.getTargetContainerId()}" />
    <input type="hidden" name="targetProcessId" value="${processInfo.getTargetProcessId()}" />
    <input type="hidden" name="useRest" value="true" />
    <table style="margin: 0px auto; width: 80%; border: 2px solid blue;">
        <tr style="border: 1px solid black;">
            <td style="border: 1px solid black; padding: 5px; min-width: 8em;">Container ID</td>
            <td style="border: 1px solid black; padding: 5px;">${processInfo.getContainerId()}</td>
        </tr>
        <tr style="border: 1px solid black;">
            <td style="border: 1px solid black; padding: 5px; min-width: 8em;">Target Container ID</td>
            <td style="border: 1px solid black; padding: 5px;">${processInfo.getTargetContainerId()}</td>
        </tr>
        <tr style="border: 1px solid black;">
            <td style="border: 1px solid black; padding: 5px; min-width: 8em;">Target Process ID</td>
            <td style="border: 1px solid black; padding: 5px;">${processInfo.getTargetProcessId()}</td>
        </tr>
    </table> 


    <h2 style="text-align: left;">Migration From 2 -- choose Running instance</h2>
    <table style="margin: 0px auto; width: 80%; border: 1px solid blue;">
        <c:set var="count" value="0" scope="page" />
        <c:forEach var="instance" items="${processInfo.getRunningInstance()}">
            <c:set var="count" value="${count + 1}" scope="page"/>
            <tr style="border: 1px solid black;">
                <td style="border: 1px solid black; padding: 5px">${instance}</td>
                <td style="border: 1px solid black; padding: 5px;">
                    <input type="checkbox" name="instanceIds" value="${instance}"/></td>                
            </tr>
        </c:forEach>
    </table>           
    <br />
    <br />

    <h2 style="text-align: left;">Migration Form 3 -- Node Mapping</h2>
    <table style="margin: 0px auto; width: 80%; border: 2px solid blue;">
        <tr>
            <td style="border: 1px solid black; padding: 5px"><b>Node name</b></td>
            <td style="border: 1px solid black; padding: 5px"><b>Node Type</b></td>
            <td style="border: 1px solid black; padding: 5px"><b>Node ID</b></td>            
            <td style="border: 1px solid black; padding: 5px"><b>Target Node ID  (Please reference table below) </b></td>    
        </tr>
        <c:forEach var="bpmNode" items="${processInfo.getNodeV1()}">
            <tr style="border: 1px solid black;">
                <td style="border: 1px solid black; padding: 5px">${bpmNode.getName()}</td>
                <td style="border: 1px solid black; padding: 5px">${bpmNode.getType()}</td>
                <td style="border: 1px solid black; padding: 5px">${bpmNode.getId()}</td>
                <td style="border: 1px solid black; padding: 5px;"><input
                        name="targetNodeId-${bpmNode.getId()}" type="text"/></td>
            </tr>
        </c:forEach>                

    </table>
   
    <div style="margin: 10px auto; width: 100%; text-align: center;">
        <button name="generate" value="true">Submit</button>
        <button name="generate" value="false">Cancel</button>
    </div>

    <br />
    <br />

    <table style="margin: 0px auto; width: 80%; border: 1px solid black;">
        <caption style="margin: 0px auto; font-size: 2em">New version node information</caption>
        <td style="border: 1px solid black; padding: 5px"><b>Node name</b></td>
        <td style="border: 1px solid black; padding: 5px"><b>Node Type</b></td>
        <td style="border: 1px solid black; padding: 5px"><b>Node ID</b></td>            

        <c:forEach var="bpmNode" items="${processInfo.getNodeV2()}">
            <tr style="border: 1px solid black;">
                <td style="border: 1px solid black; padding: 5px">${bpmNode.getName()}</td>
                <td style="border: 1px solid black; padding: 5px">${bpmNode.getType()}</td>
                <td style="border: 1px solid black; padding: 5px">${bpmNode.getId()}</td>
            </tr>
        </c:forEach>
    </table>                   

</form>
