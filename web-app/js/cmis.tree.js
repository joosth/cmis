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
 

function refreshTree(id) {
		if (id=="ALL") {
			cmis.tree.jstree("refresh",-1)
		} else {
			var el=document.getElementById(id)		
		
			cmis.tree.jstree("refresh",el)
			cmis.tree.jstree('select_node', el, false);
		//	cmis.tree.jstree('open_node', el, true);			
		}
}

function refreshMe(){
	var parentNode=document.getElementById(encodeURIComponent(cmis.parentFolder));
	if (parentNode) {
		//cmis.tree.jstree('open_node', parentNode, false);
		cmis.tree.jstree('open_node', parentNode, null);
	}
	var currentFolder=document.getElementById(encodeURIComponent(cmis.currentFolderId));
	if (currentFolder) {
	//	cmis.tree.jstree('open_node', currentFolder, true);
		//cmis.tree.jstree('deselect_all');
		//cmis.tree.jstree('select_node', currentFolder, false);
		cmis.tree.jstree('open_node', currentFolder, null);
	}
}
    
function initTree() {
	
	$(".cmis-tree").bind("refresh",refreshMe);
	$(".cmis-tree").addClass("cmis-events");
	
	
    cmis.tree=$(".cmis-tree").jstree({
       
  	 // "plugins" : [  "themes", "json_data", "ui", "crrm", "cookies", "dnd", "search", "types", "hotkeys","contextmenu" ],
  	  //"plugins" : [  "json_data", "ui", "crrm", "cookies", "dnd", "search", "types", "hotkeys","contextmenu" ],
       "plugins" : [  "json_data","themes","contextmenu" ,"ui","crrm","cookies"],
         "json_data" : {
  	            "ajax" : {
  	                "url" : cmis.baseUrl+"/cmisBrowse/nodejson",
  	                "data" : function (n) {    					
  	                    return { id : n.attr ? n.attr("id") : "" , rootNode:cmis.rootFolderId }
    					//return { "" }	
  	                }
  	            }
  	        },
        
           "animation": 100,
          
  	      "ui" : {
  				// this makes the node with ID node_4 selected onload
  		//		"initially_select" : [ "workspace://SpacesStore/fe1528db-4a48-4495-8204-9a1bc56a0926" ]
  	    	"select_limit" : 1
  			},
  	        
         
         "contextmenu" : {
          	"items" : {
          	
          	  "rename" : {
          		    // The item label
          		    "label"             : "Rename"
          }
             }
          
          	
          
          },	
          
          "themes" : {
        	  // Really, we don't need to load this again ...
        	  url: cmis.baseUrl+"/css/theme/theme.css"
        	  
        	  
          }
          
          
          
          });
    
          $("#treediv").bind("select_node.jstree", function(e,data) {
        	  var node=data.rslt.obj[0];
        	  cmis.gotoObject(node.id);
        	  cmis.refresh();
        	  /*
        	  // get entry info
        	  if ($(node).hasClass("jstree-folder") && $(".file-list")[0]) {
        		  gotoFolder(node.id);
        	  } else {
				  var parentPath=$(node).attr("parentPath");
		      		  $(".detail-pane").load(cmis.baseUrl+"/cmisBrowse/detail/?objectId="+data.rslt.obj[0].id+'&parentPath='+escape(parentPath),'',function() {      			  
		                  $(".detail-pane").find("span.help").cluetip({
		        				splitTitle: '|'
		        				});
		                  cmis.currentFolderId=data.rslt.obj[0].id;
		                  cmis.datatable=null;
		                  initDatatable();
		                  //cmis.datatable.fnDraw(-1);
		                  //cmis.datatable.fnDraw(true);
		                  var toolbar=$("#list-toolbar")
		                  $(this).find('div.dataTables_length').prepend(toolbar);		           	
		      		  });
        	  }
        	  */
          });     
}

	
$(function() {	
	initTree()	
});
