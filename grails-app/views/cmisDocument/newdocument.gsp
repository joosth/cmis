<dialog:form name="cmisDocument.newdocument" width="700px;">			 							
	<g:hiddenField name="objectId" value="${cmisCommandObject.objectId}"/>
	<dialog:upload action="${createLink(controller:'cmisDocument',action: 'fileupload')}" class="btn"/>
</dialog:form>           