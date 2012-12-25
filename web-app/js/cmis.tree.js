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
 * CMIS plugin Javascript for tree view
 * This part of the plugin is EXPERIMENTAL. You have been warned.
 * 
 * @author Joost Horward
 */

// Refresh (part of) the tree. This is usually in response to a dialog that updated a specific node
cmis.refreshTree = function refreshTree(id) {
	if (id=="ALL") {
		cmis.tree.jstree("refresh",-1)
	} else {
		var el=document.getElementById(id)		
	
		cmis.tree.jstree("refresh",el)
		cmis.tree.jstree('select_node', el, false);			
	}
}

// Refresh function that responds to generic CMIS refresh events
cmis.refreshMe = function refreshMe(){
	//
	var currentFolderElement=document.getElementById(cmis.currentFolderId);

	if (currentFolderElement) {	
		cmis.tree.jstree('open_node', currentFolderElement, null);
	}
	
	var rootFolderElement=$("#treediv ul li")[0];
	if (rootFolderElement!=null && cmis.currentFolderEntry!=null) {
		cmis.tree.jstree('open_node', rootFolderElement, null);		
		var path=cmis.currentFolderEntry.properties["cmis:path"];
		
		var pathElements=path.split('/');
		var currentElement=rootFolderElement;
		var currentPath=""
		for (i in pathElements) {	
			// Skip empty element
			if (pathElements[i]) {
				currentPath+="/"+pathElements[i];
				var currentElement=$(currentElement).find("li[title='"+pathElements[i]+"']")[0];
				if (currentElement){
					cmis.tree.jstree('open_node', currentElement, null);
					if (i==pathElements.length-1) {
						cmis.tree.jstree('select_node', currentElement, null);
					} else {
						cmis.tree.jstree('deselect_node', currentElement, null);
					}
				}
			}			
		}		
	}	
}

// Initialize the tree
cmis.initTree = function initTree() {
	
	$(".cmis-tree").bind("refresh",cmis.refreshMe);
	$(".cmis-tree").addClass("cmis-events");
	
	cmis.tree=$(".cmis-tree").jstree({
		// "plugins" : [  "themes", "json_data", "ui", "crrm", "cookies", "dnd", "search", "types", "hotkeys","contextmenu" ],
		"plugins" : [  "json_data","themes","contextmenu" ,"ui","crrm","cookies"],
		"json_data" : {
			"ajax" : {
				"url" : cmis.baseUrl+"/cmisBrowse/nodejson",
				"data" : function (n) {    					
					return { id : n.attr ? n.attr("id") : "" , rootNode:cmis.rootFolderId }
                }
            }
		},

		"animation": 100,
		
		"ui" : {
			//this makes the node with ID node_4 selected onload
			//		"initially_select" : [ "workspace://SpacesStore/fe1528db-4a48-4495-8204-9a1bc56a0926" ]
			"select_limit" : 1
		},
		
		"contextmenu" : {        
			"items": function ( node ) {
				var obj = {            					
					"properties" : {
						"label": 'Properties',
						"action" : function( node ) { 
							var id=node.attr("id");
							dialog.formDialog('null','cmisDocument', {'dialogname':'props','nosubmit':true},{'getObjectId':id} ,null)      					    		    
	    		    	}
					},
					"editproperties" : {
						"label": 'Edit properties',
						"action" : function( node ) { 
							var id=node.attr("id");
							dialog.formDialog('null','cmisDocument', {'dialogname':'edit'},{'getObjectId':id} ,null)
						}
					},
					"delete" : {
						"label": 'Delete',
						"action" : function( node ) { 
							var id=node.attr("id");
							dialog.formDialog('null','cmisDocument', {'dialogname':'delete'},{'getObjectId':id} ,null)
						}
					},
				}
				return obj;
			}        			
		},
		"themes" : {
			//Really, we don't need to load this again ...
			url: cmis.baseUrl+"/css/theme/theme.css"        	  
		}
	});
	
	$("#treediv").bind("select_node.jstree", function(e,data) {
		var node=data.rslt.obj[0];
		cmis.gotoObject(node.id);
		cmis.refresh();        
	});     
}

// Intialization
$(function() {	
	cmis.initTree();
});
