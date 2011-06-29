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
	        	});
	 			$(this).dialog("close");
	 			$("#treediv").jstree("refresh",-1);
	 				 			
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
		 			//cmis.tree.jstree("refresh",-1);
		 			window.location.reload();
		 			//cmis.tree.set_focus();
		 			//cmis.datatable.fnDraw(false);
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
      			action: cmis.baseUrl+'/document/fileupload',
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

        
		
$(function() {		        
	
	$("a.simpleDialog").click(function() {
		simpleDialog(this.href);
		return false;
	});
	
	$("a.uploadDialog").click(function() {
		uploadDialog(this.href);
		return false;
	});
	
    $("#outer-treediv").resizable();
    $("#outer-detail-pane").resizable();
    
    $("span.help").cluetip({
		splitTitle: '|',  
		cluezIndex: 2000
	});
    
    
    cmis.tree=$("#treediv").jstree({
       
  	  "plugins" : [  "themes", "json_data", "ui", "crrm", "cookies", "dnd", "search", "types", "hotkeys","contextmenu" ],
         "json_data" : {
  	            "ajax" : {
  	                "url" : cmis.baseUrl+"/browse/nodejson",
  	                "data" : function (n) {    					
  	                    return { id : n.attr ? n.attr("id") : "" , rootNode:cmis.rootNode }
    					//return { "" }	
  	                }
  	            }
  	        },
        
         // "initially_open": ["workspace://SpacesStore/fe1528db-4a48-4495-8204-9a1bc56a0926"],
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
          
          
          
          
          
          });
         
          $("#treediv").bind("select_node.jstree", function(e,data) {
          		$("#detail-pane").load(cmis.baseUrl+"/browse/detail/?objectId="+data.rslt.obj[0].id,'',function() {
                  //$("#tabs").tabs();
                  $("#detail-pane").find("span.help").cluetip({
        				splitTitle: '|',  
        				cluezIndex: 2000
        				});
                  
                      
                  cmis.datatable=$(this).find(".file-list").dataTable({
                  "bJQueryUI": true,
              	  "sPaginationType": "full_numbers",
                 // "sDom": '<"toolbar">lfrtip'
                  
                  });
              	  var toolbar=$("#list-toolbar")
                  $(this).find('div.dataTables_length').prepend(toolbar);

                
  				           	
          });
                  	
          });
        
});