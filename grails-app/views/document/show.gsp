<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />       
        <title>Documents</title>
    </head>
    <body>

   <table>
    
    
    <tr>
    	<td class="thumbnail"><img src="${entry.thumbnailUrl}" /></td>
    	<td class="details" >
    		<h1>${entry.title}</h1>
    		<div>Creation date: ${entry.creationDate}<br />
    		Version: ${entry.versionLabel}	
    		</div>
   		</td>
   		
    	<td class="actions">
    		<g:link class="action-edit action" action="edit" params="[objectId:entry.objectId]" >Edit</g:link>    		
    		<g:if test="${entry.baseTypeId=='cmis:document'}" >
	    		<g:link class="action-download action" controller="document" action="download" params="[objectId:entry.objectId]" >Download</g:link>    		
    			<g:link class="action-upload action" action="upload" params="[objectId:entry.objectId]">Upload</g:link>
    			<g:if test="${entry.link.via}" >
    			<g:link class="action simpleDialog" action="checkin" params="[objectId:entry.objectId]">Checkin</g:link>
    			</g:if>
    		</g:if>
    		
    		    		
    		
    	
    	</td></tr>
    	</table>
    <br />
    <table>
    	<tr><th colspan="2">Properties</th></tr>
    	<g:each in="${entry.properties}" var="property" >    
    		<tr><td>${property.key}</td><td>${property.value}</td></tr>
    	</g:each>
    </table>
    
	       <br /> 
	    <table>
	    	<tr><th colspan="2">Links</th></tr>
	    	<g:each in="${entry.links}" var="link">    
	    		<tr><td>${link.key}</td><td>${link.value}</td></tr>
	    	</g:each>
	    </table>
	       
    </body>
</html>
