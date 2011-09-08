 <div id="dialog" title="${message(code:'cmis.dialog.checkin.title')}" style="width:600px;">
	<g:form name="ajaxdialogform" action="checkinsubmit" >			 							
		<g:hiddenField name="objectId" value="${entry.objectId}"/>						
		<table style="border:none;">
		    <tbody>                        
		        <tr class="prop">
		            <td valign="top" class="name"><g:message code="properties.name" default="Document name" />:</td>                                
		            <td valign="top" class="value">${entry.title}</td>
		            <td><a tabindex="9999" class="help button" title="${message(code:'properties.title.help',default:'Document name|This is the name of the document')}" href="" >?</a></td>		                               
		        </tr>
		        
		        <tr class="prop">
		            <td valign="top" class="name"><g:message code="checkin.major" default="Major update" />:</td>
		            <td valign="top" ><g:checkBox name="major" value="${false}" /></td>
		            <td><a tabindex="9999" class="help button" title="${message(code:'checkin.major.help',default:'Major update|Check this if you want to change the version number to the next x.0 version')}" href="" >?</a></td>
	            </tr>
	            
	            <tr class="prop">
		            <td valign="top" class="name"><g:message code="checkin.comment" default="Check in comment" />:</td>                                
		            <td valign="top" ><g:textArea name="checkinComment" cols="40" rows="5"></g:textArea></td>		            
		            <td><a tabindex="9999" class="help button" title="${message(code:'properties.checkin.comment.help')}" href="" >?</a></td>		                               
		        </tr>
		        
		    </tbody>
		</table>
	</g:form>     
</div> 
               