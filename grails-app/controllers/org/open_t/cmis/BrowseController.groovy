package org.open_t.cmis
import org.open_t.cmis.*;
import grails.converters.JSON;
class BrowseController {

	def cmisService
	def restService
	
	
	def beforeInterceptor = {
			
			if ((!restService.authenticated) || cmisService.repositories==null) {				
				restService.login(grailsApplication.config.cmis.username,grailsApplication.config.cmis.password)				
				restService.authenticate()
				cmisService.repositories = new Repositories(restService)
				cmisService.contextPath=request.contextPath
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
			if (!params.id || params.id=="") {				
				params.id=cmisService.repositories.rootFolderId
				if (params.rootNode && params.rootNode.length()>0) {
					cmisEntry=cmisService.getEntry(params.rootNode)
				} else {
					cmisEntry=cmisService.getEntry(cmisService.repositories.rootFolderId)
				}

				CmisEntry checkedOutEntry= cmisService.getEntry("checkedout")
				
				documents=[cmisEntry,checkedOutEntry]	            
			} else {
				cmisEntry=cmisService.getEntry(params.id)
				documents=cmisService.listDescendants(cmisEntry)
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
                    attr: [id: theDocument.uuid,title:theDocument.title,class: nodeClass,rel:nodeRel],
                 	data: theDocument.title,
					title: theDocument.title,
					state:nodeState,
					
					rel:nodeRel
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
				//params.objectId="workspace://SpacesStore/fe1528db-4a48-4495-8204-9a1bc56a0926"
				params.objectId=cmisService.repositories.rootFolderId					
			def cmisEntry=cmisService.getEntry(params.objectId)					
			def documents=cmisService.listDescendants(cmisEntry)
			[documents:documents,cmisEntry:cmisEntry]
	 
	   }
	// Shows document defails in sidepane
	def document = {
			[entry:cmisService.getEntry(params.objectId),params:params]
	}
	
}
