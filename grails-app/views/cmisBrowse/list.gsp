
<%-- This view is used for the sidepane document list --%>

<div id="list-toolbar"
	class="fg-toolbar ui-toolbar xui-widget-header ui-corner-tl ui-corner-tr table-title">
	<g:link onclick="simpleDialog(this.href+'?parentId='+cmis.currentFolder);return false;" title="New folder"
		class="action-newfolder action list-action simpleDialog" controller="cmisDocument"
		action="newfolder" >&nbsp;
	</g:link>
	<g:link onclick="uploadDialog(this.href+'?parentId='+cmis.currentFolder);return false;" title="New document"
		class="action-newdocument action list-action uploadDialog" controller="cmisDocument"
		action="newdocument" >&nbsp;
	</g:link>
</div>
<div id="list-body" class="datatable">
	<table id="file-list" cellpadding="0" cellspacing="0" border="0"
		class="display file-list">
		<thead>
			<tr>
				<th><g:message code="cmis.list.icon" default="Icon" /></th>
				<th><g:message code="cmis.list.name" default="Name" /></th>
			<!-- 	<th>Size</th>
				<th>Author</th>
				<th>Last modified</th> -->
				<th><g:message code="cmis.list.actions" default="Actions" /></th>
			</tr>
		</thead>
		<tbody>
		<tr>
			<td colspan="3" class="dataTables_empty">Loading data from server</td>
		</tr>
		<%--
			<g:each in="${documents}" var="document">
				<tr>
					<td>
						<a href="#" class="mime-16 ${document.cssClassName}-16">&nbsp;
						</a>
					</td>
					<td>${document.title}</td>
				<!-- 	<td>${document.prop.contentStreamLength}</td>
					<td>${document.prop.lastModifiedBy}</td>
					<td>${document.prop.lastModificationDate}</td>
					-->
					<td>
						<g:link onclick="simpleDialog(this.href);return false;"
							title="${message(code:'cmis.list.showproperties')}" class="action-show action list-action simpleDialog"
							controller="cmisDocument" action="props" params="[objectId:document.prop.objectId]">&nbsp;
						</g:link>
						<g:link onclick="simpleDialog(this.href);return false;"
							title="${message(code:'cmis.list.editproperties')}" class="action-edit action list-action simpleDialog"
							controller="cmisDocument" action="edit" params="[objectId:document.prop.objectId]">&nbsp;
						</g:link>



						<g:if test="${document.hasHistory()}">
							<g:link onclick="simpleDialog(this.href);return false;"
								title="${message(code:'cmis.list.showhistory')}" class="action-history action list-action simpleDialog"
								controller="cmisDocument" action="history" params="[objectId:document.prop.objectId]">&nbsp;
							</g:link>
						</g:if>

						<g:if test="${document.isDocument()}">
							<g:link onclick="simpleDialog(this.href);return false;"
								title="${message(code:'cmis.list.download')}" class="action-download action list-action simpleDialog"
								controller="cmisDocument" action="download" params="[objectId:document.prop.objectId]">&nbsp;
							</g:link>
							<g:link onclick="uploadDialog(this.href);return false;"
								title="${message(code:'cmis.list.upload')}" class="action-upload action list-action uploadDialog"
								controller="cmisDocument" action="updatedocument" params="[objectId:document.prop.objectId]">&nbsp;
							</g:link>

							<g:if test="${document.isPwc()}">
								<g:link onclick="simpleDialog(this.href);return false;"
									title="${message(code:'cmis.list.checkin')}" class="action-checkin action list-action simpleDialog"
									controller="cmisDocument" action="checkin"
									params="[objectId:document.prop.objectId]">&nbsp;
								</g:link>
							</g:if>
							<g:else>
								<g:if test="${!document.isCheckedOut()}">
									<g:link onclick="simpleDialog(this.href);return false;"
										title="${message(code:'cmis.list.checkout')}" class="action-checkout action list-action simpleDialog"
										controller="cmisDocument" action="checkout"
										params="[objectId:document.prop.objectId]">&nbsp;
									</g:link>
								</g:if>


							</g:else>
						</g:if>


						<g:link onclick="simpleDialog(this.href);return false;"
							title="${message(code:'cmis.list.delete')}" class="action-delete action list-action simpleDialog"
							controller="cmisDocument" action="delete" params="[objectId:document.prop.objectId]">&nbsp;
						</g:link>

					</td>


				</tr>
			</g:each>
			--%>
		</tbody>
		<tfoot>
			<tr>
				<th><g:message code="cmis.list.icon" default="Icon" /></th>
				<th><g:message code="cmis.list.name" default="Name" /></th>
			<!-- 	<th>Size</th>
				<th>Author</th>
				<th>Last modified</th> -->
				<th><g:message code="cmis.list.actions" default="Actions" /></th>
			</tr>
		</tfoot>

	</table>
</div>
                
