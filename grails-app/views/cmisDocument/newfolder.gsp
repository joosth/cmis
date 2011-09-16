 <div id="dialog" title="${message(code:'cmis.dialog.newfolder.title')}">
	<g:form name="ajaxdialogform" action="newfoldersubmit" >			 							
		<g:hiddenField name="parentId" value="${parentId}"/>						
		<table style="border:none;">
		    <tbody>                        
		        <tr class="prop">
		            <td valign="top" class="name"><g:message code="folder.name" default="Folder name" />:</td>                                
		            <td valign="top" class="value"><g:textField name="name" /></td>
		            <td><span class="help action" title="${message(code:'folder.summary.help',default:'Folder name|Enter the name of the folder here')}" href="" >&nbsp;</span></td>		                               
		        </tr>
		        <tr class="prop null">
		            <td valign="top" class="name"><g:message code="folder.summary" default="Folder summary" /></td>
		            <td valign="top" ><g:textArea name="summary" cols="40" rows="5" /></td>
		            <td><span class="help action" title="${message(code:'folder.summary.help',default:'Summary|Enter a folder summary here')}" href="" >&nbsp;</span></td></tr>                                                   
		    </tbody>
		</table>
	</g:form>     
</div> 
               