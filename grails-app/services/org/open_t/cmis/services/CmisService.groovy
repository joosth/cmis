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
 * CMIS Service bean
 * 
 * @author Joost Horward
 */
 
package org.open_t.cmis.services

import java.io.FileOutputStream;

import groovy.xml.*
import java.net.*;
import java.util.Date;
import java.text.*;

import org.open_t.cmis.*;
import org.open_t.base64.*;
import net.sf.jmimemagic.*;
import groovy.xml.StreamingMarkupBuilder
import org.codehaus.groovy.grails.commons.ConfigurationHolder


import java.text.SimpleDateFormat
import org.apache.chemistry.opencmis.client.api.CmisObject
import org.apache.chemistry.opencmis.client.api.OperationContext
import org.apache.chemistry.opencmis.client.api.Document
import org.apache.chemistry.opencmis.client.api.Folder
import org.apache.chemistry.opencmis.commons.data.ContentStream
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.data.ContentStream
import org.apache.chemistry.opencmis.commons.enums.BaseTypeId
import org.apache.chemistry.opencmis.commons.enums.*
import org.apache.commons.lang.time.*
import org.apache.commons.lang.*
import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.ItemIterable;
import org.apache.chemistry.opencmis.client.api.Repository;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.api.SessionFactory;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.enums.BindingType;

import org.open_t.cmis.authentication.*

import net.sf.jmimemagic.*;

class CmisService {
    
    static transactional = false
	static scope="session"
	def restService

	List<Repository> repositories = new ArrayList<Repository>();
    def contextPath=""
    def url=""
    def initialized=false
    def enabled=false
	def pluginManager
	def grailsApplication
	def sppBasePath=ConfigurationHolder.config.cmis.sppBasePath
	def webdavBasePath=ConfigurationHolder.config.cmis.webdavBasePath
	
	CmisObject rootFolder
	
	Session cmisSession
	
	def onlineEditMimetypes =
	[
	   "application/vnd.ms-excel": "Excel.Sheet",
	   "application/vnd.ms-powerpoint": "PowerPoint.Slide",
	   "application/msword": "Word.Document",
	   "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet": "Excel.Sheet",
	   "application/vnd.openxmlformats-officedocument.presentationml.presentation": "PowerPoint.Slide",
	   "application/vnd.openxmlformats-officedocument.wordprocessingml.document": "Word.Document"
	]
	
    /**
     * Initialize this CmisService bean, remember url, username,password
     * 
     * @param theUrl URL of the repository
     * @param username Default username to use
     * @param password Default password to use
     */
	
    def init(theUrl,username,password) {
		log.debug "Initializing with url: ${theUrl}, username: ${username}, password: ${password}"
		
		Map<String, String> parameter = new HashMap<String, String>();

		// we are using the AtomPUB binding
		
		parameter.put(SessionParameter.ATOMPUB_URL, theUrl);
		parameter.put(SessionParameter.USER, username);
		parameter.put(SessionParameter.PASSWORD, password);
		
		def authenticationClass=ConfigurationHolder.config.cmis.authenticationClass
		def authenticationParameters=ConfigurationHolder.config.cmis.authenticationParameters
		
		// Use our own authentication class if provided
		if (authenticationClass) {
			parameter.put(SessionParameter.AUTHENTICATION_PROVIDER_CLASS, authenticationClass);
			authenticationParameters.each { key,value ->
				parameter.put(key,value)
			}
		}

		parameter.put(SessionParameter.BINDING_TYPE, BindingType.ATOMPUB.value());
		parameter.put(SessionParameter.OBJECT_FACTORY_CLASS, "org.alfresco.cmis.client.impl.AlfrescoObjectFactoryImpl");
		
		SessionFactory sessionFactory = SessionFactoryImpl.newInstance();
		
		// find all the repositories at this URL - there should only be one.
		repositories = sessionFactory.getRepositories(parameter);
		
		// create session with the first (and only) repository
		Repository repository = repositories.get(0);
		parameter.put(SessionParameter.REPOSITORY_ID, repository.getId());
		cmisSession = sessionFactory.createSession(parameter);
		
		OperationContext oc = cmisSession.createOperationContext();
		oc.setIncludePathSegments(true)
		oc.setRenditionFilterString("cmis:thumbnail")
		oc.setOrderBy("cmis:name ASC")
		oc.setCacheEnabled(true)
		cmisSession.setDefaultContext(oc)
				
		//This is the root path we fall back on when nothing is defined in a view 
		def rootPath=ConfigurationHolder.config.cmis.authenticationParameters?:"/"
		rootFolder = cmisSession.getObjectByPath(rootPath);
		
    	initialized=true
    	enabled=true
		
		contextPath=org.open_t.util.SpringUtil.applicationContext.id+pluginManager.getPluginPath("cmis")
		
    }
	
	
	/**
	 * List all children of the given object
	 * 
	 * @param obj CmisObject or CmisObject id
	 * @param params Parameters for the operation context:
	 * - orderBy default is cmis:name
	 * - sortDir - ASC or DESC
	 * - skipCount
	 * - maxItemsPerPage - default is 10
	 * 
	 * @return page of results
	 */
   
	def listChildren(obj,params=[:]) {
		Folder cmisFolder=obj.class==java.lang.String?getObject(obj):obj
	   
		OperationContext oc = cmisSession.createOperationContext();
		if (params?.orderBy) {
			def sortDir = params.sortDir?:"ASC"
			oc.orderBy="${params.orderBy} ${sortDir}"		   
		} else {
			oc.orderBy="cmis:name ASC"
		}
		def maxItemsPerPage=params.maxItemsPerPage?new Integer(params.maxItemsPerPage):10	   	   
		oc.setMaxItemsPerPage(maxItemsPerPage);

		oc.setIncludePathSegments(true)
		oc.setRenditionFilterString("cmis:thumbnail")
		if (params.skipCount) {
			return cmisFolder.getChildren(oc).skipTo(new Integer(params.skipCount)).getPage()
		} else {
			return cmisFolder.getChildren(oc).getPage()
		}
	}
	
    
	/**
	 * List all checked out entries
	 * 
	 * @return The checked out documents
	 */
    def listCheckedOut() {
		cmisSession.getCheckedOutDocs()	
    }
    
	/**
	 * List the version history of this entry
	 * 
	 * @param obj CmisObject or CmisObject id
	 * @return The version history
	 */
    
    def listHistory(obj) {
    	CmisObject cmisObject=obj.class==java.lang.String?getObject(obj):obj
		return cmisObject.getAllVersions()
	}
	
	/**
	 * Get Object by object ID
	 *
	 * @param obj CmisObject or CmisObject id
	 * @return The CmisObject
	 */    
	def getObject(obj) {
		CmisObject cmisObject=obj.class==java.lang.String?cmisSession.getObject(obj):obj		
	}
	
	
	/**
	 * Get CMIS Entry by path
	 * 
	 * @param path The path
	 * @return The CmisObject 
	 */
    
    def getObjectByPath(path) {
		cmisSession.getObjectByPath(path)    	
    }
	
	/**
	 * Create a folder
	 * 
	 * @param parent CmisObject or CmisObject id
	 * @param name The name of the folder
	 * @param description Optional description
	 */
    
    def createFolder(parent,name,description=null) {
    	def parentFolder=getObject(parent)		
		def properties=[:]
		properties.put(PropertyIds.OBJECT_TYPE_ID, "cmis:folder,P:cm:titled");
		if (description) {			
			properties.put("cm:description",description);
		}
		properties.put(PropertyIds.NAME, name);
		Folder newFolder=parentFolder.createFolder(properties)				
    }
    
	/**
	 * Create a document in the CMIS repository
	 * 
	 * @param parent CmisObject or CmisObject id
	 * @param filename Filename to be uploaded
	 * @param name name in the repository
	 * @param description Optional description
	 * 
	 * @return The CmisObject	  
	 */
    def createDocument(parent,String filename,String name,String description=null) {
    	def parentFolder=getObject(parent)
		
    	Magic parser = new Magic() ;
    	// getMagicMatch accepts Files or byte[],
    	// which is nice if you want to test streams
    	def mimetype="application/octet-stream"
    	try {
    		// The false means no extension hints.
    		MagicMatch match = parser.getMagicMatch(new File(filename),false);
    		mimetype=match.getMimeType();
    		log.debug "Hey, we have a mimetype match: ${mimetype}"
		} catch (Exception e) {
    		// Quietly stay at the default if we can't find it ...
    	}
		
		File f = new File(filename)
		
		def is = new FileInputStream(f);
		
		ContentStream contentStream = cmisSession.getObjectFactory().createContentStream(name,f.size(), mimetype, is);
		
		def properties=[:]
		properties.put(PropertyIds.OBJECT_TYPE_ID, "cmis:document,P:cm:titled");
		properties.put(PropertyIds.NAME, name);
		if (description) {
			properties.put("cm:description",description);
		}
		
		Document doc = parentFolder.createDocument(properties, contentStream, VersioningState.NONE);
		is.close();
		
		return doc
    	
    }
	
	/**
	 * Update a document in the CMIS repository
	 * 
	 * @param obj CmisObject or CmisObject id
	 * @param filename The file to be uploaded
	 * @return The CmisObject 
	 */
	def updateDocument(obj,String filename) {
		def cmisObject=getObject(obj)
		
		Magic parser = new Magic() ;
		// getMagicMatch accepts Files or byte[],
		// which is nice if you want to test streams
		def mimetype="application/octet-stream"
		try {
			// The false means no extension hints.
			MagicMatch match = parser.getMagicMatch(new File(filename),false);
			mimetype=match.getMimeType();
			log.debug "Hey, we have a mimetype match: ${mimetype}"
		} catch (Exception e) {
			// Quietly stay at the default if we can't find it ...
		}
		
		File f = new File(filename)
		
		def is = new FileInputStream(f);
		
		ContentStream contentStream = cmisSession.getObjectFactory().createContentStream(f.name,f.size(), mimetype, is);
		
		def properties=[:]
		cmisObject.setContentStream(contentStream,true)
		is.close();
		
		return cmisObject
	}
		
	/**
	 * Check out a CMIS document
	 * 
	 * @param obj CmisObject or CmisObject id
	 * @return The working copy
	 */
    def checkout(obj) {
		Document cmisDocument=getObject(obj)
    	return cmisDocument.checkOut()
    }
    
	/**
	 * Check in a CMIS Entry
	 * 
	 * @param obj CmisObject or CmisObject id
	 * @param comment The comment to be used
	 * @param major Determines if this checkin will be a major new version (2.0, 3.0 ...)
	 */
    def checkin(obj,comment,major=false) {    	
    	Document cmisDocument=getObject(obj)
    	return cmisDocument.checkIn(major, null, null, comment)
    	
    }	
	/**
	 * Create an entire path in the repository
	 * 
	 * @param path The path to be created
	 * @return the CMISObject at the path 
	 */
	def createPath(cmisPath) {
		
		def pathElements=cmisPath.split("/")
	
		def parent=getObjectByPath("/")
		
		def path=""
		pathElements.each { pathElement ->
			if (pathElement.length()>0) {
	
				path+="/"+pathElement
				def entry=getObjectByPath(path)
				if(!entry) {
					createFolder(parent.objectId,pathElement,message(code:'cmisservice.createpath.message',default:"Created by CMIS service"))
					parent=getObjectByPath(path)
				} else {
					parent=entry
				}
			}
		}
		return getObjectByPath(cmisPath)
	}
	
	/**
	 * Get info needed to create a SPP link
	 * 
	 * @param obj CmisObject or CmisObject id
	 * @param parentPath The path to the parent object. This speeds up generation by saving an extra CMIS call. Useful when generating a list of items that all need an SPP link.
	 * @return SPP info map:
	 * - path: The SPP path
	 * - appProgId: The prog id needed for the SPP link
	 */
	
	def getSpp(def obj,parentPath=null) {
		def cmisObject=getObject(obj)
		String path
		if (parentPath) {
			path=parentPath+"/"+obj.prop.name
		} else {
			path=cmisObject.paths[0]
		}
		def sppPath=null	
		def sppAppProgId=onlineEditMimetypes[cmisObject.prop.contentStreamMimeType]
		log.debug "SPP base path: ${sppBasePath} path: ${path} sppAppProgId: ${sppAppProgId}" 
				
		if (sppBasePath && sppAppProgId) {
			if (path.startsWith("/Sites/")) {
				def spath=path.replace("/Sites/","/")
				sppPath=sppBasePath+spath
			}
		}
		return [path:sppPath,appProgId:sppAppProgId]
		
	}
	
	/**
	 * Get WebDAV link for CmisObject
	 * @param obj CmisObject or CmisObject id
	 * @param parentPath The path to the parent object. This speeds up generation by saving an extra CMIS call. Useful when generating a list of items that all need an WebDAV link.
	 * 
	 * @return the WebDAV link 
	 */
	def getWebdavPath(obj,parentPath=null) {
		def cmisObject=getObject(obj)
		String path
		if (parentPath) {
			path=parentPath+"/"+obj.prop.name
		} else {
			path=cmisObject.paths[0]
		}
		def webdavPath=null		
		if (webdavBasePath) {
			webdavPath=webdavBasePath+path
		}
		return webdavPath
	}
	
	/**
	 * Stream file from CMIS repository to client
	 * 
	 * @param contentstream The content stream
	 * @param The servlet response to use
	 */
	
	def streamFile(ContentStream contentStream,response) {		
		response.setHeader("Content-disposition", "attachment; filename=\"" +contentStream.fileName+"\"")		
		response.setHeader("Content-Type", contentStream.mimeType)
		
		def inputStream=contentStream.stream		
		def bufsize=100000
		byte[] bytes=new byte[(int)bufsize]

		def offset=0
		def len=1
		while (len>0) {
			len=inputStream.read(bytes, 0, bufsize)
			if (len>0)
			response.outputStream.write(bytes,0,len)
			offset+=bufsize
		}		   
		response.outputStream.flush()
	}
	
}
