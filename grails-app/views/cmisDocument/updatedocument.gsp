<dialog:form name="cmisDocument.updatedocument">
	<g:hiddenField name="objectId" value="${entry.prop.objectId}"/>
	<dialog:table>						
		<dialog:simplerow name="cmisDocument.updatedocument.name">${entry.prop.name}</dialog:simplerow>
		<tr><td colspan="2"><dialog:upload action="cmisDocument/fileupload" class="btn"/></td></tr>				
	</dialog:table>		         
</dialog:form>