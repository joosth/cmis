 <%-- This view is used for the document deatails sidepane --%>
 <div id="dialog" title="${message(code:'cmis.properties.title')}" style="float:left;"> 		
		<g:hiddenField name="objectId" value="${entry.objectId}"/>
		<img src="${entry.thumbnailUrl}" />						
		<table style="border:none;width:500px;">
		    <tbody>                        
		        <tr class="prop">
		            <td valign="top" class="name"><g:message code="cmis.properties.name.title" default="Document name" />:</td>                                
		            <td valign="top" class="value">${entry.title}</td>
		            <td><span tabindex="9999" class="help action" title="${message(code:'cmis.properties.title.help',default:'Document name|This is the name of the document')}" href="" >&nbsp;</span></td>		                               
		        </tr>
		        <tr class="prop">
		            <td valign="top" class="name"><g:message code="cmis.properties.summary" default="Document summary" />:</td>
		            <td valign="top" >${entry.summary}</td>
		            <td><span tabindex="9999" class="help action" title="${message(code:'cmis.properties.summary.help',default:'Summary|This is the summary of the document')}" href="" >&nbsp;</span></td>
	            </tr>
	            
	            <tr class="prop">
		            <td valign="top" class="name"><g:message code="cmis.properties.size.title" default="Size" />:</td>                                
		            <td valign="top" class="value">${entry.prop.contentStreamLength}</td>
		            <td><span tabindex="9999" class="help action" title="${message(code:'cmis.properties.size.help')}" href="" >&nbsp;</span></td>		                               
		        </tr>
		        
		        <tr class="prop">
		            <td valign="top" class="name"><g:message code="cmis.properties.mimetype.title" default="Mimetype" />:</td>                                
		            <td valign="top" class="value">${entry.prop.contentStreamMimeType}</td>
		            <td><span tabindex="9999" class="help action" title="${message(code:'cmis.properties.mimetype.help')}" href="" >&nbsp;</span></td>		                               
		        </tr>
		        
	            <tr class="prop">
		            <td valign="top" class="name"><g:message code="cmis.properties.version.title" default="Version" />:</td>                                
		            <td valign="top" class="value">${entry.prop.versionLabel}</td>
		            <td><span tabindex="9999" class="help action" title="${message(code:'cmis.properties.mimetype.help')}" href="" >&nbsp;</span></td>		                               
		        </tr>
		        
		        <tr class="prop">
		            <td valign="top" class="name"><g:message code="cmis.properties.lastModifiedBy.title" default="Last modified by" />:</td>                                
		            <td valign="top" class="value">${entry.prop.lastModifiedBy}</td>
		            <td><span tabindex="9999" class="help action" title="${message(code:'cmis.properties.lastModifiedBy.help')}" href="" >&nbsp;</span></td>		                               
		        </tr>
		        
		        <tr class="prop">
		            <td valign="top" class="name"><g:message code="cmis.properties.lastModificationDate.title" default="Last modification date" />:</td>                                
		            <td valign="top" class="value">${entry.prop.lastModificationDate}</td>
		            <td><span tabindex="9999" class="help action" title="${message(code:'cmis.properties.lastModificationDate.help')}" href="" >&nbsp;</span></td>		                               
		        </tr>
		        
		        <tr class="prop">
		            <td valign="top" class="name"><g:message code="cmis.properties.checkinComment.title" default="Checkin comment" />:</td>                                
		            <td valign="top" class="value">${entry.prop.checkinComment}</td>
		            <td><span tabindex="9999" class="help action" title="${message(code:'cmis.properties.checkinComment.help')}" href="" >&nbsp;</span></td>		                               
		        </tr>
		        
		        
		        <tr class="prop">
		            <td valign="top" class="name"><g:message code="cmis.properties.createdBy.title" default="Created by" />:</td>                                
		            <td valign="top" class="value">${entry.prop.createdBy}</td>
		            <td><span tabindex="9999" class="help action" title="${message(code:'cmis.properties.createdBy.help')}" href="" >&nbsp;</span></td>		                               
		        </tr>

		        <tr class="prop">
		            <td valign="top" class="name"><g:message code="cmis.properties.creationDate.title" default="Creation date" />:</td>                                
		            <td valign="top" class="value">${entry.prop.creationDate}</td>
		            <td><span tabindex="9999" class="help action" title="${message(code:'cmis.properties.creationDate.help')}" href="" >&nbsp;</span></td>		                               
		        </tr>
		        

	            
	                                                               
		    </tbody>
		</table>
</div>


<div id="document-actions" style="float:left;background-color:#EEE;margin:10px;padding:10px;border:1px dotted #888;">
	
	<g:link onclick="simpleDialog(this.href);return false;" title="${message(code:'cmis.actions.showproperties.tooltip')}" class="action-show action simpleDialog" controller="cmisDocument" action="props" params="[objectId:entry.prop.objectId]"><g:message code="cmis.actions.showproperties.title" /></g:link><br />
 	<g:link onclick="simpleDialog(this.href);return false;" title="${message(code:'cmis.actions.editproperties.tooltip')}" class="action-edit action simpleDialog" controller="cmisDocument" action="edit" params="[objectId:entry.prop.objectId]"><g:message code="cmis.actions.editproperties.title" /></g:link><br />
 	
 	
 	
 	<g:if test="${entry.hasHistory()}" >
 		<g:link onclick="simpleDialog(this.href);return false;" title="${message(code:'cmis.actions.showhistory.tooltip')}" class="action-history action simpleDialog" controller="cmisDocument" action="history" params="[objectId:entry.prop.objectId]"><g:message code="cmis.actions.showhistory.title" /></g:link><br />
	</g:if>
	<g:if test="${sppPath}" >
 		<span href="${sppPath}" title="${message(code:'cmis.actions.editonlinespp.tooltip')}" sppAppProgId="${sppAppProgId}" class="action-edit-online action spp-link"><g:message code="cmis.actions.editonlinespp.title" /></span><br />
	</g:if>
	<g:if test="${webdavPath}" >
 		<a href="${webdavPath}" title="${message(code:'cmis.actions.editonlinewebdav.tooltip')}" class="action-edit-online action" target="_blank"><g:message code="cmis.actions.editonlinewebdav.title" /></a><br />
	</g:if>
	
	
	<g:if test="${entry.isDocument()}" >
		<g:link title="${message(code:'cmis.actions.download.tooltip')}" class="action-download action simpleDialog" controller="cmisDocument" action="download" params="[objectId:entry.prop.objectId]"><g:message code="cmis.actions.download.title" /></g:link><br />
		<g:link onclick="uploadDialog(this.href);return false;" title="${message(code:'cmis.actions.update.tooltip')}" class="action-upload action uploadDialog" controller="cmisDocument" action="updatedocument" params="[objectId:entry.prop.objectId]"><g:message code="cmis.actions.update.title" /></g:link><br />
		
 		<g:if test="${entry.isPwc()}">		
 			<g:link onclick="simpleDialog(this.href);return false;" title="${message(code:'cmis.actions.checkin.tooltip')}" class="action-checkin action simpleDialog" controller="cmisDocument" action="checkin" params="[objectId:entry.prop.objectId]"><g:message code="cmis.actions.checkin.title" /></g:link><br />
 		</g:if>
 		<g:else>
 		<g:if test="${!entry.isCheckedOut()}" >
			<g:link onclick="simpleDialog(this.href);return false;" title="${message(code:'cmis.actions.checkout.tooltip')}" class="action-checkout action simpleDialog" controller="cmisDocument" action="checkout" params="[objectId:entry.prop.objectId]"><g:message code="cmis.actions.checkout.title" /></g:link><br />		
 		</g:if>
 		
 		
 		</g:else>
 		
 	</g:if>
 	
 	
 	<g:link onclick="simpleDialog(this.href);return false;" title="${message(code:'cmis.actions.delete.tooltip')}" class="action-delete action simpleDialog" controller="cmisDocument" action="delete" params="[objectId:entry.prop.objectId]"><g:message code="cmis.actions.delete.title" /></g:link><br />
 

</div>
               
