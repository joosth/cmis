/*
 * CMIS plugin Javascript dialogs
 */

cmis.flashMessage=function flashMessage(message) {
	$("#status").html(message).fadeIn(200).delay(500).fadeOut(200);
}

cmis.simpleAlert=function simpleAlert(message) {
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
        	//var zindex=parseInt($(event.currentTarget.activeElement).css('z-index'));
        	var zindex=parseInt($(this.parentNode).css('z-index'));

        	zindex=zindex+1
      		$("span.help").cluetip({
      			splitTitle: '|'  ,
      			cluezIndex: zindex
      		});
         },
    
         close: function(event, ui) {      
              theDialog.dialog("destroy").remove();
              }
         });
	
}


cmis.simpleDialog=function simpleDialog(dialogUrl) {
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
			 			cmis.simpleAlert(data.result.message);
			 			}
			 		var result=data.result;
			 		cmis.datatable.fnDraw(false);
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
        	
        	var zindex=parseInt($(this.parentNode).css('z-index'));

            //$(this).find(".help").cluetip({
              //      splitTitle: '|',
                //    cluezIndex: parentZIndex+1
            //});

        	
        	//var zindex=parseInt($(event.currentTarget.activeElement).css('z-index'));
        	zindex=zindex+1
       		$("span.help").cluetip({
       			splitTitle: '|' ,
       				cluezIndex: zindex
      	    	});
       	},
        close: function(event, ui) {
        	   
               theDialog.dialog("destroy").remove();
             	
               }
             });	
}

cmis.uploadDialog=function uploadDialog(dialogUrl) {
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
		 				cmis.simpleAlert(data.result.message);
		 			}		 			
			 		cmis.datatable.fnDraw(false);
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
    	    
    	    //var zindex=parseInt($(event.currentTarget.activeElement).css('z-index'));
        	var zindex=parseInt($(this.parentNode).css('z-index'));

        	zindex=zindex+1
      		$("span.help").cluetip({
      			splitTitle: '|',
      			cluezIndex: zindex
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
      			},
      			template: '<div class="qq-uploader">' + 
                    '<div class="qq-upload-drop-area"><span>'+window.uploader.dropfilesMessage+'</span></div>' +
                    '<div class="qq-upload-button">'+window.uploader.uploadMessage+ '</div>' +
                    '<ul class="qq-upload-list"></ul>' + 
                 	'</div>'
   			});

      		
                    },
       close: function(event, ui) {      
              theDialog.dialog("destroy").remove();
              }
            });	
}


$(function() {	
	$("a.simpleDialog").live("click",function() {
		cmis.simpleDialog(this.href);
		return false;
	});
	
	
	$("a.uploadDialog").live("click",function() {
		cmis.uploadDialog(this.href);
		return false;
	});


});

