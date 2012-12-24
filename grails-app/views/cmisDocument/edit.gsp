 <dialog:form name="cmisDocument.edit" width="400px;">			 							
	<g:hiddenField name="objectId" value="${cmisCommandObject.objectId}"/>
	<dialog:table>
		<dialog:textField object="${cmisCommandObject}" propertyName="name" mode="edit"/>
		<dialog:textArea object="${cmisCommandObject}" propertyName="description" mode="edit"/>	
	</dialog:table>
</dialog:form>