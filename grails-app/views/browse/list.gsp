
<%-- This view is used for the sidepane document list --%>

<div id="list-toolbar"
	class="fg-toolbar ui-toolbar xui-widget-header ui-corner-tl ui-corner-tr table-title">
	<g:link onclick="simpleDialog(this.href);return false;" title="New folder"
		class="action-newfolder action simpleDialog" controller="document"
		action="newfolder" params="[parentId:cmisEntry.prop.objectId]">&nbsp;
	</g:link>
	<g:link onclick="uploadDialog(this.href);return false;" title="New document"
		class="action-newdocument action uploadDialog" controller="document"
		action="newdocument" params="[parentId:cmisEntry.prop.objectId]">&nbsp;
	</g:link>
</div>

<div id="list-body" class="datatable">
	<table id="file-list" cellpadding="0" cellspacing="0" border="0"
		class="display file-list">
		<thead>
			<tr>
				<th>Icon</th>
				<th>Name</th>
			<!-- 	<th>Size</th>
				<th>Author</th>
				<th>Last modified</th> -->
				<th>Actions</th>
			</tr>
		</thead>
		<tbody>
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
							title="Show properties" class="action-show action simpleDialog"
							controller="document" action="props" params="[objectId:document.prop.objectId]">&nbsp;
						</g:link>
						<g:link onclick="simpleDialog(this.href);return false;"
							title="Edit properties" class="action-edit action simpleDialog"
							controller="document" action="edit" params="[objectId:document.prop.objectId]">&nbsp;
						</g:link>



						<g:if test="${document.hasHistory()}">
							<g:link onclick="simpleDialog(this.href);return false;"
								title="Show history" class="action-history action simpleDialog"
								controller="document" action="history" params="[objectId:document.prop.objectId]">&nbsp;
							</g:link>
						</g:if>

						<g:if test="${document.isDocument()}">
							<g:link onclick="simpleDialog(this.href);return false;"
								title="Download" class="action-download action simpleDialog"
								controller="document" action="download" params="[objectId:document.prop.objectId]">&nbsp;
							</g:link>
							<g:link onclick="uploadDialog(this.href);return false;"
								title="Upload" class="action-upload action uploadDialog"
								controller="document" action="updatedocument" params="[objectId:document.prop.objectId]">&nbsp;
							</g:link>

							<g:if test="${document.isPwc()}">
								<g:link onclick="simpleDialog(this.href);return false;"
									title="Checkin" class="action-checkin action simpleDialog"
									controller="document" action="checkin"
									params="[objectId:document.prop.objectId]">&nbsp;
								</g:link>
							</g:if>
							<g:else>
								<g:if test="${!document.isCheckedOut()}">
									<g:link onclick="simpleDialog(this.href);return false;"
										title="Checkout" class="action-checkout action simpleDialog"
										controller="document" action="checkout"
										params="[objectId:document.prop.objectId]">&nbsp;
									</g:link>
								</g:if>


							</g:else>
						</g:if>


						<g:link onclick="simpleDialog(this.href);return false;"
							title="Delete document" class="action-delete action simpleDialog"
							controller="document" action="delete" params="[objectId:document.prop.objectId]">&nbsp;
						</g:link>

					</td>


				</tr>
			</g:each>
		</tbody>
		<tfoot>
			<tr>
				<th>Icon</th>
			 	<th>Name</th>
			<!-- 	<th>Size</th>
				<th>Author</th>
				<th>Last modified</th> -->
				<th>Actions</th>
			</tr>
		</tfoot>

	</table>
</div>
                