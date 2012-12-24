<dialog:form name="cmisDocument.checkin">
 	<g:hiddenField name="objectId" value="${checkinCommandObject.objectId}"/>
 	<dialog:table> 		
 		<dialog:checkBox object="${checkinCommandObject}" propertyName="major" mode="edit"/>
		<dialog:textArea object="${checkinCommandObject}" propertyName="comment" mode="edit"/>	
 	</dialog:table> 
</dialog:form>