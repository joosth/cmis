 <dialog:form name="cmisDocument.cancelcheckout">
 	<g:hiddenField name="objectId" value="${cmisCommandObject.objectId}" />
 	<dialog:table> 		
 		<dialog:simplerow name="cmisDocument.cancelcheckout.name" >${cmisCommandObject.name}</dialog:simplerow>
 		<dialog:simplerow name="cmisDocument.cancelcheckout.description" >${cmisCommandObject.description}</dialog:simplerow>
 	</dialog:table> 
</dialog:form>
