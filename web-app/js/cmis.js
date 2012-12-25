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
 * CMIS plugin Javascript
 * 
 * @author Joost Horward
 */

// Send message to inform other components that a refresh is needed. 
// If the context changed the refresh should be a bit more drastic ie reset pagination on lists
// This function also sets the hash value in the URL which can be used for bookmarking and allows users to hit F5
cmis.refresh = function refresh (contextChange){
	var cg=contextChange?true:false
	window.location.hash=cmis.currentFolderId;
	$(".cmis-events").trigger("refresh",{contextChange:cg});
}

// Change to the folder of the given ID.
cmis.gotoFolder = function gotoFolder(folderId) {
	cmis.currentFolderId=folderId;
	$.getJSON(cmis.baseUrl+'/cmisBrowse/cmisEntryById?objectId='+cmis.currentFolderId, function (json) {
		cmis.currentFolderEntry=json;
		cmis.refresh(true);	
	}).error(function(event, jqXHR, ajaxSettings, thrownError) { 
		window.location.reload();
	});	
}

// Go to the given path (relative to the root path) and perform refresh
cmis.gotoPath = function gotoPath(path) {
	var rootPath=cmis.rootFolder.properties["cmis:path"];
	if (rootPath!="/") {
		path=rootPath+path
	}
	$.getJSON(cmis.baseUrl+"/cmisBrowse/cmisEntryByPath?path="+path, function (json) {
		cmis.currentFolderEntry=json;
		cmis.currentFolderId=json.id;
		cmis.refresh(true);	
	}).error(function(event, jqXHR, ajaxSettings, thrownError) { 
		window.location.reload();
	});
}

//Go to the given object
cmis.gotoObject=function gotoObject(objectId) {
	cmis.currentObjectId=objectId;
	$.getJSON(cmis.baseUrl+"/cmisBrowse/cmisEntryById?objectId="+cmis.currentObjectId, function (json) {
		cmis.currentObjectEntry=json;
		if(cmis.currentObjectEntry.properties["cmis:baseTypeId"]=="cmis:folder") {
			cmis.currentFolderId=cmis.currentObjectId;
			cmis.currentFolderEntry=cmis.currentObjectEntry;
		}
		cmis.refresh(true);	
	}).error(function(event, jqXHR, ajaxSettings, thrownError) { 
		window.location.reload();
	});
}

// go to the parent of the current folder 
cmis.gotoParentFolder = function gotoParentFolder() {
	if (cmis.currentFolderId!=cmis.rootFolderId) {
		var res=$.getJSON(cmis.baseUrl+"/cmisBrowse/cmisParent?objectId="+cmis.currentFolderId, function (json) {
			cmis.gotoFolder(json.objectId);
		}).error(function(event, jqXHR, ajaxSettings, thrownError) { 
			window.location.reload();
		});
	}
}

// Go to the home folder
cmis.gotoHomeFolder = function gotoHomeFolder() {
	cmis.gotoFolder(cmis.rootFolderId);
}

// This is performed on a full page reload
cmis.reload = function reload() {
    $("span.help").tooltip({});		
    $("li.menu-item a").tooltip({});            	          
}

// Performed after page load
$(function() {
	cmis.reload()
	
	// This allows us to click on a clickable row and execute the onclick handler that's in the span element of the first column
	$(".clickable-row td").live("click", function() {
		var clickable = $(this).find('span.clickable-cell:first')[0]
		if (clickable) {
			clickable.click()
		}
	});
	// When restoring is enabled we navigate to the object that is in the current hash
	if (cmis.restore) {
		if (window.location.hash!="") {
			var objectId=window.location.hash.substring(1);		
			cmis.gotoObject(objectId);		
		}
	}

});
