package org.open_t.cmis
import org.open_t.cmis.*;
import grails.converters.JSON;
import org.codehaus.groovy.grails.commons.ConfigurationHolder
import java.net.*

class CmisBrowseController {

	def cmisService
	def restService
	
	
	def onlineEditMimetypes =
	[
	   "application/vnd.ms-excel": "Excel.Sheet",
	   "application/vnd.ms-powerpoint": "PowerPoint.Slide",
	   "application/msword": "Word.Document",
	   "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet": "Excel.Sheet",
	   "application/vnd.openxmlformats-officedocument.presentationml.presentation": "PowerPoint.Slide",
	   "application/vnd.openxmlformats-officedocument.wordprocessingml.document": "Word.Document"
	]
	

	def beforeInterceptor = {
		if (!cmisService.initialized) {
			redirect(controller:'cmisAuthenticate',action:'login')
			return false
		} 
	}
	
    
    def index = {redirect(action:browse)}
	
	
    def browse = {

			def documents=[]
           def cmisEntry
			if (params.objectId=="checkedout") {
				documents=cmisService.listCheckedOut()
				cmisEntry=null
			} else {
			
				if (!params.objectId)								   
					params.objectId=cmisService.repositories.rootFolderId
				cmisEntry=cmisService.getEntry(params.objectId)
				documents=cmisService.listDescendants(cmisEntry)
			}
			[documents:documents,cmisEntry:cmisEntry]		
    }
	
	
	def nodejson = {		
			def documents
			def cmisEntry
			def theParentPath
			if (!params.id || params.id=="") {				
				params.id=cmisService.repositories.rootFolderId
				if (params.rootNode && params.rootNode.length()>0) {
					cmisEntry=cmisService.getEntry(params.rootNode)
				} else {
					cmisEntry=cmisService.getEntry(cmisService.repositories.rootFolderId)
				}

				CmisEntry checkedOutEntry= cmisService.getEntry("checkedout")
				theParentPath=checkedOutEntry?.path
				
				documents=[cmisEntry,checkedOutEntry]	            
			} else {
				cmisEntry=cmisService.getEntry(params.id)
				documents=cmisService.listDescendants(cmisEntry)
				theParentPath=cmisEntry?.path
			}

			
			
			
			def doclist = { documents.collect { theDocument -> 
			
					def nodeRel = (theDocument.prop.baseTypeId=='cmis:folder') ? 'folder' : 'default'
					
					def nodeClass="jstree-${nodeRel} ${theDocument.cssClassName}-16"
					def nodeState =(theDocument.prop.baseTypeId=='cmis:folder') ? 'closed' : ''
					//if(theDocument.title=='Company Home') {
				//		nodeState='open'
				//	}
						
					[
                    //attr: [id: theDocument.objectId,title:theDocument.title,class: nodeClass,rel:nodeRel],
                    attr: [id: URLEncoder.encode(theDocument.uuid),title:theDocument.title,class: nodeClass,rel:nodeRel,parentPath:theParentPath],
                 	data: theDocument.title,
					title: theDocument.title,
					state:nodeState,
					
					rel:nodeRel,
				 ]
				}
			}		
			render doclist() as JSON 
			
			
			 
			 
	}
	
	def detail = {
			def cmisEntry=cmisService.getEntry(params.objectId)
			if (cmisEntry.prop.baseTypeId=='cmis:folder') {
				render(view:'list',model:list(params:params))
			} else {
				render(view:'document',model:document(params:params))
			}
			
			
	}
	// Shows document list in sidepane
	 def list = {
			if (!params.objectId)
				params.objectId=cmisService.repositories.rootFolderId					
			def cmisEntry=cmisService.getEntry(params.objectId)					
			def documents=cmisService.listDescendants(cmisEntry)
			[documents:documents,cmisEntry:cmisEntry]
	 
	   }
	// Shows document defails in sidepane
	def document = {
		log.debug "params:${params}"

		def cmisEntry=cmisService.getEntry(params.objectId)
		
		def sppPath=null
		def sppBasePath=ConfigurationHolder.config.cmis.sppBasePath
		
		def sppAppProgId=onlineEditMimetypes[cmisEntry.prop.contentStreamMimeType]
		
		if (params.parentPath && sppBasePath && sppAppProgId) {
			if (params.parentPath.startsWith("/Sites/")) {
				def path=params.parentPath.replace("/Sites/","/")
				sppPath=sppBasePath+path+"/"+cmisEntry.name
			}			
		}
		def webdavPath=null
		def webdavBasePath=ConfigurationHolder.config.cmis.webdavBasePath
		if (params.parentPath && webdavBasePath) {
			webdavPath=webdavBasePath+params.parentPath+"/"+cmisEntry.name
		}

			[entry:cmisEntry,params:params,sppPath:sppPath,sppAppProgId:sppAppProgId,webdavPath:webdavPath]
	}
	
}
