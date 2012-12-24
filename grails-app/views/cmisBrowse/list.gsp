<%-- This view is used for the sidepane document list --%>
<div id="list-body" class="datatable">
	<cmis:list />
</div>
<script type="text/javascript" >
	cmis.refreshBreadcrumb();
	$(".cmis-breadcrumb").bind("refresh",cmis.refreshBreadcrumb);
	$(".cmis-breadcrumb").addClass("cmis-events");
</script>      
