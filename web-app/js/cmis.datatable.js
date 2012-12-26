/* 
 * CMIS Plugin for Grails
 * Copyright 2010-2013, Open-T B.V., and individual contributors as indicated
 * by the @author tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License
 * version 3 published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.

 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses
 */

/**
 * CMIS plugin Javascript for datatables list
 * 
 * @author Joost Horward
 */

// Handle refresh event
cmis.refreshDatatable = function refreshDatatable(event,eventData) {
	cmis.initDatatable();
	cmis.datatable.fnDraw(eventData.contextChange);	 
}

// Initialize the datatable
cmis.initDatatable = function initDatatable() {
	if (!cmis.datatable) {
	
		$(".cmis-datatable").bind("refresh",cmis.refreshDatatable);	  
		$(".cmis-datatable").addClass("cmis-events");
	
		$(".cmis-datatable").bind("dialog-refresh",cmis.refreshDatatable);
		$(".cmis-datatable").addClass("dialog-events");
	  
		  
		cmis.datatable=$(".cmis-datatable").dataTable( {
			"bProcessing": false,
			"bServerSide": true,		
			"sAjaxSource": cmis.baseUrl+"/cmisBrowse/jsonlist",
			"fnServerData": function ( sSource, aoData, fnCallback ) {
				aoData.push( { "name":"objectId","value": window.cmis.currentFolder.id } );
				aoData.push( { "name":"readOnly","value": window.cmis.readOnly } );
				aoData.push( { "name":"cico","value": window.cmis.cico } );
				$.getJSON( sSource, aoData, function (json) { 
					fnCallback(json);
					$("span.action,a.action").tooltip();	                 
				});
			},
	 		/*
				sDom explanation:
				l - Length changing
				f - Filtering input
				t - The table!
				i - Information
				p - Pagination
				r - pRocessing
				< and > - div elements
				<"class" and > - div with a class
				Examples: <"wrapper"flipt>, <lf<t>ip>
			*/			
			"sDom": 'tlip',	  		
			"sPaginationType": "bootstrap",
			"bFilter": false,
			"bJQueryUI": false,
			"oLanguage": {
				"sUrl": dialog.dataTablesLanguageUrl
			},
			"aoColumnDefs": [ { "bSortable": false, "aTargets": [ 0,2 ] } ]
		});
	}
}
        
//Initialization	
$(function() {
	cmis.initDatatable();
	$("#list-toolbar a").tooltip();
});
