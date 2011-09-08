 <div id="dialog" title="${message(code:'cmis.dialog.checkout.title')}" style="width:600px;">
	<g:form name="ajaxdialogform" action="checkoutsubmit" >			 							
		<g:hiddenField name="objectId" value="${entry.objectId}"/>
		<p style="color:red;font-size:14px;text-align:center;">Are you sure you want to checkout this document?</p>						
		<table style="border:none;">
		    <tbody>                        
		        <tr class="prop">
		            <td valign="top" class="name"><g:message code="properties.name" default="Document name" />:</td>                                
		            <td valign="top" class="value">${entry.title}</td>
		            <td><a tabindex="9999" class="help button" title="${message(code:'properties.title.help',default:'Document name|This is the name of the document')}" href="" >?</a></td>		                               
		        </tr>
		        <tr class="prop">
		            <td valign="top" class="name"><g:message code="properties.summary" default="Document summary" />:</td>
		            <td valign="top" >${entry.summary}</td>
		            <td><a tabindex="9999" class="help button" title="${message(code:'properties.summary.help',default:'Summary|This is the summary of the document')}" href="" >?</a></td>
	            </tr>
	            
	            <tr class="prop">
		            <td valign="top" class="name"><g:message code="properties.size" default="Size" />:</td>                                
		            <td valign="top" class="value">${entry.prop.contentStreamLength}</td>
		            <td><a tabindex="9999" class="help button" title="${message(code:'properties.title.help')}" href="" >?</a></td>		                               
		        </tr>
		        
		        <tr class="prop">
		            <td valign="top" class="name"><g:message code="properties.size" default="Mimetype" />:</td>                                
		            <td valign="top" class="value">${entry.prop.contentStreamMimeType}</td>
		            <td><a tabindex="9999" class="help button" title="${message(code:'properties.mimetype.help')}" href="" >?</a></td>		                               
		        </tr>
		        
	            <tr class="prop">
		            <td valign="top" class="name"><g:message code="properties.version" default="Version" />:</td>                                
		            <td valign="top" class="value">${entry.prop.versionLabel}</td>
		            <td><a tabindex="9999" class="help button" title="${message(code:'properties.mimetype.help')}" href="" >?</a></td>		                               
		        </tr>
		        
		        <tr class="prop">
		            <td valign="top" class="name"><g:message code="properties.lastModifiedBy" default="Last modified by" />:</td>                                
		            <td valign="top" class="value">${entry.prop.lastModifiedBy}</td>
		            <td><a tabindex="9999" class="help button" title="${message(code:'properties.lastModifiedBy.help')}" href="" >?</a></td>		                               
		        </tr>
		        
		        <tr class="prop">
		            <td valign="top" class="name"><g:message code="properties.lastModificationDate" default="Last modification date" />:</td>                                
		            <td valign="top" class="value">${entry.prop.lastModificationDate}</td>
		            <td><a tabindex="9999" class="help button" title="${message(code:'properties.lastModificationDate.help')}" href="" >?</a></td>		                               
		        </tr>
		        
		        <tr class="prop">
		            <td valign="top" class="name"><g:message code="properties.checkinComment" default="Checkin comment" />:</td>                                
		            <td valign="top" class="value">${entry.prop.checkinComment}</td>
		            <td><a tabindex="9999" class="help button" title="${message(code:'properties.checkinComment.help')}" href="" >?</a></td>		                               
		        </tr>
		        
		        
		        <tr class="prop">
		            <td valign="top" class="name"><g:message code="properties.createdBy" default="Created by" />:</td>                                
		            <td valign="top" class="value">${entry.prop.createdBy}</td>
		            <td><a tabindex="9999" class="help button" title="${message(code:'properties.createdBy.help')}" href="" >?</a></td>		                               
		        </tr>

		        <tr class="prop">
		            <td valign="top" class="name"><g:message code="properties.creationDate" default="Creation date" />:</td>                                
		            <td valign="top" class="value">${entry.prop.creationDate}</td>
		            <td><a tabindex="9999" class="help button" title="${message(code:'properties.creationDate.help')}" href="" >?</a></td>		                               
		        </tr>
		        

	            
	                                                               
		    </tbody>
		</table>
	</g:form>     
</div> 
               