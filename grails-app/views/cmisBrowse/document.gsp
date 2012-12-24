 <%-- This view is used for the document details sidepane --%>
 <div id="dialog" class="span4" > 		
	<g:hiddenField name="objectId" value="${entry.objectId}"/>		
	<cmis:thumbnail object="${entry}" />						
	<dialog:table>
		<dialog:simplerow name="cmisDocument.props.name">${cmisObject.prop.name}</dialog:simplerow>
		<dialog:simplerow name="cmisDocument.props.description">${cmisObject.prop.description}</dialog:simplerow>
		<dialog:simplerow name="cmisDocument.props.contentStreamLength">${cmisObject.prop.contentStreamLength}</dialog:simplerow>
		<dialog:simplerow name="cmisDocument.props.contentStreamMimeType">${cmisObject.prop.contentStreamMimeType}</dialog:simplerow>
		<dialog:simplerow name="cmisDocument.props.versionLabel">${cmisObject.prop.versionLabel}</dialog:simplerow>
		<dialog:simplerow name="cmisDocument.props.lastModifiedBy">${cmisObject.prop.lastModifiedBy}</dialog:simplerow>
		<dialog:simplerow name="cmisDocument.props.checkinComment">${cmisObject.prop.checkinComment}</dialog:simplerow>
		<dialog:simplerow name="cmisDocument.props.createdBy">${cmisObject.prop.createdBy}</dialog:simplerow>
		<dialog:simplerow name="cmisDocument.props.creationDate">${cmisObject.prop.creationDate.time}</dialog:simplerow>		
	</dialog:table>
</div>

<div id="document-actions" class="offset1 span2" >
	<ul class="nav nav-tabs nav-stacked">
		<dialog:menuitem icon="icon-edit" code="cmisDocument.edit" onclick="dialog" controller="cmisDocument" action="edit" params="${[getObjectId:entry.prop.objectId]}" />
		<g:if test="${webdavPath}" >
	   		<li class="menu-item">	   		
	   			<a href="${webdavPath}" title="${message(code:'menu.cmisDocument.editonlinewebdav.help')}" target="_blank"><i class="icon-edit" /> <g:message code="menu.cmisDocument.editonlinewebdav.label" /></a>
	   		</li>
		</g:if>
		<g:if test="${sppPath}" >
			<li class="menu-item">
				<span href="${sppPath}" title="${message(code:'menu.cmisDocument.editonlinespp.help')}" sppAppProgId="${sppAppProgId}" ><i class="icon-edit" /> <g:message code="menu.cmisDocument.editonlinespp.label" /></span>
			</li>
		</g:if>
		<g:if test="${entry.isDocument}" >		
	   		<dialog:menuitem icon="icon-download" controller="cmisDocument" action="download" params="${[objectId:entry.prop.objectId]}" />
	   		<dialog:menuitem icon="icon-upload" code="cmisDocument.upload" onclick="dialog" controller="cmisDocument" action="updatedocument" params="${[getObjectId:entry.prop.objectId]}" />
	   		<g:if test="${entry.isPwc}">
	   			<dialog:menuitem icon="icon-share" code="cmisDocument.checkin" onclick="dialog" controller="cmisDocument" action="checkin" params="${[getObjectId:entry.prop.objectId]}" />
	   		</g:if>		
	   		<g:if test="${!entry.isCheckedOut}" >
		   		<dialog:menuitem icon="icon-share" code="cmisDocument.checkout" onclick="dialog" controller="cmisDocument" action="checkout" params="${[getObjectId:entry.prop.objectId]}" />
	   		</g:if>
	   		<dialog:menuitem icon="icon-trash" code="cmisDocument.delete" onclick="dialog" controller="cmisDocument" action="delete" params="${[getObjectId:entry.prop.objectId]}" />
 		</g:if>
	   			
    </ul>
</div>
               
