 <div id="dialog" title="${message(code:'cmis.dialog.delete.title')}" style="width:600px;">
	<g:form name="ajaxdialogform" action="deletesubmit" controller="cmisDocument" >			 							
		<g:hiddenField name="objectId" value="${entry.objectId}"/>
		<p style="color:red;font-size:14px;text-align:center;">${message(code:'cmis.dialog.delete.areyousure')}</p>						
		<table style="border:none;">
		   	    <tbody>                        
		        <tr class="prop">
		            <td valign="top" class="name"><g:message code="cmis.properties.name.title" default="Document name" />:</td>                                
		            <td valign="top" class="value">${entry.title}</td>
		            <td><span tabindex="9999" class="help action" title="${message(code:'cmis.properties.name.help',default:'Document name|This is the name of the document')}" href="" >&nbsp;</span></td>
		            		                               
		        </tr>
		        <tr class="prop">
		            <td valign="top" class="name"><g:message code="cmis.properties.summary.title" default="Document summary" />:</td>
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
	</g:form>     
</div> 
               