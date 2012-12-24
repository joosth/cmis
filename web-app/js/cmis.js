/*
 * Grails CMIS Plugin
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



/*
 * CMIS plugin Javascript
 */
/*

cmis.refreshCallbacks=[];

cmis.registerRefreshCallback =function registerRefreshCallback (callbackFunction){
	cmis.refreshCallbacks.push(callbackFunction);
}
*/
cmis.refresh = function refresh (){
	window.location.hash=cmis.currentFolderId;
	$(".cmis-events").trigger("refresh");
}

cmis.gotoFolder = function gotoFolder(folderId) {
	//cmis.parentFolderId=cmis.currentFolderId;
	cmis.currentFolderId=folderId;
	$.getJSON(cmis.baseUrl+'/cmisBrowse/jsonEntry?objectId='+cmis.currentFolderId, function (json) {
		cmis.currentFolderEntry=json;
		cmis.refresh();	
	}).error(function(event, jqXHR, ajaxSettings, thrownError) { 
		window.location.reload();
	});
	
	

}

cmis.gotoPath = function gotoPath(path) {
	var rootPath=cmis.rootFolder.properties["cmis:path"];
	$.getJSON(cmis.baseUrl+"/cmisBrowse/jsonFolderByPath?path="+rootPath+path, function (json) {
		cmis.currentFolderEntry=json;
		cmis.currentFolderId=json.id;
		cmis.refresh();	
	}).error(function(event, jqXHR, ajaxSettings, thrownError) { 
		window.location.reload();
	});
}

cmis.gotoObject=function gotoObject(objectId) {
	cmis.currentObjectId=objectId;
	$.getJSON(cmis.baseUrl+"/cmisBrowse/jsonEntry?objectId="+cmis.currentObjectId, function (json) {
		cmis.currentObjectEntry=json;
		if(cmis.currentObjectEntry.properties["cmis:baseTypeId"]=="cmis:folder") {
			cmis.parentFolderId=cmis.currentObjectEntry.properties["cmis:parentId"];
			cmis.currentFolderId=cmis.currentObjectId;
			cmis.currentFolderEntry=cmis.currentObjectEntry;
		}
		cmis.refresh();	
	}).error(function(event, jqXHR, ajaxSettings, thrownError) { 
		window.location.reload();
	});
}


cmis.gotoParentFolder = function gotoParentFolder() {
	if (cmis.currentFolderId!=cmis.rootFolderId) {
		var res=$.getJSON(cmis.baseUrl+"/cmisBrowse/jsonparent?objectId="+cmis.currentFolderId, function (json) {
			cmis.gotoFolder(json.objectId);
		}).error(function(event, jqXHR, ajaxSettings, thrownError) { 
			window.location.reload();
		});
	}
}

cmis.gotoHomeFolder = function gotoHomeFolder() {
	cmis.gotoFolder(cmis.rootFolderId);
}

cmis.reload = function reload() {
    $("span.help").tooltip({});		
    $("li.menu-item a").tooltip({});
            	          
}


	
$(function() {
	cmis.reload()
	
	// This allows us to click on a clickable row and execute the onclick handler that's in the span element of the first column
	$(".clickable-row td").live("click", function() {
		var clickable = $(this).find('span.clickable-cell:first')[0]
		if (clickable) {
			clickable.click()
		}
	});
	if (cmis.restore) {
		if (window.location.hash!="") {
			var objectId=window.location.hash.substring(1);		
			cmis.gotoObject(objectId);		
		}
	}

});
