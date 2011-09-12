 <div id="dialog" title="${message(code:'cmis.dialog.checkout.title')}" style="width:600px;">
	<g:form name="ajaxdialogform" action="checkoutsubmit" >			 							
		<g:hiddenField name="objectId" value="${entry.objectId}"/>
		<p style="color:red;font-size:14px;text-align:center;"><g:message code="cmis.dialog.checkout.title" ></g:message></p>						
		<table style="border:none;">
		    <tbody>                        
		        <tr class="prop">
		            <td valign="top" class="name"><g:message code="properties.name" default="Document name" />:</td>                                
		            <td valign="top" class="value">${entry.title}</td>
		            <td><span tabindex="9999" class="help action" title="${message(code:'properties.title.help',default:'Document name|This is the name of the document')}" >&nbsp;</span></td>		                               
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
		            <td valign="top" class="name"><g:message code="properties.size" default="Mimetype" />:</td>                                
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
	</g:form>     
</div> 
               