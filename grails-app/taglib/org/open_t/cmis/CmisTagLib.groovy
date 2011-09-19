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
	  
	  
	  def cico=false
	  def readOnly=false
	  
	  if (attrs.cico && (attrs.cico=="true" || attrs.cico==true)) cico=true;
	  if (attrs.readOnly && (attrs.readOnly=="true" || attrs.readOnly==true)) readOnly=true;
	  	  
	  cmismap+=[rootEntry:rootEntry,cico:cico,readOnly:readOnly]
	  request.cmis=cmismap
	  def html="""<!-- CMIS variables -->
		  		  <script type="text/javascript">
	  		      var cmis={};  				    				  
  				  cmis.baseUrl="${request.contextPath}";
  				  cmis.pluginPath="${resource(absolute:false,plugin:'cmis')}";
  				  cmis.rootNode="${request.cmis?.rootEntry?.uuid}";  				    				  
  				  cmis.currentFolder="${request.cmis?.rootEntry?.uuid}";  				  
  				  cmis.language="${java.util.Locale.getDefault().getLanguage()}";
  				  cmis.readOnly=${readOnly};
  				  cmis.cico=${cico};
  				  
  				  var uploader={}
  				  uploader.uploadMessage="${message(code:'cmis.uploader.uploadafile')}";
  				  uploader.dropfilesMessage="${message(code:'cmis.uploader.dropfileshere')}";
  				    				    				  
  				  </script>"""
	  out << html
	  
      
	  
	  out << """<link rel="stylesheet" type="text/css" href="${resource(dir:'css',file:'cmis.css',contextPath:pluginContextPath,plugin:'cmis')}" />"""
	  out << """<link rel="stylesheet" type="text/css" href="${resource(dir:'css/theme',file:'mimetypes.css',contextPath:pluginContextPath,plugin:'cmis')}" />"""

      out << """<link rel="stylesheet" href="${resource(dir:'js/uploader',file:'fileuploader.css',contextPath:pluginContextPath,plugin:'cmis')}" />"""
      out << """<link rel="stylesheet" href="${resource(dir:'css',file:'jquery.jstree.css',contextPath:pluginContextPath,plugin:'cmis')}" />"""
      out << "\n"
	  out << g.javascript(src:'cmis.js',contextPath:pluginContextPath,plugin:'cmis')
	  out << "\n"
	  out << g.javascript(src:'uploader/fileuploader.js',contextPath:pluginContextPath,plugin:'cmis')
	  out << "\n"
	  out << g.javascript(src:'jquery/jquery.cookie.js',contextPath:pluginContextPath,plugin:'cmis')
	  out << "\n"
	  out << g.javascript(src:'jquery/jquery.hotkeys.js',contextPath:pluginContextPath,plugin:'cmis')
	  out << "\n"
	  out << g.javascript(src:'jquery/jquery.jstree.js',contextPath:pluginContextPath,plugin:'cmis')
	  out << "\n"
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
      			},
      			template: '<div class="qq-uploader">' + 
                '<div class="qq-upload-drop-area"><span>${message(code:'cmis.uploader.dropfileshere')}</span></div>' +
                '<div class="qq-upload-button">${message(code:'cmis.uploader.uploadafile')}</div>' +
                '<ul class="qq-upload-list"></ul>' + 
             	'</div>'
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
	
	def list = { attrs ->		
		def html="""
		<div id="list-toolbar" class="fg-toolbar ui-toolbar xui-widget-header ui-corner-tl ui-corner-tr table-title">
			${g.link(onclick:"gotoHomeFolder();return false;", title:"${g.message(code:'cmis.browse.homefolder.tooltip')}",class:"action-home action list-action") {"&nbsp;"} }
			${g.link(onclick:"gotoParentFolder();return false;", title:"${g.message(code:'cmis.browse.parentfolder.tooltip')}",class:"action-up action list-action") {"&nbsp;"} }
			${g.link(onclick:"simpleDialog(this.href+'?parentId='+cmis.currentFolder);return false;",title:"${g.message(code:'cmis.browse.newfolder.tooltip')}",class:"action-newfolder action list-action",controller:"cmisDocument",action:"newfolder") {"&nbsp;"}  }			
			${g.link(onclick:"uploadDialog(this.href+'?parentId='+cmis.currentFolder);return false;", title:"${g.message(code:'cmis.browse.newdocument.tooltip')}",class:"action-newdocument action list-action",controller:"cmisDocument",action:"newdocument") {"&nbsp;"} }						
		</div>
		
		
		<div id="list-body" class="datatable">
			<table id="file-list" cellpadding="0" cellspacing="0" border="0"
				   class="display file-list">
				<thead>
					<tr>
						<th>${g.message(code:"cmis.list.icon",    default:"Icon")}		</th>
						<th>${g.message(code:"cmis.list.name",    default:"Name")}		</th>			
						<th>${g.message(code:"cmis.list.actions", default:"Actions")}   </th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td colspan="3" class="dataTables_empty">${g.message(code:"cmis.loading", default:"Loading data from server")}</td>
					</tr>
				</tbody>
				<tfoot>
					<tr>
						<th>${g.message(code:"cmis.list.icon",    default:"Icon")}		</th>
						<th>${g.message(code:"cmis.list.name",    default:"Name")}		</th>			
						<th>${g.message(code:"cmis.list.actions", default:"Actions")}   </th>
					</tr>
				</tfoot>
			</table>
		</div>"""
		
		if (applicationContext.cmisService.enabled) {					
			out << html		
		}
	}
}