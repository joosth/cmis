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

/*

 * GenericJavascript dialogs
 */

OpenT.dialogs={}

jQuery.fn.center = function () {
	this.css("position","absolute");
	this.css("top", (($(window).height() - this.outerHeight()) / 2) + $(window).scrollTop() + "px");
	this.css("left", (($(window).width() - this.outerWidth()) / 2) + $(window).scrollLeft() + "px");
	return this;
}

OpenT.dialogs.flashMessage=function flashMessage(message) {
	$("#flashmessage").center();   	
	$("#flashmessage").html(message).fadeIn(100).delay(500).fadeOut(200);
}

OpenT.dialogs.simpleAlert=function simpleAlert(message) {
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


OpenT.dialogs.simpleDialog=function simpleDialog(dialogUrl) {
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
			 			OpenT.dialogs.flashMessage(data.result.message);
			 		} else {
			 			Opent.dialogs.simpleAlert(data.result.message);
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
        	
        	//var zindex=parseInt($(event.currentTarget.activeElement).css('z-index'));
        	var zindex=parseInt($(this.parentNode).css('z-index'));

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

OpenT.dialogs.uploadDialog=function uploadDialog(dialogUrl) {
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
		 				OpenT.dialogs.flashMessage(data.result.message);
		 			} else {
		 				OpenT.dialogs.simpleAlert(data.result.message);
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
	// No longer needed ! Dialog plugin already does this.
	$("xa.simpleDialog").live("click",function() {
		OpenT.dialogs.simpleDialog(this.href);
		return false;
	});
	
	
	$("a.uploadDialog").live("click",function() {
		OpenT.dialogs.uploadDialog(this.href);
		return false;
	});


});

