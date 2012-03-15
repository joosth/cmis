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
 



function refreshDetailsPane(){
	
	if (cmis.currentObjectEntry.properties["cmis:baseTypeId"]=="cmis:folder" && $(".file-list")[0]) {		
		initDatatable();
	} else {
		var parentPath=cmis.currentObjectEntry.properties["cmis:path"];
		var objectId=cmis.currentObjectEntry.properties["cmis:objectId"];
			
		$(".detail-pane").load(cmis.baseUrl+"/cmisBrowse/detail/?objectId="+objectId+'&parentPath='+escape(parentPath),'',function() {      			  
            $(".detail-pane").find("span.help").cluetip({
  				splitTitle: '|'
  				});
            cmis.datatable=null;
            initDatatable();
            var toolbar=$("#list-toolbar")
            $(this).find('div.dataTables_length').prepend(toolbar);		           	
		  });
	}	
}
    
function initDetailsPane() {	
	$(".cmis-detailspane").bind("refresh",refreshDetailsPane);
	$(".cmis-detailspane").addClass("cmis-events");
}
	
$(function() {	
	initDetailsPane();
});
