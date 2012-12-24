<dialog:form name="cmisDocument.newfolder" width="400px;">			 							
	<g:hiddenField name="parentId" value="${newFolderCommandObject.parentId}"/>
	<dialog:table>
		<dialog:textField object="${newFolderCommandObject}" propertyName="name" mode="edit"/>
		<dialog:textArea object="${newFolderCommandObject}" propertyName="description" mode="edit"/>	
	</dialog:table>
</dialog:form>