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
 * CMIS plugin Javascript
 */
/*

cmis.refreshCallbacks=[];

cmis.registerRefreshCallback =function registerRefreshCallback (callbackFunction){
	cmis.refreshCallbacks.push(callbackFunction);
}
*/
cmis.refresh = function refresh (){
	$(".cmis-events").trigger("refresh");
}

function gotoFolder(folderId) {
	cmis.parentFolderId=cmis.currentFolderId;
	cmis.currentFolderId=folderId;
	$.getJSON(cmis.baseUrl+"/cmisBrowse/jsonEntry?objectId="+cmis.currentFolderId, function (json) {
		cmis.currentFolderEntry=json;
		//alert(cmis.currentFolderEntry.properties["cmis:path"]);
		cmis.refresh();	
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
	});
}


function gotoParentFolder() {
	if (cmis.currentFolderId!=cmis.rootFolderId) {
		var res=$.getJSON(cmis.baseUrl+"/cmisBrowse/jsonparent?objectId="+cmis.currentFolderId, function (json) {
			gotoFolder(json.objectId);
		});
	}

}

function gotoHomeFolder() {
	gotoFolder(cmis.rootFolderId);
}

function cmisReload() {
    $("span.help").cluetip({
		splitTitle: '|',  
		cluezIndex: 2000
	});
            	          
}
	
$(function() {
	cmisReload()	
});
