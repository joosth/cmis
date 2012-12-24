/*
 * Grails CMIS Plugin
 * Copyright 2010-2012, Open-T B.V., and individual contributors as indicated
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
 * Refresh the breadcrumb. The path is taken from cmis.currentFolderEntry
 */
cmis.refreshBreadcrumb = function refreshBreadcrumb() {
	if (cmis.currentFolderEntry) {
		var path=cmis.currentFolderEntry.properties["cmis:path"];
		var rootPath=cmis.rootFolder.properties["cmis:path"];
		if (rootPath!='/') {
			path=path.substring(rootPath.length+1);
		}
		
		var homeLabel=$(".cmis-breadcrumb ul li a").html()
		if (!homeLabel) {
			 homeLabel=$(".cmis-breadcrumb ul li").html()
		}
		var html='<ul class="breadcrumb">';
		html+='<li><a href="#" onclick="cmis.gotoPath(\'/\');">'+homeLabel+'</a> <span class="divider">/</span></li>'
		var pathElements=path.split('/');
		var currentPath=""
		for (i in pathElements) {
			// Skip empty element
			if (pathElements[i] && i<pathElements.length-1) {
				currentPath+="/"+pathElements[i];
				html+='<li><a href="#" onclick="cmis.gotoPath(\''+currentPath+'\')">'+pathElements[i]+'</a> <span class="divider">/</span></li>'		
			}
		}
		html+='<li class="active">'+pathElements[pathElements.length-1]+'</li>'
		html+='</ul>';
		$(".cmis-breadcrumb").html(html);
	}	
}
        
	
$(function() {
	cmis.refreshBreadcrumb();
	$(".cmis-breadcrumb").bind("refresh",cmis.refreshBreadcrumb);
	$(".cmis-breadcrumb").addClass("cmis-events");
});
