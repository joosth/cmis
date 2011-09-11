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
import org.codehaus.groovy.grails.commons.ConfigurationHolder

import org.springframework.context.*
class CmisTagLib  implements ApplicationContextAware {
	//def cmisService
	//def restService
	static namespace = 'cmis'
	ApplicationContext applicationContext
	
		

	def head = { attrs ->
	  
	  def cmismap=[:]
	  def rootEntry
	  
	  if (ConfigurationHolder.config.cmis.enabled) {
		  if (!applicationContext.cmisService.initialized) {
			  if (attrs.username) {
				  applicationContext.cmisService.init(ConfigurationHolder.config.cmis.url,attrs.username,attrs.password)			  
			  } else {
			  	applicationContext.cmisService.init(ConfigurationHolder.config.cmis.url,ConfigurationHolder.config.cmis.username,ConfigurationHolder.config.cmis.password)
			  }
		  }
		  if (attrs.rootNode) {
			  rootEntry=applicationContext.cmisService.getEntryById(attrs.rootNode)		 
		  } else if (attrs.path) {
			  rootEntry=applicationContext.cmisService.getEntryByPath(attrs.path)
		  } else {
			  rootEntry=applicationContext.cmisService.getEntryByPath("/")
		  }
		  
		 
			
	  
		  applicationContext.cmisService.contextPath=request.contextPath
	  
	  
	  
	  cmismap+=[rootEntry:rootEntry]
	  
	  request.cmis=cmismap
	  def html="""<!-- CMIS variables -->
		  		  <script type="text/javascript">
	  		      var cmis={};  				  
  				  cmis.baseUrl="${resource(absolute:false,plugin:'cmis')}";
  				  cmis.rootNode="${request.cmis?.rootEntry?.uuid}";
  				  cmis.language="${java.util.Locale.getDefault().getLanguage()}";
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
	}
	
	def uploadHead = { attrs ->
		def html=""
		if (applicationContext.cmisService.enabled) { 
		html="""
         <script  type="text/javascript">       
        \$(function() {		 
        var uploader = new qq.FileUploader({      			
      			element: document.getElementById('file-uploader'),
      			// path to server-side upload script
      			action: cmis.baseUrl+'/cmisDocument/fileupload',
      			params: {      	
      				},
      			onComplete: function(id, fileName, responseJSON){
  					\$("#form").append('<input type=\"hidden\" name=\"filename\" value=\""+fileName+"\" />');      			
      			}
   			});
   			});
		</script> 
		"""
		}
		out << html
		
	}
	
	
	def tree = { attrs ->
			
	def html="""<div id="outer-treediv" class="outer-treediv" >   
		            <div id="treediv" class="treediv" >		             
		            </div>
				</div>"""
		out << html
	}

	def pane = { attrs ->
		def html="""<div id="outer-detail-pane" class="outer-detail-pane disabled" ></div>"""
			if (applicationContext.cmisService.enabled) {
				html="""<div id="outer-detail-pane" class="outer-detail-pane" >
						            <div id="detail-pane" class="detail-pane" >${g.message(code:'cmis.pane.body')}</div>
							</div>"""
				}
			out << html
	}
}
