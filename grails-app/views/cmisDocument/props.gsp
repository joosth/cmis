<dialog:form name="cmisDocument.props" >						
	<g:hiddenField name="objectId" value="${cmisObject.objectId}"/>
	<dialog:table>
		<dialog:simplerow name="cmisDocument.props.name">${cmisObject.prop.name}</dialog:simplerow>
		<dialog:simplerow name="cmisDocument.props.description">${cmisObject.prop.description}</dialog:simplerow>
		<dialog:simplerow name="cmisDocument.props.contentStreamLength">${cmisObject.prop.contentStreamLength}</dialog:simplerow>
		<dialog:simplerow name="cmisDocument.props.contentStreamMimeType">${cmisObject.prop.contentStreamMimeType}</dialog:simplerow>
		<dialog:simplerow name="cmisDocument.props.versionLabel">${cmisObject.prop.versionLabel}</dialog:simplerow>
		<dialog:simplerow name="cmisDocument.props.lastModifiedBy">${cmisObject.prop.lastModifiedBy}</dialog:simplerow>
		<dialog:simplerow name="cmisDocument.props.lastModificationDate">${cmisObject.prop.lastModificationDate.time}</dialog:simplerow>
			<%-- <dialog:simplerow name="cmis.properties.lastModificationDate">${String.format('%ta %<td %<tb %<tY %<tH:%<tM:%<tS', cmisObject.prop.lastModificationDate)}</dialog:simplerow>--%>
		<dialog:simplerow name="cmisDocument.props.checkinComment">${cmisObject.prop.checkinComment}</dialog:simplerow>
		<dialog:simplerow name="cmisDocument.props.createdBy">${cmisObject.prop.createdBy}</dialog:simplerow>
		<dialog:simplerow name="cmisDocument.props.creationDate">${cmisObject.prop.creationDate.time}</dialog:simplerow>				                                            
	</dialog:table>
</dialog:form>     

               