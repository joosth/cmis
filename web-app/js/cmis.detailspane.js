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
 * CMIS plugin Javascript for details pane
 * 
 * @author Joost Horward
 */

// event handler for refresh events. This either reloads the pane or instructs the AJAX based list view to refresh itself.
cmis.refreshDetailsPane = function refreshDetailsPane(){	
	if (cmis.currentObjectEntry.properties["cmis:baseTypeId"]=="cmis:folder" && $(".file-list")[0]) {		
		cmis.initDatatable();
	} else {
		var parentPath=cmis.currentObjectEntry.properties["cmis:path"];
		var objectId=cmis.currentObjectEntry.properties["cmis:objectId"];
			
		$(".detail-pane").load(cmis.baseUrl+"/cmisBrowse/detail/?objectId="+objectId+'&parentPath='+escape(parentPath),'',function() {      			  
            cmis.datatable=null;
            cmis.initDatatable();            
            $(".detail-pane").find("span.help").tooltip({placement:'top'});            
            $(".detail-pane").find("li.menu-item a").tooltip({placement:'top'});            
		  });
	}	 
}

// Register the event handlers
cmis.initDetailsPane = function initDetailsPane() {	
	$(".cmis-detailspane").bind("refresh",cmis.refreshDetailsPane);
	$(".cmis-detailspane").addClass("cmis-events");
}

// Initialization
$(function() {	
	cmis.initDetailsPane();
});
