package org.open_t.cmis
import java.io.File;
import java.io.InputStream;

import java.io.Reader;
import org.open_t.cmis.*;
import groovy.xml.StreamingMarkupBuilder
import java.net.URLEncoder;
import grails.converters.*

class DocumentController {
	def cmisService
	def restService

	// Interceptor that allows us to use our own authenticator which performs a pass-through to the CMIS repository 
	def beforeInterceptor = {
			if ((!restService.authenticated) || cmisService.repositories==null) {
				redirect(controller:'authenticate',action:'login')
				return false
			} else {
			}
	}
	
	
	
    def index = {redirect(action:list)}
    // Show list of files in folder, if no objectId given use the root folder
    def list = {		
		if (!params.objectId)		
			params.objectId=cmisService.repositories.rootFolderId
		def cmisEntry=cmisService.getEntry(params.objectId)
		def documents=cmisService.listDescendants(cmisEntry)
		[documents:documents,cmisEntry:cmisEntry]
    }
	
	// List checked out documents
	def checkedout = {
		[documents:cmisService.listCheckedOut()]
	}
	
	// Show document history
    def history = {					
		def cmisEntry=cmisService.getEntry(params.objectId)					
		def documents=cmisService.listHistory(cmisEntry)
		[documents:documents,cmisEntry:cmisEntry]
    }
	
	
	// Show document
	def show = {
		println "PARAMS:${params}"
		[entry:cmisService.getEntry(params.objectId)]
	}

	// Edit document properties
	def edit = {
			[entry:cmisService.getEntry(params.objectId),params:params]
	}
	
	// Update document properties
	def update = {
		def cmisEntry=cmisService.getEntry(params.objectId)
		
		cmisEntry.title=params.title
		cmisEntry.summary=params.summary
		
		cmisService.update(cmisEntry)

		def theRefreshNodes=[ "${URLEncoder.encode(params.objectId)}" ]		
		//render(contentType:"text/json") {
		def result = [
					'success': true,
					message:"Document ${cmisEntry.title} updated",
					refreshNodes:theRefreshNodes
				]
		def res=[result:result]
		render res as JSON	
		//}
	}

	
	// Show object properties
	def props = {
			[entry:cmisService.getEntry(params.objectId),params:params]
	}
	
	// Delete object dialog
	def delete = {
		[entry:cmisService.getEntry(params.objectId),params:params]
	}
	// Subit delete object dialog
	def deletesubmit = {
			def cmisEntry=cmisService.getEntry(params.objectId)
			restService.delete(cmisEntry.link.self);
			render(contentType:"text/json") {
				result(
						'success': true,
						message:"Document ${cmisEntry.title} deleted"
					)
			}
		}
	
	// Cancel checkout dialog
	def cancelcheckout = {
			[entry:cmisService.getEntry(params.objectId),params:params]
	}
	
	// Submit cancel checkout
	def cancelcheckoutsubmit = {
		def cmisEntry=cmisService.getEntry(params.objectId)
		restService.delete(cmisEntry.link.self);
		render(contentType:"text/json") {
			result(
					'success': true,
					message:"Checkout of ${cmisEntry.title} cancelled"
				)
		}
	}
	
	
	
	// Check out document dialog
	def checkout = {
		[entry:cmisService.getEntry(params.objectId),params:params]
	}
	
	// Check out dialog submit
	def checkoutsubmit = {
			
			def cmisEntry=cmisService.getEntry(params.objectId)			
			def response=cmisService.checkout(params.objectId)			
			render(contentType:"text/json") {
				result(
						'success': true,
						message:"Document ${cmisEntry.title} checked out"
					)
			}
		}
	
	def workingcopy = {
		def pwcEntry=cmisService.workingCopy(params.objectId)
		render (view:'show',model:[entry:pwcEntry])
	}
	// Check in dialog
	def checkin = {
		[entry:cmisService.getEntry(params.objectId)]
	}
	
	// Submit check in dialog
	def checkinsubmit = {		
		boolean major = params.major ? true : false
		def entry=cmisService.getEntry(params.objectId)
		
		def response=cmisService.checkin(params.objectId,params.checkinComment,major)
		
		render(contentType:"text/json") {
			result(
					'success': true,
					message:"Document ${entry.title} checked in"
				)
		}

	}
	
	// Create new folder dialog
	def newfolder= {
			[parentId:params.parentId]
	}
	
	// Submit new folder dialog
	def newfoldersubmit = {	
		def cmisresult=cmisService.createFolder(params.parentId,params.name,params.summary)
		render(contentType:"text/json") {
			result(
					success: true,
					message:"Folder ${params.name} created"
				)
		}
	}
	
	def newdocument= {
			[parentId:params.parentId]
	}
	
	def newdocumentsubmit = {
		String message=""
		def success=true
		if (params.filename.class.name=="java.lang.String") {
			if (cmisService.createDocument(params.parentId,session['files'][params.filename],params.filename,params.filename)) {
					message+="${params.filename} created"
			} else {
				message+="creating ${params.filename} failed"
				success=false
			}
			
		} else {		
			params.filename.each { filename ->
			if(cmisService.createDocument(params.parentId,session['files'][filename],filename,filename)) {
				message+="<br />${filename} created" 				
			} else {
				message+="<br /> creating ${filename} failed"
				success=false
			}
		}
		}
		def theRefreshNodes=[URLEncoder.encode(params.parentId)]
		
		def result =[
					'success': success,
					message:"${message}",
					refreshNodes:theRefreshNodes
				]
		
		
		def res=[result:result]
		render res as JSON
		
	}
	
	
	def updatedocument= {
			session['files']=[:]
			[entry:cmisService.getEntry(params.objectId),params:params]
	}
	
	def updatedocumentsubmit = {
			String message=""
			def success=true
			
			def entry=cmisService.getEntry(params.objectId)
			
			if (session['files'].size()!=1) {
				message="You should upload exactly one file"
				success=false
			} else {
				def filename=session['files'].each { filename,tmpFilename ->
					def file = new File(tmpFilename)					
					restService.writeFile("PUT",entry.link.'edit-media',file);
					message="${entry.title} updated"
				}
			}
			
			// TODO remove uploaded files from temp
						
			def theRefreshNodes=[params.objectId]
			render(contentType:"text/json") {
				result(
						'success': success,
						message:"${message}",
						refreshNodes:theRefreshNodes
					)
			}
		}
		
	
	
	def download = {
		def cmisEntry=cmisService.getEntry(params.objectId)		
		def downloadUrl=cmisEntry.link.enclosure
		def fileName=cmisEntry.prop.contentStreamFileName		
		restService.streamFile(downloadUrl,fileName,response)	
	}
	
	def thumbnail = {
		def url=cmisService.objectUrlById(params.objectId)
		def cmisEntry=cmisService.getEntry(params.objectId)
				
		def iconUrl=cmisEntry.link.alternate_thumbnail
		if (iconUrl.length()>0) {			
			restService.streamFile(iconUrl,"thumbnail.png",response)
		} 
		
	}
	
	
	def fileupload = { uploadbody ->
			def filename=params.qqfile
			InputStream is =request.getInputStream()
			def mimetype=request.getHeader("Content-Type")
			
			
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
	
			render(contentType:"text/json") {
				success(success : true)
					
			}
	}
	
	
	
}
