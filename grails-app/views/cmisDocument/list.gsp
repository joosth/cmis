<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />       
        <title>${message(code:'cmis.dialog.list.title')}</title>
        <link rel="stylesheet" href="${resource(dir:'js/uploader',file:'fileuploader.css')}" />
        <%--
        <g:javascript src="jquery/jquery.form.js" />
            --%>   
        <g:javascript src="uploader/fileuploader.js" />
        <%--
        <jq:jquery>
        	var uploader = new qq.FileUploader({
    		// pass the dom node (ex. $(selector)[0] for jQuery users)
    		element: document.getElementById('file-uploader'),
    		// path to server-side upload script
    		action: '/smurf/buttons/fileupload'
			});                	
        </jq:jquery>
        --%>
    </head>
    <body>
   <table>
   
            
   <h1>${cmisEntry.title}</h1>
   <h1>${cmisEntry.summary}</h1>
   <h4>${documents.size()} files(s) and/or folder(s)</h4>
   <div id="toolbar">
   		<g:link controller="document" action="newfolder" class="simpleDialog" params="[parentId:cmisEntry.objectId]">New folder</g:link>
   		<g:link controller="document" action="newdocument" class="uploadDialog" params="[parentId:cmisEntry.objectId]">New document</g:link>
	</div>
   <g:if test="${cmisEntry.parentId}">
   <h4><g:link controller="document" action="list" params="[objectId:cmisEntry.parentId]">..</g:link></h4>
   </g:if>
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
			<g:link class="action-show action" controller="document" action="props" class="simpleDialog" params="[objectId:entry.prop.objectId]">Edit properties</g:link>    		    		
    		<%--<g:link class="action-edit action" action="edit" params="[objectId:entry.prop.objectId]">Edit</g:link> --%>
    		<g:link controller="document" action="edit" class="simpleDialog" params="[objectId:entry.prop.objectId]">Edit</g:link>    		
    		<g:if test="${entry.link.'version-history'}" >
    			<g:link controller="document" action="history" class="simpleDialog" params="[objectId:entry.prop.objectId]">History</g:link>    		
    		</g:if>
    		<g:link controller="document" action="delete" class="simpleDialog" params="[objectId:entry.prop.objectId]">Delete</g:link>
    		<g:link controller="document" action="checkout" class="simpleDialog" params="[objectId:entry.prop.objectId]">Checkout</g:link>
    		<g:if test="${entry.link.'working-copy'}">
    			<g:link controller="document" action="workingcopy" params="[objectId:entry.prop.objectId]">Working copy</g:link>    			
    		</g:if>
    		
    	</td></tr>
    
    
    
    </g:each>
    </table>
    </body>
</html>
