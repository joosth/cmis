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
import java.io.File;
import java.io.InputStream;

import java.io.Reader;
import org.apache.chemistry.opencmis.client.api.CmisObject
import org.open_t.cmis.*;
import groovy.xml.StreamingMarkupBuilder
import java.net.URLEncoder;
import grails.converters.*
import org.open_t.cmis.command.*

class CmisDocumentController {
	def cmisService
	def restService

	// Interceptor that allows us to use our own authenticator which performs a pass-through to the CMIS repository 
	def beforeInterceptor = {
			if (!cmisService.initialized) {
				redirect(controller:'cmisAuthenticate',action:'login')
				return false
			} else {
			}
	}
		
	// Show document history
    def history = {					
		//def cmisObject=cmisService.getObject(params.getObjectId)					
		def documents=cmisService.listHistory(params.getObjectId)
		[documents:documents]
    }
	
	// Edit document properties
	def edit = {
		def cmisObject=cmisService.getObject(params.getObjectId)
		def cmisCommandObject = new CmisCommandObject([objectId:cmisObject.id,name:cmisObject.prop.name,description:cmisObject.prop.'cm:description'])		
		[cmisCommandObject:cmisCommandObject]
	}
	
	// Update document properties
	def submitedit = { CmisCommandObject cmisCommandObject ->		
		CmisObject cmisObject=cmisService.getObject(cmisCommandObject.objectId)
		
		// ugly ... 
		def theRefreshNodes=[ "ALL" ]
		
		def success=false
		def msg=""
		try {
			cmisObject.updateProperties(['cmis:name':cmisCommandObject.name,'cm:description':cmisCommandObject.description])		
			success=true
			msg=message(code:'cmisDocument.submitedit.message',default:"Document {0} updated",args:[cmisCommandObject.name])
		} catch (Exception e) {
			success=false
			msg=message(code:'cmisDocument.submitedit.errormessage',default:"Error while updating document {0}: {1}",args:[cmisCommandObject.name,e.message])
		}
		
		render(contentType:"text/json") {
			result(
					'success': success,
					message:msg,
					refreshNodes:["ALL"]
				)
		}
		
	}

	
	// Show object properties
	def props = {
			[cmisObject:cmisService.getObject(params.getObjectId),params:params]
	}
		
	// Delete object dialog
	def delete = {
		[entry:cmisService.getObject(params.getObjectId),params:params]
	}
	// Subit delete object dialog
	def submitdelete = {
		def cmisObject=cmisService.getObject(params.getObjectId)
		def name=cmisObject.prop.name
		def success=false
		def msg=""
		try {
			cmisObject.delete()
			success=true
			msg=message(code:'cmisDocument.submitdelete.message',default:"Document {0} deleted",args:[name])
		} catch (Exception e) {
			success=false
			msg=message(code:'cmisDocument.submitdelete.errormessage',default:"Error while deleting document {0}: {1}",args:[name,e.message])			
		}
		
		render(contentType:"text/json") {
			result(
					'success': success,
					message:msg,
					refreshNodes:["ALL"]						
				)
		}
		
		
	}
	
	// Cancel checkout dialog
	def cancelcheckout = {
		def cmisObject=cmisService.getObject(params.getObjectId)
		def cmisCommandObject = new CmisCommandObject([objectId:cmisObject.id,name:cmisObject.prop.name,description:cmisObject.prop.'cm:description'])		
		[cmisCommandObject:cmisCommandObject]
	}
	
	// Submit cancel checkout
	def submitcancelcheckout = { CmisCommandObject cmisCommandObject ->
		def cmisObject=cmisService.getObject(params.objectId)
		def msg=""
		def success=true
		try {
			cmisObject.cancelCheckOut()
			msg=message(code:'cmisDocument.submitcancelcheckout.message',default:"Checkout of {0} cancelled",args:[cmisObject.prop.name])
		} catch (Exception e) {
			success=false
			msg=message(code:'cmisDocument.submitcancelcheckout.errormessage',default:"Error while cancelling checkout of document {0}: {1}",args:[cmisCommandObject.name,e.message])
		}
		render(contentType:"text/json") {
			result(
					'success': success,
					message:msg,
					refreshNodes:["ALL"]
					
				)
		}
	}
	
	
	// Check out document dialog
	def checkout = {
		def cmisObject=cmisService.getObject(params.getObjectId)
		def cmisCommandObject = new CmisCommandObject([objectId:cmisObject.objectId,name:cmisObject.prop.name,description:cmisObject.prop.description])
		[cmisCommandObject:cmisCommandObject]		
	}
	
	// Check out dialog submit
	def submitcheckout = { CmisCommandObject cmisCommandObject ->			
		def cmisObject=cmisService.getObject(cmisCommandObject.objectId)
		
		def msg=""
		def success=true
		try {
			cmisService.checkout(cmisCommandObject.objectId)
			msg=message(code:'cmisDocument.submitcheckout.message',default:"Document {0} checked out",args:[cmisObject.prop.name])
		} catch (Exception e) {
			success=false
			msg=message(code:'cmisDocument.submitcheckout.errormessage',default:"Error while checking out document {0}: {1}",args:[cmisCommandObject.name,e.message])
		}
		
		render(contentType:"text/json") {
			result(
					'success': success,
					message:msg,
					refreshNodes:["ALL"]
					
				)
		}
		
	}
	

	// Check in dialog
	def checkin = {  
		def checkinCommandObject = new CheckinCommandObject([objectId:params.getObjectId])		
		[checkinCommandObject:checkinCommandObject]
	}
	
	// Submit check in dialog
	def submitcheckin = { CheckinCommandObject checkinCommandObject ->
		def name=cmisService.getObject(checkinCommandObject.objectId).prop.name					
		
		def msg=""
		def success=true
		try {
			def cmisObjectId=cmisService.checkin(checkinCommandObject.objectId,checkinCommandObject.comment,checkinCommandObject.major)
			msg=message(code:'cmisDocument.submitcheckin.message',default:"Document {0} checked in",args:[name])
		} catch (Exception e) {
			success=false
			msg=message(code:'cmisDocument.submitcheckin.errormessage',default:"Error while checking in document {0}: {1}",args:[name,e.message])
		}
		
		render(contentType:"text/json") {
			result(
					'success': success,
					message:msg,
					refreshNodes:["ALL"]
					
				)
		}
		
	}
	
	// Create new folder dialog
	def newfolder= {
		[newFolderCommandObject:new NewFolderCommandObject ([parentId:params.getParentId])]
	}
	
	// Submit new folder dialog
	def submitnewfolder = { NewFolderCommandObject newFolderCommandObject ->	
		def success=true
		def msgs=[]
		try {			
			cmisService.createFolder(newFolderCommandObject.parentId,newFolderCommandObject.name,newFolderCommandObject.description)
			msgs+=message(code:'cmisDocument.submitnewfolder.message',default:"{0} created",args:[newFolderCommandObject.name])
		} catch (Exception e ) {
			msgs+=message(code:'cmisDocument.submitnewfolder.errormessage',default:"Creating {0} failed: {1}",args:[newFolderCommandObject.name,e.message])
			success=false
		}
		String msg=msgs.join(", ")
		
		render(contentType:"text/json") {
			result(
					success: success,
					message:msg,
					refreshNodes:["ALL"]
				)
		}
	}
	
	def newdocument= {
		session['files']=[:]		
		def cmisCommandObject= new CmisCommandObject([objectId:params.getParentId])
		[cmisCommandObject:cmisCommandObject]		
	}
	
	def submitnewdocument = { CmisCommandObject cmisCommandObject ->
		def msgs=[]
		def success=true
		
		def filename=session['files'].each { filename,tmpFilename ->
			try {
				cmisService.createDocument(cmisCommandObject.objectId,tmpFilename,filename)
				msgs+=message(code:'cmisDocument.submitnewdocument.message',default:"{0} created",args:[filename])
			} catch (Exception e ) {
				msgs+=message(code:'cmisDocument.submitnewdocument.errormessage',default:"An error occurred while creating {0}: {1}",args:[filename,e.message])
				success=false
			}					
		}
		String msg=msgs.join(", ")

		def theRefreshNodes=[URLEncoder.encode(cmisCommandObject.objectId)]
		
		def result =[
					'success': success,
					'message':msg,
					refreshNodes:theRefreshNodes
				]
		
		
		def res=[result:result]
		render res as JSON
		
	}
	
	
	def updatedocument= {
			session['files']=[:]
			[entry:cmisService.getObject(params.getObjectId),params:params]
	}
	
	def submitupdatedocument = {
			String msg=""
			def success=true
			
			def entry=cmisService.getObject(params.objectId)

			if (session['files'].size()!=1) {
				msg="You should upload exactly one file"
				msg=message(code:'cmisDocument.submitupdatedocument.onefileerrormessage',default:"You should upload exactly one file")
				success=false
			} else {
				def filename=session['files'].each { filename,tmpFilename ->
					try {					
						cmisService.updateDocument(params.objectId,tmpFilename)
						msg=message(code:'cmisDocument.submitupdatedocument.message',default:"{0} updated",args:[entry.name])
					} catch (Exception e) {
						msg=message(code:'cmisDocument.submitupdatedocument.errormessage',default:"An error occurred while updating {0}: {1}",args:[entry.name,e.message])
						success=false
					}										
					
				}
			}
			
			// remove uploaded files from temp
			session['files'].each { filename,tmpFilename ->
				new File(tmpFilename).delete()	
			}
			session['files']=[:]
						
			def theRefreshNodes=[params.objectId]
			render(contentType:"text/json") {
				result(
						'success': success,
						message:"${msg}",
						refreshNodes:theRefreshNodes
					)
			}
		}
		
	
	
	def download = {
		def cmisObject=cmisService.getObject(params.objectId)
		cmisService.streamFile(cmisObject.contentStream,response)
	}
	
	def thumbnail = {
		def cmisObject=cmisService.getObject(params.objectId)
		def thumbnailRendition=cmisObject.getRendition("cmis:thumbnail")
		if (thumbnailRendition) {
			cmisService.streamFile(thumbnailRendition.contentStream,response)
		} else {
			redirect(uri:"/static/css/theme/images/generic-file-32.png")
		}		
	}
	
	
	def fileupload = { 
			def filename
			def is
			def mimetype 
			if (params.qqfile.class.name=="org.springframework.web.multipart.commons.CommonsMultipartFile") {
				filename=params.qqfile.getOriginalFilename()
				is =params.qqfile.getInputStream()
				mimetype=params.qqfile.getContentType()
			} else {
				filename=params.qqfile
				is =request.getInputStream()
				mimetype=request.getHeader("Content-Type")
			}
			
			char[] cbuf=new char[100000]
            byte[] bbuf=new byte[100000]                     
			
			File tempFile=File.createTempFile("upload", "bin");
			OutputStream os=new FileOutputStream(tempFile)			
			
			int nread =is.read(bbuf, 0, 100000)
			int total=nread
			while (nread>0) {
				os.write(bbuf, 0, nread)
				nread =is.read(bbuf, 0, 100000)
				if (nread>0)
					total+=nread
			}
			os.flush()

			is.close()
			os.close()
			if (!session["files"]) {
				session["files"]=[:]
				session["mimetypes"]=[:]
			}			
			session["files"][filename]=tempFile.getAbsolutePath()
			session["mimetypes"][filename]=mimetype
	
			
			render(contentType:"text/html",text:"{success:true}")
			
	}	
	
}
