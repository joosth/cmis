 <dialog:form name="cmisDocument.checkout">
 	<g:hiddenField name="objectId" value="${cmisCommandObject.objectId}"/>
 	<dialog:table> 		
 		<dialog:textField object="${cmisCommandObject}" propertyName="name" mode="show"/>
		<dialog:textArea object="${cmisCommandObject}" propertyName="description" mode="show"/>
 	</dialog:table> 
 </dialog:form>