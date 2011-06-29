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

package org.open_t.cmis
import org.codehaus.groovy.grails.commons.* 


class CmisTagLib {
	def cmisService
	static namespace = 'cmis'

	def head = { attrs ->
	  def cmismap=[:]
	  def rootEntry
	  
	  
	  if (attrs.rootNode) {
		  rootEntry=cmisService.getEntryById(attrs.rootNode)		 
	  } else if (attrs.path) {
		  rootEntry=cmisService.getEntryByPath(attrs.path)
	  } else {
		  rootEntry=cmisService.getEntryByPath("/")
	  }
	  if (attrs.username) {
		  restService.login(attrs.username,attrs.password)				
		  restService.authenticate()
		  if (!cmisService.repositories) {
			  cmisService.repositories = new Repositories(restService)
		  }
		  cmisService.contextPath=request.contextPath
	  }
	  
	  
	  cmismap+=[rootEntry:rootEntry]
	  
	  request.cmis=cmismap
	  def html="""<!-- CMIS variables -->
		  		  <script type="text/javascript">
	  		      var cmis={};
  				  cmis.baseUrl="${request.contextPath}";
  				  //cmis.rootNode="${attrs.rootNode}";
  				  cmis.rootNode="${request.cmis.rootEntry.uuid}";
  				  </script>"""
	  out << html
	  
      
	  
	  out << """<link rel="stylesheet" type="text/css" href="${resource(dir:'css',file:'cmis.css',contextPath:pluginContextPath)}" />"""
	  out << """<link rel="stylesheet" type="text/css" href="${resource(dir:'css/theme',file:'mimetypes.css',contextPath:pluginContextPath)}" />"""

      out << """<link rel="stylesheet" href="${resource(dir:'js/uploader',file:'fileuploader.css',contextPath:pluginContextPath)}" />"""
      out << """<link rel="stylesheet" href="${resource(dir:'css',file:'jquery.jstree.css',contextPath:pluginContextPath)}" />"""
      
	  out << g.javascript(src:'cmis.js',contextPath:pluginContextPath)
	  
	  out << g.javascript(src:'uploader/fileuploader.js',contextPath:pluginContextPath)
	  
	  out << g.javascript(src:'jquery/jquery.cookie.js',contextPath:pluginContextPath)
	  out << g.javascript(src:'jquery/jquery.hotkeys.js',contextPath:pluginContextPath)
	  out << g.javascript(src:'jquery/jquery.jstree.js',contextPath:pluginContextPath)
	}
	
	def tree = { attrs ->
	def html="""<div id="outer-treediv" class="outer-treediv" >   
		            <div id="treediv" class="treediv" >		             
		            </div>
				</div>"""
		out << html
	}

	def pane = { attrs ->
	def html="""<div id="outer-detail-pane" class="outer-detail-pane" >
			            <div id="detail-pane" class="detail-pane" >            
		            	Select a file on the left to show the file details in this pane.
		            </div>
				</div>"""
		out << html
	}
	
	

}
