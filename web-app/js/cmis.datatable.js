/*
 * Grails CMIS Plugin
 * Copyright 2010-2011, Open-T B.V., and individual contributors as indicated
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

function refreshDatatable() {
	initDatatable();
	cmis.datatable.fnDraw(-1);
}

function initDatatable() {
	  if (!cmis.datatable) {
	
	  $(".cmis-datatable").bind("refresh",refreshDatatable);
	  $(".cmis-datatable").addClass("cmis-events");
			
		  
	  cmis.datatable=$(".cmis-datatable").dataTable( {
	  		"bProcessing": true,
	  		"bServerSide": true,		
	  		"sAjaxSource": cmis.baseUrl+"/cmisBrowse/jsonlist",
	  		 "fnServerData": function ( sSource, aoData, fnCallback ) {
	             aoData.push( { "name":"objectId","value": window.cmis.currentFolderId } );
	             aoData.push( { "name":"readOnly","value": window.cmis.readOnly } );
	             aoData.push( { "name":"cico","value": window.cmis.cico } );
	             $.getJSON( sSource, aoData, function (json) { 
	                 fnCallback(json)
	             } );
	  		 },
	  		
	  		"sPaginationType": "full_numbers",
	  		"bFilter": false,
	  		"bJQueryUI": true,
	  		 "oLanguage": {
	     	      "sUrl": cmis.pluginPath+"/js/jquery/dataTables/localisation/dataTables."+cmis.language+".txt"
	     	    },
	  		"aoColumnDefs": [ 
	  			{ "bSortable": false, "aTargets": [ 0,2 ] }
	  		]
	  		
		
	  		} );

	}

}
        
	
$(function() {
	initDatatable();	
});
