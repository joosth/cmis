 <div id="dialog" title="${message(code:'form.cmisDocument.history.title')}" style="width:750px;">
	<g:form name="ajaxdialogform" style="width:100%;">			 							
		<g:hiddenField name="parentId" value="${parentId}"/>						
		<table style="border:none;" class="history list table table-striped table-condensed table-hover">
		    <tbody>                        
		        <tr>
		            <th valign="top"><g:message code="cmisDocument.history.versionLabel.label" default="Version label" /></th>                                
		            <th valign="top"><g:message code="cmisDocument.history.creationDate.label" default="Creation date" /></th>
		            <th valign="top"><g:message code="cmisDocument.history.createdBy.label" default="Created by" /></th>
		            <th valign="top"><g:message code="cmisDocument.history.checkinComment.label" default="Comment" /></th>
		            <th valign="top"><g:message code="cmisDocument.history.download.label" default="Download" /></th>
		        </tr>
		        <g:each in="${documents}" var="document">
		        <tr class="prop">
		            <td valign="top">${document.prop.versionLabel}</td>
		            <td valign="top">${document.prop.lastModificationDate.time}</td>
		            <td valign="top">${document.prop.lastModifiedBy}</td>
		            <td valign="top">${document.prop.checkinComment}</td>		            
		            <td valign="top"><g:link class="action-download action" controller="cmisDocument" action="download" params="[objectId:document.objectId]" ><g:message code="cmisDocument.history.download.label" default="Download" /></g:link></td>
	            </tr>		            
				</g:each>		                                                               
		    </tbody>
		</table>
	</g:form>     
</div> 
               