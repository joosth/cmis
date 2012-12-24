 <dialog:form name="cmisDocument.delete" action="deletesubmit">
 	<g:hiddenField name="objectId" value="${entry.objectId}"/>
 	<dialog:table>
 		<dialog:simplerow name="cmisDocument.delete.name">${entry.prop.name}</dialog:simplerow>
		<dialog:simplerow name="cmisDocument.delete.description">${entry.prop.description}</dialog:simplerow>			
 	</dialog:table>
</dialog:form>
 