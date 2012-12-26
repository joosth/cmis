modules = {
	cmis {
		dependsOn 'jquery,dialog,dialog-altselect,dialog-dataTables,bootstrap-responsive-css,bootstrap-tooltip,bootstrap-popover,bootstrap-modal,dialog-bootstrap,dialog-fileuploader'
		resource url:'/js/cmis.js'
		resource url:'/js/cmis.spp.js'
		resource url:'/js/cmis.tree.js'
		resource url:'/js/cmis.datatable.js'
		resource url:'/js/cmis.detailspane.js'
		resource url:'/js/cmis.breadcrumb.js'

		resource url:'/css/cmis.css'
		resource url:'/images/favicon.ico'
		resource url:'/css/theme/mimetypes.css'
		resource url:'/css/theme/images/generic-file-32.png'
		
	}
		
	'cmis-tree' {
		resource url:'/js/jquery/jquery.cookie.js'
		resource url:'/js/jquery/jquery.hotkeys.js'
		resource url:'/css/jquery.jstree.css'
		resource url:'/js/jquery/jquery.jstree.js'	
	}
}