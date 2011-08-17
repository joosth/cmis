/*
 * CMIS plugin Javascript
 */


function flashMessage(message) {
	$("#status").html(message).fadeIn(200).delay(500).fadeOut(200);
}

function simpleAlert(message) {
	 var dialogHTML = "<div>"+message+"</div>"
	 var theDialog=$(dialogHTML).dialog({ 
		 modal:true,
		 width:300,
		 height:150,
		 buttons: { 
		 	"Ok": function() {
	 			$( this ).dialog( "close" );
         }
	 	},
         open: function(event, ui) {           	       		 
      		$("span.help").cluetip({
      			splitTitle: '|',  
      			cluezIndex: 2000
      		});
         },
    
         close: function(event, ui) {      
              theDialog.dialog("destroy").remove();
              }
         });
	
}




function refreshTree(id) {
		var el=document.getElementById(id)		
		/*var parentel=$.jstree._focused()._get_parent(el)
		cmis.tree.jstree("refresh",parentel)
		*/
		cmis.tree.jstree("refresh",el)
		cmis.tree.jstree('select_node', el, true);
}

function simpleDialog(dialogUrl) {
	 var dialogHTML = $.ajax({
		  url: dialogUrl,
		  async: false
		 }).responseText;
	 // for some reason this always returns 0 so replaced by fixed value below	 
	 var theWidth=$(dialogHTML).css("width");
	 
	 var theDialog=$(dialogHTML).dialog({ 
		 modal:true,

		 //width:theWidth,
		 width:600,
		 buttons: { 
		 	"Ok": function() {
			 	var formData=$("form#ajaxdialogform").serialize();
			 	var action=$("form#ajaxdialogform").attr("action");
				$.post(action,formData, function(data) {
		 			// any refresh action here
			 		if (data.result.success) {
			 			flashMessage(data.result.message);
			 		} else {
			 			simpleAlert(data.result.message);
			 			}
			 		var result=data.result;
			 		
			 		for (i in result.refreshNodes) {
			 			refreshTree(result.refreshNodes[i])
			 		}
			 		
	        	});
				$(this).dialog("close");
	 			
	 				 			
 			},
	 		Cancel: function() {
	        		$( this ).dialog( "close" );
	        }
       	},
        open: function(event, ui) {           	       		 
       		$("span.help").cluetip({
       			splitTitle: '|',  
       			cluezIndex: 2000
      	    	});
       	},
        close: function(event, ui) {      
               theDialog.dialog("destroy").remove();
               
               }
             });	
}

function uploadDialog(dialogUrl) {
	 var dialogHTML = $.ajax({
		  url: dialogUrl,
		  async: false
		 }).responseText;
	 var theDialog=$(dialogHTML).dialog({ 
		 modal:true,		 
		 buttons: { 
		 	"Save": function() {			 	
		 		var formData=$("form#ajaxdialogform").serialize();
		 		var action=$("form#ajaxdialogform").attr("action");
		 		$.post(action,formData, function(data) {
		 			// any refresh action here
		 			if (data.result.success) {
		 				flashMessage(data.result.message);
		 			} else {
		 				simpleAlert(data.result.message);
		 			}		 			

		 			var result=data.result;
		 			for (i in result.refreshNodes) {
			 			refreshTree(result.refreshNodes[i])
			 		}
		 			
     	});
			$(this).dialog("close");
	        	},
      	Cancel: function() {
	        		$( this ).dialog( "close" );
	        	}
      	},
       open: function(event, ui) {           	       		 
      		$("span.help").cluetip({
      			splitTitle: '|',  
      			cluezIndex: 2000
     	    	});
      		var parentNode=$("#parentId").val();
      		var uploader = new qq.FileUploader({
      			// pass the dom node (ex. $(selector)[0] for jQuery users)
      			element: document.getElementById('file-uploader'),
      			// path to server-side upload script
      			action: cmis.baseUrl+'/cmisDocument/fileupload',
      			params: {
      				parentNode: $("#parentId").val()
      				},
      			onComplete: function(id, fileName, responseJSON){
  					$("#ajaxdialogform").append("<input type=\"hidden\" name=\"filename\" value=\""+fileName+"\" />");      			
      			}
   			});

      		
                    },
       close: function(event, ui) {      
              theDialog.dialog("destroy").remove();
              }
            });	
}

function cmisReload() {
	$("a.simpleDialog").click(function() {
		simpleDialog(this.href);
		return false;
	});
	
	
	$("a.uploadDialog").click(function() {
		uploadDialog(this.href);
		return false;
	});
	
  //  $("#outer-treediv").resizable();
 //   $("#outer-detail-pane").resizable();
    
    $("span.help").cluetip({
		splitTitle: '|',  
		cluezIndex: 2000
	});
    
    
    cmis.tree=$("#treediv").jstree({
       
  	 // "plugins" : [  "themes", "json_data", "ui", "crrm", "cookies", "dnd", "search", "types", "hotkeys","contextmenu" ],
  	  //"plugins" : [  "json_data", "ui", "crrm", "cookies", "dnd", "search", "types", "hotkeys","contextmenu" ],
       "plugins" : [  "json_data","themes","contextmenu" ,"ui","crrm","cookies"],
         "json_data" : {
  	            "ajax" : {
  	                "url" : cmis.baseUrl+"/cmisBrowse/nodejson",
  	                "data" : function (n) {    					
  	                    return { id : n.attr ? n.attr("id") : "" , rootNode:cmis.rootNode }
    					//return { "" }	
  	                }
  	            }
  	        },
        
           "animation": 100,
          
  	      // "ui" : {
  				// this makes the node with ID node_4 selected onload
  		//		"initially_select" : [ "workspace://SpacesStore/fe1528db-4a48-4495-8204-9a1bc56a0926" ]
  		//	},
  	        
         
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
		  var parentPath=$(node).attr("parentPath");
          		$("#detail-pane").load(cmis.baseUrl+"/cmisBrowse/detail/?objectId="+data.rslt.obj[0].id+'&parentPath='+parentPath,'',function() {
                  $("#detail-pane").find("span.help").cluetip({
        				splitTitle: '|',  
        				cluezIndex: 2000
        				});
                                    
                  cmis.datatable=$(this).find(".file-list").dataTable({
                  "bJQueryUI": true,
              	  "sPaginationType": "full_numbers",
              	  "bProcessing": true,
              	  "bServerSide": false,        		
              	   "bFilter": false
                  
                  });
                  
              	  var toolbar=$("#list-toolbar")
                  $(this).find('div.dataTables_length').prepend(toolbar);

                
  				           	
          });
                  	
          });
}

/*
 * Handles on-line editing through Sharepoint protocol
 */
jQuery.fn.sppLink = function () {
	$(this.selector).live("click",function(e) {
		e.stopPropagation();
		var controlProgID = "SharePoint.OpenDocuments"
		var sppAppProgId=$(this).attr("sppAppProgId");
		var url=$(this).attr("href");
	
		 try
         {			
            activeXControl = new ActiveXObject(controlProgID + ".3");
            
            return activeXControl.EditDocument3(window, url, true, sppAppProgId);
         }
         catch(e)
         {
            try
            {
               activeXControl = new ActiveXObject(controlProgID + ".2");
               
               return activeXControl.EditDocument2(window, url, sppAppProgId);
            }
            catch(e1)
            {
               try
               {
                  activeXControl = new ActiveXObject(controlProgID + ".1");
               
                  return activeXControl.EditDocument(url, sppAppProgId);
               }
               catch(e2)
               {
                   return window.open(url, "_blank");
               }
            }
         }
		
		return false
		
	});
	
}
        
	
$(function() {
	$("span.spp-link").sppLink();	
	cmisReload()	
});
