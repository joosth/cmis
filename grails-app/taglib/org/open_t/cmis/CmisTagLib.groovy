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
 * CMIS taglib
 * 
 * @author Joost Horward
 */
 
package org.open_t.cmis
import org.codehaus.groovy.grails.commons.* 
import org.codehaus.groovy.grails.commons.ConfigurationHolder
import org.springframework.context.ApplicationContextAware
import org.springframework.context.ApplicationContext

import org.springframework.context.*

class CmisTagLib  implements ApplicationContextAware {

	static namespace = 'cmis'
	ApplicationContext applicationContext

	/**
	 * cmis:head
	 * Element to place in HTML page's <head> section
	 * 
	 * @param username
	 * @param password
	 * @param rootNode		id of root element, takes precedence over path
	 * @param path			path of root element
	 * @param cico			boolean, defines if check in/out should be presented to the user
	 * @param readOnly		boolean, defines if this view should be read-only
	 * @param restore		boolean, defines if this view should be restored from the URL hash (#id)
	 * 
	 */
	def head = { attrs ->
	  
	  def cmismap=[:]
	  def rootEntry
	  
	  if (ConfigurationHolder.config.cmis.enabled) {
		  println "AAAA"
		  if (!applicationContext.cmisService.initialized) {
			  println "BBB"
			  if (attrs.username) {
				  println "CCCC"
				  applicationContext.cmisService.init(ConfigurationHolder.config.cmis.url,attrs.username,attrs.password)
				  println "DDD"
			  } else {
			  println "EEE"
			  	applicationContext.cmisService.init(ConfigurationHolder.config.cmis.url,ConfigurationHolder.config.cmis.username,ConfigurationHolder.config.cmis.password)
			  }
		  }
		  println "ZZZZZ"
		  if (attrs.rootNode) {
			  rootEntry=applicationContext.cmisService.getObject(attrs.rootNode)		 
		  } else if (attrs.path) {
			  rootEntry=applicationContext.cmisService.getObjectByPath(attrs.path)			  
		  } else {
			  rootEntry=applicationContext.cmisService.getObjectByPath("/")
		  }
		  def rootFolderEntry=[success:true,objectId:rootEntry.id,id:rootEntry.id,properties:rootEntry.props,isDocument:rootEntry.isDocument,isFolder:rootEntry.isFolder];
		  applicationContext.cmisService.contextPath=request.contextPath
	  
	  
	  def cico=false
	  def readOnly=false
	  def restore=false
	  
	  if (attrs.cico && (attrs.cico=="true" || attrs.cico==true)) cico=true;
	  if (attrs.readOnly && (attrs.readOnly=="true" || attrs.readOnly==true)) readOnly=true;
	  if (attrs.restore && (attrs.restore=="true" || attrs.restore==true)) restore=true;
	  	  
	  cmismap+=[rootEntry:rootEntry,cico:cico,readOnly:readOnly]
	  request.cmis=cmismap
	  def html="""<!-- CMIS variables -->
		  		  <script type="text/javascript">
	  		      var cmis={};  				    				  

  				  cmis.baseUrl="${request.contextPath}";
  				  cmis.pluginPath="${resource(absolute:false,plugin:'cmis')}";

  				  cmis.rootFolder=${rootFolderEntry.encodeAsJSON()};
				  cmis.currentFolder=${rootFolderEntry.encodeAsJSON()};
				  cmis.currentObject=${rootFolderEntry.encodeAsJSON()};

  				  cmis.readOnly=${readOnly};
  				  cmis.cico=${cico};
				  cmis.restore=${restore};
  				  
  				  </script>"""
	  out << html
	 
	  }
	}
	
	/**
	 * cmis:tree
	 * Generates tree view
	 */	
	def tree = { attrs ->
		out << """<div id="outer-treediv" class="outer-treediv" >   
			            <div id="treediv" class="treediv cmis-tree" >		             
			            </div>
					</div>"""

	}
	
	/**
	 * cmis:pane
	 * Generates detail pane
	 */
	def pane = { attrs ->
		def html="""<div id="outer-detail-pane" class="outer-detail-pane disabled" ></div>"""
			if (applicationContext.cmisService.enabled) {
				html="""<div id="detail-pane" class="detail-pane cmis-detailspane" >${g.message(code:'cmis.pane.body')}</div>"""
				}
			out << html
	}
	
	/**
	 * cmis:list
	 * Generates document/folder list
	 * 
	 * @param breadcrumb Enables breadcrumb (default true)
	 * 
	 */
	def list = { attrs ->
		def breadcrumb=""
		if (!attrs.breadcrumb || attrs.breadcrumb=="true") {
			breadcrumb="""<div class="cmis-breadcrumb">
				<ul class="breadcrumb">
					<li class="active">${g.message(code:"cmis.home.label")}</li>
				</ul>				
			</div>"""			
		}

		def html="""
		<div id="list-toolbar" class="navbar btn-group" >
			<span href="#" onclick="cmis.gotoHomeFolder();event.returnValue=false;return false;" title="${g.message(code:'cmis.list.homefolder.help')}" class="btn btn-small" ><img src="${resource(dir:'css/theme/icons/actions/16', file:'gohome.png')}" /></span>
			<a href="#" onclick="cmis.gotoParentFolder();event.returnValue=false;return false;" title="${g.message(code:'cmis.list.parentfolder.help')}" class="btn btn-small" ><img src="${resource(dir:'css/theme/icons/actions/16', file:'go-up.png')}" /></a>
			<a onclick="dialog.formDialog(null,'cmisDocument',{'dialogname':'newfolder'},{'getParentId':cmis.currentFolder.id});event.returnValue=false;return false;" title="${g.message(code:'cmis.list.newfolder.help')}" class="btn btn-small" href="${createLink(controller:'cmisDocument',action: 'newfolder')}" ><img src="${resource(dir:'css/theme/icons/actions/16', file:'folder-new.png')}" /></a>
			<a onclick="dialog.formDialog(null,'cmisDocument',{'dialogname':'newdocument'},{'getParentId':cmis.currentFolder.id});event.returnValue=false;return false;" title="${g.message(code:'cmis.list.newdocument.help')}" class="btn btn-small" href="${createLink(controller:'cmisDocument',action: 'newdocument')}" ><img src="${resource(dir:'css/theme/icons/actions/16', file:'document-new.png')}" /></a>
		</div>
		<div id="list-body" class="datatable">
			${breadcrumb}
			
				<table id="file-list" cellpadding="0" cellspacing="0" border="0"
				   class="display file-list cmis-datatable table table-striped table-bordered table-hover">
				<thead>
					<tr>
						<th class="cmis-list-icon">${g.message(code:"cmis.list.icon",    default:"Icon")}		</th>
						<th class="cmis-list-name">${g.message(code:"cmis.list.name",    default:"Name")}		</th>			
						<th class="cmis-list-actions"> ${g.message(code:"cmis.list.actions", default:"Actions")}   </th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td colspan="3" class="dataTables_empty">${g.message(code:"cmis.loading", default:"Loading data from server")}</td>
					</tr>
				</tbody>
				<tfoot>
					<tr>
						<th class="cmis-list-icon">${g.message(code:"cmis.list.icon",    default:"Icon")}		</th>
						<th class="cmis-list-name">${g.message(code:"cmis.list.name",    default:"Name")}		</th>			
						<th class="cmis-list-actions"> ${g.message(code:"cmis.list.actions", default:"Actions")}   </th>
					</tr>
				</tfoot>
			</table>
		</div>"""
		
		if (applicationContext.cmisService.enabled) {					
			out << html		
		}
	}
	
	/**
	 * cmis:thumbnail
	 * Generates thumbnail element	 
	 *
	 */
	def thumbnail = { attrs ->		
		out << """<img src="${createLink(controller:'cmisDocument',action: 'thumbnail',params:[objectId:attrs.object.prop.objectId])}" />"""		
	}
}
