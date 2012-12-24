<dialog:form name="cmisDocument.newdocument" width="700px;">			 							
	<g:hiddenField name="objectId" value="${cmisCommandObject.objectId}"/>
	<dialog:upload action="/cmis/cmisDocument/fileupload" class="btn"/>
</dialog:form>           