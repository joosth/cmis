 <div id="dialog" title="${message(code:'cmis.dialog.history.title')}" style="width:750px;">
	<g:form name="ajaxdialogform" action="historysubmit" style="width:100%;">			 							
		<g:hiddenField name="parentId" value="${parentId}"/>						
		<table style="border:none;" class="history list">
		    <tbody>                        
		        <tr>
		            <th valign="top"><g:message code="history.versionLabel" default="Version label" /></th>                                
		            <th valign="top"><g:message code="history.creationDate" default="Creation date" /></th>
		            <th valign="top"><g:message code="history.createdBy" default="Created by" /></th>
		            <th valign="top"><g:message code="history.checkinComment" default="Comment" /></th>
		            <th valign="top"><g:message code="history.download" default="Download" /></th>
		        </tr>
		        <g:each in="${documents}" var="document">
		        <tr class="prop null">
		            <td valign="top">${document.prop.versionLabel}</td>
		            <td valign="top">${document.prop.creationDate}</td>
		            <td valign="top">${document.prop.createdBy}</td>
		            <td valign="top">${document.prop.checkinComment}</td>		            
		            <td valign="top"><g:link class="action-download action" controller="document" action="download" params="[objectId:document.objectId]" ><g:message code="history.download" default="Download" /></g:link></td>
	            </tr>		            
				</g:each>		                                                               
		    </tbody>
		</table>
	</g:form>     
</div> 
               