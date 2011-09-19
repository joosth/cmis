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
	 
	 /*
	  * Delivers JSON datasource to datatable
	  */
	 def jsonlist =  {
		 log.debug "params: ${params}"
		 if (!params.objectId)
	       params.objectId=cmisService.repositories.rootFolderId
		 
		 def cmisParams=[:]
		 def columns=["","cmis:name",""]
		 String sortDir=params.sSortDir_0.toUpperCase()
		 def orderName=columns[new Integer(params.iSortCol_0)]
		 if (orderName.length()<1) {
			 orderName="cmis:name"
		 }
		 
		 cmisParams.orderBy="${orderName}%20${sortDir}"
		 
		 cmisParams.maxItems=params.iDisplayLength
		 cmisParams.skipCount=params.iDisplayStart
		 log.debug ("objectId:${params.objectId}")
	     def cmisEntry=cmisService.getEntry(params.objectId)
		 def parentPath=cmisEntry.prop.path
		 log.debug ("cmisEntry:${cmisEntry}")
		 def res=cmisService.listChildren(cmisEntry,cmisParams)   
		 def documents=res.documents
		 def response=res.response	
		 
		 def iTotalRecords=response.numItems.text()
		 def iTotalDisplayRecords=response.numItems.text()
		 
		 
		 def aaData=[]
		 documents.each { doc ->
			 def icon
			 if (doc.isDocument()) {
			  icon="""<a href="#" class="mime-16 ${doc.cssClassName}-16">&nbsp;</a>"""
			 } else {
			  icon="""<a href="#" onclick="gotoFolder('${doc.prop.objectId}');return false;" class="mime-16 ${doc.cssClassName}-16">&nbsp;</a>"""
			 }
			 def actions=link(onclick:"simpleDialog(this.href);return false;",title:"${message(code:'cmis.list.showproperties')}","class":"action-show action list-action simpleDialog",controller:"cmisDocument",action:"props", params:[objectId:doc.prop.objectId])
			 if (params.readOnly!="true") {
				 actions+=link(onclick:"simpleDialog(this.href);return false;",title:"${message(code:'cmis.list.editproperties')}","class":"action-edit action list-action simpleDialog",controller:"cmisDocument",action:"edit", params:[objectId:doc.prop.objectId])
			 }
			 if (doc.isDocument()) {
				 actions+=link(onclick:"simpleDialog(this.href);return false;",title:"${message(code:'cmis.list.showhistory')}","class":"action-history action list-action simpleDialog",controller:"cmisDocument",action:"history", params:[objectId:doc.prop.objectId])
				 actions+=link(title:"${message(code:'cmis.list.download')}","class":"action-download action list-action simpleDialog",controller:"cmisDocument",action:"download", params:[objectId:doc.prop.objectId])
				 if (params.readOnly!="true") {
					 actions+=link(onclick:"uploadDialog(this.href);return false;",title:"${message(code:'cmis.list.upload')}","class":"action-upload action list-action simpleDialog",controller:"cmisDocument",action:"updatedocument", params:[objectId:doc.prop.objectId])
					 def spp=cmisService.getSppPath(doc,parentPath) 
					 if (spp.path){
						 actions+="""<span href="${spp.path}" title="${message(code:'cmis.actions.editonlinespp.tooltip')}" sppAppProgId="${spp.appProgId}" class="action-edit-online list-action action spp-link">&nbsp;</span>"""
					 }
					 def webdavPath=cmisService.getWebdavPath(doc,parentPath)
					 if(webdavPath) {
						 actions+="""<a href="${webdavPath}" title="${message(code:'cmis.actions.editonlinewebdav.tooltip')}" class="action-edit-online list-action action" target="_blank">&nbsp;</a>"""						 
					 }
					 
				 }
				 if(params.cico!="false") {
					 if (doc.isPwc()) {
						 actions+=link(onclick:"simpleDialog(this.href);return false;",title:"${message(code:'cmis.list.checkin')}","class":"action-checkin action list-action simpleDialog",controller:"cmisDocument",action:"checkin", params:[objectId:doc.prop.objectId])										 
					 } else {
					 if (!doc.isCheckedOut()){
						 actions+=link(onclick:"simpleDialog(this.href);return false;",title:"${message(code:'cmis.list.checkout')}","class":"action-checkout action list-action simpleDialog",controller:"cmisDocument",action:"checkout", params:[objectId:doc.prop.objectId])					 
					 }
					 }
				 }
			
			 }
			 if (params.readOnly!="true") {
				 actions+=link(onclick:"simpleDialog(this.href);return false;",title:"${message(code:'cmis.list.delete')}","class":"action-delete action list-action simpleDialog",controller:"cmisDocument",action:"delete", params:[objectId:doc.prop.objectId])
			 }
				 
/*			 def actions="""<g:link onclick="simpleDialog(this.href);return false;"
							title="${message(code:'cmis.list.showproperties')}" class="action-show action list-action simpleDialog"
							controller="cmisDocument" action="props" params="[objectId:${doc.prop.objectId}]">&nbsp;
							</g:link>			 
			 			"""*/
			 //def actions="${action}"						 
			 def inLine=[icon,doc.name,actions]			 
			 def aaLine=[inLine]
			 aaData+=(aaLine)
		 }

		 def json = [sEcho:params.sEcho,iTotalRecords:iTotalRecords,iTotalDisplayRecords:iTotalDisplayRecords,aaData:aaData]
		 render json as JSON
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
	
	def documentlist = {
		
	} 
	
	def jsonparent =  {		
		if (!params.objectId)
		  params.objectId=cmisService.repositories.rootFolderId
		def entry=cmisService.getEntry(params.objectId)
				
		def response=restService.get(entry.link.up)
		def parentEntry=new CmisEntry(response)
		def res=[success:true,objectId:parentEntry.uuid]
		render res as JSON
	}
}
