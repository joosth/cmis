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
 * CMIS Browse controller, provides the backend for the list and tree view
 * 
 * @author Joost Horward
 */
 
 package org.open_t.cmis
import org.open_t.cmis.*;
import org.open_t.cmis.services.*;
import grails.converters.JSON;
import org.codehaus.groovy.grails.commons.ConfigurationHolder
import org.apache.chemistry.opencmis.commons.enums.BaseTypeId
import org.apache.chemistry.opencmis.client.api.OperationContext

import java.net.*

class CmisBrowseController {

	CmisService cmisService
	def restService
	
	// Online edit mime types for Sharepoint Protocol
	def onlineEditMimetypes =
	[
	   "application/vnd.ms-excel": "Excel.Sheet",
	   "application/vnd.ms-powerpoint": "PowerPoint.Slide",
	   "application/msword": "Word.Document",
	   "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet": "Excel.Sheet",
	   "application/vnd.openxmlformats-officedocument.presentationml.presentation": "PowerPoint.Slide",
	   "application/vnd.openxmlformats-officedocument.wordprocessingml.document": "Word.Document"
	]
	
	// Catch unauthorized access
	def beforeInterceptor = {
		if (!cmisService.initialized) {
			redirect(controller:'cmisAuthenticate',action:'login')
			return false
		} 
	}
	
    // Show the document list by default 
    def index = { redirect(action:documentlist) }
	
	// Show the tree view
	def browse = {		
		def documents=[]
		def cmisEntry
			if (params.objectId=="checkedout") {
				documents=cmisService.listCheckedOut()
				cmisEntry=null
			} else {			
				if (!params.objectId)								   
					cmisEntry=cmisService.rootFolder					
					documents=cmisEntry.children					
			}
			[documents:documents,cmisEntry:cmisEntry]		
    }
	
	// Get info on one node. This is used by the tree view
	def nodejson = {		
		def documents
		def cmisEntry
		def theParentPath
		// If we have no id, use the root node, either from the rootNode parameter or the global one in the CMIS Service
		if (!params.id || params.id=="") {				
			params.id=cmisService.cmisSession.rootFolder.id
			if (params.rootNode && params.rootNode.length()>0) {					
				cmisEntry=cmisService.cmisSession.getObject(params.rootNode)
			} else {
				cmisEntry=cmisService.cmisSession.rootFolder
			}
			documents=[cmisEntry]
		} else {			
			def decodedid=URLDecoder.decode(params.id)			
			cmisEntry=cmisService.getObject(decodedid)
			documents=cmisService.listChildren(cmisEntry)
			theParentPath=cmisEntry?.path				
		}
		
		// Format the list in the correct way for jsTree
		def doclist = { documents.collect { theDocument -> 
		
			def nodeRel = (theDocument.getProperty("baseTypeId")=='cmis:folder') ? 'folder' : 'default'				
			def nodeClass="jstree-${nodeRel} ${theDocument.cssClassName}-16"				
			def nodeState =theDocument.isFolder ? 'closed' : ''
					
			[
				attr: [id: theDocument.id,title:theDocument.prop.'cmis:name',class: nodeClass,rel:nodeRel,parentPath:theParentPath],
             	data: theDocument.prop.name,
				title: theDocument.prop.name,
				state:nodeState,				
				rel:nodeRel,
			 ]
			}
		}
		render doclist() as JSON 			
	}
	
	// Show detail view: a list for folder types or a propery list for documents
	def detail = {
		def cmisObject=cmisService.getObject(params.objectId)
		if (cmisObject.isFolder) {
			render(view:'list')
		} else {
			render(view:'document',model:document(params:params))
		}			
	}
	
	// Helper function that generates the HTML for an action in the document list 
	def createDialogAction = {name,id,nosubmit=false ->
		def href=g.createLink(controller:"cmisDocument",action:name,params:[objectId:id])		 
		def title=g.message(code:"cmisBrowse.jsonlist.${name}.help",default:'')
		def onclick="""onclick="dialog.formDialog('null','cmisDocument', {'dialogname':'${name}','nosubmit':${nosubmit}},{'getObjectId':'${id}'} ,null)" """
		 		 
		return """<span class="action-${name} action list-action" title="${title}" ${onclick} >&nbsp;</span>""" 
	}
	 
	/*
	 * Delivers JSON datasource to datatable
	 */
	def jsonlist =  {
	log.debug "params: ${params}"
		if (!params.objectId) {
			def emtpyjson = [sEcho:params.sEcho,iTotalRecords:0,iTotalDisplayRecords:0,aaData:[]]
			render emptyjson as JSON
		} else {
		def cmisParams=[:]
		def columns=["","cmis:name",""]
		String sortDir=params.sSortDir_0.toUpperCase()
		def orderName=columns[new Integer(params.iSortCol_0)]
		if (orderName.length()<1) {
			orderName="cmis:name"
		}		 
		cmisParams.orderBy="${orderName}%20${sortDir}"
		 
		cmisParams.maxItems=params.iDisplayLength
		// TODO skipCount is interpreted as the page number by Alfresco !?
		cmisParams.skipCount=new Integer(params.iDisplayStart)/new Integer(params.iDisplayLength)
		int skipCount=new Integer(params.iDisplayStart)/new Integer(params.iDisplayLength)
		log.debug ("objectId:${params.objectId}")
		def cmisObject=cmisService.getObject(params.objectId)
		def parentPath=cmisObject.prop.path
		log.debug ("cmisObject:${cmisObject}")
		 
		def documents=cmisService.listChildren(cmisObject,[orderBy:orderName,sortDir:sortDir,skipCount:skipCount,maxItemsPerPage:params.iDisplayLength])		 
		def response			 
		def iTotalRecords=documents.totalNumItems		 
		def iTotalDisplayRecords=documents.totalNumItems
		 
		def aaData=[]
		documents.each { doc ->
			def id=doc.id
			def icon
			def namespan
			 
			if (doc.isDocument) {
				icon="""<span class='mime-16 ${doc.cssClassName}-16'>&nbsp;</span>"""
				namespan="""<span class='${doc.cssClassName}'>${doc.name}</span>"""
			} else {
				icon="""<span onclick="cmis.gotoFolder('${doc.id}');event.returnValue=false; return false;" class='mime-16 ${doc.cssClassName}-16 clickable-cell'>&nbsp;</span>"""
				namespan="""<span onclick="cmis.gotoFolder('${doc.id}');event.returnValue=false; return false;" class='${doc.cssClassName} clickable-cell'>${doc.name}</span>"""
			}
			 
			def actions=createDialogAction("props",id,true)
			 
			if (params.readOnly!="true") {
				actions+=createDialogAction("edit",id)
			}
			 
			if(doc.isDocument) {
				actions+=createDialogAction("history",id,true)
				actions+=link(title:"${message(code:'cmisBrowse.jsonlist.download.help')}","class":"action-download action list-action simpleDialog",controller:"cmisDocument",action:"download", params:[objectId:doc.id])
				 				 
				if (params.readOnly!="true") {
					actions+=createDialogAction("updatedocument",id)
					def spp=cmisService.getSpp(doc,parentPath) 
					if (spp?.path){
						actions+="""<span href='${spp.path}' title='${message(code:'cmisBrowse.jsonlist.editonlinespp.help')}' sppAppProgId='${spp.appProgId}' class='action-edit-online list-action action spp-link'>&nbsp;</span>"""
					}
					def webdavPath=cmisService.getWebdavPath(doc,parentPath)
					 
					if(webdavPath) {
						actions+="""<a href='${webdavPath}' title='${message(code:'cmisBrowse.jsonlist.editonlinewebdav.help')}' class='action-edit-online list-action action' target='_blank'>&nbsp;</a>"""						 
					}					 
				}
				 
				if(params.cico!="false") {
					if (doc.isPwc) {
						actions+=createDialogAction("checkin",id)
						actions+=createDialogAction("cancelcheckout",id)
					} else {
						if (!doc.isCheckedOut) {
							actions+=createDialogAction("checkout",id)
						}
					}
				}			
			}
			 
			if (params.readOnly!="true") {
				actions+=createDialogAction("delete",id)
			}			 
			actions=actions.replace('"', '\"')						 
			def aaLine=["DT_RowClass":"clickable-row","0":icon,"1":namespan,"2":actions]			 
			aaData+=(aaLine)
		}

		def json = [sEcho:params.sEcho,iTotalRecords:iTotalRecords,iTotalDisplayRecords:iTotalDisplayRecords,aaData:aaData]
		render json as JSON
		}
	}
 
	// Shows document details in sidepane
	def document = {
		def cmisObject=cmisService.getObject(params.objectId)
		def spp=cmisService.getSpp(cmisObject)
		def webdavPath=cmisService.getWebdavPath(cmisObject)
		[entry:cmisObject,cmisObject:cmisObject,params:params,sppPath:spp.path,sppAppProgId:spp.appProgId,webdavPath:webdavPath]
	}
	// Show document list
	def documentlist = {		
	}
	
	// get parent id, this is used by the 'up' button in the toolbar
	def cmisParent =  {
		def folder
		def entry=cmisService.rootFolder		
		if (params.objectId) {
			try {		
				folder=cmisService.getObject(params.objectId)
				entry=folder.getFolderParent()
			} catch (Exception e) {
				entry=cmisService.rootFolder
			}
		}		
		def res=[success:true,objectId:entry.id,id:entry.id,properties:entry.props,isDocument:entry.isDocument,isFolder:entry.isFolder]
		render res as JSON
	}
	
	// get CMIS object for given id
	def cmisEntryById =  {
		def entry
		try {
			entry=cmisService.getObject(params.objectId)
		} catch (Exception e) {
			entry=cmisService.rootFolder
		}
		def res=[success:true,objectId:entry.id,id:entry.id,properties:entry.props,isDocument:entry.isDocument,isFolder:entry.isFolder]
		render res as JSON
	}
	
	// get CMIS entry by path
	def cmisEntryByPath =  {
		def entry
		if (!params.path) {
			entry=cmisService.repositories.rootFolder
		} else {
			entry=cmisService.getObjectByPath(params.path)
		}
		def res=[success:true,objectId:entry.id,id:entry.id,properties:entry.props,isDocument:entry.isDocument,isFolder:entry.isFolder]
		render res as JSON
	}
}
