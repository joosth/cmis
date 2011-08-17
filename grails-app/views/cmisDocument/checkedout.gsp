<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />       
        <title>Documents</title>
        <link rel="stylesheet" href="${resource(dir:'js/uploader',file:'fileuploader.css')}" />
        
        <g:javascript src="uploader/fileuploader.js" />
       
    </head>
    <body>
   <table>
   
   <h1>Checked out documents</h1>
   <h4>${documents.size()} files(s)</h4>
   <div id="toolbar">
	</div>
    <g:each in="${documents}" var="entry" >
    
    <tr>
    	<td class="thumbnail">
    		<g:if test="${entry.prop.baseTypeId=='cmis:folder'}" >
    			<g:link action="list" params="[objectId:entry.prop.objectId]">  <img src="${resource(dir:'images',file:'folder-48.png',contextPath:'')}" /></g:link>
    		</g:if>
    		<g:else>
    			<g:link action="show" params="[objectId:entry.prop.objectId]">  <img src="${entry.thumbnailUrl}" /></g:link>
    		</g:else>
    	
    	</td>
    	<td class="details" >
    		<g:if test="${entry.baseTypeId=='cmis:folder'}" >
    			<g:link action="list" params="[objectId:entry.prop.objectId]">  <h1>${entry.title}</h1></g:link>
    		</g:if>
    		<g:else>
    			<g:link action="show" params="[objectId:entry.prop.objectId]">  <h1>${entry.title}</h1></g:link>
    		</g:else>
    		
    		
    		<div>Creation date: ${entry.prop.creationDate}<br />
    		Version: ${entry.prop.versionLabel}
    		</div>
   		</td>
    	<td class="actions">
    		
    		<g:link class="action-show action" action="show" params="[objectId:entry.prop.objectId]">Show properties</g:link><br />
			<g:link class="action-show action" controller="document" action="props" class="simpleDialog" params="[objectId:entry.prop.objectId]">Show properties</g:link>    		    		
    		<%--<g:link class="action-edit action" action="edit" params="[objectId:entry.prop.objectId]">Edit</g:link> --%>
    		<g:link controller="document" action="edit" class="simpleDialog" params="[objectId:entry.prop.objectId]">Edit</g:link>    		
    		<g:if test="${entry.link.'version-history'}" >
    		<g:link controller="document" action="history" class="simpleDialog" params="[objectId:entry.prop.objectId]">History</g:link>    		
    		</g:if>
    		<g:link controller="document" action="cancelcheckout" class="simpleDialog" params="[objectId:entry.prop.objectId]">Cancel checkout</g:link>
    		<g:link controller="document" action="checkin" class="simpleDialog" params="[objectId:entry.prop.objectId]">Checkin</g:link>
    	</td></tr>
    
    
    
    </g:each>
    </table>
    </body>
</html>
