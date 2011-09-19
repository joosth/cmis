 <div id="dialog" title="${message(code:'cmis.dialog.editproperties.title')}">
	<g:form name="ajaxdialogform" action="update" >			 							
		<g:hiddenField name="objectId" value="${entry.objectId}"/>						
		<table style="border:none;">
		    <tbody>                        
		        <tr class="prop">
		            <td valign="top" class="name"><g:message code="cmis.dialog.document.name.title" default="Document name" />:</td>                                
		            <td valign="top" class="value"><g:textField name="title" value="${entry.title}" /></td>
		            <td><span class="help action" title="${message(code:'cmis.dialog.document.name.help',default:'Document name|Enter the name of the document here')}" href="" >&nbsp;</span></td>		                               
		        </tr>
		        <tr class="prop null">
		            <td valign="top" class="name"><g:message code="cmis.dialog.document.summary.title" default="Document summary" />:</td>
		            <td valign="top" ><g:textArea name="summary" cols="40" rows="5">${entry.summary}</g:textArea></td>
		            <td><span tabindex="9999" class="help action" title="${message(code:'cmis.dialog.document.summary.help',default:'Summary|Enter a document summary here')}" href="" >&nbsp;</span></td></tr>                                                   
		    </tbody>
		</table>
	</g:form>     
</div> 
               