 <%-- This view is used for the document deatails sidepane --%>
 <div id="dialog" title="Show document properties" style="wwidth:600px;float:left;"> 		
		<g:hiddenField name="objectId" value="${entry.objectId}"/>
		<img src="${entry.thumbnailUrl}" />						
		<table style="border:none;width:500px;">
		    <tbody>                        
		        <tr class="prop">
		            <td valign="top" class="name"><g:message code="properties.name" default="Document name" />:</td>                                
		            <td valign="top" class="value">${entry.title}</td>
		            <td><span tabindex="9999" class="help action" title="${message(code:'properties.title.help',default:'Document name|This is the name of the document')}" href="" >&nbsp;</span></td>		                               
		        </tr>
		        <tr class="prop">
		            <td valign="top" class="name"><g:message code="properties.summary" default="Document summary" />:</td>
		            <td valign="top" >${entry.summary}</td>
		            <td><span tabindex="9999" class="help action" title="${message(code:'properties.summary.help',default:'Summary|This is the summary of the document')}" href="" >&nbsp;</span></td>
	            </tr>
	            
	            <tr class="prop">
		            <td valign="top" class="name"><g:message code="properties.size" default="Size" />:</td>                                
		            <td valign="top" class="value">${entry.prop.contentStreamLength}</td>
		            <td><span tabindex="9999" class="help action" title="${message(code:'properties.title.help')}" href="" >&nbsp;</span></td>		                               
		        </tr>
		        
		        <tr class="prop">
		            <td valign="top" class="name"><g:message code="properties.mimetype" default="Mimetype" />:</td>                                
		            <td valign="top" class="value">${entry.prop.contentStreamMimeType}</td>
		            <td><span tabindex="9999" class="help action" title="${message(code:'properties.mimetype.help')}" href="" >&nbsp;</span></td>		                               
		        </tr>
		        
	            <tr class="prop">
		            <td valign="top" class="name"><g:message code="properties.version" default="Version" />:</td>                                
		            <td valign="top" class="value">${entry.prop.versionLabel}</td>
		            <td><span tabindex="9999" class="help action" title="${message(code:'properties.mimetype.help')}" href="" >&nbsp;</span></td>		                               
		        </tr>
		        
		        <tr class="prop">
		            <td valign="top" class="name"><g:message code="properties.lastModifiedBy" default="Last modified by" />:</td>                                
		            <td valign="top" class="value">${entry.prop.lastModifiedBy}</td>
		            <td><span tabindex="9999" class="help action" title="${message(code:'properties.lastModifiedBy.help')}" href="" >&nbsp;</span></td>		                               
		        </tr>
		        
		        <tr class="prop">
		            <td valign="top" class="name"><g:message code="properties.lastModificationDate" default="Last modification date" />:</td>                                
		            <td valign="top" class="value">${entry.prop.lastModificationDate}</td>
		            <td><span tabindex="9999" class="help action" title="${message(code:'properties.lastModificationDate.help')}" href="" >&nbsp;</span></td>		                               
		        </tr>
		        
		        <tr class="prop">
		            <td valign="top" class="name"><g:message code="properties.checkinComment" default="Checkin comment" />:</td>                                
		            <td valign="top" class="value">${entry.prop.checkinComment}</td>
		            <td><span tabindex="9999" class="help action" title="${message(code:'properties.checkinComment.help')}" href="" >&nbsp;</span></td>		                               
		        </tr>
		        
		        
		        <tr class="prop">
		            <td valign="top" class="name"><g:message code="properties.createdBy" default="Created by" />:</td>                                
		            <td valign="top" class="value">${entry.prop.createdBy}</td>
		            <td><span tabindex="9999" class="help action" title="${message(code:'properties.createdBy.help')}" href="" >&nbsp;</span></td>		                               
		        </tr>

		        <tr class="prop">
		            <td valign="top" class="name"><g:message code="properties.creationDate" default="Creation date" />:</td>                                
		            <td valign="top" class="value">${entry.prop.creationDate}</td>
		            <td><span tabindex="9999" class="help action" title="${message(code:'properties.creationDate.help')}" href="" >&nbsp;</span></td>		                               
		        </tr>
		        

	            
	                                                               
		    </tbody>
		</table>
</div>


<div id="document-actions" style="float:left;background-color:#EEE;margin:10px;padding:10px;border:1px dotted #888;">
	
	<g:link onclick="simpleDialog(this.href);return false;" title="Show properties" class="action-show action simpleDialog" controller="document" action="props" params="[objectId:entry.prop.objectId]">Show properties</g:link><br />
 	<g:link onclick="simpleDialog(this.href);return false;" title="Edit properties" class="action-edit action simpleDialog" controller="document" action="edit" params="[objectId:entry.prop.objectId]">Edit properties</g:link><br />
 	
 	
 	
 	<g:if test="${entry.hasHistory()}" >
 		<g:link onclick="simpleDialog(this.href);return false;" title="Show history" class="action-history action simpleDialog" controller="document" action="history" params="[objectId:entry.prop.objectId]">Show history</g:link><br />
	</g:if>
	<g:if test="${sppPath}" >
 		<span href="${sppPath}" title="Edit online (SPP)" sppAppProgId="${sppAppProgId}" class="action-edit-online action spp-link">Edit online (SPP)</span><br />
	</g:if>
	<g:if test="${webdavPath}" >
 		<a href="${webdavPath}" title="Edit online (WebDAV)" class="action-edit-online action" target="_blank">Edit online (WebDAV)</a><br />
	</g:if>
	
	
	<g:if test="${entry.isDocument()}" >
		<g:link title="Download" class="action-download action simpleDialog" controller="document" action="download" params="[objectId:entry.prop.objectId]">Download</g:link><br />
		<g:link onclick="uploadDialog(this.href);return false;" title="Upload" class="action-upload action uploadDialog" controller="document" action="updatedocument" params="[objectId:entry.prop.objectId]">Update</g:link><br />
		
 		<g:if test="${entry.isPwc()}">		
 			<g:link onclick="simpleDialog(this.href);return false;" title="Checkin" class="action-checkin action simpleDialog" controller="document" action="checkin" params="[objectId:entry.prop.objectId]">Checkin</g:link><br />
 		</g:if>
 		<g:else>
 		<g:if test="${!entry.isCheckedOut()}" >
			<g:link onclick="simpleDialog(this.href);return false;" title="Checkout" class="action-checkout action simpleDialog" controller="document" action="checkout" params="[objectId:entry.prop.objectId]">Checkout</g:link><br />		
 		</g:if>
 		
 		
 		</g:else>
 		
 	</g:if>
 	
 	
 	<g:link onclick="simpleDialog(this.href);return false;" title="Delete document" class="action-delete action simpleDialog" controller="document" action="delete" params="[objectId:entry.prop.objectId]">Delete</g:link><br />
 

</div>
               
