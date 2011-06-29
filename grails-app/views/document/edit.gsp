 <div id="dialog" title="Edit document properties">
	<g:form name="ajaxdialogform" action="update" controller="document">			 							
		<g:hiddenField name="objectId" value="${entry.objectId}"/>						
		<table style="border:none;">
		    <tbody>                        
		        <tr class="prop">
		            <td valign="top" class="name"><g:message code="document.name" default="Document name" />:</td>                                
		            <td valign="top" class="value"><g:textField name="title" value="${entry.title}" /></td>
		            <td><a tabindex="9999" class="help action" title="${message(code:'document.title.help',default:'Document name|Enter the name of the document here')}" href="" >&nbsp;</a></td>		                               
		        </tr>
		        <tr class="prop null">
		            <td valign="top" class="name"><g:message code="document.summary" default="Document summary" /></td>
		            <td valign="top" ><g:textArea name="summary" cols="40" rows="5">${entry.summary}</g:textArea></td>
		            <td><a tabindex="9999" class="help action" title="${message(code:'document.summary.help',default:'Summary|Enter a document summary here')}" href="" >&nbsp;</a></td></tr>                                                   
		    </tbody>
		</table>
	</g:form>     
</div> 
               